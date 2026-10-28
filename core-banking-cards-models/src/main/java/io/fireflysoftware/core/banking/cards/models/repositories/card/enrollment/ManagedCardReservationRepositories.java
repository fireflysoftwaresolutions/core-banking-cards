/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.impl.ManagedCardReservationOperationsImpl;
import java.util.Objects;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
public final class ManagedCardReservationRepositories {
    private ManagedCardReservationRepositories() { }
    public static ManagedCardReservationRepository create(R2dbcEntityTemplate operations) {
        Objects.requireNonNull(operations);
        var fragment=new ManagedCardReservationOperationsImpl(operations);
        return new R2dbcRepositoryFactory(operations).getRepository(ManagedCardReservationRepository.class,RepositoryFragments.just(fragment));
    }
}
