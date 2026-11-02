/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import java.util.Objects;
/** Protected local reservation metadata; never an issued, active or financially admitted card. */
public record ManagedCardReservationReceipt(Acceptance acceptance,Progress progress) {
    public ManagedCardReservationReceipt {
        Objects.requireNonNull(acceptance);Objects.requireNonNull(progress);
        if(!acceptance.actionId().equals(progress.actionId())||!acceptance.selection().managedCardId().equals(progress.managedCardId()))throw new IllegalArgumentException("EXACT_RESERVATION_RECEIPT_REQUIRED");
    }
    @Override public String toString() { return "ManagedCardReservationReceipt"; }
}
