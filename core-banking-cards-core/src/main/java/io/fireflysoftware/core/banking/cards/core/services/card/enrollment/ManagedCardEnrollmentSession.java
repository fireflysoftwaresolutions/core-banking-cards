/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.*;
import java.time.Duration;
import java.util.Objects;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.core.transaction.impl.DefaultBoundedRootTransaction;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
/** Explicit caller-built graph; preserves client filters, mapping strategy, converter and callbacks. */
public final class ManagedCardEnrollmentSession {
    public enum Role { COMMAND,READER }
    private final Role role;private final OwnedLeaseConnectionFactory factory;private final R2dbcEntityTemplate operations;
    private final ManagedCardReservationRepository repository;private final ManagedCardEnrollmentNativeContext nativeContext;
    private final BoundedRootTransaction transactions;
    public ManagedCardEnrollmentSession(Role role,OwnedLeaseConnectionFactory factory,DatabaseClient client,
            R2dbcEntityTemplate operations,R2dbcTransactionManager manager,ManagedCardEnrollmentNativeContext nativeContext) {
        this.role=Objects.requireNonNull(role);this.factory=Objects.requireNonNull(factory);this.operations=Objects.requireNonNull(operations);this.nativeContext=Objects.requireNonNull(nativeContext);
        if(nativeContext.operations()!=operations)throw new IllegalArgumentException("EXACT_MANAGED_NATIVE_OPERATIONS_REQUIRED");
        var resources=BoundedR2dbcResources.verify(factory,client,operations,manager);
        repository=ManagedCardReservationRepositories.create(operations);
        transactions=new DefaultBoundedRootTransaction(resources,new CleanupPolicy(Duration.ofSeconds(2),Duration.ofSeconds(2)));
    }
    public Role role() { return role; }public OwnedLeaseConnectionFactory factory() { return factory; }
    public R2dbcEntityTemplate operations() { return operations; }public ManagedCardReservationRepository repository() { return repository; }
    public ManagedCardEnrollmentNativeContext nativeContext() { return nativeContext; }public BoundedRootTransaction transactions() { return transactions; }
}
