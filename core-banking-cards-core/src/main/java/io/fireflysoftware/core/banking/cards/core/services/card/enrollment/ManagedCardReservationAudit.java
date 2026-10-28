/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import java.time.Clock;
import java.util.function.LongSupplier;
import org.fireflyframework.core.audit.TransactionalAuditAppender.ProvisionalAppend;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import reactor.core.publisher.Mono;

/** Opt-in original reservation capture; the owning root alone acknowledges commit. */
public interface ManagedCardReservationAudit {
    enum BusinessDisposition { NEW_RESERVATION, EXACT_RESERVE_REPLAY }
    OwnedLeaseConnectionFactory factory();R2dbcEntityTemplate operations();
    SecurityContextPort principals();ManagedCardEnrollmentAuthority authority();Clock clock();LongSupplier nanos();
    Mono<ProvisionalAppend> append(RootTransactionScope scope,java.util.UUID reservationNonce,ManagedCardEnrollmentAuthority.ReservationAuthorization authorization,
            ManagedCardReservationGraph graph,BusinessDisposition disposition);
}
