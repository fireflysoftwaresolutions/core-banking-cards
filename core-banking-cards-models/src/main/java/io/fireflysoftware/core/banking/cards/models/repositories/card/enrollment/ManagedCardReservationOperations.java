/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import java.util.UUID;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
public interface ManagedCardReservationOperations {
    R2dbcEntityTemplate operations();
    Mono<ManagedCardReservationGraph> appendReserved(ManagedCardReservationGraph graph);
    Mono<ManagedCardReservationGraph> loadReserved(Scope scope,UUID actionId);
}
