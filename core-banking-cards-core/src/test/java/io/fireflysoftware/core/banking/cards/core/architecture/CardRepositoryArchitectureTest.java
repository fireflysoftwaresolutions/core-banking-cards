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


package io.fireflysoftware.core.banking.cards.core.architecture;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

class CardRepositoryArchitectureTest {
    private static final String ROOT = "io.fireflysoftware.core.banking.cards.";

    @Test
    void activationBusinessContractHasAnImplementationWithTheExistingBeanIdentity() throws Exception {
        var contract = assertDoesNotThrow(() -> Class.forName(ROOT + "core.services.card.activation.CardActivationService"));
        assertTrue(contract.isInterface());
        var implementation = Class.forName(contract.getPackageName() + ".impl." + contract.getSimpleName() + "Impl");
        assertTrue(contract.isAssignableFrom(implementation));
        assertEquals("cardActivationService", implementation.getAnnotation(Service.class).value());
    }

    @Test
    void businessImplementationsLiveUnderTheirFunctionalImplSubpackage() throws Exception {
        var resolver = new org.springframework.core.io.support.PathMatchingResourcePatternResolver();
        var readers = new org.springframework.core.type.classreading.CachingMetadataReaderFactory(resolver);
        var resources = resolver.getResources("classpath*:io/fireflysoftware/core/banking/cards/core/services/**/*ServiceImpl.class");
        assertTrue(resources.length > 0);
        for (var resource : resources) {
            var name = readers.getMetadataReader(resource).getClassMetadata().getClassName();
            assertTrue(name.matches(".*\\.impl\\.[^.]+ServiceImpl"), name);
        }
    }

    @Test
    void activationStoreDependsOnSpringDataRepositoriesInsteadOfTheRawClient() throws Exception {
        var store = assertDoesNotThrow(() -> Class.forName(ROOT + "core.services.card.activation.CardActivationStore"));
        var constructor = store.getDeclaredConstructors()[0];
        assertEquals(2, constructor.getParameterCount());
        assertTrue(Arrays.stream(constructor.getParameterTypes()).allMatch(Repository.class::isAssignableFrom));
    }

    @Test
    void issuerBindingRepositoryExposesOnlyTheDerivedReadOperation() throws Exception {
        var repository = assertDoesNotThrow(() -> Class.forName(ROOT + "models.repositories.card.CardIssuerInstrumentRepository"));
        assertTrue(Repository.class.isAssignableFrom(repository));
        assertEquals(1, repository.getMethods().length, "Do not expose generic writes to issuer authority");
        assertNull(repository.getMethod("findByCardId", UUID.class).getAnnotation(Query.class));
    }

    @Test
    void promotionRepositoryUsesItsActualUuidIdentityAndADerivedCount() throws Exception {
        var repository = assertDoesNotThrow(() -> Class.forName(ROOT + "models.repositories.promotion.CardPromotionRepository"));
        assertEquals(UUID.class, ResolvableType.forClass(repository).as(Repository.class).getGeneric(1).resolve());
        assertNull(repository.getMethod("countByCardId", UUID.class).getAnnotation(Query.class));
    }
}
