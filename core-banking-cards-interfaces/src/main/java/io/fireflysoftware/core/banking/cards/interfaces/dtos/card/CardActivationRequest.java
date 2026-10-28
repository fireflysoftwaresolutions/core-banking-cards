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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Ephemeral possession proof; never include this object in workflow or audit state. */
public final class CardActivationRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9_-]{43,128}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "High-entropy issuer-issued possession proof", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String activationProof;

    public String getActivationProof() { return activationProof; }
    public void setActivationProof(String activationProof) { this.activationProof = activationProof; }
    @Override public String toString() { return "CardActivationRequest[proof omitted]"; }
}
