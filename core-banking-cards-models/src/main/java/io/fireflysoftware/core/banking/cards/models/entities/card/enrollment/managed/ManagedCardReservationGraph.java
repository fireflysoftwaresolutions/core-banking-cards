/*
 * Copyright 2026 Firefly Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may obtain a copy at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import java.time.Instant;
import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** Immutable reservation storage graph. Its values alone convey no current authority. */
public record ManagedCardReservationGraph(Card card, Action action, Operation operation, Event event, Outbox outbox) {
    public record Scope(UUID tenantId, UUID partyId, UUID accountId) {
        public Scope { id(tenantId);id(partyId);id(accountId); }
    }
    @Table("managed_card")
    public record Card(@Id @Column("managed_card_id") UUID managedCardId,
            @Column("tenant_id") UUID tenantId,@Column("party_id") UUID partyId,
            @Column("account_id") UUID accountId,@Column("contract_id") UUID contractId,
            @Column("product_id") UUID productId,@Column("enrollment_id") UUID enrollmentId,
            @Column("action_id") UUID actionId,@Column("restriction") String restriction,
            @Column("recorded_at") Instant recordedAt) { }
    @Table("managed_card_enrollment_action")
    public record Action(@Id @Column("action_id") UUID actionId,
            @Column("tenant_id") UUID tenantId,@Column("actor_id") UUID actorId,
            @Column("party_id") UUID partyId,@Column("account_id") UUID accountId,
            @Column("contract_id") UUID contractId,@Column("product_id") UUID productId,
            @Column("managed_card_id") UUID managedCardId,@Column("enrollment_id") UUID enrollmentId,
            @Column("account_version") long accountVersion,@Column("product_version") long productVersion,
            @Column("issuer_adapter_id") String issuerAdapterId,@Column("processor_adapter_id") String processorAdapterId,
            @Column("reason_code") String reasonCode,@Column("accept_not_after_epoch_millis") long acceptNotAfterEpochMillis,
            @Column("intent_digest") String intentDigest,@Column("receipt_id") UUID receiptId,
            @Column("account_fence_receipt_id") UUID accountFenceReceiptId,
            @Column("operation_id") UUID operationId,@Column("event_id") UUID eventId,
            @Column("outbox_id") UUID outboxId,@Column("progress_version") long progressVersion,
            @Column("state") String state,@Column("recorded_at") Instant recordedAt) {
        public Scope scope() { return new Scope(tenantId,partyId,accountId); }
        public Selection selection() { return new Selection(tenantId,actorId,partyId,accountId,contractId,productId,
                new ManagedCardId(managedCardId),enrollmentId,accountVersion,productVersion,new ConfiguredProviders(issuerAdapterId,processorAdapterId)); }
        public Request request() { return new Request(actionId,accountVersion,reasonCode,acceptNotAfterEpochMillis); }
    }
    @Table("managed_card_provider_operation")
    public record Operation(@Id @Column("operation_id") UUID operationId,@Column("tenant_id") UUID tenantId,
            @Column("action_id") UUID actionId,@Column("managed_card_id") UUID managedCardId,
            @Column("intent_digest") String intentDigest,@Column("accept_not_after_epoch_millis") long acceptNotAfterEpochMillis,
            @Column("recorded_at") Instant recordedAt) { }
    @Table("managed_card_enrollment_event")
    public record Event(@Id @Column("event_id") UUID eventId,@Column("tenant_id") UUID tenantId,
            @Column("action_id") UUID actionId,@Column("managed_card_id") UUID managedCardId,
            @Column("operation_id") UUID operationId,@Column("intent_digest") String intentDigest,
            @Column("kind") String kind,@Column("progress_version") long progressVersion,@Column("recorded_at") Instant recordedAt) { }
    @Table("managed_card_enrollment_outbox")
    public record Outbox(@Id @Column("outbox_id") UUID outboxId,@Column("tenant_id") UUID tenantId,
            @Column("action_id") UUID actionId,@Column("operation_id") UUID operationId,
            @Column("event_id") UUID eventId,@Column("kind") String kind,
            @Column("state") String state,@Column("progress_version") long progressVersion,@Column("recorded_at") Instant recordedAt) { }
    public ManagedCardReservationGraph {
        if(card==null||action==null||operation==null||event==null||outbox==null)throw invalid();
        for(UUID value:List.of(action.actionId(),action.tenantId(),action.actorId(),action.partyId(),action.accountId(),
                action.contractId(),action.productId(),action.managedCardId(),action.enrollmentId(),action.receiptId(),
                action.accountFenceReceiptId(),action.operationId(),action.eventId(),action.outboxId()))id(value);
        if(action.accountVersion()<0||action.productVersion()<0||action.progressVersion()!=0||!"RESERVED".equals(action.state())
                ||action.reasonCode()==null||!action.reasonCode().matches("[A-Z][A-Z0-9_]{0,63}")
                ||!adapter(action.issuerAdapterId())||!adapter(action.processorAdapterId())
                ||action.intentDigest()==null||!action.intentDigest().matches("[0-9a-f]{64}")
                ||action.recordedAt()==null||action.recordedAt().toEpochMilli()<=0||action.recordedAt().getNano()%1_000_000!=0
                ||action.recordedAt().toEpochMilli()>=action.acceptNotAfterEpochMillis())throw invalid();
        if(!Objects.equals(card.managedCardId(),action.managedCardId())||!Objects.equals(card.tenantId(),action.tenantId())
                ||!Objects.equals(card.partyId(),action.partyId())||!Objects.equals(card.accountId(),action.accountId())
                ||!Objects.equals(card.contractId(),action.contractId())||!Objects.equals(card.productId(),action.productId())
                ||!Objects.equals(card.enrollmentId(),action.enrollmentId())||!Objects.equals(card.actionId(),action.actionId())
                ||!"RETAINED".equals(card.restriction())||!Objects.equals(card.recordedAt(),action.recordedAt()))throw invalid();
        if(!Objects.equals(operation.operationId(),action.operationId())||!Objects.equals(operation.tenantId(),action.tenantId())
                ||!Objects.equals(operation.actionId(),action.actionId())||!Objects.equals(operation.managedCardId(),action.managedCardId())
                ||!Objects.equals(operation.intentDigest(),action.intentDigest())||operation.acceptNotAfterEpochMillis()!=action.acceptNotAfterEpochMillis()
                ||!Objects.equals(operation.recordedAt(),action.recordedAt()))throw invalid();
        if(!Objects.equals(event.eventId(),action.eventId())||!Objects.equals(event.tenantId(),action.tenantId())
                ||!Objects.equals(event.actionId(),action.actionId())||!Objects.equals(event.managedCardId(),action.managedCardId())
                ||!Objects.equals(event.operationId(),action.operationId())||!Objects.equals(event.intentDigest(),action.intentDigest())
                ||!"RESERVATION_RECORDED".equals(event.kind())||event.progressVersion()!=0||!Objects.equals(event.recordedAt(),action.recordedAt()))throw invalid();
        if(!Objects.equals(outbox.outboxId(),action.outboxId())||!Objects.equals(outbox.tenantId(),action.tenantId())
                ||!Objects.equals(outbox.actionId(),action.actionId())||!Objects.equals(outbox.operationId(),action.operationId())
                ||!Objects.equals(outbox.eventId(),action.eventId())||!"COORDINATE_INACTIVE_ENROLLMENT".equals(outbox.kind())
                ||!"COORDINATOR_PENDING".equals(outbox.state())||outbox.progressVersion()!=0||!Objects.equals(outbox.recordedAt(),action.recordedAt()))throw invalid();
    }
    public Scope scope() { return action.scope(); }
    public Selection selection() { return action.selection(); }
    public Request request() { return action.request(); }
    public Acceptance acceptance() { return new Acceptance(action.actionId(),selection(),action.intentDigest(),action.receiptId(),action.recordedAt().toEpochMilli()); }
    public Progress progress() { return new Progress(action.actionId(),new ManagedCardId(action.managedCardId()),0,State.RESERVED,Restriction.RETAINED); }
    public List<Object> rows() { return List.of(card,action,operation,event,outbox); }
    private static void id(UUID value) { if(value==null||value.equals(new UUID(0,0)))throw invalid(); }
    private static boolean adapter(String value) { return value!=null&&value.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}"); }
    private static IllegalArgumentException invalid() { return new IllegalArgumentException("EXACT_MANAGED_RESERVATION_GRAPH_REQUIRED"); }
}
