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


package io.fireflysoftware.core.banking.cards.models.entities.virtual;

import io.fireflysoftware.core.banking.cards.interfaces.enums.virtual.VirtualCardStatusEnum;
import io.fireflysoftware.core.banking.cards.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Virtual Card entity.
 * Represents a virtual payment card that exists only in digital form.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("virtual_card")
public class VirtualCard extends BaseEntity {
    @Id
    @Column("virtual_card_id")
    private UUID virtualCardId;

    @Column("card_id")
    private UUID cardId;

    @Column("device_id")
    private String deviceId;

    @Column("device_type")
    private String deviceType;

    @Column("device_model")
    private String deviceModel;

    @Column("device_os")
    private String deviceOs;

    @Column("device_os_version")
    private String deviceOsVersion;

    @Column("wallet_provider")
    private String walletProvider;

    @Column("wallet_account_id")
    private String walletAccountId;

    @Column("tokenization_provider")
    private String tokenizationProvider;

    @Column("token_id")
    private String tokenId;

    @Column("token_expiry_date")
    private LocalDateTime tokenExpiryDate;

    @Column("token_status")
    private String tokenStatus;

    @Column("virtual_card_number")
    private String virtualCardNumber;

    @Column("virtual_card_status")
    private VirtualCardStatusEnum virtualCardStatus;

    @Column("is_default_for_device")
    private Boolean isDefaultForDevice;

    @Column("is_default_for_wallet")
    private Boolean isDefaultForWallet;

    @Column("is_provisioned")
    private Boolean isProvisioned;

    @Column("provisioning_date")
    private LocalDateTime provisioningDate;

    @Column("last_used_date")
    private LocalDateTime lastUsedDate;

    @Column("creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column("update_timestamp")
    private LocalDateTime updateTimestamp;

    @Column("deactivation_date")
    private LocalDateTime deactivationDate;

    @Column("deactivation_reason")
    private String deactivationReason;

    @Column("notes")
    private String notes;
}
