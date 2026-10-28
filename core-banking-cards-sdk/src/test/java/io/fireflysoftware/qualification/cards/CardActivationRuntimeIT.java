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
import io.fireflysoftware.core.banking.cards.sdk.model.CardActivationRequest;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Real generated client and owner executable; no issuer/tenant adapter or positive journey claim. */
@Testcontainers
class CardActivationRuntimeIT {
    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine")
            .withDatabaseName("synthetic_activation_http")
            .withUsername("activation_http_bootstrap")
            .withPassword(UUID.randomUUID().toString())
            .withCreateContainerCmdModifier(c -> c.getHostConfig().withPortBindings(
                    new com.github.dockerjava.api.model.PortBinding(
                            com.github.dockerjava.api.model.Ports.Binding.bindIp("127.0.0.1"),
                            new com.github.dockerjava.api.model.ExposedPort(5432))));
    private static Process app;
    private static int port;
    private static Path log;
    private static com.sun.net.httpserver.HttpServer proxy;
    private static final java.util.concurrent.atomic.AtomicReference<String> observedBody = new java.util.concurrent.atomic.AtomicReference<>();
    private static final java.util.concurrent.atomic.AtomicReference<String> observedPath = new java.util.concurrent.atomic.AtomicReference<>();
    private static final java.util.concurrent.atomic.AtomicReference<String> observedKey = new java.util.concurrent.atomic.AtomicReference<>();

    @BeforeAll
    static void start() throws Exception {
        Flyway.configure().dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration").load().migrate();
        try (var socket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            port = socket.getLocalPort();
        }
        log = Path.of("target/activation-boundary-" + port + ".log");
        app = new ProcessBuilder(Path.of(System.getProperty("java.home"), "bin/java").toString(),
                "-Xmx384m", "-jar", "../core-banking-cards-web/target/core-banking-cards.jar",
                "--server.address=127.0.0.1", "--server.port=" + port,
                "--DB_HOST=" + postgres.getHost(), "--DB_PORT=" + postgres.getMappedPort(5432),
                "--DB_NAME=" + postgres.getDatabaseName(), "--DB_USERNAME=" + postgres.getUsername(),
                "--DB_PASSWORD=" + postgres.getPassword(), "--DB_SSL_MODE=disable",
                "--spring.flyway.enabled=false", "--firefly.eda.enabled=false", "--firefly.stepevents.enabled=false")
                .redirectErrorStream(true).redirectOutput(log.toFile()).start();
        proxy = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        proxy.createContext("/", exchange -> {
            byte[] body = exchange.getRequestBody().readAllBytes();
            observedBody.set(new String(body, java.nio.charset.StandardCharsets.UTF_8));
            observedPath.set(exchange.getRequestURI().toString());
            observedKey.set(exchange.getRequestHeaders().getFirst("X-Idempotency-Key"));
            try {
                var forwarded = HttpRequest.newBuilder(URI.create(base() + exchange.getRequestURI()))
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", observedKey.get())
                        .method(exchange.getRequestMethod(), HttpRequest.BodyPublishers.ofByteArray(body)).build();
                var response = HttpClient.newHttpClient().send(forwarded, HttpResponse.BodyHandlers.ofByteArray());
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(response.statusCode(), response.body().length);
                exchange.getResponseBody().write(response.body());
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                exchange.sendResponseHeaders(503, -1);
            } finally {
                exchange.close();
            }
        });
        proxy.start();
        var http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(1)).build();
        for (int attempt = 0; attempt < 160; attempt++) {
            assertTrue(app.isAlive(), "Owned card executable exited before startup");
            try {
                if (http.send(HttpRequest.newBuilder(URI.create(base() + "/v3/api-docs"))
                        .timeout(Duration.ofSeconds(1)).GET().build(), HttpResponse.BodyHandlers.discarding())
                        .statusCode() == 200) return;
            } catch (java.io.IOException unavailable) {
                // Wait only for the owned executable's loopback listener.
            }
            Thread.sleep(250);
        }
        fail("Owned card executable startup timeout");
    }

    @AfterAll
    static void stop() throws Exception {
        if (proxy != null) proxy.stop(0);
        if (app == null) return;
        app.destroy();
        if (!app.waitFor(8, TimeUnit.SECONDS)) {
            app.destroyForcibly();
            assertTrue(app.waitFor(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void runtimeSpecKeepsOwnerActivationReplayOutsideTransportResponseReuse() throws Exception {
        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(base() + "/v3/api-docs")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var operation = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.body())
                .path("paths").path("/api/v1/cards/{cardId}/activation").path("post");
        assertTrue(operation.path("x-disable-idempotency").asBoolean(),
                "Activation retries must reach core authorization and persisted action receipts");
        boolean requiredUuidActionHeader = false;
        for (var parameter : operation.path("parameters")) {
            if ("X-Idempotency-Key".equals(parameter.path("name").asText())
                    && "header".equals(parameter.path("in").asText())) {
                requiredUuidActionHeader = parameter.path("required").asBoolean()
                        && "uuid".equals(parameter.path("schema").path("format").asText());
            }
        }
        assertTrue(requiredUuidActionHeader, "Owner action UUID remains an explicit required contract");
    }

    @Test
    void generatedClientCannotActivateWithoutAuthenticatedPolicyAndIssuerAdapter() throws Exception {
        String proof = "syntheticPossessionProofForHttpBoundaryOnly0123456789";
        var request = new CardActivationRequest().activationProof(proof);
        assertFalse(request.toString().contains(proof));
        var client = new ApiClient();
        client.setBasePath("http://127.0.0.1:" + proxy.getAddress().getPort());
        var api = new CardsApi(client);
        UUID card = UUID.randomUUID();
        UUID action = UUID.randomUUID();
        var rejected = assertThrows(WebClientResponseException.class,
                () -> api.activateCard(card, action, request).block(Duration.ofSeconds(20)));
        assertEquals(503, rejected.getStatusCode().value());
        assertEquals(proof, new com.fasterxml.jackson.databind.ObjectMapper().readTree(observedBody.get()).get("activationProof").asText());
        assertEquals("/api/v1/cards/" + card + "/activation", observedPath.get());
        assertEquals(action.toString(), observedKey.get());
        assertFalse(rejected.getResponseBodyAsString().contains(proof));
        assertFalse(Files.readString(log).contains(proof));
        String invalidProof = "unissued-proof-must-not-be-echoed";
        request.setActivationProof(invalidProof);
        var invalid = assertThrows(WebClientResponseException.class,
                () -> api.activateCard(UUID.randomUUID(), UUID.randomUUID(), request).block(Duration.ofSeconds(20)));
        assertEquals(400, invalid.getStatusCode().value());
        assertFalse(invalid.getResponseBodyAsString().contains(invalidProof));
        assertFalse(Files.readString(log).contains(invalidProof));
        try (var connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
                var statement = connection.createStatement();
                var rows = statement.executeQuery("SELECT count(*) FROM card_activation_action")) {
            assertTrue(rows.next());
            assertEquals(0, rows.getInt(1));
        }
    }

    private static String base() { return "http://127.0.0.1:" + port; }
}
