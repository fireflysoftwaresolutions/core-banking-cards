/*
 * Copyright 2026 Firefly Software Foundation
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

package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.RecoveryRequest;
import java.util.Set;

public final class ManagedCardProviderCodec {
    private ManagedCardProviderCodec() {}

    public static RecoveryRequest decode(byte[] raw) {
        var value = ManagedCardEnrollmentCodec.object(raw, Set.of("actionId", "expectedProgressVersion"));
        var request = new RecoveryRequest(ManagedCardEnrollmentCodec.identifier(value, "actionId"),
                ManagedCardEnrollmentCodec.number(value, "expectedProgressVersion"));
        ManagedCardProviderRules.validateRecovery(request);
        return request;
    }
}
