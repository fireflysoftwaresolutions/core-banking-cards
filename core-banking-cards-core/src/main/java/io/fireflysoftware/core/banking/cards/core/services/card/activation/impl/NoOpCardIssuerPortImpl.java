/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.*;
import java.util.UUID;
import reactor.core.publisher.Mono;

public final class NoOpCardIssuerPortImpl implements CardIssuerPort {
    @Override public Mono<Observation> observeActivation(Binding binding, UUID actionId) {
        return Mono.error(new CardIssuerUnavailableException());
    }
}
