/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
/** Adapter to unchanged pure contracts; only their explicit conflicts may survive confirmed rollback. */
public final class ManagedCardReservationRules {
    private ManagedCardReservationRules() { }
    public static final class Conflict extends IllegalStateException {
        private Conflict() { super("MANAGED_CARD_RESERVATION_CONFLICT"); }
    }
    public static void validateRecovery(RecoveryRequest request) { ManagedCardProviderRules.validateRecovery(request); }
    public static void requireReplay(ManagedCardReservationGraph retained,Selection current,Request retry) {
        try { ManagedCardEnrollmentRules.requireReplay(retained.selection(),retained.request(),current,retry); }
        catch(IllegalArgumentException|IllegalStateException conflict) { throw new Conflict(); }
    }
    public static void requireRecovery(ManagedCardReservationGraph retained,RecoveryRequest request) {
        var row=retained.operation();var operation=new ManagedCardProviderContracts.Operation(row.operationId(),row.actionId(),retained.selection(),row.intentDigest(),row.acceptNotAfterEpochMillis());
        try { ManagedCardProviderRules.requireRecovery(request,operation,retained.selection(),retained.request(),retained.progress().version()); }
        catch(IllegalArgumentException|IllegalStateException conflict) { throw new Conflict(); }
    }
}
