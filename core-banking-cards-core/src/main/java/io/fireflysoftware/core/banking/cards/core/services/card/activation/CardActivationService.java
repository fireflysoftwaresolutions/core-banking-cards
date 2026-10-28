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


package io.fireflysoftware.core.banking.cards.core.services.card.activation;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CardActivationService {
    Mono<CardActivationReceipt> activate(UUID cardId, UUID actionId, CardActivationRequest request);
}
