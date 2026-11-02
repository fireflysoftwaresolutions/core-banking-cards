/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** Original completed audit identity; replay never replaces its nonce or record. */
@Table("managed_card_reservation_audit")
public record ManagedCardReservationAuditBinding(@Id @Column("action_id") UUID actionId,
        @Column("tenant_id") UUID tenantId,@Column("event_id") UUID eventId,
        @Column("audit_context_nonce") UUID auditContextNonce,@Column("audit_sequence") long auditSequence,
        @Column("audit_record_hash") String auditRecordHash) {
    public ManagedCardReservationAuditBinding {
        for(UUID value:List.of(actionId,tenantId,eventId,auditContextNonce))
            if(value.equals(new UUID(0,0)))throw new IllegalArgumentException("Original completed audit identity required");
        if(auditSequence<1||auditRecordHash==null||!auditRecordHash.matches("[0-9a-f]{64}"))
            throw new IllegalArgumentException("Original canonical audit record required");
    }
}
