/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentAuthority.ReservationAuthorization;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationAuditNativeRepository;
import java.time.*;
import java.util.*;
import java.util.function.*;
import org.fireflyframework.core.audit.*;
import org.fireflyframework.core.audit.TransactionalAuditAppender.ProvisionalAppend;
import org.fireflyframework.core.audit.impl.SpringDataTransactionalAuditAppender;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.kernel.audit.AuditEvent;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

/** Actual shared mapped append on the original Cards resource graph; never creates a transaction. */
public final class ManagedCardReservationAuditImpl implements ManagedCardReservationAudit {
    private final BoundedR2dbcResources resources;private final DatabaseClient client;private final R2dbcEntityTemplate operations;private final R2dbcTransactionManager manager;
    private final SecurityContextPort principals;private final ManagedCardEnrollmentAuthority authority;private final ManagedCardReservationAuditAdmission admission;
    private final Clock clock;private final LongSupplier nanos;private final Supplier<UUID> nonces;
    public ManagedCardReservationAuditImpl(BoundedR2dbcResources resources,DatabaseClient client,R2dbcEntityTemplate operations,R2dbcTransactionManager manager,
            SecurityContextPort principals,ManagedCardEnrollmentAuthority authority,ManagedCardReservationAuditAdmission admission,Clock clock,LongSupplier nanos,Supplier<UUID> nonces){
        this.resources=BoundedR2dbcResources.verify(Objects.requireNonNull(resources).factory(),client,operations,manager);this.client=client;this.operations=operations;this.manager=manager;
        this.principals=Objects.requireNonNull(principals);this.authority=Objects.requireNonNull(authority);this.admission=Objects.requireNonNull(admission);this.clock=Objects.requireNonNull(clock);this.nanos=Objects.requireNonNull(nanos);this.nonces=Objects.requireNonNull(nonces);
    }
    public OwnedLeaseConnectionFactory factory(){return resources.factory();}public R2dbcEntityTemplate operations(){return operations;}
    public SecurityContextPort principals(){return principals;}public ManagedCardEnrollmentAuthority authority(){return authority;}public Clock clock(){return clock;}public LongSupplier nanos(){return nanos;}
    @Override public Mono<ProvisionalAppend> append(RootTransactionScope scope,UUID reservationNonce,ReservationAuthorization authorized,ManagedCardReservationGraph graph,BusinessDisposition disposition){
        return Mono.defer(()->resources.factory().requireBoundScope(resources,scope).then(Mono.defer(()->{
            Objects.requireNonNull(reservationNonce);Objects.requireNonNull(disposition);Objects.requireNonNull(authorized);Objects.requireNonNull(graph);
            ManagedCardReservationRules.requireReplay(graph,authorized.selection(),graph.request());
            if(!graph.action().accountFenceReceiptId().equals(authorized.accountFenceReceiptId()))return Mono.error(failure());
            return current(scope,authorized,graph).flatMap(observation->{
                AuditEvent event=ManagedCardReservationAuditEvidence.event(graph,observation.actor(),observation.policyRevision());
                AuditAppendAuthority current=retained->current(scope,authorized,graph).filter(value->ManagedCardReservationAuditEvidence.event(graph,value.actor(),value.policyRevision()).equals(retained))
                    .switchIfEmpty(Mono.error(failure())).map(value->new AuditAppendAuthority.Permit(retained.stream(),value.actor(),Collections.min(List.of(value.validUntil(),authorized.effectiveUntil()))));
                var appender=new SpringDataTransactionalAuditAppender(resources,client,operations,manager,current,clock,nanos,nonces);
                return appender.append(scope,event).flatMap(provisional->{
                    if(provisional.replay()!=(disposition==BusinessDisposition.EXACT_RESERVE_REPLAY))return Mono.error(failure());
                    var nativePair=new org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory(operations).getRepository(ManagedCardReservationAuditNativeRepository.class);
                    return resources.factory().requireBoundScope(resources,scope).then(nativePair.pair(reservationNonce,event.eventId(),event.actor().clientId(),event.actor().staffRealm(),event.actor().staffId(),event.policyRevision(),event.evidenceDigest(),disposition.name()))
                        .filter(reservationNonce::equals).switchIfEmpty(Mono.error(failure())).timeout(scope.remaining()).thenReturn(provisional);
                });
            });
        })).onErrorResume(error->resources.factory().markBoundScopeRollbackOnly(resources,scope).onErrorResume(markFailure->Mono.error(error)).then(Mono.error(error))));
    }
    private Mono<ManagedCardReservationAuditAdmission.Observation> current(RootTransactionScope scope,ReservationAuthorization authorized,ManagedCardReservationGraph graph){
        return Mono.defer(principals::currentPrincipal).filter(authorized.caller()::matches).switchIfEmpty(Mono.error(failure())).flatMap(principal->authority.requireCurrent(principal,authorized)
            .then(Mono.defer(()->admission.requireCurrent(principal,authorized,graph))).filter(value->{var actor=value.actor();return Objects.equals(actor.issuer(),principal.issuer())&&Objects.equals(actor.subject(),principal.subject())&&Objects.equals(actor.platformTenantId(),principal.tenantId())
                &&value.domainActorId().equals(authorized.actorId())&&value.domainActorId().equals(graph.action().actorId())&&value.validUntil().isAfter(clock.instant());}))
            .switchIfEmpty(Mono.error(failure())).timeout(scope.remaining());
    }
    private static IllegalStateException failure(){return new IllegalStateException("Exact current original Cards audit capture required");}
}
