/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.config;

import static org.assertj.core.api.Assertions.assertThat;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.NoOpCardIssuerPortImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

class CardIssuerConfigurationTest {
    @Test void defaultAdapterExplicitlyCannotExecute() {
        new ApplicationContextRunner().withUserConfiguration(CardIssuerConfiguration.class).run(context -> {
            assertThat(context).hasSingleBean(CardIssuerPort.class);
            assertThat(context.getBean(CardIssuerPort.class)).isInstanceOf(NoOpCardIssuerPortImpl.class);
            assertThat(context.getBean(CardIssuerPort.class).configuration()).isEqualTo(CardIssuerPort.Configuration.NOT_CONFIGURED);
        });
    }
    @Test void deploymentAdapterReplacesTheNoOpWithoutOverridingBeans() {
        new ApplicationContextRunner().withUserConfiguration(Deployment.class,CardIssuerConfiguration.class).run(context -> {
            assertThat(context).hasSingleBean(CardIssuerPort.class);
            assertThat(context.getBean(CardIssuerPort.class)).isSameAs(context.getBean("deploymentIssuer"));
            assertThat(context).doesNotHaveBean(NoOpCardIssuerPortImpl.class);
        });
    }
    @Configuration(proxyBeanMethods=false)
    static class Deployment {
        @Bean CardIssuerPort deploymentIssuer() {
            return (binding,action) -> Mono.error(new IllegalStateException("Test replacement is not an issuer"));
        }
    }
}
