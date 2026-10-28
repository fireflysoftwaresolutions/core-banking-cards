package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import static org.junit.jupiter.api.Assertions.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationRepositories;
import io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment.ManagedCardReservationRepository;
import io.r2dbc.spi.*;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.mapping.callback.ReactiveEntityCallbacks;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.*;

class ManagedCardReservationRepositoryTest {
    ManagedCardReservationGraph graph() { return ManagedCardReservationGraphTest.graph(); }
    <T> T transaction(Driver d,Mono<T> work) { return TransactionalOperator.create(new R2dbcTransactionManager(d)).transactional(work).block(); }
    @Test void realSpringDataFactoryUsesTheExactProvidedOperationsAndNarrowInterface() {
        var d=new Driver();var t=d.template();var repo=ManagedCardReservationRepositories.create(t);
        assertTrue(Proxy.isProxyClass(repo.getClass()));assertSame(t,repo.operations());
        assertFalse(org.springframework.data.repository.reactive.ReactiveCrudRepository.class.isAssignableFrom(ManagedCardReservationRepository.class));
        assertNotNull(ManagedCardReservationRepository.class.getAnnotation(org.springframework.data.repository.NoRepositoryBean.class));
    }
    @Test void actualSpringDataInsertsAndReadBackVerifyAllFiveMappedRows() {
        var d=new Driver();var repo=ManagedCardReservationRepositories.create(d.template());var g=graph();
        assertEquals(g,transaction(d,repo.appendReserved(g)));assertEquals(5,d.inserts);assertEquals(5,d.countQueries);
        assertEquals(1,d.commits);assertEquals(0,d.rollbacks);
        assertTrue(d.sql.stream().filter(s->s.startsWith("INSERT")).allMatch(s->!s.toLowerCase().contains("card_number")));
    }
    @Test void actualCountZeroAndMoreThanOneAreNeverAnEntityDerivedSuccess() {
        for(long count:new long[]{0,2}) {
            var d=new Driver();d.countOverride=count;var repo=ManagedCardReservationRepositories.create(d.template());
            assertThrows(RuntimeException.class,()->transaction(d,repo.appendReserved(graph())));assertEquals(0,d.commits);assertEquals(1,d.rollbacks);
        }
    }
    @Test void entityEmissionWithoutMaterializedRowsCannotBecomeAReservation() {
        var d=new Driver();d.materialize=false;var repo=ManagedCardReservationRepositories.create(d.template());
        assertThrows(RuntimeException.class,()->transaction(d,repo.appendReserved(graph())));assertEquals(0,d.commits);assertEquals(1,d.rollbacks);
    }
    @Test void duplicateInsertPropagatesRollbackWithoutASelectInsideTheAbortedTransaction() {
        var d=new Driver();d.failInsert=2;var repo=ManagedCardReservationRepositories.create(d.template());
        assertThrows(RuntimeException.class,()->transaction(d,repo.appendReserved(graph())));
        assertEquals(0,d.commits);assertEquals(1,d.rollbacks);assertFalse(d.sql.stream().anyMatch(s->s.startsWith("SELECT")));
    }
    @Test void actualDerivedLookupBindsAllThreeScopeColumnsAndAction() {
        var d=new Driver();var t=d.template();var repo=ManagedCardReservationRepositories.create(t);var g=graph();transaction(d,repo.appendReserved(g));d.sql.clear();d.bindings.clear();
        assertEquals(g.action(),repo.findByTenantIdAndPartyIdAndAccountIdAndActionId(g.scope().tenantId(),g.scope().partyId(),g.scope().accountId(),g.action().actionId()).block());
        String sql=d.sql.getFirst();assertTrue(sql.contains("tenant_id"));assertTrue(sql.contains("party_id"));assertTrue(sql.contains("account_id"));assertTrue(sql.contains("action_id"));
        assertTrue(d.bindings.getFirst().values().containsAll(List.of(g.scope().tenantId(),g.scope().partyId(),g.scope().accountId(),g.action().actionId())));
    }
    @Test void guardedFragmentRefusesAutocommitBeforeAnyDatabaseStatement() {
        var d=new Driver();var repo=ManagedCardReservationRepositories.create(d.template());
        assertThrows(RuntimeException.class,()->repo.appendReserved(graph()).block());assertTrue(d.sql.isEmpty());
    }
    @Test void existingStatementCustomizationAndEntityCallbacksRemainEffective() {
        var d=new Driver();var t=d.template();var calls=new AtomicInteger();t.setStatementFilterFunction(s->{calls.incrementAndGet();return s;});var callbacks=new AtomicInteger();
        BeforeConvertCallback<ManagedCardReservationGraph.Card> cb=(row,table)->{callbacks.incrementAndGet();return Mono.just(row);};
        t.setEntityCallbacks(ReactiveEntityCallbacks.create(cb));var repo=ManagedCardReservationRepositories.create(t);var g=graph();
        assertEquals(g,transaction(d,repo.appendReserved(g)));assertEquals(1,callbacks.get());assertTrue(calls.get()>=15);assertSame(t,repo.operations());
    }
    @Test void emptyLookupIsNotACompleteGraphAndCannotSynthesizeAReceipt() {
        var d=new Driver();var repo=ManagedCardReservationRepositories.create(d.template());assertNull(transaction(d,repo.loadReserved(graph().scope(),graph().action().actionId())));
    }

    @Test void missingCountObservationCannotBeTreatedAsExactlyOne() {
        var d=new Driver();var t=org.mockito.Mockito.spy(d.template());
        org.mockito.Mockito.doReturn(Mono.empty()).when(t).count(org.mockito.ArgumentMatchers.any(org.springframework.data.relational.core.query.Query.class),org.mockito.ArgumentMatchers.any(Class.class));
        var repo=ManagedCardReservationRepositories.create(t);
        assertThrows(RuntimeException.class,()->transaction(d,repo.appendReserved(graph())));assertEquals(0,d.commits);assertEquals(1,d.rollbacks);
    }
    @Test void callbackChangingNativeRecordedValuesIsNotSilentlyAccepted() {
        var d=new Driver();var t=d.template();
        BeforeConvertCallback<ManagedCardReservationGraph.Card> callback=(row,table)->Mono.just(new ManagedCardReservationGraph.Card(row.managedCardId(),row.tenantId(),row.partyId(),row.accountId(),row.contractId(),row.productId(),row.enrollmentId(),row.actionId(),"DIFFERENT",row.recordedAt()));
        t.setEntityCallbacks(ReactiveEntityCallbacks.create(callback));var repo=ManagedCardReservationRepositories.create(t);
        assertThrows(RuntimeException.class,()->transaction(d,repo.appendReserved(graph())));assertEquals(0,d.commits);assertEquals(1,d.rollbacks);
    }
    @Test void retainedActionWithoutEveryOriginalChildRefusesRecovery() {
        var d=new Driver();var repo=ManagedCardReservationRepositories.create(d.template());var g=graph();transaction(d,repo.appendReserved(g));
        d.rows.remove("managed_card_enrollment_event");d.sql.clear();
        assertThrows(RuntimeException.class,()->transaction(d,repo.loadReserved(g.scope(),g.action().actionId())));assertFalse(d.sql.stream().anyMatch(x->x.startsWith("INSERT")));
    }
    @Test void returnedDifferentAccountCannotBypassTheScopedLookup() {
        var d=new Driver();var repo=ManagedCardReservationRepositories.create(d.template());var g=graph();transaction(d,repo.appendReserved(g));
        var wrong=new ManagedCardReservationGraph.Scope(g.scope().tenantId(),g.scope().partyId(),ManagedCardReservationGraphTest.id(99));
        assertThrows(RuntimeException.class,()->transaction(d,repo.loadReserved(wrong,g.action().actionId())));
    }

    /** Networkless SPI double: qualifies generated mapping and transaction callbacks, never native storage. */
    static final class Driver implements ConnectionFactory {
        final List<String> sql=new ArrayList<>();final List<Map<Object,Object>> bindings=new ArrayList<>();final Map<String,Map<String,Object>> rows=new HashMap<>();
        int inserts,countQueries,commits,rollbacks,closes,creates,failInsert;Publisher<Void> begin=Mono.empty(),commit=Mono.empty(),close=Mono.empty();boolean materialize=true,autoCommit=true;Long countOverride;
        R2dbcEntityTemplate template(){return new R2dbcEntityTemplate(DatabaseClient.create(this),PostgresDialect.INSTANCE);}
        final Connection connection=(Connection)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Connection.class},(p,m,a)->switch(m.getName()) {
            case "createStatement"->statement((String)a[0]);case "isAutoCommit"->autoCommit;case "getTransactionIsolationLevel"->IsolationLevel.READ_COMMITTED;
            case "setAutoCommit"->{autoCommit=(boolean)a[0];yield Mono.empty();}case "beginTransaction"->begin;case "setTransactionIsolationLevel"->Mono.empty();
            case "commitTransaction"->{commits++;yield commit;}case "rollbackTransaction"->{rollbacks++;yield Mono.empty();}case "close"->{closes++;yield close;}
            case "toString"->"NetworklessManagedReservationDriver";default->throw new UnsupportedOperationException(m.getName());});
        Statement statement(String query) {
            sql.add(query);Map<Object,Object> bound=new LinkedHashMap<>();bindings.add(bound);
            return (Statement)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Statement.class},(p,m,a)-> {
                if(m.getName().equals("bind")||m.getName().equals("bindNull")) {Object v=a[1];if(v instanceof Parameter parameter)v=parameter.getValue()==null?parameter.getType().getJavaType():parameter.getValue();bound.put(a[0],v);return p;}
                if(m.getName().equals("execute")) {
                    var table=Pattern.compile("(?i)(?:into|from)\\s+\"?([a-z_]+)\"?").matcher(query);if(!table.find())throw new AssertionError("Only real generated insert/select expected");String name=table.group(1);
                    if(query.startsWith("INSERT")) {inserts++;if(inserts==failInsert)return Mono.error(new R2dbcDataIntegrityViolationException("synthetic duplicate","23505"));
                        String[] columns=query.substring(query.indexOf('(')+1,query.indexOf(')')).replace("\"","").split(",");Map<String,Object> row=new LinkedHashMap<>();for(int k=0;k<columns.length;k++)row.put(columns[k].trim(),bound.get(k));if(materialize)rows.put(name,row);
                        return Mono.just(result(row,1));}
                    if(query.toUpperCase().contains("COUNT(")){countQueries++;long n=countOverride==null?(rows.containsKey(name)?1:0):countOverride;return Mono.just(result(Map.of("count",n),1));}
                    return Mono.just(result(rows.getOrDefault(name,Map.of()),rows.containsKey(name)?1:0));
                }
                if(m.getReturnType()==Statement.class)return p;throw new UnsupportedOperationException(m.getName());
            });
        }
        Result result(Map<String,Object> values,int count) {return new Result() {
            final List<String> names=new ArrayList<>(values.keySet());
            final List<ColumnMetadata> columns=names.stream().map(n->(ColumnMetadata)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{ColumnMetadata.class},(p,m,a)->switch(m.getName()) {
                case "getName"->n;case "getJavaType"->values.get(n).getClass();case "getNullability"->Nullability.NON_NULL;
                case "getType"->new Type(){public Class<?> getJavaType(){return values.get(n).getClass();}public String getName(){return "NetworklessTypedColumn";}};default->null;})).toList();
            final RowMetadata metadata=(RowMetadata)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{RowMetadata.class},(p,m,a)->switch(m.getName()) {
                case "getColumnMetadatas"->columns;case "contains"->names.contains(a[0]);case "getColumnMetadata"->columns.get(a[0] instanceof Integer index?index:names.indexOf(a[0]));default->throw new UnsupportedOperationException(m.getName());});
            final Row row=(Row)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Row.class},(p,m,a)->switch(m.getName()) {
                case "get"->values.get(a[0] instanceof Integer index?names.get(index):(String)a[0]);case "getMetadata"->metadata;default->throw new UnsupportedOperationException(m.getName());});
            public Publisher<Long> getRowsUpdated(){return Mono.just(1L);}
            public <T>Publisher<T> map(BiFunction<Row,RowMetadata,? extends T> f){return Flux.range(0,count).map(i->f.apply(row,metadata));}
            public <T>Publisher<T> map(Function<? super io.r2dbc.spi.Readable,? extends T> f){return Flux.range(0,count).map(i->f.apply(row));}
            public Result filter(Predicate<Segment> f){return this;}
            public <T>Publisher<T> flatMap(Function<Segment,? extends Publisher<? extends T>> f){return Flux.empty();}
        };}
        public Publisher<? extends Connection> create(){creates++;return Mono.just(connection);}public ConnectionFactoryMetadata getMetadata(){return ()->"PostgreSQL";}
    }
}
