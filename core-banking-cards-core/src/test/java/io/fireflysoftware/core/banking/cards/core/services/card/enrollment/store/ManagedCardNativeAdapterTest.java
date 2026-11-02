/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import io.r2dbc.spi.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

/** Actual repository binding on a networkless SPI; no installed schema or native admission proof. */
class ManagedCardNativeAdapterTest {
    static UUID id(long n){return ManagedCardReservationGraphTest.id(n);}
    final ManagedCardEnrollmentNativeContext.Request request=new ManagedCardEnrollmentNativeContext.Request(id(91),new Scope(id(1),id(3),id(4)),id(2),id(11),ManagedCardEnrollmentNativeContext.Purpose.RESERVE,
        new ManagedCardEnrollmentAuthority.Caller("https://sandbox.example.test","sandbox-reader","sandbox-tenant"),id(16),"a".repeat(64),"b".repeat(64),Instant.ofEpochMilli(1000005),Instant.ofEpochMilli(1000004));
    ManagedCardEnrollmentNativeContext context(R2dbcEntityTemplate template)throws Exception {
        Class<?> type=assertDoesNotThrow(()->Class.forName("io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardEnrollmentNativeContextImpl"));
        return (ManagedCardEnrollmentNativeContext)type.getConstructor(R2dbcEntityTemplate.class).newInstance(template);
    }
    @Test void actualAdapterConstructsOnExactTemplateWithoutAcquiring()throws Exception {
        var d=new NativeDriver();var template=d.template();assertSame(template,context(template).operations());assertEquals(0,d.base.creates);
    }
    @Test void actualSpringDataEnterBindsEveryFieldAndReadsTypedDatabaseStamp()throws Exception {
        var d=new NativeDriver();d.values=new LinkedHashMap<>(Map.of("context_nonce",request.nonce(),"action_id",request.actionId(),"purpose","RESERVE","scope_digest",request.scopeDigest(),"db_now_millis",1000000L,"authority_until_millis",1000005L,"command_until_millis",1000004L));
        var nativeContext=context(d.template());var stamp=TransactionalOperator.create(new R2dbcTransactionManager(d)).transactional(nativeContext.enter(request)).block();
        assertEquals(new ManagedCardEnrollmentNativeContext.Stamp(request.nonce(),request.actionId(),request.purpose(),request.scopeDigest(),1000000,1000005,1000004),stamp);
        assertEquals(1,d.sql.size());assertTrue(d.sql.getFirst().contains("public.cards_managed_enrollment_enter"));
        assertTrue(d.bound.values().containsAll(List.of(request.nonce(),request.scope().tenantId(),request.scope().partyId(),request.scope().accountId(),request.actorId(),request.actionId(),"RESERVE",request.caller().issuer(),request.caller().subject(),request.caller().tenantKey(),request.accountFenceReceiptId(),request.intentDigest(),request.scopeDigest(),OffsetDateTime.ofInstant(request.authorityUntil(),ZoneOffset.UTC),OffsetDateTime.ofInstant(request.commandUntil(),ZoneOffset.UTC))));
    }
    @Test void actualCompletionReadsDatabaseAcknowledgment()throws Exception {
        var d=new NativeDriver();d.values=Map.of("nonce",request.nonce());var nativeContext=context(d.template());
        assertEquals(request.nonce(),TransactionalOperator.create(new R2dbcTransactionManager(d)).transactional(nativeContext.complete(request)).block());
        assertEquals(1,d.sql.size());assertTrue(d.sql.getFirst().contains("public.cards_managed_enrollment_complete"));assertTrue(d.bound.containsValue(request.nonce()));
    }
    @Test void autocommitRefusesBeforeNativeStatement()throws Exception {
        var d=new NativeDriver();var nativeContext=context(d.template());assertThrows(RuntimeException.class,()->nativeContext.enter(request).block());assertTrue(d.sql.isEmpty());assertEquals(0,d.base.creates);
    }
    static final class NativeDriver implements ConnectionFactory {
        final ManagedCardReservationRepositoryTest.Driver base=new ManagedCardReservationRepositoryTest.Driver();final List<String> sql=new ArrayList<>();Map<Object,Object> bound=new LinkedHashMap<>();Map<String,Object> values=Map.of();
        R2dbcEntityTemplate template(){return new R2dbcEntityTemplate(DatabaseClient.create(this),PostgresDialect.INSTANCE);}
        final Connection connection=(Connection)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Connection.class},(p,m,a)->{
            if(m.getName().equals("createStatement")){String query=(String)a[0];sql.add(query);return statement();}
            try{return m.invoke(base.connection,a);}catch(InvocationTargetException e){throw e.getCause();}
        });
        Statement statement(){return (Statement)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Statement.class},(p,m,a)->{
            if(m.getName().equals("bind")||m.getName().equals("bindNull")){Object value=a[1];if(value instanceof io.r2dbc.spi.Parameter parameter)value=parameter.getValue()==null?parameter.getType().getJavaType():parameter.getValue();bound.put(a[0],value);return p;}
            if(m.getName().equals("execute"))return Mono.just(base.result(values,1));if(m.getReturnType()==Statement.class)return p;throw new UnsupportedOperationException(m.getName());
        });}
        public Publisher<? extends Connection> create(){base.creates++;return Mono.just(connection);}
        public ConnectionFactoryMetadata getMetadata(){return ()->"PostgreSQL";}
    }
}
