/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
/** Required real V8 context/lock/clock bridge. Unimplemented until its separate schema/role gate. */
public interface ManagedCardEnrollmentNativeContext {
    enum Purpose { RESERVE,RECOVER }
    record Request(UUID nonce,Scope scope,UUID actorId,UUID actionId,Purpose purpose,ManagedCardEnrollmentAuthority.Caller caller,UUID accountFenceReceiptId,String intentDigest,String scopeDigest,Instant authorityUntil,Instant commandUntil) {
        @Override public String toString() { return "ManagedCardNativeContextRequest"; }
    }
    record Stamp(UUID nonce,UUID actionId,Purpose purpose,String scopeDigest,long dbNowMillis,long authorityUntilMillis,long commandUntilMillis) { }
    R2dbcEntityTemplate operations();
    /** Lock tenant/account/action in the declared order before any duplicate lookup or ordinary write. */
    Mono<Stamp> enter(Request request);
    /** Actual installed-context completion acknowledgment; never a business permission. */
    Mono<UUID> complete(Request request);
}
