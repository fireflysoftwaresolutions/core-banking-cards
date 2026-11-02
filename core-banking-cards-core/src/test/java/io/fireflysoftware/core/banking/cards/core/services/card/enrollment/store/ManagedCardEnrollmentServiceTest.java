package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardEnrollmentServiceImpl;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.LongSupplier;
import org.fireflyframework.core.transaction.OwnedLeaseConnectionFactory;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

/** Synthetic authority/native observations qualify mechanics, not real owner or issuer admission. */
class ManagedCardEnrollmentServiceTest {
    static final Instant NOW=Instant.ofEpochMilli(ManagedCardReservationGraphTest.NOW);
    static UUID id(long n){return ManagedCardReservationGraphTest.id(n);}
    static final SecurityPrincipal PRINCIPAL=new SecurityPrincipal("synthetic-staff","https://synthetic.invalid","synthetic-tenant",Set.of(),Set.of(),Map.of(),NOW,null,Set.of(),Map.of());
    static final class Fixture implements ManagedCardEnrollmentAuthority {
        final ManagedCardReservationRepositoryTest.Driver commandDriver=new ManagedCardReservationRepositoryTest.Driver(),readerDriver=new ManagedCardReservationRepositoryTest.Driver();
        final AtomicLong clockOffset=new AtomicLong(),ids=new AtomicLong(100);final AtomicReference<SecurityPrincipal> principal=new AtomicReference<>(PRINCIPAL);
        final Clock clock;final LongSupplier nanos;final Native commandNative,readerNative;final ManagedCardEnrollmentSession command,reader;final ManagedCardEnrollmentService service;
        boolean missingFence,missingFacts,shortOwnerHorizon,denyFinal,denyBeforeCommit,stallFinal,stallAuthorization;int authorizations,currents;Runnable finalAction=()->{};
        Fixture(){this(null);}
        Fixture(VirtualTimeScheduler virtual){
            clock=new Clock(){public ZoneId getZone(){return ZoneOffset.UTC;}public Clock withZone(ZoneId z){return this;}public Instant instant(){return NOW.plusMillis(clockOffset.get()+(virtual==null?0:virtual.now(java.util.concurrent.TimeUnit.MILLISECONDS)));}};
            nanos=virtual==null?System::nanoTime:()->virtual.now(java.util.concurrent.TimeUnit.NANOSECONDS);
            var cw=new OwnedLeaseConnectionFactory(commandDriver);var cc=DatabaseClient.create(cw);var ct=new R2dbcEntityTemplate(cc,PostgresDialect.INSTANCE);commandNative=new Native(ct);
            command=new ManagedCardEnrollmentSession(ManagedCardEnrollmentSession.Role.COMMAND,cw,cc,ct,new R2dbcTransactionManager(cw),commandNative);
            var rw=new OwnedLeaseConnectionFactory(readerDriver);var rc=DatabaseClient.create(rw);var rt=new R2dbcEntityTemplate(rc,PostgresDialect.INSTANCE);readerNative=new Native(rt);
            reader=new ManagedCardEnrollmentSession(ManagedCardEnrollmentSession.Role.READER,rw,rc,rt,new R2dbcTransactionManager(rw),readerNative);
            SecurityContextPort contexts=()->Mono.justOrEmpty(principal.get());
            service=new ManagedCardEnrollmentServiceImpl(contexts,this,command,reader,clock,nanos,()->id(ids.incrementAndGet()));
        }
        Request request(){return new Request(id(11),9L,"REQUESTED",clock.instant().plusSeconds(5).toEpochMilli());}
        public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal caller,UUID account,Request request){
            authorizations++;assertSame(PRINCIPAL,caller);if(stallAuthorization)return Mono.never();var s=ManagedCardReservationGraphTest.selection();Map<Owner,EligibilityReceipt> facts=new EnumMap<>(Owner.class);
            if(!missingFacts)for(Owner owner:Owner.values())facts.put(owner,new EligibilityReceipt(id(200+owner.ordinal()),owner,s.tenantId(),s.partyId(),s.accountId(),s.contractId(),s.productId(),owner==Owner.ACCOUNT?9L:10L,clock.instant().plusMillis(shortOwnerHorizon?100:5000).toEpochMilli(),Eligibility.ELIGIBLE));
            return Mono.just(new ReservationAuthorization(new Caller(caller.issuer(),caller.subject(),caller.tenantId()),s,facts,missingFence?null:id(16),clock.instant(),clock.instant().plusSeconds(5)));
        }
        public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal caller,UUID account,RecoveryRequest request){
            authorizations++;return Mono.just(new RecoveryAuthorization(new Caller(caller.issuer(),caller.subject(),caller.tenantId()),new Scope(id(1),id(3),id(4)),id(2),clock.instant(),clock.instant().plusSeconds(5)));
        }
        public Mono<Void> requireCurrent(SecurityPrincipal caller,Authorization authorization){
            currents++;assertSame(PRINCIPAL,caller);if(stallFinal&&currents>=2)return Mono.never();if(denyBeforeCommit&&currents==1||denyFinal&&currents>=2)return Mono.error(new IllegalStateException("synthetic withdrawn policy"));return currents>=2?Mono.fromRunnable(finalAction):Mono.empty();
        }
        void copyReadStorage(){readerDriver.rows.putAll(commandDriver.rows);}
        final class Native implements ManagedCardEnrollmentNativeContext {
            final R2dbcEntityTemplate operations;int entered,completed;boolean empty,wrongDigest,longerHorizon,stallComplete,sliver;RuntimeException failure;Request last;
            Native(R2dbcEntityTemplate operations){this.operations=operations;}
            public R2dbcEntityTemplate operations(){return operations;}
            public Mono<Stamp> enter(Request request){return Mono.defer(()->{entered++;last=request;if(failure!=null)return Mono.error(failure);if(empty)return Mono.empty();return Mono.just(new Stamp(request.nonce(),request.actionId(),request.purpose(),wrongDigest?"0".repeat(64):request.scopeDigest(),clock.instant().toEpochMilli(),request.authorityUntil().toEpochMilli(),sliver?clock.instant().plusMillis(1).toEpochMilli():longerHorizon?request.commandUntil().plusMillis(1).toEpochMilli():Math.min(request.commandUntil().toEpochMilli(),clock.instant().plusSeconds(4).toEpochMilli())));});}
            public Mono<UUID> complete(Request request){completed++;return stallComplete?Mono.never():Mono.just(request.nonce());}
        }
    }
    @Test void fiveRowsCommitBeforeProtectedReservationIsReturned(){
        var f=new Fixture();var receipt=f.service.reserve(id(4),f.request()).block();assertEquals(State.RESERVED,receipt.progress().state());assertEquals(Restriction.RETAINED,receipt.progress().restriction());
        assertEquals(5,f.commandDriver.inserts);assertEquals(1,f.commandDriver.commits);assertEquals(0,f.readerDriver.creates);assertEquals(1,f.commandNative.completed);assertEquals(2,f.currents);assertEquals(id(2),receipt.acceptance().selection().actorId());
    }
    @Test void nativeContextBindsTheWholeIntentFenceAndValidatedCaller() {
        var f=new Fixture();var receipt=f.service.reserve(id(4),f.request()).block();
        assertEquals(receipt.acceptance().intentDigest(),f.commandNative.last.intentDigest());assertEquals(id(16),f.commandNative.last.accountFenceReceiptId());
        assertTrue(f.commandNative.last.caller().matches(PRINCIPAL));
    }
    @Test void exactRetryReadsTheOriginalReceiptWithoutAnotherGraphOrOperation(){
        var f=new Fixture();var request=f.request();var first=f.service.reserve(id(4),request).block();var again=f.service.reserve(id(4),request).block();
        assertEquals(first,again);assertEquals(5,f.commandDriver.inserts);assertEquals(2,f.commandNative.entered);assertEquals(2,f.commandDriver.commits);
    }
    @Test void changedIntentRollsBackWithoutMutatingTheRetainedGraph(){
        var f=new Fixture();var request=f.request();f.service.reserve(id(4),request).block();
        var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),new Request(request.actionId(),9L,"CHANGED",request.acceptNotAfterEpochMillis())).block());
        assertEquals(ManagedCardEnrollmentException.Reason.CONFLICT,error.reason());assertEquals(5,f.commandDriver.inserts);assertEquals(1,f.commandDriver.rollbacks);assertTrue(error.transactionFacts().rollbackAcknowledged());
    }
    @Test void recoveryAfterOriginalMutationDeadlineUsesTheReaderAndFreshReadPurpose(){
        var f=new Fixture();var request=f.request();var original=f.service.reserve(id(4),request).block();f.copyReadStorage();f.clockOffset.set(10_000);
        var recovered=f.service.recover(id(4),new RecoveryRequest(id(11),0L)).block();assertEquals(original,recovered);assertEquals(5,f.commandDriver.inserts);assertEquals(0,f.readerDriver.inserts);
        assertEquals(ManagedCardEnrollmentNativeContext.Purpose.RECOVER,f.readerNative.last.purpose());assertTrue(f.readerNative.last.commandUntil().isAfter(Instant.ofEpochMilli(request.acceptNotAfterEpochMillis())));
    }
    @Test void absentPrincipalAndMissingOwnerOrFenceEvidenceRefuseBeforeDatabaseAcquisition(){
        for(int kind=0;kind<3;kind++){var f=new Fixture();if(kind==0)f.principal.set(null);if(kind==1)f.missingFacts=true;if(kind==2)f.missingFence=true;
            assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());assertEquals(0,f.commandDriver.creates);assertEquals(0,f.commandNative.entered);}
    }
    @Test void differentAccountSelectionRefusesBeforeNativeOrBusinessWork(){
        var f=new Fixture();assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(99),f.request()).block());assertEquals(0,f.commandDriver.creates);
    }
    @Test void unavailableMismatchedOrExtendedNativeStampCannotWrite(){
        for(int kind=0;kind<3;kind++){var f=new Fixture();if(kind==0)f.commandNative.empty=true;if(kind==1)f.commandNative.wrongDigest=true;if(kind==2)f.commandNative.longerHorizon=true;
            assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());assertEquals(0,f.commandDriver.inserts);assertEquals(0,f.commandDriver.commits);assertEquals(1,f.commandDriver.rollbacks);}
    }
    @Test void currentPolicyWithdrawalWithholdsReceiptButPreservesKnownCommitFacts(){
        var f=new Fixture();f.denyFinal=true;var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());
        assertEquals(ManagedCardEnrollmentException.Reason.WITHHELD,error.reason());assertTrue(error.transactionFacts().commitAcknowledged());assertTrue(error.transactionFacts().boundaryCompleted());assertEquals(1,f.commandDriver.commits);assertNull(error.getCause());assertFalse(error.toString().contains("synthetic withdrawn"));
    }
    @Test void duplicateFailureDoesNotReadAgainInsideTheAbortedTransaction(){
        var f=new Fixture();f.commandDriver.failInsert=2;var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());
        assertEquals(ManagedCardEnrollmentException.Reason.CONFLICT,error.reason());assertEquals(1,f.commandDriver.rollbacks);assertEquals(0,f.commandDriver.commits);assertTrue(f.commandDriver.sql.getLast().startsWith("INSERT"));
    }
    @Test void expiredFinalHandoffDoesNotRenewTheOriginalWindow(){
        var f=new Fixture();f.finalAction=()->f.clockOffset.addAndGet(5_000);var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());
        assertEquals(ManagedCardEnrollmentException.Reason.WITHHELD,error.reason());assertTrue(error.transactionFacts().boundaryCompleted());assertEquals(1,f.commandDriver.commits);
    }
    @Test void unknownNativeFailureCannotBeRestoredAsABusinessConflict() {
        var f=new Fixture();f.commandNative.failure=new IllegalStateException("synthetic private driver failure");
        var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());
        assertEquals(ManagedCardEnrollmentException.Reason.UNCERTAIN,error.reason());assertNull(error.getCause());assertFalse(error.toString().contains("private driver"));
    }
    @Test void stalledBeginClosesOnlyTheCreatedLeaseAndRemainsUncertain(){
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.commandDriver.begin=Mono.never();return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(8)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);assertFalse(((ManagedCardEnrollmentException)error).transactionFacts().boundaryCompleted());}).verify();
        assertEquals(1,ref.get().commandDriver.closes);assertEquals(0,ref.get().commandDriver.inserts);
    }
    @Test void stalledCommitCannotBecomeNoEffectOrAReplay(){
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.commandDriver.commit=Mono.never();return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(8)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);assertEquals(ManagedCardEnrollmentException.Reason.UNCERTAIN,((ManagedCardEnrollmentException)error).reason());assertFalse(((ManagedCardEnrollmentException)error).transactionFacts().commitAcknowledged());}).verify();
        assertEquals(5,ref.get().commandDriver.inserts);assertEquals(1,ref.get().commandDriver.closes);
    }
    @Test void stalledFinalPolicyIsBoundedByTheNativeWindowAfterKnownCommit(){
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.stallFinal=true;return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(5)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);assertEquals(ManagedCardEnrollmentException.Reason.WITHHELD,((ManagedCardEnrollmentException)error).reason());assertTrue(((ManagedCardEnrollmentException)error).transactionFacts().boundaryCompleted());}).verify();assertEquals(1,ref.get().commandDriver.commits);
    }
    @Test void stalledNativeCompletionIsBoundedBeforeCommit() {
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.commandNative.stallComplete=true;return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(6)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);assertEquals(ManagedCardEnrollmentException.Reason.UNCERTAIN,((ManagedCardEnrollmentException)error).reason());assertFalse(((ManagedCardEnrollmentException)error).transactionFacts().boundaryCompleted());}).verify();
        assertEquals(0,ref.get().commandDriver.commits);assertEquals(1,ref.get().commandDriver.closes);
    }
    @Test void stalledLeaseCloseRetainsTheActualCommitAcknowledgment() {
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.commandDriver.close=Mono.never();return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(6)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);var failure=(ManagedCardEnrollmentException)error;assertEquals(ManagedCardEnrollmentException.Reason.WITHHELD,failure.reason());assertTrue(failure.transactionFacts().commitAcknowledged());assertEquals(org.fireflyframework.core.transaction.CleanupFact.INCOMPLETE,failure.cleanupFact());}).verify();
        assertEquals(1,ref.get().commandDriver.closes);
    }
    @Test void oneMillisecondNativeSliverCannotAdmitOrWrite() {
        var f=new Fixture();f.commandNative.sliver=true;assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());assertEquals(0,f.commandDriver.inserts);assertEquals(0,f.commandDriver.commits);
    }
    @Test void stalledOwnerAuthorizationCannotAcquireADatabaseLease() {
        AtomicReference<Fixture> ref=new AtomicReference<>();StepVerifier.withVirtualTime(()->{var f=new Fixture(VirtualTimeScheduler.get());ref.set(f);f.stallAuthorization=true;return f.service.reserve(id(4),f.request());})
            .thenAwait(Duration.ofSeconds(6)).expectErrorSatisfies(error->{assertInstanceOf(ManagedCardEnrollmentException.class,error);assertFalse(((ManagedCardEnrollmentException)error).transactionFacts().nativeAttempted());}).verify();assertEquals(0,ref.get().commandDriver.creates);
    }
    @Test void readerCannotFallBackToTheCommandSession() {
        var f=new Fixture();assertThrows(IllegalArgumentException.class,()->new ManagedCardEnrollmentServiceImpl(()->Mono.just(PRINCIPAL),f,f.command,f.command,f.clock,f.nanos,()->id(500)));assertEquals(0,f.commandDriver.creates);
    }

    @Test void preCommitWithdrawalRollsBackBeforeAnyReceipt() {
        var f=new Fixture();f.denyBeforeCommit=true;var error=assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());
        assertFalse(error.transactionFacts().boundaryCompleted());assertFalse(error.transactionFacts().commitAcknowledged());assertEquals(0,f.commandDriver.commits);assertEquals(1,f.commandDriver.rollbacks);assertEquals(0,f.commandNative.completed);
    }

    @Test void ownerEvidenceCannotRideALongerPolicyHorizon() {
        var f=new Fixture();f.shortOwnerHorizon=true;assertThrows(ManagedCardEnrollmentException.class,()->f.service.reserve(id(4),f.request()).block());assertEquals(0,f.commandDriver.creates);assertEquals(0,f.commandDriver.inserts);
    }

}
