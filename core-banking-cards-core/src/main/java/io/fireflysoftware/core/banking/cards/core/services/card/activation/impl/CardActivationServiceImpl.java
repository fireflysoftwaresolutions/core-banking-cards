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
import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CardActivationStoreImpl;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardIssuerPort;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Core reserves proof and claims dispatch before any issuer command; only verified evidence can activate. */
@Service("cardActivationService")
public final class CardActivationServiceImpl implements CardActivationService {
    private final CardActivationStoreImpl store;
    private final Optional<CardActivationPolicy> policy;
    private final Optional<CardIssuerPort> issuer;

    public CardActivationServiceImpl(CardActivationStoreImpl store, Optional<CardActivationPolicy> policy,
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
            return authorize(cardId).flatMap(scope -> {
                if (issuer.get().configuration() != CardIssuerPort.Configuration.CONFIGURED) {
                    return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                            "Card issuer NOT_CONFIGURED: NOT_EXECUTED"));
                }
                return store.reserve(actionId, cardId, scope, digest)
                        .flatMap(receipt -> terminal(receipt) ? Mono.just(receipt)
                                : authorize(cardId).flatMap(current -> store.binding(cardId, current.bindingVersion())
                                        .flatMap(binding -> store.claimDispatch(actionId, current)
                                                .flatMap(claimed -> observeOrInitiate(binding, actionId, current, claimed))
                                                .flatMap(observed -> authorize(cardId).flatMap(finalScope ->
                                                        store.finish(actionId, finalScope, observed.outcome(), observed.receipt()))))));
            });
        });
    }

    @Override
    public Mono<CardActivationReceipt> recover(UUID cardId, UUID actionId) {
        return Mono.defer(() -> {
            if (policy.isEmpty()) return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Authenticated activation policy is required"));
            if (cardId == null || actionId == null || new UUID(0, 0).equals(cardId) || new UUID(0, 0).equals(actionId)) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid activation action"));
            }
            return authorize(cardId).flatMap(scope -> store.currentReceipt(actionId, cardId, scope));
        });
    }

    private Mono<Verified> observeOrInitiate(CardIssuerPort.Binding binding, UUID action,
                                            CardActivationPolicy.AuthorizedScope scope, boolean claimed) {
        return Mono.defer(() -> {
            Instant deadline=scope.expiresAt().toInstant();
            Duration remaining=Duration.between(Instant.now(), deadline);
            if (remaining.isNegative() || remaining.isZero()) return Mono.<CardIssuerPort.Observation>error(
                    new ResponseStatusException(HttpStatus.FORBIDDEN, "Activation authority expired"));
            return (claimed ? issuer.orElseThrow().initiateActivation(binding, action, deadline)
                    : issuer.orElseThrow().observeActivation(binding, action)).timeout(remaining);
        }).map(observation -> verified(binding, action, observation))
                .defaultIfEmpty(new Verified("UNCERTAIN", null)).onErrorReturn(new Verified("UNCERTAIN", null));
    }

    private Mono<CardActivationPolicy.AuthorizedScope> authorize(UUID card) {
        return policy.orElseThrow().authorize(card).filter(scope -> scope.tenantId() != null
                && scope.actorId() != null && scope.partyId() != null && scope.accountId() != null
                && scope.expiresAt() != null && scope.bindingVersion() > 0).switchIfEmpty(Mono.error(new ResponseStatusException(
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
