/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import java.util.UUID;
import org.fireflyframework.core.transaction.*;
/** Only the safe action reference and exact technical facts; never a private receipt or no-effect claim. */
public final class ManagedCardEnrollmentException extends IllegalStateException {
    public enum Reason { UNAVAILABLE,CONFLICT,NOT_FOUND,WITHHELD,UNCERTAIN }
    private final UUID actionId;private final Reason reason;private final TransactionFacts facts;private final CleanupFact cleanup;
    public ManagedCardEnrollmentException(UUID actionId,Reason reason,TransactionFacts facts,CleanupFact cleanup) {
        super("MANAGED_CARD_ENROLLMENT_"+reason.name());this.actionId=actionId;this.reason=reason;this.facts=facts;this.cleanup=cleanup;
    }
    public UUID actionId() { return actionId; }public Reason reason() { return reason; }
    public TransactionFacts transactionFacts() { return facts; }public CleanupFact cleanupFact() { return cleanup; }
}
