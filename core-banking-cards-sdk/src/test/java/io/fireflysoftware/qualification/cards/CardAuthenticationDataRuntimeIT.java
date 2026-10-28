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

package io.fireflysoftware.qualification.cards;

import static org.junit.jupiter.api.Assertions.*;

import io.fireflysoftware.core.banking.cards.sdk.api.CardsApi;
import io.fireflysoftware.core.banking.cards.sdk.invoker.ApiClient;
import io.fireflysoftware.core.banking.cards.sdk.model.CardDTO;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.net.*;
import java.net.http.*;
import java.nio.file.Path;
import java.sql.*;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Real owner executable/client credential boundary, without processor or tenant-authority claims.
 */
@Testcontainers
class CardAuthenticationDataRuntimeIT {
    private static final Duration WAIT = Duration.ofSeconds(20);
    private static final String PASSWORD = UUID.randomUUID().toString();
    private static final UUID LEGACY = UUID.randomUUID();
    private static final UUID ISSUER = UUID.randomUUID(), NETWORK = UUID.randomUUID();
    private static final UUID TYPE = UUID.randomUUID(), BIN = UUID.randomUUID();
    private static Process app;
    private static int port;
    private static String historicalDigest;

    @Container
    static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17-alpine")
                    .withDatabaseName("synthetic_card_http")
                    .withUsername("card_http_bootstrap")
                    .withPassword(UUID.randomUUID().toString())
                    .withCreateContainerCmdModifier(
                            c ->
                                    c.getHostConfig()
                                            .withPortBindings(
                                                    new com.github.dockerjava.api.model.PortBinding(
                                                            com.github.dockerjava.api.model.Ports
                                                                    .Binding.bindIp("127.0.0.1"),
                                                            new com.github.dockerjava.api.model
                                                                    .ExposedPort(5432))));

    @BeforeAll
    static void start() throws Exception {
        var migration =
                Flyway.configure()
                        .dataSource(
                                postgres.getJdbcUrl(),
                                postgres.getUsername(),
                                postgres.getPassword())
                        .locations("classpath:db/migration");
        migration.target("5").load().migrate();
        try (var connection = owner();
                var sql = connection.createStatement()) {
            sql.execute(
                    "INSERT INTO card(card_id,card_number,pin,cvv,card_status) VALUES ('"
                            + LEGACY
                            + "','4111111111111111','1234','987','ACTIVE')");
            sql.execute(
                    "INSERT INTO issuer(issuer_id,issuer_name,issuer_code) VALUES ('"
                            + ISSUER
                            + "','Synthetic issuer','SYNTHETIC')");
            sql.execute(
                    "INSERT INTO card_network(card_network_id,network_name,network_code) VALUES ('"
                            + NETWORK
                            + "','Synthetic network','SYNTHETIC')");
            sql.execute(
                    "INSERT INTO card_type(card_type_id,type_name,type_code) VALUES ('"
                            + TYPE
                            + "','Synthetic debit','SYNTHETIC')");
            sql.execute(
                    "INSERT INTO bin(bin_id,bin_number,issuer_id,card_network_id,card_type_id)"
                        + " VALUES ('"
                            + BIN
                            + "','411111','"
                            + ISSUER
                            + "','"
                            + NETWORK
                            + "','"
                            + TYPE
                            + "')");
            sql.execute(
                    "CREATE ROLE card_http_app LOGIN PASSWORD '"
                            + PASSWORD
                            + "' NOSUPERUSER NOBYPASSRLS");
            sql.execute("GRANT USAGE ON SCHEMA public TO card_http_app");
            sql.execute(
                    "GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA public TO"
                        + " card_http_app");
        }
        historicalDigest =
                scalar(
                        "SELECT md5(row_to_json(c)::text) FROM card c WHERE card_id='"
                                + LEGACY
                                + "'");
        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();
        try (var socket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            port = socket.getLocalPort();
        }
        var jar =
                Path.of("../core-banking-cards-web/target/core-banking-cards.jar")
                        .toAbsolutePath()
                        .normalize();
        assertTrue(java.nio.file.Files.isRegularFile(jar));
        app =
                new ProcessBuilder(
                                Path.of(System.getProperty("java.home"), "bin/java").toString(),
                                "-Xmx384m",
                                "-jar",
                                jar.toString(),
                                "--server.address=127.0.0.1",
                                "--server.port=" + port,
                                "--DB_HOST=" + postgres.getHost(),
                                "--DB_PORT=" + postgres.getMappedPort(5432),
                                "--DB_NAME=" + postgres.getDatabaseName(),
                                "--DB_USERNAME=card_http_app",
                                "--DB_PASSWORD=" + PASSWORD,
                                "--DB_SSL_MODE=disable",
                                "--spring.flyway.enabled=false",
                                "--firefly.eda.enabled=false",
                                "--firefly.stepevents.enabled=false")
                        .redirectErrorStream(true)
                        .redirectOutput(Path.of("target/card-boundary-" + port + ".log").toFile())
                        .start();
        var http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(1)).build();
        for (int i = 0; i < 160; i++) {
            assertTrue(app.isAlive(), "Private card owner exited before startup");
            try {
                if (http.send(
                                        HttpRequest.newBuilder(URI.create(base() + "/v3/api-docs"))
                                                .timeout(Duration.ofSeconds(1))
                                                .GET()
                                                .build(),
                                        HttpResponse.BodyHandlers.discarding())
                                .statusCode()
                        == 200) return;
            } catch (java.io.IOException unavailable) {
                // Retry the owned process while its listener starts.
            }
            Thread.sleep(250);
        }
        fail("Private card owner startup timeout");
    }

    @AfterAll
    static void stop() throws Exception {
        if (app == null) return;
        app.destroy();
        if (!app.waitFor(8, TimeUnit.SECONDS)) {
            app.destroyForcibly();
            assertTrue(app.waitFor(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void generatedClientSupportsCredentialFreeCreateReadAndUpdate() {
        var api = api();
        var created = api.createCard(body(), UUID.randomUUID().toString()).block(WAIT);
        assertNotNull(created.getCardId());
        assertNull(created.getPin());
        assertNull(created.getCvv());
        var stored = api.getCard(created.getCardId(), null).block(WAIT);
        stored.setLockReason("Synthetic service request");
        var updated =
                api.updateCard(created.getCardId(), stored, UUID.randomUUID().toString())
                        .block(WAIT);
        assertEquals("Synthetic service request", updated.getLockReason());
        assertEquals(
                "Synthetic service request",
                api.getCard(created.getCardId(), null).block(WAIT).getLockReason());
    }

    @Test
    void credentialRejectionsHaveNoWriteAndDoNotEchoSubmittedValues() throws Exception {
        var api = api();
        var existing = api.createCard(body(), UUID.randomUUID().toString()).block(WAIT);
        long before = Long.parseLong(scalar("SELECT count(*) FROM card"));
        for (boolean pin : new boolean[] {true, false}) {
            for (String input :
                    new String[] {"", "synthetic-private-credential-must-not-be-echoed"}) {
                var request = body();
                if (pin) request.setPin(input);
                else request.setCvv(input);
                rejected(
                        400,
                        input,
                        () -> api.createCard(request, UUID.randomUUID().toString()).block(WAIT));
                rejected(
                        400,
                        input,
                        () ->
                                api.updateCard(
                                                existing.getCardId(),
                                                request,
                                                UUID.randomUUID().toString())
                                        .block(WAIT));
                assertEquals(before, Long.parseLong(scalar("SELECT count(*) FROM card")));
                assertEquals(
                        "0",
                        scalar(
                                "SELECT count(*) FROM card WHERE card_id='"
                                        + existing.getCardId()
                                        + "' AND (pin IS NOT NULL OR cvv IS NOT NULL)"));
            }
        }
    }

    @Test
    void historicalReadIsRedactedAndOrdinaryUpdatePreservesTheRow() throws Exception {
        var api = api();
        var stored = api.getCard(LEGACY, null).block(WAIT);
        assertNull(stored.getPin());
        assertNull(stored.getCvv());
        var raw =
                HttpClient.newHttpClient()
                        .send(
                                HttpRequest.newBuilder(
                                                URI.create(base() + "/api/v1/cards/" + LEGACY))
                                        .header("X-Request-ID", UUID.randomUUID().toString())
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, raw.statusCode());
        var json = ApiClient.createDefaultMapper(null).readTree(raw.body());
        assertFalse(json.has("pin"));
        assertFalse(json.has("cvv"));
        rejected(
                409,
                null,
                () -> api.updateCard(LEGACY, body(), UUID.randomUUID().toString()).block(WAIT));
        assertEquals(
                historicalDigest,
                scalar(
                        "SELECT md5(row_to_json(c)::text) FROM card c WHERE card_id='"
                                + LEGACY
                                + "'"));
    }

    private static void rejected(int status, String input, Runnable action) {
        var error = assertThrows(WebClientResponseException.class, action::run);
        assertEquals(status, error.getStatusCode().value());
        assertFalse(error.getResponseBodyAsString().contains("rejectedValue"));
        if (input != null && !input.isEmpty())
            assertFalse(error.getResponseBodyAsString().contains(input));
    }

    private static CardsApi api() {
        var transport =
                ApiClient.buildWebClientBuilder()
                        .filter(
                                (request, next) ->
                                        next.exchange(
                                                ClientRequest.from(request)
                                                        .header(
                                                                "X-Request-ID",
                                                                UUID.randomUUID().toString())
                                                        .build()))
                        .build();
        var client = new ApiClient(transport);
        client.setBasePath(base());
        return new CardsApi(client);
    }

    private static CardDTO body() {
        return new CardDTO()
                .cardNumber("4111111111111111")
                .maskedCardNumber("************1111")
                .binId(BIN)
                .cardTypeId(TYPE)
                .cardNetworkId(NETWORK)
                .issuerId(ISSUER)
                .accountId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .cardStatus(CardDTO.CardStatusEnum.DRAFT)
                .cardHolderName("Synthetic Cardholder")
                .cardHolderId("synthetic-cardholder")
                .expirationMonth(11)
                .expirationYear(2029)
                .isPhysical(true)
                .isVirtual(false)
                .isPrimary(true)
                .isActive(false)
                .isLocked(false)
                .currencyCode("EUR");
    }

    private static String base() {
        return "http://127.0.0.1:" + port;
    }

    private static Connection owner() throws SQLException {
        return DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    private static String scalar(String query) throws SQLException {
        try (var connection = owner();
                var statement = connection.createStatement();
                var rows = statement.executeQuery(query)) {
            assertTrue(rows.next());
            return rows.getString(1);
        }
    }
}
