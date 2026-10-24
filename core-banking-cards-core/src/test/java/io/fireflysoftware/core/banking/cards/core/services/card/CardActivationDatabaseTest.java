/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.core.banking.cards.core.services.card;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CardActivationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.time.Duration;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Persistence mechanics only: seeded proofs do not qualify an issuer journey. */
@Testcontainers
class CardActivationDatabaseTest {
    private static final Fixture HISTORICAL = new Fixture(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine")
            .withDatabaseName("synthetic_activation_boundary")
            .withUsername("activation_boundary_bootstrap")
            .withPassword(UUID.randomUUID().toString())
            .withCreateContainerCmdModifier(c -> c.getHostConfig().withPortBindings(
                    new com.github.dockerjava.api.model.PortBinding(
                            com.github.dockerjava.api.model.Ports.Binding.bindIp("127.0.0.1"),
                            new com.github.dockerjava.api.model.ExposedPort(5432))));

    @BeforeAll
    static void setup() throws Exception {
        Flyway.configure().dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration").target("5").load().migrate();
        try (var connection = owner()) {
            execute(connection, "INSERT INTO card(card_id,card_number,party_id,account_id,card_status,is_active,is_locked,expiration_date,pin,cvv) VALUES ('"
                    + HISTORICAL.card + "','4111111111111111','" + HISTORICAL.party + "','" + HISTORICAL.account
                    + "','DRAFT',false,true,clock_timestamp()+interval '1 year','1234','987')");
        }
        Flyway.configure().dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration").load().migrate();
    }

    @Test
    void unissuedProofCannotCreateAnActivationAction() throws Exception {
        try (var connection = owner()) {
            requireBoundary(connection);
            UUID id = UUID.randomUUID();
            try (var statement = connection.createStatement()) {
                statement.execute("INSERT INTO card(card_id,card_number,card_status,is_active,is_locked) "
                        + "VALUES ('" + id + "','4111111111111111','DRAFT',false,true)");
            }
            var failure = assertThrows(SQLException.class, () -> reserve(connection, id, UUID.randomUUID(),
                    UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "a".repeat(64)));
            assertEquals("P0001", failure.getSQLState());
            assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + id + "'"));
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + id + "'"));
        }
    }

    @Test
    void wrongExpiredAndCrossTenantProofsLeaveTheCardUnusable() throws Exception {
        try (var connection = owner()) {
            for (String scenario : new String[]{"wrong", "expired", "tenant", "party", "account"}) {
                var fixture = seed(connection);
                if (scenario.equals("expired")) execute(connection, "UPDATE card_activation_proof SET expires_at=clock_timestamp()-interval '1 second' WHERE card_id='" + fixture.card + "'");
                var failure = assertThrows(SQLException.class, () -> reserve(connection, fixture.card, UUID.randomUUID(),
                        scenario.equals("tenant") ? UUID.randomUUID() : fixture.tenant, fixture.actor,
                        scenario.equals("party") ? UUID.randomUUID() : fixture.party,
                        scenario.equals("account") ? UUID.randomUUID() : fixture.account,
                        scenario.equals("wrong") ? "b".repeat(64) : "a".repeat(64)));
                assertEquals("P0001", failure.getSQLState());
                assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
                assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + fixture.card + "'"));
            }
        }
    }

    @Test
    void proofConsumptionCardStateAndReceiptCommitOnceAndRejectNewKeyReplay() throws Exception {
        try (var connection = owner()) {
            var fixture = seed(connection);
            reserve(connection, fixture.card, fixture.action, fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
            assertEquals("CONFIRMED", finish(connection, fixture, "CONFIRMED"));
            var activated = scalar(connection, "SELECT activation_date::text FROM card WHERE card_id='" + fixture.card + "'");
            reserve(connection, fixture.card, fixture.action, fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
            assertEquals("CONFIRMED", finish(connection, fixture, "CONFIRMED"));
            assertEquals(activated, scalar(connection, "SELECT activation_date::text FROM card WHERE card_id='" + fixture.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (consumed_at IS NOT NULL)::text FROM card_activation_proof WHERE card_id='" + fixture.card + "'"));
            assertEquals("1", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + fixture.card + "'"));
            assertThrows(SQLException.class, () -> reserve(connection, fixture.card, UUID.randomUUID(), fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64)));
        }
    }

    @Test
    void finalizationRollbackRetainsNeitherActivationNorConsumptionNorReceipt() throws Exception {
        Fixture fixture;
        try (var connection = owner()) {
            fixture = seed(connection);
            connection.setAutoCommit(false);
            reserve(connection, fixture.card, fixture.action, fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
            assertEquals("CONFIRMED", finish(connection, fixture, "CONFIRMED"));
            connection.rollback();
        }
        try (var connection = owner()) {
            assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (consumed_at IS NULL AND reserved_action_id IS NULL)::text FROM card_activation_proof WHERE card_id='" + fixture.card + "'"));
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void aRestrictionDuringIssuerVerificationCannotBeOverwrittenByConfirmation() throws Exception {
        try (var connection = owner()) {
            var fixture = seed(connection);
            reserve(connection, fixture.card, fixture.action, fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
            execute(connection, "UPDATE card SET card_status='BLOCKED' WHERE card_id='" + fixture.card + "'");
            assertEquals("RECONCILIATION_REQUIRED", finish(connection, fixture, "CONFIRMED"));
            assertEquals("BLOCKED", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (consumed_at IS NULL)::text FROM card_activation_proof WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void proofExpiryIsCheckedAfterWaitingForTheCardLock() throws Exception {
        try (var blocker = owner()) {
            var fixture = seed(blocker);
            execute(blocker, "UPDATE card_activation_proof SET expires_at=clock_timestamp()+interval '500 milliseconds' WHERE card_id='" + fixture.card + "'");
            blocker.setAutoCommit(false);
            scalar(blocker, "SELECT card_id::text FROM card WHERE card_id='" + fixture.card + "' FOR UPDATE");
            var pending = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                try (var contender = owner()) {
                    reserve(contender, fixture.card, fixture.action, fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
                    return "accepted";
                } catch (SQLException rejection) { return rejection.getSQLState(); }
            });
            Thread.sleep(750);
            assertFalse(pending.isDone());
            blocker.commit();
            assertEquals("P0001", pending.get(5, java.util.concurrent.TimeUnit.SECONDS));
            assertEquals("0", scalar(blocker, "SELECT count(*) FROM card_activation_action WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void concurrentDifferentKeysCannotReserveTheSameProof() throws Exception {
        Fixture fixture;
        try (var connection = owner()) { fixture = seed(connection); }
        var start = new java.util.concurrent.CountDownLatch(1);
        var attempts = new java.util.ArrayList<java.util.concurrent.CompletableFuture<Boolean>>();
        for (int index=0; index<2; index++) attempts.add(java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try (var connection = owner()) {
                start.await();
                reserve(connection, fixture.card, UUID.randomUUID(), fixture.tenant, fixture.actor, fixture.party, fixture.account, "a".repeat(64));
                return true;
            } catch (SQLException rejection) { assertEquals("P0001", rejection.getSQLState()); return false; }
            catch (InterruptedException interrupted) { throw new RuntimeException(interrupted); }
        }));
        start.countDown();
        int successes=0;
        for (var attempt : attempts) if (attempt.get(5, java.util.concurrent.TimeUnit.SECONDS)) successes++;
        assertEquals(1, successes);
        try (var connection = owner()) {
            assertEquals("1", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void coreIndependentlyRejectsWrongIssuerBindingAndPersistsTheUncertainReceipt() throws Exception {
        Fixture fixture;
        String proof = "syntheticPossessionProofForPersistenceTestsOnly0123456789";
        try (var connection=owner()) {
            fixture=seed(connection);
            String digest=java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(proof.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
            execute(connection,"UPDATE card_activation_proof SET proof_digest='"+digest+"' WHERE card_id='"+fixture.card+"'");
        }
        var factory=io.r2dbc.spi.ConnectionFactories.get("r2dbc:postgresql://"+postgres.getUsername()+":"
                +postgres.getPassword()+"@"+postgres.getHost()+":"+postgres.getMappedPort(5432)+"/"+postgres.getDatabaseName());
        var store=activationStore(factory);
        io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy policy=card -> reactor.core.publisher.Mono.just(
                new io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy.AuthorizedScope(
                        fixture.tenant,fixture.actor,fixture.party,fixture.account,java.time.OffsetDateTime.now().plusMinutes(1)));
        var wrongInstrument=new java.util.concurrent.atomic.AtomicBoolean(true);
        io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort issuer=(binding,action) -> reactor.core.publisher.Mono.just(
                new io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort.Observation(action,binding.tenantId(),
                        binding.adapterId(),wrongInstrument.get()?"different-instrument":binding.instrumentReference(),binding.version(),
                        io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort.Outcome.ACTIVATED,"synthetic-fixture-receipt"));
        var service=new io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CardActivationServiceImpl(store,
                java.util.Optional.of(policy),java.util.Optional.of(issuer));
        var request=new io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest();
        request.setActivationProof(proof);
        var first=service.activate(fixture.card,fixture.action,request).block(Duration.ofSeconds(5));
        assertNotNull(first);
        assertEquals(io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt.State.UNCERTAIN,first.state());
        try(var connection=owner()) {
            assertEquals("DRAFT",scalar(connection,"SELECT card_status::text FROM card WHERE card_id='"+fixture.card+"'"));
            assertEquals("UNCERTAIN",scalar(connection,"SELECT state FROM card_activation_action WHERE action_id='"+fixture.action+"'"));
            assertEquals("true",scalar(connection,"SELECT (consumed_at IS NULL)::text FROM card_activation_proof WHERE card_id='"+fixture.card+"'"));
        }
        wrongInstrument.set(false);
        var recovered=service.activate(fixture.card,fixture.action,request).block(Duration.ofSeconds(5));
        assertNotNull(recovered);
        assertEquals(io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt.State.CONFIRMED,recovered.state());
        assertEquals(recovered,service.activate(fixture.card,fixture.action,request).block(Duration.ofSeconds(5)));
    }

    @Test
    void anExpiredCardWithOnlyMonthAndYearCannotReserveActivation() throws Exception {
        try (var connection=owner()) {
            var fixture=seed(connection);
            execute(connection,"UPDATE card SET expiration_date=NULL,expiration_month=1,expiration_year=2000 WHERE card_id='"+fixture.card+"'");
            assertThrows(SQLException.class,() -> reserve(connection,fixture.card,fixture.action,fixture.tenant,
                    fixture.actor,fixture.party,fixture.account,"a".repeat(64)));
            assertEquals("DRAFT",scalar(connection,"SELECT card_status::text FROM card WHERE card_id='"+fixture.card+"'"));
        }
    }

    @Test
    void staleOrdinaryWritesCannotEraseCommittedActivationHistoryOrIssuerOwnership() throws Exception {
        try (var connection=owner()) {
            var fixture=seed(connection);
            reserve(connection,fixture.card,fixture.action,fixture.tenant,fixture.actor,fixture.party,fixture.account,"a".repeat(64));
            assertEquals("CONFIRMED",finish(connection,fixture,"CONFIRMED"));
            assertThrows(SQLException.class,() -> execute(connection,"UPDATE card SET card_status='DRAFT',activation_date=NULL,is_active=false,is_locked=true WHERE card_id='"+fixture.card+"'"));
            assertThrows(SQLException.class,() -> execute(connection,"UPDATE card SET party_id='"+UUID.randomUUID()+"' WHERE card_id='"+fixture.card+"'"));
            assertEquals("ACTIVE",scalar(connection,"SELECT card_status::text FROM card WHERE card_id='"+fixture.card+"'"));
            assertEquals("true",scalar(connection,"SELECT (activation_date IS NOT NULL)::text FROM card WHERE card_id='"+fixture.card+"'"));
        }
    }

    @Test
    void aStaleOrdinaryWriteCannotLiftARestrictionAppliedAfterItsRead() throws Exception {
        try (var connection=owner()) {
            var fixture=seed(connection);
            reserve(connection,fixture.card,fixture.action,fixture.tenant,fixture.actor,fixture.party,fixture.account,"a".repeat(64));
            assertEquals("CONFIRMED",finish(connection,fixture,"CONFIRMED"));
            execute(connection,"UPDATE card SET card_status='BLOCKED',is_active=false,is_locked=true WHERE card_id='"+fixture.card+"'");
            assertThrows(SQLException.class,() -> execute(connection,"UPDATE card SET card_status='ACTIVE',is_active=true,is_locked=false WHERE card_id='"+fixture.card+"'"));
            assertEquals("BLOCKED",scalar(connection,"SELECT card_status::text FROM card WHERE card_id='"+fixture.card+"'"));
        }
    }

    @Test
    void contradictoryOrUnknownExpiryCannotBeResolvedByExtendingTheKnownDate() throws Exception {
        try (var connection=owner()) {
            for(String dates : new String[]{
                    "expiration_date=clock_timestamp()+interval '1 year',expiration_month=1,expiration_year=2000",
                    "expiration_date=clock_timestamp()+interval '1 year',expiration_month=1,expiration_year=2099",
                    "expiration_date=NULL,expiration_month=NULL,expiration_year=NULL"}) {
                var fixture=seed(connection);
                execute(connection,"UPDATE card SET "+dates+" WHERE card_id='"+fixture.card+"'");
                assertThrows(SQLException.class,() -> reserve(connection,fixture.card,fixture.action,fixture.tenant,
                        fixture.actor,fixture.party,fixture.account,"a".repeat(64)));
            }
        }
    }

    @Test
    void historicalAuthenticationDataRemainsQuarantinedAtTheNewActivationBoundary() throws Exception {
        try (var connection = owner()) {
            seedBinding(connection, HISTORICAL);
            var failure = assertThrows(SQLException.class, () -> reserve(connection, HISTORICAL.card, HISTORICAL.action,
                    HISTORICAL.tenant, HISTORICAL.actor, HISTORICAL.party, HISTORICAL.account, "a".repeat(64)));
            assertEquals("P0001", failure.getSQLState());
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE card_id='" + HISTORICAL.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (pin='1234' AND cvv='987' AND card_status='DRAFT')::text FROM card WHERE card_id='" + HISTORICAL.card + "'"));
        }
    }

    @Test
    void concurrentCrossCardActionKeyReuseReturnsConflictThroughPersistenceAdapter() throws Exception {
        try (var firstTransaction = owner(); var observer = owner()) {
            var first = seed(firstTransaction);
            var second = seed(firstTransaction);
            firstTransaction.setAutoCommit(false);
            reserve(firstTransaction, first.card, first.action, first.tenant, first.actor, first.party, first.account,
                    "a".repeat(64));
            var factory = io.r2dbc.spi.ConnectionFactories.get("r2dbc:postgresql://" + postgres.getUsername() + ":"
                    + postgres.getPassword() + "@" + postgres.getHost() + ":" + postgres.getMappedPort(5432)
                    + "/" + postgres.getDatabaseName());
            var store = activationStore(factory);
            var scope = new io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy.AuthorizedScope(
                    second.tenant, second.actor, second.party, second.account, java.time.OffsetDateTime.now().plusMinutes(1));
            var competing = store.reserve(first.action, second.card, scope, "a".repeat(64)).toFuture();
            boolean waitingForFirstCommit = false;
            long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.SECONDS.toNanos(5);
            while (!waitingForFirstCommit && System.nanoTime() < deadline) {
                waitingForFirstCommit = "true".equals(scalar(observer,
                        "SELECT EXISTS (SELECT 1 FROM pg_stat_activity WHERE datname=current_database() "
                                + "AND wait_event_type='Lock' AND query LIKE 'SELECT * FROM reserve_card_activation%')::text"));
                if (!waitingForFirstCommit) Thread.sleep(10);
            }
            assertTrue(waitingForFirstCommit, "Competing legitimate reservations must overlap at the action-key insert");
            firstTransaction.commit();
            var failed = assertThrows(java.util.concurrent.ExecutionException.class,
                    () -> competing.get(5, java.util.concurrent.TimeUnit.SECONDS));
            var response = assertInstanceOf(org.springframework.web.server.ResponseStatusException.class, failed.getCause());
            assertEquals(org.springframework.http.HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("1", scalar(observer, "SELECT count(*) FROM card_activation_action WHERE action_id='" + first.action + "'"));
            assertEquals("DRAFT", scalar(observer, "SELECT card_status::text FROM card WHERE card_id='" + second.card + "'"));
            assertEquals("true", scalar(observer, "SELECT (reserved_action_id IS NULL AND consumed_at IS NULL)::text "
                    + "FROM card_activation_proof WHERE card_id='" + second.card + "'"));
        }
    }

    @Test
    void derivedIssuerReadMapsTheActualBindingAndRejectsAnUnknownCard() throws Exception {
        Fixture fixture;
        try (var connection = owner()) { fixture = seed(connection); }
        var store = activationStore(reactiveFactory());
        var binding = store.binding(fixture.card).block(Duration.ofSeconds(5));
        assertNotNull(binding);
        assertEquals(fixture.card, binding.cardId());
        assertEquals(fixture.tenant, binding.tenantId());
        assertEquals(fixture.party, binding.partyId());
        assertEquals(fixture.account, binding.accountId());
        assertEquals("synthetic-persistence-fixture", binding.adapterId());
        assertEquals("instrument-" + fixture.card, binding.instrumentReference());
        assertEquals(1L, binding.version());
        var failure = assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> store.binding(UUID.randomUUID()).block(Duration.ofSeconds(5)));
        assertEquals(org.springframework.http.HttpStatus.CONFLICT, failure.getStatusCode());
    }

    @Test
    void springDataNativeOperationsRetainNullReceiptRecoveryAndHistoricalReplay() throws Exception {
        Fixture fixture;
        try (var connection = owner()) { fixture = seed(connection); }
        var store = activationStore(reactiveFactory());
        var scope = scope(fixture);
        var reserved = store.reserve(fixture.action, fixture.card, scope, "a".repeat(64)).block(Duration.ofSeconds(5));
        assertNotNull(reserved);
        assertEquals("RESERVED", reserved.state().name());
        assertNull(reserved.activationTime());
        var uncertain = store.finish(fixture.action, scope, "UNCERTAIN", null).block(Duration.ofSeconds(5));
        assertNotNull(uncertain);
        assertEquals("UNCERTAIN", uncertain.state().name());
        assertNull(uncertain.activationTime());
        try (var connection = owner()) {
            assertEquals("true", scalar(connection, "SELECT (issuer_receipt IS NULL)::text FROM card_activation_action WHERE action_id='" + fixture.action + "'"));
            assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
        }
        var confirmed = store.finish(fixture.action, scope, "CONFIRMED", "synthetic-fixture-receipt").block(Duration.ofSeconds(5));
        assertNotNull(confirmed);
        assertEquals(fixture.action, confirmed.actionId());
        assertEquals(fixture.card, confirmed.cardId());
        assertEquals("CONFIRMED", confirmed.state().name());
        assertNotNull(confirmed.activationTime());
        assertEquals(confirmed, store.reserve(fixture.action, fixture.card, scope, "a".repeat(64)).block(Duration.ofSeconds(5)));
        assertEquals(confirmed, store.finish(fixture.action, scope, "CONFIRMED", "synthetic-fixture-receipt").block(Duration.ofSeconds(5)));
    }

    @Test
    void repositoryNativeCallsUseTheSameReactiveTransactionAndRollbackEveryEffect() throws Exception {
        Fixture fixture;
        try (var connection = owner()) { fixture = seed(connection); }
        var factory = reactiveFactory();
        var store = activationStore(factory);
        var transaction = org.springframework.transaction.reactive.TransactionalOperator.create(
                new org.springframework.r2dbc.connection.R2dbcTransactionManager(factory));
        var marker = new IllegalStateException("synthetic rollback marker");
        var result = transaction.transactional(store.reserve(fixture.action, fixture.card, scope(fixture), "a".repeat(64))
                .then(store.finish(fixture.action, scope(fixture), "CONFIRMED", "synthetic-fixture-receipt"))
                .flatMap(receipt -> reactor.core.publisher.Mono.error(marker)));
        assertSame(marker, assertThrows(IllegalStateException.class, () -> result.block(Duration.ofSeconds(5))));
        try (var connection = owner()) {
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE action_id='" + fixture.action + "'"));
            assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (reserved_action_id IS NULL AND consumed_at IS NULL)::text FROM card_activation_proof WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void derivedPromotionCountKeepsCardScopeAndUsesTheActualUuidId() throws Exception {
        Fixture first;
        Fixture other;
        UUID promotion = UUID.randomUUID();
        try (var connection = owner()) {
            first = seed(connection);
            other = seed(connection);
            execute(connection, "INSERT INTO card_promotion(promotion_id,card_id,promotion_name) VALUES ('" + promotion + "','" + first.card + "','Synthetic persistence fixture')");
        }
        var factory = new org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory(
                new org.springframework.data.r2dbc.core.R2dbcEntityTemplate(reactiveFactory()));
        var promotions = factory.getRepository(io.fireflysoftware.core.banking.cards.models.repositories.promotion.CardPromotionRepository.class);
        assertEquals(1L, promotions.countByCardId(first.card).block(Duration.ofSeconds(5)));
        assertEquals(0L, promotions.countByCardId(other.card).block(Duration.ofSeconds(5)));
        assertTrue(promotions.existsById(promotion).block(Duration.ofSeconds(5)));
        assertFalse(promotions.existsById(UUID.randomUUID()).block(Duration.ofSeconds(5)));
    }

    @Test
    void unrelatedNativeGuardFailureRemainsUnavailableInsteadOfBecomingABusinessConflict() throws Exception {
        Fixture fixture;
        try (var connection = owner()) {
            fixture = seed(connection);
            String name = "synthetic_guard_" + fixture.action.toString().replace('-', '_');
            execute(connection, "CREATE FUNCTION " + name + "() RETURNS trigger LANGUAGE plpgsql AS $$ BEGIN "
                    + "RAISE EXCEPTION 'Synthetic unrelated persistence failure'; END; $$");
            execute(connection, "CREATE TRIGGER " + name + " BEFORE INSERT ON card_activation_action "
                    + "FOR EACH ROW WHEN (NEW.action_id='" + fixture.action + "'::uuid) EXECUTE FUNCTION " + name + "()");
        }
        var store = activationStore(reactiveFactory());
        var unavailable = assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> store.reserve(fixture.action, fixture.card, scope(fixture), "a".repeat(64)).block(Duration.ofSeconds(5)));
        assertEquals(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, unavailable.getStatusCode());
        assertFalse(unavailable.getReason().contains("Synthetic"));
        try (var connection = owner()) {
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE action_id='" + fixture.action + "'"));
            assertEquals("DRAFT", scalar(connection, "SELECT card_status::text FROM card WHERE card_id='" + fixture.card + "'"));
            assertEquals("true", scalar(connection, "SELECT (reserved_action_id IS NULL AND consumed_at IS NULL)::text FROM card_activation_proof WHERE card_id='" + fixture.card + "'"));
        }
    }

    @Test
    void knownNativeActivationRejectionsStillMapToBusinessConflict() throws Exception {
        Fixture fixture;
        try (var connection = owner()) { fixture = seed(connection); }
        var store = activationStore(reactiveFactory());
        var rejected = assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> store.reserve(fixture.action, fixture.card, scope(fixture), "b".repeat(64)).block(Duration.ofSeconds(5)));
        assertEquals(org.springframework.http.HttpStatus.CONFLICT, rejected.getStatusCode());
        try (var connection = owner()) {
            assertEquals("0", scalar(connection, "SELECT count(*) FROM card_activation_action WHERE action_id='" + fixture.action + "'"));
        }
    }

    private static io.r2dbc.spi.ConnectionFactory reactiveFactory() {
        return io.r2dbc.spi.ConnectionFactories.get("r2dbc:postgresql://" + postgres.getUsername() + ":"
                + postgres.getPassword() + "@" + postgres.getHost() + ":" + postgres.getMappedPort(5432)
                + "/" + postgres.getDatabaseName());
    }

    private static io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy.AuthorizedScope scope(Fixture fixture) {
        return new io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy.AuthorizedScope(
                fixture.tenant, fixture.actor, fixture.party, fixture.account, java.time.OffsetDateTime.now().plusMinutes(1));
    }

    private static io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationStore activationStore(
            io.r2dbc.spi.ConnectionFactory connectionFactory) {
        var operations = new org.springframework.data.r2dbc.core.R2dbcEntityTemplate(connectionFactory);
        var repositories = new org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory(operations);
        return new io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationStore(
                repositories.getRepository(io.fireflysoftware.core.banking.cards.models.repositories.card.CardActivationActionRepository.class),
                repositories.getRepository(io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository.class));
    }

    private record Fixture(UUID card, UUID action, UUID tenant, UUID actor, UUID party, UUID account) {}
    private static Fixture seed(Connection connection) throws SQLException {
        var fixture = new Fixture(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        execute(connection, "INSERT INTO card(card_id,card_number,party_id,account_id,card_status,is_active,is_locked,expiration_date) VALUES ('" + fixture.card + "','4111111111111111','" + fixture.party + "','" + fixture.account + "','DRAFT',false,true,clock_timestamp()+interval '1 year')");
        seedBinding(connection, fixture);
        return fixture;
    }
    private static void seedBinding(Connection connection, Fixture fixture) throws SQLException {
        execute(connection, "INSERT INTO card_issuer_instrument(card_id,tenant_id,party_id,account_id,adapter_id,instrument_reference,issuance_reference,issued_inactive) VALUES ('" + fixture.card + "','" + fixture.tenant + "','" + fixture.party + "','" + fixture.account + "','synthetic-persistence-fixture','instrument-" + fixture.card + "','synthetic-issuance',true)");
        execute(connection, "INSERT INTO card_activation_proof(card_id,proof_digest,binding_version,expires_at) VALUES ('" + fixture.card + "','" + "a".repeat(64) + "',1,clock_timestamp()+interval '1 minute')");
    }
    private static void execute(Connection connection, String sql) throws SQLException {
        try (var statement = connection.createStatement()) { statement.execute(sql); }
    }
    private static String finish(Connection connection, Fixture fixture, String outcome) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT state FROM finish_card_activation(?,?,?,?,?,clock_timestamp()+interval '1 minute',?,'synthetic-verified-receipt')")) {
            int index=1;
            for (UUID value : new UUID[]{fixture.action,fixture.tenant,fixture.actor,fixture.party,fixture.account}) statement.setObject(index++, value);
            statement.setString(index, outcome);
            try (var rows=statement.executeQuery()) { assertTrue(rows.next()); return rows.getString(1); }
        }
    }

    private static void requireBoundary(Connection connection) throws SQLException {
        assertEquals("true", scalar(connection,
                "SELECT (to_regprocedure('reserve_card_activation(uuid,uuid,uuid,uuid,uuid,uuid,text,timestamp with time zone)') IS NOT NULL)::text"),
                "Activation must use a persisted proof and action boundary rather than ordinary card CRUD");
    }

    static String scalar(Connection connection, String sql) throws SQLException {
        try (var statement = connection.createStatement(); var rows = statement.executeQuery(sql)) {
            assertTrue(rows.next());
            return rows.getString(1);
        }
    }

    static void reserve(Connection connection, UUID card, UUID action, UUID tenant, UUID actor,
            UUID party, UUID account, String digest) throws SQLException {
        try (var statement = connection.prepareStatement(
                "SELECT * FROM reserve_card_activation(?,?,?,?,?,?,?,clock_timestamp()+interval '1 minute')")) {
            int index=1;
            for (UUID value : new UUID[]{action,card,tenant,actor,party,account}) statement.setObject(index++, value);
            statement.setString(index, digest);
            statement.executeQuery().close();
        }
    }

    static Connection owner() throws SQLException {
        return DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}
