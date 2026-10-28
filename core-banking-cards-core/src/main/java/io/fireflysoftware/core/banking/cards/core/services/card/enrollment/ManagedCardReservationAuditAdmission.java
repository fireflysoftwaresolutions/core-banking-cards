/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import java.time.Instant;
import java.util.*;
import org.fireflyframework.kernel.audit.AuditActor;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import reactor.core.publisher.Mono;

/** Required trusted current owner/profile/PDP projection; no implementation or fallback is installed. */
public interface ManagedCardReservationAuditAdmission {
    /** Proves the authenticated client/staff namespace and its relation to the domain actor and resource. */
    record Observation(AuditActor actor,UUID domainActorId,String policyRevision,Instant validUntil){
        public Observation{Objects.requireNonNull(actor);Objects.requireNonNull(domainActorId);Objects.requireNonNull(policyRevision);Objects.requireNonNull(validUntil);}
    }
    Mono<Observation> requireCurrent(SecurityPrincipal principal,ManagedCardEnrollmentAuthority.ReservationAuthorization authorization,ManagedCardReservationGraph graph);
}
