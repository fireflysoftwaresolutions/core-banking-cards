/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentNativeContext;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardNativeContextRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

/** Real technical scope/clock bridge on the exact caller-owned transaction graph. */
public final class ManagedCardEnrollmentNativeContextImpl implements ManagedCardEnrollmentNativeContext {
    private final R2dbcEntityTemplate operations;
    private final ManagedCardNativeContextRepository repository;
    public ManagedCardEnrollmentNativeContextImpl(R2dbcEntityTemplate operations) {
        this.operations=Objects.requireNonNull(operations);
        repository=new R2dbcRepositoryFactory(operations).getRepository(ManagedCardNativeContextRepository.class);
    }
    @Override public R2dbcEntityTemplate operations(){return operations;}
    @Override public Mono<Stamp> enter(Request request){return transaction(()->{
        Objects.requireNonNull(request);var scope=request.scope();var caller=request.caller();
        return repository.enter(request.nonce(),scope.tenantId(),scope.partyId(),scope.accountId(),request.actorId(),request.actionId(),request.purpose().name(),
                caller.issuer(),caller.subject(),caller.tenantKey(),request.accountFenceReceiptId(),request.intentDigest(),request.scopeDigest(),
                OffsetDateTime.ofInstant(request.authorityUntil(),ZoneOffset.UTC),OffsetDateTime.ofInstant(request.commandUntil(),ZoneOffset.UTC))
            .map(row->new Stamp(row.contextNonce(),row.actionId(),Purpose.valueOf(row.purpose()),row.scopeDigest(),
                    Objects.requireNonNull(row.dbNowMillis()),Objects.requireNonNull(row.authorityUntilMillis()),Objects.requireNonNull(row.commandUntilMillis())));
    });}
    @Override public Mono<UUID> complete(Request request){return transaction(()->repository.complete(Objects.requireNonNull(request).nonce()));}
    private static <T> Mono<T> transaction(Supplier<Mono<T>> work){
        return TransactionSynchronizationManager.forCurrentTransaction().flatMap(manager->{
            if(!manager.isActualTransactionActive())return Mono.error(new IllegalStateException("MANAGED_NATIVE_TRANSACTION_REQUIRED"));
            return Mono.defer(work);
        }).onErrorMap(NoTransactionException.class,error->new IllegalStateException("MANAGED_NATIVE_TRANSACTION_REQUIRED"));
    }
}
