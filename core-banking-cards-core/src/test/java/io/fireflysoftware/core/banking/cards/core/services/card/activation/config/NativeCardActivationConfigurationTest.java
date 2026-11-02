/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CurrentCardActivationPolicyImpl;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import org.fireflyframework.security.spi.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import reactor.core.publisher.Mono;

class NativeCardActivationConfigurationTest {
    private final ApplicationContextRunner context=new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(NativeCardActivationConfiguration.class));
    @Test void noImplicitNativePolicyOrIdentityExistsByDefault() {context.run(c->{assertThat(c).hasNotFailed();assertThat(c).doesNotHaveBean(CardActivationPolicy.class);});}
    @Test void enablementWithoutRequiredNativeAuthorityCannotStart() {context.withPropertyValues("firefly.cards.activation.native.enabled=true").run(c->assertThat(c).hasFailed());}
    @Test void explicitNativeProfileUsesTheActualPrincipalAndOwnerRepositoryBindings() {
        context.withBean(SecurityContextPort.class,()->Mono::empty).withBean(PolicyEvaluationPort.class,()->mock(PolicyEvaluationPort.class))
                .withBean(CardIssuerInstrumentRepository.class,()->mock(CardIssuerInstrumentRepository.class)).withPropertyValues(
                "firefly.cards.activation.native.enabled=true","firefly.cards.activation.native.issuer=https://identity.example.test/realm",
                "firefly.cards.activation.native.platform-tenant=opaque-customer-tenant","firefly.cards.activation.native.resource-tenant=7031bd23-8106-42ed-af23-870335c0a180",
                "firefly.cards.activation.native.audience=core-banking-cards","firefly.cards.activation.native.client-ids=domain-cards",
                "firefly.cards.activation.native.client-claim=client_id","firefly.cards.activation.native.actor-claim=actor_id",
                "firefly.cards.activation.native.party-claim=party_id","firefly.cards.activation.native.kind-claim=principal_kind")
                .run(c->{assertThat(c).hasNotFailed();assertThat(c).hasSingleBean(CardActivationPolicy.class);assertThat(c.getBean(CardActivationPolicy.class)).isInstanceOf(CurrentCardActivationPolicyImpl.class);});
    }
    @Test void explicitExternalPolicyBacksOffWithoutCreatingAnIdentityProvider() {
        CardActivationPolicy external=card->Mono.error(new IllegalStateException("External adapter control only"));
        context.withPropertyValues("firefly.cards.activation.native.enabled=true").withBean(CardActivationPolicy.class,()->external)
                .run(c->{assertThat(c).hasNotFailed();assertThat(c.getBean(CardActivationPolicy.class)).isSameAs(external);assertThat(c).doesNotHaveBean(SecurityContextPort.class);});
    }
}
