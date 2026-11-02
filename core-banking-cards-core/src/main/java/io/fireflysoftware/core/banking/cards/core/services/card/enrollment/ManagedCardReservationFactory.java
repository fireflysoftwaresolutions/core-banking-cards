/*
 * Copyright 2026 Firefly Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may obtain a copy at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.*;
import java.time.Instant;
import java.util.UUID;

/** Creates storage values only. Eligibility, current authority and native time are separate prerequisites. */
public final class ManagedCardReservationFactory {
    private ManagedCardReservationFactory() { }
    public static ManagedCardReservationGraph create(Selection selected,Request request,UUID operationId,UUID receiptId,
            UUID eventId,UUID outboxId,UUID accountFenceReceiptId,long nativeRecordedAtEpochMillis) {
        String digest=ManagedCardEnrollmentRules.digest(selected,request);
        Instant recordedAt=Instant.ofEpochMilli(nativeRecordedAtEpochMillis);
        var card=new Card(selected.managedCardId().value(),selected.tenantId(),selected.partyId(),selected.accountId(),
                selected.contractId(),selected.productId(),selected.enrollmentId(),request.actionId(),"RETAINED",recordedAt);
        var action=new Action(request.actionId(),selected.tenantId(),selected.actorId(),selected.partyId(),selected.accountId(),
                selected.contractId(),selected.productId(),selected.managedCardId().value(),selected.enrollmentId(),
                selected.accountVersion(),selected.productVersion(),selected.providers().issuerAdapterId(),selected.providers().processorAdapterId(),
                request.reasonCode(),request.acceptNotAfterEpochMillis(),digest,receiptId,accountFenceReceiptId,operationId,eventId,outboxId,0,"RESERVED",recordedAt);
        var operation=new Operation(operationId,selected.tenantId(),request.actionId(),selected.managedCardId().value(),digest,request.acceptNotAfterEpochMillis(),recordedAt);
        var event=new Event(eventId,selected.tenantId(),request.actionId(),selected.managedCardId().value(),operationId,digest,"RESERVATION_RECORDED",0,recordedAt);
        var outbox=new Outbox(outboxId,selected.tenantId(),request.actionId(),operationId,eventId,"COORDINATE_INACTIVE_ENROLLMENT","COORDINATOR_PENDING",0,recordedAt);
        return new ManagedCardReservationGraph(card,action,operation,event,outbox);
    }
}
