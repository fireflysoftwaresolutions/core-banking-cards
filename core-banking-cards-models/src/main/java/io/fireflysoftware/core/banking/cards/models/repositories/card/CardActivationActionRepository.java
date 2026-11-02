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


package io.fireflysoftware.core.banking.cards.models.repositories.card;

import io.fireflysoftware.core.banking.cards.models.entities.card.CardActivationAction;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

/** The native functions atomically enforce proof, ownership, expiry and action identity. */
public interface CardActivationActionRepository extends Repository<CardActivationAction, UUID> {
    @Query("SELECT * FROM public.reserve_card_activation(:action,:card,:tenant,:actor,:party,:account,:digest,:expires,:bindingVersion)")
    Mono<CardActivationAction> reserve(
            @Param("action") UUID action, @Param("card") UUID card,
            @Param("tenant") UUID tenant, @Param("actor") UUID actor,
            @Param("party") UUID party, @Param("account") UUID account,
            @Param("digest") String digest, @Param("expires") OffsetDateTime expires,
            @Param("bindingVersion") long bindingVersion);

    @Query("SELECT * FROM public.finish_card_activation(:action,:tenant,:actor,:party,:account,:expires,:bindingVersion,:outcome,:receipt)")
    Mono<CardActivationAction> finish(
            @Param("action") UUID action, @Param("tenant") UUID tenant,
            @Param("actor") UUID actor, @Param("party") UUID party,
            @Param("account") UUID account, @Param("expires") OffsetDateTime expires,
            @Param("bindingVersion") long bindingVersion,
            @Param("outcome") String outcome, @Param("receipt") String receipt);

    @Query("SELECT public.claim_card_activation_dispatch(:action,:tenant,:actor,:party,:account,:expires,:bindingVersion)")
    Mono<Boolean> claimDispatch(@Param("action") UUID action, @Param("tenant") UUID tenant,
            @Param("actor") UUID actor, @Param("party") UUID party, @Param("account") UUID account,
            @Param("expires") OffsetDateTime expires, @Param("bindingVersion") long bindingVersion);

    @Query("""
            SELECT a.* FROM public.card_activation_action a
            JOIN public.card_issuer_instrument b ON b.card_id=a.card_id
            JOIN public.card c ON c.card_id=a.card_id
            WHERE a.action_id=:action AND a.card_id=:card AND a.tenant_id=:tenant AND a.actor_id=:actor
              AND b.tenant_id=:tenant AND b.party_id=:party AND b.account_id=:account
              AND c.party_id=b.party_id AND c.account_id=b.account_id
              AND a.binding_version=:bindingVersion AND b.binding_version=:bindingVersion
              AND :expires > clock_timestamp()
            """)
    Mono<CardActivationAction> currentReceipt(@Param("action") UUID action, @Param("card") UUID card,
            @Param("tenant") UUID tenant, @Param("actor") UUID actor, @Param("party") UUID party,
            @Param("account") UUID account, @Param("expires") OffsetDateTime expires,
            @Param("bindingVersion") long bindingVersion);
}
