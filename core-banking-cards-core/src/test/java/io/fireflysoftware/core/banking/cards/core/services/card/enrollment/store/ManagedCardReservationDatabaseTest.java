/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardEnrollmentNativeContextImpl;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationRepositories;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

/** Native reservation storage only; fixture scope is not owner eligibility or an account fence. */
class ManagedCardReservationDatabaseTest {
    static String host,user,password,database;static int port;
    static final String commandPassword=UUID.randomUUID().toString(),readerPassword=UUID.randomUUID().toString();
    static final org.testcontainers.postgresql.PostgreSQLContainer postgres=new org.testcontainers.postgresql.PostgreSQLContainer("postgres:17-alpine")
        .withDatabaseName("synthetic_managed_reservation").withUsername("managed_bootstrap").withPassword(UUID.randomUUID().toString())
        .withCreateContainerCmdModifier(c->c.getHostConfig().withPortBindings(new com.github.dockerjava.api.model.PortBinding(
            com.github.dockerjava.api.model.Ports.Binding.bindIp("127.0.0.1"),new com.github.dockerjava.api.model.ExposedPort(5432))));
    static boolean container;
    @BeforeAll static void setup()throws Exception {
        String profile=System.getProperty("cards.managed.test.database-profile");
        if(profile==null){postgres.start();container=true;host=postgres.getHost();port=postgres.getMappedPort(5432);database=postgres.getDatabaseName();user=postgres.getUsername();password=postgres.getPassword();}
        else {var path=Path.of(profile);assertEquals(Set.of(java.nio.file.attribute.PosixFilePermission.OWNER_READ,java.nio.file.attribute.PosixFilePermission.OWNER_WRITE),Files.getPosixFilePermissions(path));
            var json=new com.fasterxml.jackson.databind.ObjectMapper().readTree(Files.readString(path));
            assertEquals("CARD_MANAGED_DISPOSABLE_TEST",json.get("purpose").asText());host=json.get("host").asText();assertEquals("127.0.0.1",host);port=json.get("port").asInt();database=json.get("database").asText();user=json.get("user").asText();password=json.get("password").asText();}
        try(var c=owner()){assertEquals("0",scalar(c,"SELECT count(*) FROM pg_tables WHERE schemaname='public'"));}
        Flyway.configure().dataSource(url(),user,password).locations("filesystem:"+System.getProperty("cards.managed.migrations")).load().migrate();
    }
    @AfterAll static void stop(){if(container)postgres.stop();}
    static String url(){return "jdbc:postgresql://"+host+":"+port+"/"+database;}
    static Connection owner()throws SQLException{return DriverManager.getConnection(url(),user,password);}
    static String scalar(Connection c,String sql)throws SQLException{try(var s=c.createStatement();var r=s.executeQuery(sql)){assertTrue(r.next());return r.getString(1);}}
    static void sql(Connection c,String sql)throws SQLException{try(var s=c.createStatement()){s.execute(sql);}}
    static synchronized void roles()throws SQLException{try(var c=owner()){
        if(scalar(c,"SELECT count(*) FROM pg_roles WHERE rolname='managed_test_command'").equals("0")){
            sql(c,"CREATE ROLE managed_test_command LOGIN PASSWORD '"+commandPassword+"' IN ROLE cards_managed_command");
            sql(c,"CREATE ROLE managed_test_reader LOGIN PASSWORD '"+readerPassword+"' IN ROLE cards_managed_reader");}}}
    record Session(io.r2dbc.spi.ConnectionFactory factory,R2dbcEntityTemplate template,ManagedCardEnrollmentNativeContext context) {
        <T>T run(Mono<T> work){var definition=new org.springframework.transaction.support.DefaultTransactionDefinition();definition.setIsolationLevel(org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ);
            return TransactionalOperator.create(new R2dbcTransactionManager(factory),definition).transactional(work).block(Duration.ofSeconds(10));}
    }
    static Session session(boolean reader)throws SQLException {roles();var f=io.r2dbc.spi.ConnectionFactories.get(io.r2dbc.spi.ConnectionFactoryOptions.builder()
        .option(io.r2dbc.spi.ConnectionFactoryOptions.DRIVER,"postgresql").option(io.r2dbc.spi.ConnectionFactoryOptions.HOST,host)
        .option(io.r2dbc.spi.ConnectionFactoryOptions.PORT,port).option(io.r2dbc.spi.ConnectionFactoryOptions.DATABASE,database)
        .option(io.r2dbc.spi.ConnectionFactoryOptions.USER,reader?"managed_test_reader":"managed_test_command")
        .option(io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD,reader?readerPassword:commandPassword).build());
        var t=new R2dbcEntityTemplate(f);return new Session(f,t,new ManagedCardEnrollmentNativeContextImpl(t));}
    record Fixture(Selection selection,Request request,UUID fence) {
        ManagedCardReservationGraph.Scope scope(){return new ManagedCardReservationGraph.Scope(selection.tenantId(),selection.partyId(),selection.accountId());}
        ManagedCardEnrollmentNativeContext.Request context(boolean read){return new ManagedCardEnrollmentNativeContext.Request(UUID.randomUUID(),scope(),selection.actorId(),request.actionId(),
            read?ManagedCardEnrollmentNativeContext.Purpose.RECOVER:ManagedCardEnrollmentNativeContext.Purpose.RESERVE,
            new ManagedCardEnrollmentAuthority.Caller("https://fixture.invalid","native-scope-fixture",selection.tenantId().toString()),read?null:fence,
            read?null:ManagedCardEnrollmentRules.digest(selection,request),"a".repeat(64),Instant.now().plusSeconds(25),Instant.now().plusSeconds(20));}
        ManagedCardReservationGraph graph(long now){return ManagedCardReservationFactory.create(selection,request,UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),fence,now);}
    }
    static Fixture fixture(){return new Fixture(new Selection(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),
        new ManagedCardId(UUID.randomUUID()),UUID.randomUUID(),3L,4L,new ConfiguredProviders("fixtureIssuer","fixtureProcessor")),new Request(UUID.randomUUID(),3L,"NEW_CARD",System.currentTimeMillis()+60000),UUID.randomUUID());}
    static ManagedCardReservationGraph reserve(Session s,Fixture f){var context=f.context(false);var repo=ManagedCardReservationRepositories.create(s.template());
        return s.run(s.context().enter(context).flatMap(stamp->repo.appendReserved(f.graph(stamp.dbNowMillis())))
            .flatMap(g->s.context().complete(context).thenReturn(g)));}
    static long count(UUID action)throws SQLException{try(var c=owner()){return Long.parseLong(scalar(c,"SELECT count(*) FROM managed_card_enrollment_action WHERE action_id='"+action+"'"));}}
    @Test void migrationProvidesTheExistingFiveMappedTablesAndScopeFunctions()throws Exception{try(var c=owner()){
        for(String table:List.of("managed_card","managed_card_enrollment_action","managed_card_provider_operation","managed_card_enrollment_event","managed_card_enrollment_outbox"))
            assertEquals(table,scalar(c,"SELECT coalesce(to_regclass('public."+table+"')::text,'MISSING')"));}}
    @Test void originalMappedFiveRowAppendAndReadOnlyRecoveryPreserveExactReceipt()throws Exception{
        var f=fixture();var command=session(false);var graph=reserve(command,f);var reader=session(true);var context=f.context(true);
        var repo=ManagedCardReservationRepositories.create(reader.template());
        assertEquals(graph,reader.run(reader.context().enter(context).then(repo.loadReserved(f.scope(),f.request().actionId())).flatMap(g->reader.context().complete(context).thenReturn(g))));
        assertEquals(1,count(f.request().actionId()));assertEquals("RESERVED",graph.action().state());assertEquals("RETAINED",graph.card().restriction());}
    @Test void rollbackAfterAllMappedRowsLeavesNoReservation()throws Exception{var f=fixture();var s=session(false);var context=f.context(false);var repo=ManagedCardReservationRepositories.create(s.template());
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(context).flatMap(stamp->repo.appendReserved(f.graph(stamp.dbNowMillis()))).then(Mono.error(new IllegalStateException("controlled rollback")))));
        assertEquals(0,count(f.request().actionId()));}
    @Test void noContextAndForgedSessionSettingsCannotWriteOrRead()throws Exception{var f=fixture();var s=session(false);var graph=reserve(s,f);var repo=ManagedCardReservationRepositories.create(s.template());
        assertNull(s.run(repo.loadReserved(f.scope(),f.request().actionId())));
        assertThrows(RuntimeException.class,()->s.run(s.template().insert(fixture().graph(System.currentTimeMillis()).card())));
        try(var c=DriverManager.getConnection(url(),"managed_test_command",commandPassword)){sql(c,"SET firefly.cards.tenant='"+f.scope().tenantId()+"'");assertEquals("0",scalar(c,"SELECT count(*) FROM managed_card"));}}
    @Test void wrongCurrentOwnerScopeCannotReadTheOriginalAction()throws Exception{var f=fixture();reserve(session(false),f);for(int part=0;part<4;part++){
        var s=session(true);var original=f.context(true);var scope=new ManagedCardReservationGraph.Scope(part==0?UUID.randomUUID():f.scope().tenantId(),part==1?UUID.randomUUID():f.scope().partyId(),part==2?UUID.randomUUID():f.scope().accountId());
        var request=new ManagedCardEnrollmentNativeContext.Request(original.nonce(),scope,part==3?UUID.randomUUID():original.actorId(),original.actionId(),original.purpose(),original.caller(),null,null,original.scopeDigest(),original.authorityUntil(),original.commandUntil());
        var repo=ManagedCardReservationRepositories.create(s.template());
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(request).then(repo.loadReserved(f.scope(),f.request().actionId())).switchIfEmpty(Mono.error(new IllegalStateException("no disclosure")))));}
        assertEquals(1,count(f.request().actionId()));}
    @Test void readerCannotEnterReserveOrMutateBusinessRows()throws Exception{var s=session(true);var f=fixture();assertThrows(RuntimeException.class,()->s.run(s.context().enter(f.context(false))));
        assertThrows(RuntimeException.class,()->s.run(s.template().insert(f.graph(System.currentTimeMillis()).card())));assertEquals(0,count(f.request().actionId()));}
    @Test void missingCompletionCannotCommitAnOtherwiseCompleteGraph()throws Exception{var f=fixture();var s=session(false);var repo=ManagedCardReservationRepositories.create(s.template());
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(f.context(false)).flatMap(stamp->repo.appendReserved(f.graph(stamp.dbNowMillis())))));assertEquals(0,count(f.request().actionId()));}
    @Test void incompleteGraphCannotAcknowledgeCompletion()throws Exception{var f=fixture();var s=session(false);var context=f.context(false);
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(context).flatMap(stamp->s.template().insert(f.graph(stamp.dbNowMillis()).card())).then(s.context().complete(context))));assertEquals(0,count(f.request().actionId()));}
    @Test void exactReplayReadsOriginalGraphButDuplicateAppendCannotChangeIt()throws Exception{var f=fixture();var s=session(false);var graph=reserve(s,f);var repo=ManagedCardReservationRepositories.create(s.template());var context=f.context(false);
        assertEquals(graph,s.run(s.context().enter(context).then(repo.loadReserved(f.scope(),f.request().actionId())).flatMap(g->s.context().complete(context).thenReturn(g))));
        assertThrows(RuntimeException.class,()->reserve(s,f));assertEquals(1,count(f.request().actionId()));}
    @Test void directMappedCardReadCannotDiscloseAnotherActorsReservation()throws Exception {
        var f=fixture();reserve(session(false),f);var s=session(true);var original=f.context(true);
        var request=new ManagedCardEnrollmentNativeContext.Request(original.nonce(),original.scope(),UUID.randomUUID(),original.actionId(),original.purpose(),original.caller(),null,null,original.scopeDigest(),original.authorityUntil(),original.commandUntil());
        var observed=new java.util.concurrent.atomic.AtomicLong(-1);
        assertThrows(RuntimeException.class,()->s.run(s.context().enter(request).then(s.template().count(org.springframework.data.relational.core.query.Query.empty(),ManagedCardReservationGraph.Card.class))
            .doOnNext(observed::set).then(Mono.error(new IllegalStateException("controlled read rollback")))));
        assertEquals(0,observed.get(),"Actor scope must also protect direct mapped Card reads");
    }
    @Test void authorityIsCheckedAfterWaitingForTheOriginalAccountLock()throws Exception {
        var f=fixture();var s=session(false);var original=f.context(false);var until=Instant.now().plusMillis(500);
        var request=new ManagedCardEnrollmentNativeContext.Request(original.nonce(),original.scope(),original.actorId(),original.actionId(),original.purpose(),original.caller(),original.accountFenceReceiptId(),original.intentDigest(),original.scopeDigest(),until,until);
        try(var blocker=owner()) {blocker.setAutoCommit(false);sql(blocker,"SELECT pg_advisory_xact_lock(hashtextextended('cards.managed.account:"+f.scope().tenantId()+":"+f.scope().accountId()+"',0))");
            var future=java.util.concurrent.CompletableFuture.supplyAsync(()->s.run(s.context().enter(request)));
            long limit=System.nanoTime()+Duration.ofSeconds(3).toNanos();boolean waiting=false;
            while(System.nanoTime()<limit){try(var observation=owner()){waiting=!scalar(observation,"SELECT count(*) FROM pg_stat_activity WHERE usename='managed_test_command' AND wait_event='advisory'").equals("0");}if(waiting)break;Thread.sleep(10);}
            assertTrue(waiting,"The real native admission must wait on the original account lock");
            long remaining=until.toEpochMilli()-System.currentTimeMillis()+50;if(remaining>0)Thread.sleep(remaining);
            assertFalse(future.isDone());blocker.commit();assertThrows(java.util.concurrent.ExecutionException.class,()->future.get(5,java.util.concurrent.TimeUnit.SECONDS));}
        assertEquals(0,count(f.request().actionId()));
    }
    @Test void changedFenceAndIntentCannotCompleteReplayOrCreateAnotherReceipt()throws Exception {
        var f=fixture();reserve(session(false),f);
        for(boolean changeFence:new boolean[]{true,false}) {var s=session(false);var original=f.context(false);
            var request=new ManagedCardEnrollmentNativeContext.Request(original.nonce(),original.scope(),original.actorId(),original.actionId(),original.purpose(),original.caller(),changeFence?UUID.randomUUID():original.accountFenceReceiptId(),changeFence?original.intentDigest():"b".repeat(64),original.scopeDigest(),original.authorityUntil(),original.commandUntil());
            assertThrows(RuntimeException.class,()->s.run(s.context().enter(request).then(s.context().complete(request))));}
        assertEquals(1,count(f.request().actionId()));
    }

}
