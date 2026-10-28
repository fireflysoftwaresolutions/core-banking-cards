/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.impl;

import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.*;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationOperations;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationStoreException;
import static io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationStoreException.Reason.*;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

/** Ordinary mapped operations only; current admission and native locks are separate required gates. */
public final class ManagedCardReservationOperationsImpl implements ManagedCardReservationOperations {
    private final R2dbcEntityTemplate operations;
    public ManagedCardReservationOperationsImpl(R2dbcEntityTemplate operations) { this.operations=Objects.requireNonNull(operations); }
    @Override public R2dbcEntityTemplate operations() { return operations; }
    @Override public Mono<ManagedCardReservationGraph> appendReserved(ManagedCardReservationGraph graph) {
        Objects.requireNonNull(graph);
        return transaction(()->insert(graph.card()).then(insert(graph.action())).then(insert(graph.operation()))
                .then(insert(graph.event())).then(insert(graph.outbox()))
                // An emitted entity is not an affected-row count; verify real scoped cardinality and readback.
                .then(exactCount(cardQuery(graph),Card.class)).then(exactCount(actionQuery(graph.scope(),graph.action().actionId()),Action.class))
                .then(exactCount(child(graph, "operationId",graph.action().operationId()),Operation.class))
                .then(exactCount(child(graph,"eventId",graph.action().eventId()),Event.class))
                .then(exactCount(child(graph,"outboxId",graph.action().outboxId()),Outbox.class))
                .then(load(graph.scope(),graph.action().actionId()))
                .filter(graph::equals).switchIfEmpty(Mono.error(new ManagedCardReservationStoreException(WRITE_OBSERVATION))));
    }
    @Override public Mono<ManagedCardReservationGraph> loadReserved(Scope scope,UUID actionId) {
        Objects.requireNonNull(scope);Objects.requireNonNull(actionId);return transaction(()->load(scope,actionId));
    }
    private Mono<ManagedCardReservationGraph> load(Scope scope,UUID actionId) {
        return operations.select(Action.class).matching(actionQuery(scope,actionId)).one().flatMap(action->{
            if(!scope.equals(action.scope())||!actionId.equals(action.actionId()))return Mono.error(new ManagedCardReservationStoreException(INCOMPLETE_GRAPH));
            Criteria children=Criteria.where("tenantId").is(scope.tenantId()).and("actionId").is(actionId);
            return Mono.zip(
                operations.select(Card.class).matching(Query.query(scope(scope).and("managedCardId").is(action.managedCardId()).and("actionId").is(actionId))).one(),
                operations.select(Operation.class).matching(Query.query(children.and("operationId").is(action.operationId()))).one(),
                operations.select(Event.class).matching(Query.query(children.and("eventId").is(action.eventId()))).one(),
                operations.select(Outbox.class).matching(Query.query(children.and("outboxId").is(action.outboxId()))).one())
                .map(rows->new ManagedCardReservationGraph(rows.getT1(),action,rows.getT2(),rows.getT3(),rows.getT4()))
                .switchIfEmpty(Mono.error(new ManagedCardReservationStoreException(INCOMPLETE_GRAPH)));
        });
    }
    private <T> Mono<Void> insert(T row) {
        return operations.insert(row).filter(row::equals)
                .switchIfEmpty(Mono.error(new ManagedCardReservationStoreException(WRITE_OBSERVATION))).then();
    }
    private Mono<Void> exactCount(Query query,Class<?> type) {
        return operations.count(query,type).switchIfEmpty(Mono.error(new ManagedCardReservationStoreException(WRITE_OBSERVATION)))
                .flatMap(count->count==1?Mono.<Void>empty():Mono.error(new ManagedCardReservationStoreException(WRITE_OBSERVATION)));
    }
    private <T> Mono<T> transaction(Supplier<Mono<T>> work) {
        return TransactionSynchronizationManager.forCurrentTransaction().flatMap(manager->{
            if(!manager.isActualTransactionActive())return Mono.error(new ManagedCardReservationStoreException(TRANSACTION_REQUIRED));
            return Mono.defer(work);
        }).onErrorMap(NoTransactionException.class,error->new ManagedCardReservationStoreException(TRANSACTION_REQUIRED))
          .onErrorMap(DataIntegrityViolationException.class,error->new ManagedCardReservationStoreException(CONFLICT))
          .onErrorMap(IncorrectResultSizeDataAccessException.class,error->new ManagedCardReservationStoreException(INCOMPLETE_GRAPH));
    }
    private static Criteria scope(Scope scope) { return Criteria.where("tenantId").is(scope.tenantId()).and("partyId").is(scope.partyId()).and("accountId").is(scope.accountId()); }
    private static Query actionQuery(Scope scope,UUID actionId) { return Query.query(scope(scope).and("actionId").is(actionId)); }
    private static Query cardQuery(ManagedCardReservationGraph graph) { return Query.query(scope(graph.scope()).and("managedCardId").is(graph.card().managedCardId()).and("actionId").is(graph.action().actionId())); }
    private static Query child(ManagedCardReservationGraph graph,String key,UUID id) { return Query.query(Criteria.where("tenantId").is(graph.scope().tenantId()).and("actionId").is(graph.action().actionId()).and(key).is(id)); }
}
