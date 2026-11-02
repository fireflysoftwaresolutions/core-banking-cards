/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.config;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.NoOpCardIssuerPortImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods=false)
public class CardIssuerConfiguration {
    @Bean
    @ConditionalOnMissingBean(CardIssuerPort.class)
    CardIssuerPort cardIssuerPort() { return new NoOpCardIssuerPortImpl(); }
}
