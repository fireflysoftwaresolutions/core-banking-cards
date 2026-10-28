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

import io.fireflysoftware.core.banking.cards.core.services.card.impl.CardServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import io.fireflysoftware.core.banking.cards.core.mappers.card.CardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardRepository;
import io.r2dbc.spi.ConnectionFactories;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationRequest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import reactor.test.StepVerifier;

import java.sql.*;
import java.time.Duration;
import java.util.UUID;

/** Disposable owner database checks; this does not qualify a card processor or activation. */
@Testcontainers
class CardSensitiveDataDatabaseTest {
    private static final Duration WAIT = Duration.ofSeconds(15);
    private static final String APP = "card_boundary_app";
    private static final String PASSWORD = UUID.randomUUID().toString();
    private static final UUID LEGACY = UUID.fromString("79a78ab8-c18a-48ce-be0d-f1e538e72673");
    private static String beforeMigration;
    private static String afterMigration;
    private static CardServiceImpl service;

    @Container
    static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17-alpine")
                    .withDatabaseName("synthetic_card_boundary")
                    .withUsername("card_boundary_bootstrap")
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
    static void setup() throws Exception {
        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .target("5")
                .load()
                .migrate();
        try (var connection = owner();
                var statement = connection.createStatement()) {
            statement.execute(
                    "INSERT INTO card(card_id,card_number,pin,cvv,card_status) VALUES ('"
                            + LEGACY
                            + "','4111111111111111','1234','987','ACTIVE')");
            statement.execute("CREATE ROLE " + APP + " LOGIN PASSWORD '" + PASSWORD + "'");
            statement.execute("GRANT USAGE ON SCHEMA public TO " + APP);
            statement.execute(
                    "GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA public TO " + APP);
        }
        beforeMigration = legacyDigest();
        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();
        afterMigration = legacyDigest();
        var factory =
                ConnectionFactories.get(
                        "r2dbc:postgresql://"
                                + APP
                                + ":"
                                + PASSWORD
                                + "@"
                                + postgres.getHost()
                                + ":"
                                + postgres.getMappedPort(5432)
                                + "/"
                                + postgres.getDatabaseName());
        var template = new R2dbcEntityTemplate(factory);
        FilterUtils.initializeTemplate(template);
        var repository = new R2dbcRepositoryFactory(template).getRepository(CardRepository.class);
        service = new CardServiceImpl();
        ReflectionTestUtils.setField(service, "repository", repository);
        ReflectionTestUtils.setField(service, "mapper", Mappers.getMapper(CardMapper.class));
    }

    @Test
    void migrationRetainsHistoricalRowsWithoutRewritingCredentials() throws Exception {
        assertEquals(beforeMigration, afterMigration);
    }

    @Test
    void nonownerCannotInsertCredentialsIncludingEmptyStrings() throws Exception {
        for (String column : new String[] {"pin", "cvv"}) {
            for (String value : new String[] {"", "123"}) {
                try (var connection = app();
                        var statement =
                                connection.prepareStatement(
                                        "INSERT INTO card(card_number,"
                                                + column
                                                + ") VALUES ('4111111111111111',?)")) {
                    statement.setString(1, value);
                    SQLException error = assertThrows(SQLException.class, statement::executeUpdate);
                    assertEquals("23514", error.getSQLState());
                }
            }
        }
    }

    @Test
    void nonownerCannotIntroduceCredentialsThroughAnUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        try (var connection = app();
                var statement = connection.createStatement()) {
            statement.execute(
                    "INSERT INTO card(card_id,card_number) VALUES ('"
                            + id
                            + "','4111111111111111')");
            for (String column : new String[] {"pin", "cvv"}) {
                SQLException error =
                        assertThrows(
                                SQLException.class,
                                () ->
                                        statement.executeUpdate(
                                                "UPDATE card SET "
                                                        + column
                                                        + "='' WHERE card_id='"
                                                        + id
                                                        + "'"));
                assertEquals("23514", error.getSQLState());
            }
            assertEquals(
                    1,
                    statement.executeUpdate(
                            "UPDATE card SET notes='Synthetic safe edit' WHERE card_id='"
                                    + id
                                    + "'"));
        }
    }

    @Test
    void legacyRowsAreReadableWithoutReturningCredentials() {
        var dto = service.getCard(LEGACY).block(WAIT);
        assertNotNull(dto);
        assertEquals(LEGACY, dto.getCardId());
        assertNull(dto.getPin());
        assertNull(dto.getCvv());
    }

    @Test
    void ordinaryUpdateQuarantinesHistoricalCredentialsWithoutMutatingTheRow() throws Exception {
        var request = new CardDTO();
        request.setCardNumber("4111111111111111");
        StepVerifier.create(service.updateCard(LEGACY, request))
                .expectErrorSatisfies(
                        error -> {
                            var response = assertInstanceOf(ResponseStatusException.class, error);
                            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                        })
                .verify(WAIT);
        assertEquals(beforeMigration, legacyDigest());
    }

    @Test
    void historicalCredentialsCannotBeSearchedOrUsedAsSortKeys() {
        for (String field : new String[] {"pin", "cvv"}) {
            var filters = new CardDTO();
            if (field.equals("pin")) filters.setPin("1234");
            else filters.setCvv("987");
            var request = new FilterRequest<CardDTO>();
            request.setFilters(filters);
            rejectFilter(request);
            request.setFilters(null);
            var pagination = new PaginationRequest();
            pagination.setSortBy(field);
            request.setPagination(pagination);
            rejectFilter(request);
        }
    }

    private static void rejectFilter(FilterRequest<CardDTO> request) {
        StepVerifier.create(service.filterCards(request))
                .expectErrorSatisfies(
                        error -> {
                            var response = assertInstanceOf(ResponseStatusException.class, error);
                            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                        })
                .verify(WAIT);
    }

    private static Connection owner() throws SQLException {
        return DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    private static Connection app() throws SQLException {
        return DriverManager.getConnection(postgres.getJdbcUrl(), APP, PASSWORD);
    }

    private static String legacyDigest() throws SQLException {
        try (var connection = owner();
                var statement = connection.createStatement();
                var rows =
                        statement.executeQuery(
                                "SELECT md5(row_to_json(c)::text) FROM card c WHERE card_id='"
                                        + LEGACY
                                        + "'")) {
            assertTrue(rows.next());
            return rows.getString(1);
        }
    }
}
