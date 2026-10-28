/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;
/** Payload-free local failure. It does not establish rollback or business no-effect. */
public final class ManagedCardReservationStoreException extends IllegalStateException {
    public enum Reason { TRANSACTION_REQUIRED,CONFLICT,INCOMPLETE_GRAPH,WRITE_OBSERVATION }
    private final Reason reason;
    public ManagedCardReservationStoreException(Reason reason) { super("MANAGED_CARD_RESERVATION_STORE_"+reason.name());this.reason=reason; }
    public Reason reason() { return reason; }
}
