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


package io.fireflysoftware.core.banking.cards.core.services.card.impl;

import io.fireflysoftware.core.banking.cards.interfaces.enums.card.CardStatusEnum;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Allowed moves for a card. A draft is the only status that can be removed.
 */
final class CardLifecycle {

    private static final Map<CardStatusEnum, Set<CardStatusEnum>> ALLOWED = Map.of(
            CardStatusEnum.DRAFT, EnumSet.of(CardStatusEnum.ACTIVE, CardStatusEnum.CANCELLED),
            CardStatusEnum.ACTIVE, EnumSet.of(
                    CardStatusEnum.BLOCKED, CardStatusEnum.EXPIRED, CardStatusEnum.CANCELLED),
            CardStatusEnum.BLOCKED, EnumSet.of(
                    CardStatusEnum.ACTIVE, CardStatusEnum.EXPIRED, CardStatusEnum.CANCELLED)
    );

    private CardLifecycle() {
    }

    static boolean canTransition(CardStatusEnum from, CardStatusEnum to) {
        if (from == to) {
            return true;
        }
        Set<CardStatusEnum> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }
}
