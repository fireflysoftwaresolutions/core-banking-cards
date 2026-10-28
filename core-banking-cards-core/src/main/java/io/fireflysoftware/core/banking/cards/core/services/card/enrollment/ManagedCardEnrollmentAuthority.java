/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import java.time.Instant;
import java.util.*;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import reactor.core.publisher.Mono;

/**
 * Required genuine owner/current-policy bridge. No implementation or permissive fallback is supplied.
 * Implementations must resolve verified caller namespaces, bank/contract/party purposes and the complete
 * account-owned pending fence; supplied UUIDs or the two pure eligibility facts are insufficient authority.
 */
public interface ManagedCardEnrollmentAuthority {
    record Caller(String issuer,String subject,String tenantKey) {
        public Caller { if(!text(issuer)||!text(subject)||!text(tenantKey))throw new IllegalArgumentException("VALIDATED_CALLER_BINDING_REQUIRED"); }
        private static boolean text(String value) { return value!=null&&!value.isBlank()&&value.length()<=2048; }
        public boolean matches(SecurityPrincipal principal) { return principal!=null&&Objects.equals(issuer,principal.issuer())&&Objects.equals(subject,principal.subject())&&Objects.equals(tenantKey,principal.tenantId()); }
        @Override public String toString() { return "ValidatedCallerBinding"; }
    }
    /** effectiveUntil must be the actual minimum of all owner, fence, validated-caller and PDP bounds. */
    sealed interface Authorization permits ReservationAuthorization,RecoveryAuthorization {
        Caller caller();Scope scope();UUID actorId();Instant observedAt();Instant effectiveUntil();
    }
    record ReservationAuthorization(Caller caller,Selection selection,Map<Owner,EligibilityReceipt> eligibility,
            UUID accountFenceReceiptId,Instant observedAt,Instant effectiveUntil) implements Authorization {
        public ReservationAuthorization { Objects.requireNonNull(caller);Objects.requireNonNull(selection);eligibility=eligibility==null?Map.of():Map.copyOf(eligibility); }
        public Scope scope() { return new Scope(selection.tenantId(),selection.partyId(),selection.accountId()); }
        public UUID actorId() { return selection.actorId(); }
        @Override public String toString() { return "ReservationAuthorization"; }
    }
    record RecoveryAuthorization(Caller caller,Scope scope,UUID actorId,Instant observedAt,Instant effectiveUntil) implements Authorization {
        public RecoveryAuthorization { Objects.requireNonNull(caller);Objects.requireNonNull(scope); }
        @Override public String toString() { return "RecoveryAuthorization"; }
    }
    Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal principal,UUID accountId,Request request);
    Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal principal,UUID accountId,RecoveryRequest request);
    /**
     * Re-evaluate the current principal and all owner/purpose/fence/PDP bindings. A successful completion
     * must still cover the inherited authorization bound; shorter observations must refuse, never mint
     * a replacement TTL. This bridge cannot silently return a new, unreported authority horizon.
     */
    Mono<Void> requireCurrent(SecurityPrincipal currentPrincipal,Authorization authorization);
}
