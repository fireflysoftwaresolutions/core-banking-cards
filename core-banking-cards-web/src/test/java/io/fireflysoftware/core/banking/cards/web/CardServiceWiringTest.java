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


package io.fireflysoftware.core.banking.cards.web;

import static org.junit.jupiter.api.Assertions.*;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationService;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CardActivationServiceImpl;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationStore;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import io.r2dbc.spi.ConnectionFactory;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

class CardServiceWiringTest {
    @Test
    void controllersInjectBusinessInterfaces() throws Exception {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        var controllers = scanner.findCandidateComponents("io.fireflysoftware.core.banking.cards.web.controllers");
        assertFalse(controllers.isEmpty());
        for (var candidate : controllers) {
            var controller = Class.forName(candidate.getBeanClassName());
            for (var field : controller.getDeclaredFields()) {
                if (field.getType().getSimpleName().matches(".*Service(?:Impl)?")) {
                    assertTrue(field.getType().isInterface(), controller.getSimpleName() + "." + field.getName());
                }
            }
        }
    }

    @Test
    void actualRepositoryProxiesAndServiceUseTheOriginalBeanIdentity() {
        new ApplicationContextRunner().withUserConfiguration(RepositoryConfiguration.class).run(context -> {
            if (context.getStartupFailure() != null) {
                throw new AssertionError("Actual Spring Data wiring failed", context.getStartupFailure());
            }
            var service = context.getBean("cardActivationService", CardActivationService.class);
            assertInstanceOf(CardActivationServiceImpl.class, service);
            assertNotNull(context.getBean(CardIssuerInstrumentRepository.class));
            StepVerifier.create(service.activate(UUID.randomUUID(), UUID.randomUUID(), null))
                    .expectErrorSatisfies(error -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,
                            assertInstanceOf(ResponseStatusException.class, error).getStatusCode())).verify();
        });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableR2dbcRepositories(basePackageClasses = CardIssuerInstrumentRepository.class,
            entityOperationsRef = "cardEntityOperations")
    @Import({CardActivationStore.class, CardActivationServiceImpl.class})
    static class RepositoryConfiguration {
        @Bean
        ConnectionFactory cardConnectionFactory() {
            return io.r2dbc.spi.ConnectionFactories.get("r2dbc:postgresql://unconnected_fixture@127.0.0.1:1/unused_no_database_wiring_test");
        }

        @Bean
        R2dbcEntityOperations cardEntityOperations(ConnectionFactory cardConnectionFactory) {
            return new R2dbcEntityTemplate(cardConnectionFactory);
        }
    }
}
