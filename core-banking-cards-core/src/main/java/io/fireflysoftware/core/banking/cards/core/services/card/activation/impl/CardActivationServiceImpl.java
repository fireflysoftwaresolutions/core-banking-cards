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


package io.fireflysoftware.core.banking.cards.core.services.card.activation.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationService;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationStore;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Core alone verifies issuer observations before committing a local activation. */
@Service("cardActivationService")
public final class CardActivationServiceImpl implements CardActivationService {
    private final CardActivationStore store;
    private final Optional<CardActivationPolicy> policy;
    private final Optional<CardIssuerPort> issuer;

    public CardActivationServiceImpl(CardActivationStore store, Optional<CardActivationPolicy> policy,
                                 Optional<CardIssuerPort> issuer) {
        this.store = store;
        this.policy = policy;
        this.issuer = issuer;
    }

    @Override
    public Mono<CardActivationReceipt> activate(UUID cardId, UUID actionId, CardActivationRequest request) {
        return Mono.defer(() -> {
            if (policy.isEmpty() || issuer.isEmpty()) {
                return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Authenticated activation policy and issuer adapter are required"));
            }
            String proof = request == null ? null : request.getActivationProof();
            if (cardId == null || actionId == null || proof == null || !proof.matches("[A-Za-z0-9_-]{43,128}")) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid activation request"));
            }
            String digest = digest(proof);
            return authorize(cardId).flatMap(scope -> store.reserve(actionId, cardId, scope, digest))
                    .flatMap(receipt -> terminal(receipt) ? Mono.just(receipt)
                            : store.binding(cardId).flatMap(binding -> issuer.get().observeActivation(binding, actionId)
                                    .map(observation -> verified(binding, actionId, observation))
                                    .defaultIfEmpty(new Verified("UNCERTAIN", null))
                                    .onErrorReturn(new Verified("UNCERTAIN", null))
                                    .flatMap(verified -> authorize(cardId).flatMap(scope ->
                                            store.finish(actionId, scope, verified.outcome(), verified.receipt())))));
        });
    }

    private Mono<CardActivationPolicy.AuthorizedScope> authorize(UUID card) {
        return policy.orElseThrow().authorize(card).filter(scope -> scope.tenantId() != null
                && scope.actorId() != null && scope.partyId() != null && scope.accountId() != null
                && scope.expiresAt() != null).switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Activation is not permitted")));
    }

    private static Verified verified(CardIssuerPort.Binding binding, UUID action, CardIssuerPort.Observation observation) {
        if (!Objects.equals(action, observation.actionId()) || !Objects.equals(binding.tenantId(), observation.tenantId())
                || !Objects.equals(binding.adapterId(), observation.adapterId())
                || !Objects.equals(binding.instrumentReference(), observation.instrumentReference())
                || binding.version() != observation.bindingVersion() || observation.outcome() == null) {
            return new Verified("UNCERTAIN", null);
        }
        return switch (observation.outcome()) {
            case ACTIVATED -> observation.receiptReference() == null || observation.receiptReference().isBlank()
                    ? new Verified("UNCERTAIN", null) : new Verified("CONFIRMED", observation.receiptReference());
            case REJECTED -> new Verified("REJECTED", observation.receiptReference());
            case PENDING, UNCERTAIN -> new Verified("UNCERTAIN", observation.receiptReference());
        };
    }
    private static boolean terminal(CardActivationReceipt receipt) {
        return receipt.state() == CardActivationReceipt.State.CONFIRMED
                || receipt.state() == CardActivationReceipt.State.REJECTED
                || receipt.state() == CardActivationReceipt.State.RECONCILIATION_REQUIRED;
    }
    private static String digest(String proof) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(proof.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 unavailable");
        }
    }
    private record Verified(String outcome, String receipt) {}
}
