/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.config;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CurrentCardActivationPolicyImpl;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import java.time.Clock;
import java.util.*;
import org.fireflyframework.security.spi.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods=false)
@ConditionalOnProperty(name="firefly.cards.activation.native.enabled",havingValue="true")
public class NativeCardActivationConfiguration {
    @Bean
    @ConditionalOnMissingBean(CardActivationPolicy.class)
    CardActivationPolicy currentCardActivationPolicy(SecurityContextPort principals,PolicyEvaluationPort policies,CardIssuerInstrumentRepository bindings,
            @Value("${firefly.cards.activation.native.issuer}")String issuer,
            @Value("${firefly.cards.activation.native.platform-tenant}")String platformTenant,
            @Value("${firefly.cards.activation.native.resource-tenant}")UUID resourceTenant,
            @Value("${firefly.cards.activation.native.audience}")String audience,
            @Value("${firefly.cards.activation.native.client-ids}")String clients,
            @Value("${firefly.cards.activation.native.client-claim}")String clientClaim,
            @Value("${firefly.cards.activation.native.actor-claim}")String actorClaim,
            @Value("${firefly.cards.activation.native.party-claim}")String partyClaim,
            @Value("${firefly.cards.activation.native.kind-claim}")String kindClaim) {
        var profile=new CurrentCardActivationPolicyImpl.Profile(issuer,platformTenant,resourceTenant,audience,Set.of(clients.split(",",-1)),clientClaim,actorClaim,partyClaim,kindClaim);
        return new CurrentCardActivationPolicyImpl(principals,policies,bindings,profile,Clock.systemUTC());
    }
}
