/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.config.ManagedCardEnrollmentConfiguration;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.Request;
import java.util.concurrent.atomic.AtomicInteger;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import reactor.core.publisher.Mono;

/** Unavailable ports only: construction/refusal is not genuine upstream authorization. */
class ManagedCardAuditedConfigurationTest {
    static final class RefusingPorts implements ManagedCardEnrollmentAuthority {
        final AtomicInteger principalReads=new AtomicInteger(),ownerReads=new AtomicInteger(),auditReads=new AtomicInteger();
        final SecurityContextPort principals=()->Mono.defer(()->{principalReads.incrementAndGet();return Mono.empty();});
        final ManagedCardReservationAuditAdmission admission=(principal,authorization,graph)->Mono.defer(()->{auditReads.incrementAndGet();return Mono.error(new IllegalStateException("Genuine audit profile/PDP unavailable"));});
        public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal principal,java.util.UUID account,Request request){ownerReads.incrementAndGet();return Mono.error(new IllegalStateException("Genuine owner authority unavailable"));}
        public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal principal,java.util.UUID account,io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest request){ownerReads.incrementAndGet();return Mono.error(new IllegalStateException("Genuine owner recovery unavailable"));}
        public Mono<Void> requireCurrent(SecurityPrincipal principal,Authorization authorization){ownerReads.incrementAndGet();return Mono.error(new IllegalStateException("Genuine current owner authority unavailable"));}
    }
    ApplicationContextRunner context(ManagedCardAuditFixture f,RefusingPorts ports){return new ApplicationContextRunner().withUserConfiguration(ManagedCardEnrollmentConfiguration.class)
        .withPropertyValues("firefly.cards.managed-enrollment.enabled=true")
        .withBean(SecurityContextPort.class,()->ports.principals).withBean(ManagedCardEnrollmentAuthority.class,()->ports)
        .withBean("managedCardCommandSession",ManagedCardEnrollmentSession.class,()->f.command).withBean("managedCardReaderSession",ManagedCardEnrollmentSession.class,()->f.reader);}
    @Test void enablingWithExistingOwnerSessionsButNoAuditAdmissionCannotCreateUnauditedService(){var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();context(f,ports).run(app->{assertNotNull(app.getStartupFailure());assertEquals(0,f.driver.creates);assertEquals(0,f.readerDriver.creates);assertEquals(0,ports.ownerReads.get());});}
    @Test void completeExplicitRefusingBindingsConstructAndRefuseBeforeDatabaseAcquisition(){var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();context(f,ports).withBean(ManagedCardReservationAuditAdmission.class,()->ports.admission).run(app->{assertNull(app.getStartupFailure());var service=app.getBean(ManagedCardEnrollmentService.class);var error=assertThrows(ManagedCardEnrollmentException.class,()->service.reserve(ManagedCardAuditFixture.id(4),new Request(ManagedCardAuditFixture.id(11),9L,"REQUESTED",System.currentTimeMillis()+5000)).block());assertEquals(ManagedCardEnrollmentException.Reason.UNAVAILABLE,error.reason());assertFalse(error.transactionFacts().nativeAttempted());assertEquals(0,f.driver.creates);assertEquals(0,f.readerDriver.creates);assertEquals(1,ports.principalReads.get());assertEquals(0,ports.ownerReads.get());assertEquals(0,ports.auditReads.get());});}
    @Test void auditAdmissionCannotReplaceMissingGenuineOwnerAuthority(){var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();new ApplicationContextRunner().withUserConfiguration(ManagedCardEnrollmentConfiguration.class).withPropertyValues("firefly.cards.managed-enrollment.enabled=true").withBean(SecurityContextPort.class,()->ports.principals).withBean(ManagedCardReservationAuditAdmission.class,()->ports.admission).withBean("managedCardCommandSession",ManagedCardEnrollmentSession.class,()->f.command).withBean("managedCardReaderSession",ManagedCardEnrollmentSession.class,()->f.reader).run(app->{assertNotNull(app.getStartupFailure());assertEquals(0,f.driver.creates);assertEquals(0,f.readerDriver.creates);});}

    @Test void commandAuditFactoryRetainsExactOriginalGraphTimePairAndPorts(){var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();var audit=f.command.reservationAudit(ports.principals,ports,ports.admission,f.clock,f.nanos,java.util.UUID::randomUUID);assertInstanceOf(io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardReservationAuditImpl.class,audit);assertSame(f.factory,audit.factory());assertSame(f.template,audit.operations());assertSame(ports.principals,audit.principals());assertSame(ports,audit.authority());assertSame(f.clock,audit.clock());assertSame(f.nanos,audit.nanos());assertEquals(0,f.driver.creates);assertEquals(0,ports.principalReads.get());assertEquals(0,ports.ownerReads.get());assertEquals(0,ports.auditReads.get());}
    @Test void readerCannotCreateACommandAuditOrAcquireThroughTheFactory(){var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();assertThrows(IllegalArgumentException.class,()->f.reader.reservationAudit(ports.principals,ports,ports.admission,f.clock,f.nanos,java.util.UUID::randomUUID));assertEquals(0,f.driver.creates);assertEquals(0,f.readerDriver.creates);}
    @Test void retainedOriginalRootGraphReachesUnavailablePrincipalAndRollsBackBeforeAnyStatement(){
        var f=new ManagedCardAuditFixture();var ports=new RefusingPorts();var audit=f.command.reservationAudit(ports.principals,ports,ports.admission,f.clock,f.nanos,java.util.UUID::randomUUID);
        // These are value-only rows; no port authenticates or authorizes them.
        var graph=ManagedCardReservationGraphTest.graph();var authorization=new ManagedCardEnrollmentAuthority.ReservationAuthorization(new ManagedCardEnrollmentAuthority.Caller("https://unavailable.invalid","unavailable","sandbox-platform"),graph.selection(),java.util.Map.of(),graph.action().accountFenceReceiptId(),f.now,f.now.plusSeconds(5));
        var budget=org.fireflyframework.core.transaction.R2dbcOperationBudget.observed(f.now,f.now.plusSeconds(5),f.nanos.getAsLong(),java.time.Duration.ofSeconds(5),f.clock,f.nanos);
        var failure=assertThrows(org.fireflyframework.core.transaction.RootTransactionException.class,()->f.command.transactions().execute(budget,new org.springframework.transaction.support.DefaultTransactionDefinition(),scope->audit.append(scope,ManagedCardAuditFixture.id(77),authorization,graph,ManagedCardReservationAudit.BusinessDisposition.NEW_RESERVATION)).block());
        assertEquals(1,ports.principalReads.get());assertEquals(0,ports.ownerReads.get());assertEquals(0,ports.auditReads.get());assertTrue(failure.transactionFacts().rollbackAcknowledged());assertFalse(failure.transactionFacts().commitAcknowledged());assertEquals(org.fireflyframework.core.transaction.CleanupFact.COMPLETE,failure.cleanupFact());assertEquals(1,f.driver.creates);assertEquals(1,f.driver.rollbacks);assertTrue(f.driver.sql.isEmpty());assertTrue(f.driver.tables.isEmpty());assertEquals(0,f.driver.auditPairs);
    }
}
