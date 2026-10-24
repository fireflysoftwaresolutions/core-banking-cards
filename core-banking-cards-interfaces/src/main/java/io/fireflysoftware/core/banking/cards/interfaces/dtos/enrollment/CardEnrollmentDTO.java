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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Enrollment.
 * Contains the essential information about a Card Enrollment that needs to be exposed through the API.
 * A Card Enrollment represents a cardholder's enrollment in a specific promotion, program, or feature.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardEnrollmentDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID enrollmentId;

    @FilterableId
    private UUID cardId;

    @FilterableId
    private UUID partyId;

    @FilterableId
    private UUID accountId;

    private String enrollmentType;

    private String enrollmentReference;

    private String enrollmentStatus;

    private LocalDateTime enrollmentTimestamp;

    private String enrollmentChannel;

    private String enrollmentSource;

    private String enrollmentAgent;

    @FilterableId
    private UUID promotionId;

    @FilterableId
    private UUID programId;

    @FilterableId
    private UUID featureId;

    private String featureCode;

    private String featureName;

    private Boolean isAutoEnrolled;

    private Boolean isOptIn;

    private Boolean isOptOut;

    private LocalDateTime optInTimestamp;

    private LocalDateTime optOutTimestamp;

    private String optOutReason;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isActive;

    private LocalDateTime activationTimestamp;

    private LocalDateTime deactivationTimestamp;

    private String deactivationReason;

    private Boolean isRecurring;

    private String recurrenceFrequency;

    private LocalDateTime nextRecurrenceDate;

    private LocalDateTime lastRecurrenceDate;

    private Boolean termsAccepted;

    private LocalDateTime termsAcceptedTimestamp;

    private String termsVersion;

    private String confirmationCode;

    private Boolean confirmationSent;

    private String confirmationChannel;

    private LocalDateTime confirmationTimestamp;

    private String confirmationRecipient;
}