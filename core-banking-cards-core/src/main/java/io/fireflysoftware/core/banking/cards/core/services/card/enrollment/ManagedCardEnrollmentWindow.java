/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;
import java.time.*;
import java.util.Objects;
import java.util.function.LongSupplier;
import org.fireflyframework.core.transaction.R2dbcOperationBudget;
import org.fireflyframework.core.transaction.RootTransactionScope;
/** Technical bounds only; no observation creates caller or owner authority. */
public final class ManagedCardEnrollmentWindow {
    private static final Duration MAXIMUM=Duration.ofSeconds(5);
    private final Clock clock;private final LongSupplier nanos;private final long beforeAuthorization;
    private final Instant started,bootstrapUntil;private R2dbcOperationBudget budget;
    public ManagedCardEnrollmentWindow(Clock clock,LongSupplier nanos) {
        this.clock=Objects.requireNonNull(clock);this.nanos=Objects.requireNonNull(nanos);
        beforeAuthorization=nanos.getAsLong();started=clock.instant();bootstrapUntil=started.plus(MAXIMUM);
        budget=R2dbcOperationBudget.observed(started,bootstrapUntil,beforeAuthorization,MAXIMUM,clock,nanos);
    }
    public void constrain(ManagedCardEnrollmentAuthority.Authorization authorization) {
        var observed=R2dbcOperationBudget.observed(authorization.observedAt(),authorization.effectiveUntil(),beforeAuthorization,MAXIMUM,clock,nanos);
        budget=budget.intersect(observed);budget.remaining();
    }
    public R2dbcOperationBudget budget() { budget.remaining();return budget; }
    public Duration remaining() { return budget.remaining(); }
    public long beforeNative() { remaining();return nanos.getAsLong(); }
    public Instant commandUntil(Instant authorityUntil,Instant requestedUntil) {
        Instant end=bootstrapUntil.isBefore(authorityUntil)?bootstrapUntil:authorityUntil;
        if(requestedUntil!=null&&requestedUntil.isBefore(end))end=requestedUntil;
        remaining();return end;
    }
    public void installed(ManagedCardEnrollmentNativeContext.Request requested,ManagedCardEnrollmentNativeContext.Stamp actual,
            long beforeNative,RootTransactionScope scope) {
        if(actual==null||!Objects.equals(requested.nonce(),actual.nonce())||!Objects.equals(requested.actionId(),actual.actionId())
                ||requested.purpose()!=actual.purpose()||!Objects.equals(requested.scopeDigest(),actual.scopeDigest())
                ||actual.dbNowMillis()<=0||actual.authorityUntilMillis()>requested.authorityUntil().toEpochMilli()
                ||actual.commandUntilMillis()>requested.commandUntil().toEpochMilli())throw new IllegalStateException("ACTUAL_MANAGED_NATIVE_STAMP_REQUIRED");
        long until=Math.subtractExact(Math.min(actual.authorityUntilMillis(),actual.commandUntilMillis()),1L);
        if(until<=actual.dbNowMillis())throw new IllegalStateException("MANAGED_NATIVE_WINDOW_EXHAUSTED");
        var nativeCap=R2dbcOperationBudget.observed(Instant.ofEpochMilli(actual.dbNowMillis()),Instant.ofEpochMilli(until),beforeNative,MAXIMUM,clock,nanos);
        budget=budget.intersect(nativeCap);scope.narrow(nativeCap);budget.remaining();
    }
    @Override public String toString() { return "ManagedCardEnrollmentWindow"; }
}
