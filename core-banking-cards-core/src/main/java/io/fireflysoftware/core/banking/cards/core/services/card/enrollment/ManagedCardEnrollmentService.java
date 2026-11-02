/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.Request;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardReservationReceipt;
import java.util.UUID;
import reactor.core.publisher.Mono;
public interface ManagedCardEnrollmentService {
    Mono<ManagedCardReservationReceipt> reserve(UUID accountId,Request request);
    Mono<ManagedCardReservationReceipt> recover(UUID accountId,RecoveryRequest request);
}
