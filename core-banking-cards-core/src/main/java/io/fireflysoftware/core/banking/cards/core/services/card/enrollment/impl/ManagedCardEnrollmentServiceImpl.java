/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentAuthority.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentNativeContext.Purpose;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardReservationReceipt;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationStoreException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reactor.core.publisher.Mono;

/** Five-row local reservation only. No provider invocation, activation or admission fallback. */
public final class ManagedCardEnrollmentServiceImpl implements ManagedCardEnrollmentService {
    private final SecurityContextPort principals;private final ManagedCardEnrollmentAuthority authority;
    private final ManagedCardEnrollmentSession command,reader;private final Clock clock;private final LongSupplier nanos;private final Supplier<UUID> ids;
    public ManagedCardEnrollmentServiceImpl(SecurityContextPort principals,ManagedCardEnrollmentAuthority authority,
            ManagedCardEnrollmentSession command,ManagedCardEnrollmentSession reader,Clock clock,LongSupplier nanos,Supplier<UUID> ids) {
        this.principals=Objects.requireNonNull(principals);this.authority=Objects.requireNonNull(authority);this.clock=Objects.requireNonNull(clock);this.nanos=Objects.requireNonNull(nanos);this.ids=Objects.requireNonNull(ids);
        this.command=Objects.requireNonNull(command);this.reader=Objects.requireNonNull(reader);
        if(command.role()!=ManagedCardEnrollmentSession.Role.COMMAND||reader.role()!=ManagedCardEnrollmentSession.Role.READER
                ||command.operations()==reader.operations()||command.factory()==reader.factory())throw new IllegalArgumentException("DISTINCT_QUALIFIED_MANAGED_ROLE_SESSIONS_REQUIRED");
    }
    @Override public Mono<ManagedCardReservationReceipt> reserve(UUID accountId,Request request) {
        return Mono.defer(()->{
            UUID action=request==null?null:request.actionId();
            try { id(accountId);ManagedCardEnrollmentRules.validate(request); }
            catch(RuntimeException invalid){return Mono.error(failure(action,ManagedCardEnrollmentException.Reason.CONFLICT));}
            var window=new ManagedCardEnrollmentWindow(clock,nanos);
            return principal(window,action).flatMap(principal->Mono.defer(()->authority.authorizeReservation(principal,accountId,request))
                    .switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE))).timeout(window.remaining())
                    .flatMap(authorized->{
                        validateAuthorization(principal,accountId,authorized);id(authorized.accountFenceReceiptId());
                        // Early refusal only; admission is checked again with the actual native database stamp below.
                        if(ManagedCardEnrollmentRules.eligibility(authorized.selection(),authorized.eligibility(),clock.instant().toEpochMilli())!=Eligibility.ELIGIBLE)
                            return Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE));
                        if(authorized.eligibility().values().stream().anyMatch(receipt->Instant.ofEpochMilli(receipt.validUntilEpochMillis()).isBefore(authorized.effectiveUntil())))
                            return Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE));
                        window.constrain(authorized);
                        return execute(command,authorized,request.actionId(),Purpose.RESERVE,Instant.ofEpochMilli(request.acceptNotAfterEpochMillis()),ManagedCardEnrollmentRules.digest(authorized.selection(),request),window,
                                stamp->command.repository().loadReserved(authorized.scope(),action)
                                    .map(graph->{ManagedCardReservationRules.requireReplay(graph,authorized.selection(),request);
                                        if(!graph.action().accountFenceReceiptId().equals(authorized.accountFenceReceiptId()))throw failure(action,ManagedCardEnrollmentException.Reason.CONFLICT);
                                        verify(graph);return graph;})
                                    .switchIfEmpty(Mono.defer(()->{
                                        ManagedCardEnrollmentRules.admit(request,authorized.selection(),authorized.eligibility(),stamp.dbNowMillis());
                                        var graph=ManagedCardReservationFactory.create(authorized.selection(),request,nextId(),nextId(),nextId(),nextId(),authorized.accountFenceReceiptId(),stamp.dbNowMillis());
                                        return command.repository().appendReserved(graph);
                                    })));
                    })).onErrorMap(error->safePreflight(action,error));
        });
    }
    @Override public Mono<ManagedCardReservationReceipt> recover(UUID accountId,RecoveryRequest request) {
        return Mono.defer(()->{
            UUID action=request==null?null:request.actionId();
            try { id(accountId);ManagedCardReservationRules.validateRecovery(request); }
            catch(RuntimeException invalid){return Mono.error(failure(action,ManagedCardEnrollmentException.Reason.CONFLICT));}
            var window=new ManagedCardEnrollmentWindow(clock,nanos);
            return principal(window,action).flatMap(principal->Mono.defer(()->authority.authorizeRecovery(principal,accountId,request))
                    .switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE))).timeout(window.remaining())
                    .flatMap(authorized->{
                        validateAuthorization(principal,accountId,authorized);window.constrain(authorized);
                        // A fresh read purpose gets its own bound; the retained mutation deadline is never extended.
                        return execute(reader,authorized,action,Purpose.RECOVER,null,null,window,stamp->reader.repository().loadReserved(authorized.scope(),action)
                                .switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.NOT_FOUND)))
                                .map(graph->{verify(graph);ManagedCardReservationRules.requireRecovery(graph,request);return graph;}));
                    })).onErrorMap(error->safePreflight(action,error));
        });
    }
    private Mono<ManagedCardReservationReceipt> execute(ManagedCardEnrollmentSession session,Authorization authorization,UUID action,
            Purpose purpose,Instant mutationUntil,String intentDigest,ManagedCardEnrollmentWindow window,
            Function<ManagedCardEnrollmentNativeContext.Stamp,Mono<ManagedCardReservationGraph>> work) {
        UUID nonce=nextId();UUID fence=authorization instanceof ReservationAuthorization reserved?reserved.accountFenceReceiptId():null;
        var request=new ManagedCardEnrollmentNativeContext.Request(nonce,authorization.scope(),authorization.actorId(),action,purpose,authorization.caller(),fence,intentDigest,
                scopeDigest(nonce,authorization,action,purpose,fence,intentDigest),authorization.effectiveUntil(),window.commandUntil(authorization.effectiveUntil(),mutationUntil));
        AtomicReference<Throwable> workFailure=new AtomicReference<>();
        var definition=new DefaultTransactionDefinition();definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        // Both logical roles use physical RW solely for the future private context. Reader business rights remain SELECT-only.
        definition.setReadOnly(false);
        return session.transactions().execute(window.budget(),definition,scope->Mono.defer(()->{
            long beforeNative=window.beforeNative();
            return session.nativeContext().enter(request).switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE)))
                .flatMap(stamp->{window.installed(request,stamp,beforeNative,scope);return work.apply(stamp);})
                .flatMap(graph->requireCurrent(window,authorization,action).then(Mono.defer(()->{
                    window.remaining();return session.nativeContext().complete(request)
                        .filter(nonce::equals).switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE)))
                        .map(ignored->{window.remaining();return graph;});
                })));
        }).doOnError(error->workFailure.compareAndSet(null,error)))
          .onErrorMap(RootTransactionException.class,error->transactionFailure(action,error,workFailure.get()))
          .flatMap(completion->{
              if(!completion.transactionFacts().boundaryCompleted())return Mono.error(new ManagedCardEnrollmentException(action,ManagedCardEnrollmentException.Reason.UNCERTAIN,completion.transactionFacts(),completion.cleanupFact()));
              return Mono.defer(()->requireCurrent(window,authorization,action).then(Mono.fromCallable(()->{
                  window.remaining();var graph=completion.value();return new ManagedCardReservationReceipt(graph.acceptance(),graph.progress());
              }))).onErrorMap(error->new ManagedCardEnrollmentException(action,ManagedCardEnrollmentException.Reason.WITHHELD,completion.transactionFacts(),completion.cleanupFact()));
          });
    }
    private Mono<Void> requireCurrent(ManagedCardEnrollmentWindow window,Authorization authorization,UUID action) {
        return Mono.defer(()->principal(window,action).flatMap(current->{
            if(!authorization.caller().matches(current))return Mono.error(failure(action,ManagedCardEnrollmentException.Reason.WITHHELD));
            return authority.requireCurrent(current,authorization);
        }).timeout(window.remaining()));
    }
    private Mono<SecurityPrincipal> principal(ManagedCardEnrollmentWindow window,UUID action) {
        return Mono.defer(principals::currentPrincipal).filter(p->p.issuer()!=null&&!p.issuer().isBlank()&&p.subject()!=null&&!p.subject().isBlank()&&p.tenantId()!=null&&!p.tenantId().isBlank())
                .switchIfEmpty(Mono.error(failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE))).timeout(window.remaining());
    }
    private static void validateAuthorization(SecurityPrincipal principal,UUID account,Authorization authorization) {
        if(authorization==null||!authorization.caller().matches(principal)||!account.equals(authorization.scope().accountId()))throw new IllegalStateException("EXACT_CURRENT_MANAGED_AUTHORITY_REQUIRED");
        id(authorization.actorId());
    }
    private static void verify(ManagedCardReservationGraph graph) {
        ManagedCardEnrollmentRules.requireAcceptance(graph.acceptance(),graph.selection(),graph.request(),graph.action().receiptId(),graph.action().recordedAt().toEpochMilli());
    }
    private static String scopeDigest(UUID nonce,Authorization authorization,UUID action,Purpose purpose,UUID fence,String intentDigest) {
        var value=new StringBuilder("MANAGED_CARD_NATIVE_SCOPE_2\n");
        for(String part:List.of(nonce.toString(),authorization.scope().tenantId().toString(),authorization.actorId().toString(),
                authorization.scope().partyId().toString(),authorization.scope().accountId().toString(),action.toString(),purpose.name(),
                authorization.caller().issuer(),authorization.caller().subject(),authorization.caller().tenantKey(),
                fence==null?"":fence.toString(),intentDigest==null?"":intentDigest))value.append(part.getBytes(StandardCharsets.UTF_8).length).append(':').append(part);
        try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.toString().getBytes(StandardCharsets.UTF_8)));}
        catch(java.security.NoSuchAlgorithmException unavailable){throw new IllegalStateException(unavailable);}
    }
    private UUID nextId() { UUID value=ids.get();id(value);return value; }
    private static void id(UUID value) { if(value==null||value.equals(new UUID(0,0)))throw new IllegalArgumentException("MANAGED_ID_REQUIRED"); }
    private static ManagedCardEnrollmentException failure(UUID action,ManagedCardEnrollmentException.Reason reason) {
        return new ManagedCardEnrollmentException(action,reason,new TransactionFacts(false,false,false,false),CleanupFact.COMPLETE);
    }
    private static Throwable safePreflight(UUID action,Throwable error) {
        return error instanceof ManagedCardEnrollmentException?error:failure(action,ManagedCardEnrollmentException.Reason.UNAVAILABLE);
    }
    private static ManagedCardEnrollmentException transactionFailure(UUID action,RootTransactionException error,Throwable workFailure) {
        var facts=error.transactionFacts();var reason=facts.commitAcknowledged()||facts.boundaryCompleted()?ManagedCardEnrollmentException.Reason.WITHHELD:ManagedCardEnrollmentException.Reason.UNCERTAIN;
        if(!facts.commitAcknowledged()&&!facts.boundaryCompleted()&&facts.rollbackAcknowledged()&&error.cleanupFact()==CleanupFact.COMPLETE) {
            if(workFailure instanceof ManagedCardReservationStoreException store&&store.reason()==ManagedCardReservationStoreException.Reason.CONFLICT
                    ||workFailure instanceof ManagedCardEnrollmentException business&&business.reason()==ManagedCardEnrollmentException.Reason.CONFLICT
                    ||workFailure instanceof ManagedCardReservationRules.Conflict)
                reason=ManagedCardEnrollmentException.Reason.CONFLICT;
        }
        return new ManagedCardEnrollmentException(action,reason,facts,error.cleanupFact());
    }
}
