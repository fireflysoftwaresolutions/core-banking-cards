package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardEnrollmentServiceImpl;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.Request;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import java.util.UUID;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class ManagedCardFenceReplayReviewTest {
    @Test void retryCannotReturnOriginalReservationUnderADifferentAccountFenceReceipt() {
        var fixture = new ManagedCardEnrollmentServiceTest.Fixture();
        var request = fixture.request();
        var account = ManagedCardEnrollmentServiceTest.id(4);
        fixture.service.reserve(account, request).block();
        var rowsBefore = new java.util.HashMap<>(fixture.commandDriver.rows);
        ManagedCardEnrollmentAuthority changedFence = new ManagedCardEnrollmentAuthority() {
            public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal caller, UUID accountId, Request value) {
                return fixture.authorizeReservation(caller, accountId, value).map(prior -> new ReservationAuthorization(
                        prior.caller(), prior.selection(), prior.eligibility(), ManagedCardEnrollmentServiceTest.id(999),
                        prior.observedAt(), prior.effectiveUntil()));
            }
            public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal caller, UUID accountId, RecoveryRequest value) {
                return fixture.authorizeRecovery(caller, accountId, value);
            }
            public Mono<Void> requireCurrent(SecurityPrincipal caller, Authorization value) {
                return fixture.requireCurrent(caller, value);
            }
        };
        var retry = new ManagedCardEnrollmentServiceImpl(() -> Mono.just(ManagedCardEnrollmentServiceTest.PRINCIPAL),
                changedFence, fixture.command, fixture.reader, fixture.clock, fixture.nanos,
                () -> ManagedCardEnrollmentServiceTest.id(fixture.ids.incrementAndGet()));
        var error = assertThrows(ManagedCardEnrollmentException.class, () -> retry.reserve(account, request).block());
        assertEquals(ManagedCardEnrollmentException.Reason.CONFLICT, error.reason());
        assertTrue(error.transactionFacts().rollbackAcknowledged());
        assertFalse(error.transactionFacts().commitAcknowledged());
        assertFalse(error.transactionFacts().boundaryCompleted());
        assertEquals(1, fixture.commandDriver.rollbacks);
        assertEquals(1, fixture.commandDriver.commits);
        assertEquals(1, fixture.commandNative.completed);
        assertEquals(rowsBefore, fixture.commandDriver.rows);
        assertNull(error.getCause());
        assertEquals(5, fixture.commandDriver.inserts);
    }
    @Test void matchingFenceRetryReturnsTheExactReceiptWithoutAnotherProviderOperation() {
        var fixture = new ManagedCardEnrollmentServiceTest.Fixture();
        var request = fixture.request();
        var account = ManagedCardEnrollmentServiceTest.id(4);
        var first = fixture.service.reserve(account, request).block();
        var rowsBefore = new java.util.HashMap<>(fixture.commandDriver.rows);
        var again = fixture.service.reserve(account, request).block();
        assertEquals(first, again);
        assertEquals(rowsBefore, fixture.commandDriver.rows);
        assertEquals(5, fixture.commandDriver.inserts);
        assertEquals(2, fixture.commandDriver.commits);
        assertEquals(0, fixture.commandDriver.rollbacks);
        assertEquals(2, fixture.commandNative.completed);
    }
    @Test void recoveryUsesFreshReadAuthorityWithoutRequestingAReplacementReservationFence() {
        var fixture = new ManagedCardEnrollmentServiceTest.Fixture();
        var account = ManagedCardEnrollmentServiceTest.id(4);
        var first = fixture.service.reserve(account, fixture.request()).block();
        fixture.copyReadStorage();
        fixture.clockOffset.set(10_000);
        ManagedCardEnrollmentAuthority recoveryOnly = new ManagedCardEnrollmentAuthority() {
            public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal caller, UUID accountId, Request request) {
                return Mono.error(new AssertionError("Recovery must not request reservation fence authority"));
            }
            public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal caller, UUID accountId, RecoveryRequest request) {
                return fixture.authorizeRecovery(caller, accountId, request);
            }
            public Mono<Void> requireCurrent(SecurityPrincipal caller, Authorization value) {
                assertInstanceOf(RecoveryAuthorization.class, value);
                return fixture.requireCurrent(caller, value);
            }
        };
        var service = new ManagedCardEnrollmentServiceImpl(() -> Mono.just(ManagedCardEnrollmentServiceTest.PRINCIPAL),
                recoveryOnly, fixture.command, fixture.reader, fixture.clock, fixture.nanos,
                () -> ManagedCardEnrollmentServiceTest.id(fixture.ids.incrementAndGet()));
        var recovered = service.recover(account, new RecoveryRequest(ManagedCardEnrollmentServiceTest.id(11), 0L)).block();
        assertEquals(first, recovered);
        assertEquals(ManagedCardEnrollmentNativeContext.Purpose.RECOVER, fixture.readerNative.last.purpose());
        assertNull(fixture.readerNative.last.accountFenceReceiptId());
        assertEquals(5, fixture.commandDriver.inserts);
        assertEquals(0, fixture.readerDriver.inserts);
        assertEquals(1, fixture.readerDriver.commits);
    }
}
