/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Action;
import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;
/** Explicitly constructed on a qualified role/template; never a general CRUD bean. */
@NoRepositoryBean
public interface ManagedCardReservationRepository extends Repository<Action,UUID>,ManagedCardReservationOperations {
    Mono<Action> findByTenantIdAndPartyIdAndAccountIdAndActionId(UUID tenantId,UUID partyId,UUID accountId,UUID actionId);
}
