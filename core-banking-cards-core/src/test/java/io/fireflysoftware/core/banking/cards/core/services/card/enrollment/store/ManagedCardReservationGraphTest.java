package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentRules;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardReservationFactory;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import static org.junit.jupiter.api.Assertions.*;

class ManagedCardReservationGraphTest {
    static final long NOW = Instant.parse("2026-10-27T12:00:00Z").toEpochMilli();
    static UUID id(long value) { return new UUID(1, value); }
    static Selection selection() { return new Selection(id(1),id(2),id(3),id(4),id(5),id(6),new ManagedCardId(id(7)),id(8),9L,10L,new ConfiguredProviders("sandbox-issuer","sandbox-processor")); }
    static Request request() { return new Request(id(11),9L,"REQUESTED",NOW+5000); }
    static ManagedCardReservationGraph graph() { return ManagedCardReservationFactory.create(selection(),request(),id(12),id(13),id(14),id(15),id(16),NOW); }
    @Test void exactImmutableSelectionAndOriginalRequestAreRetained() {
        var g=graph();assertEquals(selection(),g.selection());assertEquals(request(),g.request());
        assertEquals(ManagedCardEnrollmentRules.digest(selection(),request()),g.action().intentDigest());
        assertEquals(id(13),g.acceptance().receiptId());assertEquals(NOW,g.acceptance().databaseRecordedAtEpochMillis());
        assertEquals(State.RESERVED,g.progress().state());assertEquals(Restriction.RETAINED,g.progress().restriction());assertEquals(0,g.progress().version());
    }
    @Test void fiveRowsHaveTheirOwnMappedOpaqueIdentities() {
        var context=new R2dbcMappingContext();var tables=new HashSet<String>();
        for(var row:graph().rows()) {var entity=context.getRequiredPersistentEntity(row.getClass());tables.add(entity.getTableName().getReference());assertEquals(UUID.class,entity.getRequiredIdProperty().getType());}
        assertEquals(Set.of("managed_card","managed_card_enrollment_action","managed_card_provider_operation","managed_card_enrollment_event","managed_card_enrollment_outbox"),tables);
    }
    @Test void managedRowsCannotCarryLegacyCardCredentialsOrRawProviderPayloads() {
        for(var row:graph().rows())for(var field:row.getClass().getRecordComponents())assertFalse(Set.of("pan","cardNumber","pin","cvv","rawResponse","payload").contains(field.getName()));
        assertEquals(id(7),graph().card().managedCardId());assertEquals(id(16),graph().action().accountFenceReceiptId());
    }
    @Test void admissionRecordedAtIsNativeSuppliedAndCannotReachOriginalDeadline() {
        assertThrows(IllegalArgumentException.class,()->ManagedCardReservationFactory.create(selection(),request(),id(12),id(13),id(14),id(15),id(16),NOW+5000));
        assertThrows(IllegalArgumentException.class,()->ManagedCardReservationFactory.create(selection(),request(),id(12),id(13),id(14),id(15),id(16),0));
    }
    @Test void incompleteOrCrossLinkedGraphCannotBecomeAReservation() {
        var g=graph();assertThrows(IllegalArgumentException.class,()->new ManagedCardReservationGraph(g.card(),g.action(),g.operation(),null,g.outbox()));
        var other=ManagedCardReservationFactory.create(selection(),request(),id(22),id(23),id(24),id(25),id(26),NOW);
        assertThrows(IllegalArgumentException.class,()->new ManagedCardReservationGraph(g.card(),g.action(),g.operation(),other.event(),g.outbox()));
    }
    @Test void pureFactoryDoesNotClaimEligibilityOrProviderCompletion() {
        var g=graph();assertEquals("RESERVATION_RECORDED",g.event().kind());assertEquals("COORDINATOR_PENDING",g.outbox().state());assertEquals(g.action().operationId(),g.operation().operationId());
        assertEquals(g.action().intentDigest(),g.event().intentDigest());assertEquals(g.action().eventId(),g.outbox().eventId());
    }
}
