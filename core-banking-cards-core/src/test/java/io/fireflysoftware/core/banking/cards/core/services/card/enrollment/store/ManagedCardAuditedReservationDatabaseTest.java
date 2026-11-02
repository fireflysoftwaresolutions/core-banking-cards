/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import static io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store.ManagedCardReservationDatabaseTest.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationAuditBinding;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.kernel.audit.AuditActor;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.junit.jupiter.api.*;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

/** Actual bounded-root audit/storage only. Current eligibility and fence inputs are controlled fixtures. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManagedCardAuditedReservationDatabaseTest {
    static Fixture legacyValues;
    static final String writerPassword=UUID.randomUUID().toString();
    static final List<String> TABLES=List.of("managed_card","managed_card_enrollment_action","managed_card_provider_operation","managed_card_enrollment_event","managed_card_enrollment_outbox","managed_card_reservation_audit","firefly_audit.audit_head","firefly_audit.audit_event","firefly_audit.audit_outbox");
    @BeforeAll static void setupAudit()throws Exception {
        ManagedCardReservationDatabaseTest.setup();roles();
        var old=fixture();legacyValues=new Fixture(old.selection(),new Request(old.request().actionId(),old.request().expectedAccountVersion(),old.request().reasonCode(),System.currentTimeMillis()+300000),old.fence());
        reserve(session(false),legacyValues);
        try(var c=owner()) {
            sql(c,"CREATE ROLE firefly_audit_guard NOLOGIN NOINHERIT NOSUPERUSER NOBYPASSRLS");
            sql(c,"CREATE ROLE firefly_audit_writer LOGIN NOINHERIT NOSUPERUSER NOBYPASSRLS PASSWORD '"+writerPassword+"'");
            sql(c,"CREATE ROLE firefly_audit_reader LOGIN NOINHERIT NOSUPERUSER NOBYPASSRLS PASSWORD '"+UUID.randomUUID()+"'");
            for(String path:List.of("db/optional/immutable-audit/postgresql.sql","db/optional/immutable-audit/completed-append.sql"))
                try(var stream=ManagedCardAuditedReservationDatabaseTest.class.getClassLoader().getResourceAsStream(path)){assertNotNull(stream);sql(c,new String(stream.readAllBytes(),java.nio.charset.StandardCharsets.UTF_8));}
            sql(c,"SET ROLE firefly_audit_guard;GRANT EXECUTE ON FUNCTION public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean) TO cards_managed_guard,firefly_audit_writer;RESET ROLE");
            var installation=Path.of(System.getProperty("cards.managed.audit-migration"));
            if(Files.exists(installation))sql(c,Files.readString(installation));
        }
    }
    @AfterAll static void stopAudit(){ManagedCardReservationDatabaseTest.stop();}
    static ManagedCardEnrollmentSession owned(boolean writer,boolean exactWriter)throws Exception {
        var delegate=io.r2dbc.spi.ConnectionFactories.get(io.r2dbc.spi.ConnectionFactoryOptions.builder().option(io.r2dbc.spi.ConnectionFactoryOptions.DRIVER,"postgresql")
            .option(io.r2dbc.spi.ConnectionFactoryOptions.HOST,host).option(io.r2dbc.spi.ConnectionFactoryOptions.PORT,port).option(io.r2dbc.spi.ConnectionFactoryOptions.DATABASE,database)
            .option(io.r2dbc.spi.ConnectionFactoryOptions.USER,writer?(exactWriter?"firefly_audit_writer":"managed_test_command"):"managed_test_reader")
            .option(io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD,writer?(exactWriter?writerPassword:commandPassword):readerPassword).build());
        var factory=new OwnedLeaseConnectionFactory(delegate);var client=DatabaseClient.create(factory);var template=new R2dbcEntityTemplate(client,PostgresDialect.INSTANCE);var manager=new R2dbcTransactionManager(factory);
        return new ManagedCardEnrollmentSession(writer?ManagedCardEnrollmentSession.Role.COMMAND:ManagedCardEnrollmentSession.Role.READER,factory,client,template,manager,new ManagedCardEnrollmentNativeContextImpl(template));
    }
    static Map<String,String> snapshot()throws Exception {var values=new LinkedHashMap<String,String>();try(var c=owner()){
        for(String table:TABLES)values.put(table,scalar(c,"SELECT coalesce(jsonb_agg(to_jsonb(t) ORDER BY to_jsonb(t)::text),'[]'::jsonb)::text FROM "+table+" t"));}return values;}
    static void installed()throws Exception{try(var c=owner()){assertEquals("managed_card_reservation_audit",scalar(c,"SELECT coalesce(to_regclass('public.managed_card_reservation_audit')::text,'MISSING')"));}}
    static final class OwnerFixture implements ManagedCardEnrollmentAuthority {
        final Fixture values;final Clock clock=Clock.systemUTC();final java.util.function.LongSupplier nanos=System::nanoTime;
        final SecurityPrincipal principal;final SecurityContextPort principals;
        final ManagedCardEnrollmentSession command,reader;final ManagedCardEnrollmentService service;boolean withdrawn;
        OwnerFixture(boolean audited,boolean exactWriter)throws Exception {this(fixture(),audited,exactWriter);}
        OwnerFixture(Fixture values,boolean audited,boolean exactWriter)throws Exception {
            this.values=values;principal=new SecurityPrincipal("native-audit-fixture","https://fixture.invalid",values.selection().tenantId().toString(),Set.of(),Set.of(),Map.of(),clock.instant(),null,Set.of(),Map.of());principals=()->Mono.just(principal);
            command=owned(true,exactWriter);reader=owned(false,false);
            ManagedCardReservationAuditAdmission admission=(p,a,g)->Mono.just(new ManagedCardReservationAuditAdmission.Observation(new AuditActor(p.issuer(),p.subject(),"cards-fixture",p.tenantId(),null,null),values.selection().actorId(),"native-fixture-policy",a.effectiveUntil()));
            service=audited?new ManagedCardEnrollmentServiceImpl(principals,this,command,reader,clock,nanos,UUID::randomUUID,command.reservationAudit(principals,this,admission,clock,nanos,UUID::randomUUID)):
                new ManagedCardEnrollmentServiceImpl(principals,this,command,reader,clock,nanos,UUID::randomUUID);
        }
        public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal p,UUID account,Request request){var now=clock.instant().truncatedTo(java.time.temporal.ChronoUnit.MILLIS);var until=now.plusSeconds(4);var selection=values.selection();var facts=new EnumMap<Owner,EligibilityReceipt>(Owner.class);
            for(Owner owner:Owner.values())facts.put(owner,new EligibilityReceipt(UUID.randomUUID(),owner,selection.tenantId(),selection.partyId(),selection.accountId(),selection.contractId(),selection.productId(),owner==Owner.ACCOUNT?selection.accountVersion():selection.productVersion(),until.toEpochMilli(),Eligibility.ELIGIBLE));
            return Mono.just(new ReservationAuthorization(new Caller(p.issuer(),p.subject(),p.tenantId()),selection,facts,values.fence(),now,until));}
        public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal p,UUID account,RecoveryRequest request){var now=clock.instant();return Mono.just(new RecoveryAuthorization(new Caller(p.issuer(),p.subject(),p.tenantId()),values.scope(),values.selection().actorId(),now,now.plusSeconds(4)));}
        public Mono<Void> requireCurrent(SecurityPrincipal p,Authorization a){return !withdrawn&&a.caller().matches(p)?Mono.empty():Mono.error(new IllegalStateException("Controlled authority withdrawn"));}
        io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardReservationReceipt reserve(){return service.reserve(values.selection().accountId(),values.request()).block(Duration.ofSeconds(8));}
    }
    @Test @Order(1) void originalMappedRootCommitsFiveBusinessRowsAndExactCompletedCanonicalAudit()throws Exception {installed();var f=new OwnerFixture(true,true);var receipt=f.reserve();assertEquals(State.RESERVED,receipt.progress().state());
        try(var c=owner()) {assertEquals("1",scalar(c,"SELECT count(*) FROM managed_card_reservation_audit b JOIN firefly_audit.audit_event e ON e.resource_tenant_id=b.tenant_id AND e.event_id=b.event_id AND e.sequence=b.audit_sequence AND e.record_hash=b.audit_record_hash WHERE b.action_id='"+f.values.request().actionId()+"'"));
            assertEquals("1",scalar(c,"SELECT count(*) FROM firefly_audit_private.context x JOIN managed_card_reservation_audit b ON b.audit_context_nonce=x.nonce WHERE b.action_id='"+f.values.request().actionId()+"' AND x.db_session_user='firefly_audit_writer' AND x.new_event"));
            assertEquals("0",scalar(c,"SELECT count(*) FROM cards_managed_private.context"));}}
    @Test @Order(2) void auditedReplayPreservesEveryBusinessRowAndOriginalAuditBinding()throws Exception {installed();var f=new OwnerFixture(true,true);var first=f.reserve();var before=snapshot();assertEquals(first,f.reserve());assertEquals(before,snapshot());}
    @Test @Order(3) void protectedRecoveryReadsOriginalAuditedReceiptWithoutAnotherAppend()throws Exception {installed();var f=new OwnerFixture(true,true);var first=f.reserve();var before=snapshot();var recovered=f.service.recover(f.values.selection().accountId(),new RecoveryRequest(f.values.request().actionId(),0L)).block(Duration.ofSeconds(8));assertEquals(first,recovered);assertEquals(before,snapshot());}
    @Test @Order(4) void unauditedConstructorCannotCommitThroughTheAuditedNativeBoundary()throws Exception {installed();var before=snapshot();var f=new OwnerFixture(false,true);var error=assertThrows(ManagedCardEnrollmentException.class,f::reserve);assertTrue(error.transactionFacts().rollbackAcknowledged());assertFalse(error.transactionFacts().commitAcknowledged());assertEquals(before,snapshot());}
    @Test @Order(5) void lookalikeCommandRoleCannotReplaceTheOriginalRestrictedAuditWriter()throws Exception {installed();var before=snapshot();var f=new OwnerFixture(true,false);var error=assertThrows(ManagedCardEnrollmentException.class,f::reserve);assertTrue(error.transactionFacts().rollbackAcknowledged());assertEquals(before,snapshot());}
    @Test @Order(6) void callbackCannotSubstituteAnotherCompletedAuditRecordBinding()throws Exception {installed();var before=snapshot();var f=new OwnerFixture(true,true);
        org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback<ManagedCardReservationAuditBinding> callback=(row,table)->Mono.just(new ManagedCardReservationAuditBinding(row.actionId(),row.tenantId(),row.eventId(),row.auditContextNonce(),row.auditSequence(),"0".repeat(64)));
        f.command.operations().setEntityCallbacks(org.springframework.data.mapping.callback.ReactiveEntityCallbacks.create(callback));var error=assertThrows(ManagedCardEnrollmentException.class,f::reserve);assertTrue(error.transactionFacts().rollbackAcknowledged());assertEquals(before,snapshot());}
    @Test @Order(7) void readerCannotAppendOrReplaceTheImmutableAuditBinding()throws Exception {installed();var f=new OwnerFixture(true,true);f.reserve();var before=snapshot();var s=session(true);var context=f.values.context(true);
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(context).then(s.template().insert(new ManagedCardReservationAuditBinding(f.values.request().actionId(),f.values.selection().tenantId(),UUID.randomUUID(),UUID.randomUUID(),1,"a".repeat(64))))));assertEquals(before,snapshot());}
    @Test @Order(8) void revokedCompletedAccessorRollsBackNewReservationAndWithholdsReplay()throws Exception {installed();var f=new OwnerFixture(true,true);f.reserve();var fresh=new OwnerFixture(true,true);var before=snapshot();
        try(var c=owner()){sql(c,"SET ROLE firefly_audit_guard;REVOKE EXECUTE ON FUNCTION public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean) FROM firefly_audit_writer;RESET ROLE");}
        try {for(var attempt:List.of(f,fresh)){var error=assertThrows(ManagedCardEnrollmentException.class,attempt::reserve);assertTrue(error.transactionFacts().rollbackAcknowledged());assertFalse(error.transactionFacts().commitAcknowledged());}assertEquals(before,snapshot());}
        finally {try(var c=owner()){sql(c,"SET ROLE firefly_audit_guard;GRANT EXECUTE ON FUNCTION public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean) TO firefly_audit_writer;RESET ROLE");}}}
    @Test @Order(9) void existingUnauditedReservationCannotBeBackfilledOrPresentedAsOriginallyAudited()throws Exception {installed();var f=new OwnerFixture(legacyValues,true,true);var before=snapshot();
        var mutation=assertThrows(ManagedCardEnrollmentException.class,f::reserve);assertTrue(mutation.transactionFacts().rollbackAcknowledged());
        var read=assertThrows(ManagedCardEnrollmentException.class,()->f.service.recover(f.values.selection().accountId(),new RecoveryRequest(f.values.request().actionId(),0L)).block(Duration.ofSeconds(8)));
        assertTrue(read.transactionFacts().rollbackAcknowledged());assertEquals(before,snapshot());
        try(var c=owner()){assertEquals("0",scalar(c,"SELECT count(*) FROM managed_card_reservation_audit WHERE action_id='"+legacyValues.request().actionId()+"'"));}}

}
