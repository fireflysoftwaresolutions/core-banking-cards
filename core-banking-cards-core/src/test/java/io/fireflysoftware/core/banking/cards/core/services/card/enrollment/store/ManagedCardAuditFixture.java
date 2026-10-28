package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;

import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Scope;
import io.r2dbc.spi.*;
import java.lang.reflect.Proxy;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.regex.*;
import org.fireflyframework.core.transaction.*;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.*;

/** Networkless real mapper/transaction model only; no PostgreSQL or genuine authority proof. */
final class ManagedCardAuditFixture implements ManagedCardEnrollmentAuthority {
    static UUID id(long n){return ManagedCardReservationGraphTest.id(n);}
    final Instant now=Instant.ofEpochMilli(ManagedCardReservationGraphTest.NOW);final Clock clock=Clock.fixed(now,ZoneOffset.UTC);final LongSupplier nanos=System::nanoTime;
    final AtomicReference<SecurityPrincipal> principal=new AtomicReference<>(new SecurityPrincipal("synthetic-operator","https://synthetic.invalid","sandbox-platform",Set.of(),Set.of(),Map.of("client_id","cards-client","staff_realm","sandbox-staff","staff_id",id(2).toString()),now,null,Set.of(),Map.of()));
    final SecurityContextPort principals=()->Mono.justOrEmpty(principal.get());final Driver driver=new Driver(),readerDriver=new Driver();
    final OwnedLeaseConnectionFactory factory=new OwnedLeaseConnectionFactory(driver);final DatabaseClient client=DatabaseClient.create(factory);final R2dbcEntityTemplate template=new R2dbcEntityTemplate(client,PostgresDialect.INSTANCE);final R2dbcTransactionManager manager=new R2dbcTransactionManager(factory);final BoundedR2dbcResources resources=BoundedR2dbcResources.verify(factory,client,template,manager);
    final ManagedCardEnrollmentSession command=new ManagedCardEnrollmentSession(ManagedCardEnrollmentSession.Role.COMMAND,factory,client,template,manager,new ManagedCardEnrollmentNativeContextImpl(template));
    final OwnedLeaseConnectionFactory readerFactory=new OwnedLeaseConnectionFactory(readerDriver);final DatabaseClient readerClient=DatabaseClient.create(readerFactory);final R2dbcEntityTemplate readerTemplate=new R2dbcEntityTemplate(readerClient,PostgresDialect.INSTANCE);final R2dbcTransactionManager readerManager=new R2dbcTransactionManager(readerFactory);
    final ManagedCardEnrollmentSession reader=new ManagedCardEnrollmentSession(ManagedCardEnrollmentSession.Role.READER,readerFactory,readerClient,readerTemplate,readerManager,new ManagedCardEnrollmentNativeContextImpl(readerTemplate));
    final AtomicLong ids=new AtomicLong(100);int currentChecks;
    Request request(){return new Request(id(11),9L,"REQUESTED",now.plusSeconds(5).toEpochMilli());}
    public Mono<ReservationAuthorization> authorizeReservation(SecurityPrincipal actor,UUID account,Request request){var selection=ManagedCardReservationGraphTest.selection();var facts=new EnumMap<Owner,EligibilityReceipt>(Owner.class);for(Owner owner:Owner.values())facts.put(owner,new EligibilityReceipt(id(200+owner.ordinal()),owner,selection.tenantId(),selection.partyId(),selection.accountId(),selection.contractId(),selection.productId(),owner==Owner.ACCOUNT?9L:10L,now.plusSeconds(5).toEpochMilli(),Eligibility.ELIGIBLE));return Mono.just(new ReservationAuthorization(new Caller(actor.issuer(),actor.subject(),actor.tenantId()),selection,facts,id(16),now,now.plusSeconds(5)));}
    public Mono<RecoveryAuthorization> authorizeRecovery(SecurityPrincipal actor,UUID account,io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest request){return Mono.just(new RecoveryAuthorization(new Caller(actor.issuer(),actor.subject(),actor.tenantId()),new Scope(id(1),id(3),id(4)),id(2),now,now.plusSeconds(5)));}
    public Mono<Void> requireCurrent(SecurityPrincipal actor,Authorization authorization){currentChecks++;return authorization.caller().matches(actor)?Mono.empty():Mono.error(new IllegalStateException("Current model caller changed"));}
    ManagedCardEnrollmentService legacy(){return new ManagedCardEnrollmentServiceImpl(principals,this,command,reader,clock,nanos,()->id(ids.incrementAndGet()));}
    final class Driver implements ConnectionFactory {
        Map<String,List<Map<String,Object>>> tables=new HashMap<>(),snapshot;final List<String> sql=new ArrayList<>();int creates,commits,rollbacks,closes,writes,auditEnters,cardsEnters,auditPairs;boolean failAuditCount;long auditCount=0;boolean missingAuditCount;
        Map<String,List<Map<String,Object>>> copy(){var value=new HashMap<String,List<Map<String,Object>>>();tables.forEach((t,rows)->value.put(t,new ArrayList<>(rows.stream().map(LinkedHashMap::new).toList())));return value;}
        final Connection connection=(Connection)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Connection.class},(p,m,a)->switch(m.getName()){
            case "isAutoCommit"->true;case "setAutoCommit"->Mono.empty();case "getTransactionIsolationLevel"->IsolationLevel.READ_COMMITTED;
            case "beginTransaction"->Mono.fromRunnable(()->snapshot=copy());case "commitTransaction"->Mono.fromRunnable(()->commits++);case "rollbackTransaction"->Mono.fromRunnable(()->{rollbacks++;tables=snapshot;});case "close"->Mono.fromRunnable(()->closes++);case "createStatement"->statement((String)a[0]);case "hashCode"->System.identityHashCode(p);case "equals"->p==a[0];case "toString"->"ManagedCardAuditNetworklessDriver";default->Mono.empty();});
        Statement statement(String original){sql.add(original);String query=original.replace("\"","");var bound=new LinkedHashMap<Integer,Object>();return (Statement)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Statement.class},(p,m,a)->{
            if(m.getName().equals("bind")||m.getName().equals("bindNull")){Object v=a[1];if(v instanceof io.r2dbc.spi.Parameter parameter)v=parameter.getValue();bound.put((Integer)a[0],v instanceof Class?null:v);return p;}
            if(!m.getName().equals("execute"))return p;
            return Mono.defer(()->{
                if(query.contains("cards_managed_enrollment_enter")){cardsEnters++;return Mono.just(result(List.of(Map.of("context_nonce",bound.get(0),"action_id",bound.get(5),"purpose",bound.get(6),"scope_digest",bound.get(12),"db_now_millis",clock.instant().toEpochMilli(),"authority_until_millis",((OffsetDateTime)bound.get(13)).toInstant().toEpochMilli(),"command_until_millis",Math.min(((OffsetDateTime)bound.get(14)).toInstant().toEpochMilli(),clock.instant().plusSeconds(4).toEpochMilli()))),1));}
                if(query.contains("firefly_audit_enter")){auditEnters++;return Mono.just(result(List.of(Map.of("context_nonce",bound.get(0),"event_id",bound.get(5),"db_now_millis",clock.instant().toEpochMilli(),"until_millis",((OffsetDateTime)bound.get(12)).toInstant().toEpochMilli())),1));}
                if(query.contains("cards_managed_original_audit_pair")){auditPairs++;return Mono.just(result(List.of(Map.of("nonce",bound.get(0))),1));}
                if(query.contains("cards_managed_enrollment_complete")||query.contains("firefly_audit_complete"))return Mono.just(result(List.of(Map.of("nonce",bound.get(0))),1));
                var tableMatch=Pattern.compile("(?:INTO|UPDATE|FROM) (?:[a-z_]+[.])?([a-z_]+)").matcher(query);if(!tableMatch.find())return Mono.error(new IllegalArgumentException("Unsupported ordinary mapping statement"));String table=tableMatch.group(1);
                if(query.startsWith("INSERT")){writes++;if(failAuditCount&&table.startsWith("audit_"))return Mono.just(result(List.of(),auditCount,missingAuditCount));String[] columns=query.substring(query.indexOf('(')+1,query.indexOf(')')).split(",");var row=new LinkedHashMap<String,Object>();for(int i=0;i<columns.length;i++)row.put(columns[i].trim(),bound.get(i));tables.computeIfAbsent(table,k->new ArrayList<>()).add(row);return Mono.just(result(List.of(row),1));}
                if(query.startsWith("UPDATE")){writes++;var row=tables.get(table).getFirst();var values=Pattern.compile("([a-z_]+) = [$]([0-9]+)").matcher(query.substring(query.indexOf(" SET ")+5,query.indexOf(" WHERE ")));while(values.find())row.put(values.group(1),bound.get(Integer.parseInt(values.group(2))-1));return Mono.just(result(List.of(),1));}
                var rows=new ArrayList<>(tables.getOrDefault(table,List.of()));var filters=Pattern.compile("(?:[a-z_]+[.])?([a-z_]+) = [$]([0-9]+)").matcher(query);while(filters.find()){String field=filters.group(1);Object value=bound.get(Integer.parseInt(filters.group(2))-1);rows.removeIf(row->!Objects.equals(row.get(field),value));}
                if(query.toUpperCase().contains("COUNT("))return Mono.just(result(List.of(Map.of("count",(long)rows.size())),1));return Mono.just(result(rows,rows.size()));
            });
        });}
        Result result(List<Map<String,Object>> values,long count){return result(values,count,false);}
        Result result(List<Map<String,Object>> values,long count,boolean missing){return new Result(){public Publisher<Long> getRowsUpdated(){return missing?Mono.empty():Mono.just(count);}public <T>Publisher<T> map(java.util.function.BiFunction<Row,RowMetadata,? extends T> f){return Flux.fromIterable(values).map(v->f.apply(row(v),metadata(v)));}public <T>Publisher<T> map(Function<? super io.r2dbc.spi.Readable,? extends T> f){return Flux.fromIterable(values).map(v->f.apply(row(v)));}public Result filter(Predicate<Segment> p){return this;}public <T>Publisher<T> flatMap(Function<Segment,? extends Publisher<? extends T>> f){return Flux.empty();}};}
        Row row(Map<String,Object> values){return (Row)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{Row.class},(p,m,a)->{if(m.getName().equals("getMetadata"))return metadata(values);Object key=a[0]instanceof Integer i?new ArrayList<>(values.keySet()).get(i):a[0];return values.get(key);});}
        RowMetadata metadata(Map<String,Object> values){List<ColumnMetadata> columns=values.keySet().stream().map(n->(ColumnMetadata)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{ColumnMetadata.class},(p,m,a)->switch(m.getName()){case "getName"->n;case "getJavaType"->values.get(n)==null?String.class:values.get(n).getClass();case "getType"->new io.r2dbc.spi.Type(){public Class<?> getJavaType(){return values.get(n)==null?String.class:values.get(n).getClass();}public String getName(){return "OrdinaryModelColumn";}};default->null;})).toList();return (RowMetadata)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{RowMetadata.class},(p,m,a)->switch(m.getName()){case "getColumnMetadatas"->columns;case "getColumnMetadata"->a[0]instanceof Integer i?columns.get(i):columns.stream().filter(c->c.getName().equals(a[0])).findFirst().orElseThrow();case "contains"->values.containsKey(a[0]);default->null;});}
        public Publisher<? extends Connection> create(){creates++;return Mono.just(connection);}public ConnectionFactoryMetadata getMetadata(){return ()->"PostgreSQL";}
    }
}
