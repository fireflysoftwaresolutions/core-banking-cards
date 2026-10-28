/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.fireflyframework.kernel.audit.*;
import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph;

/** Protected retained business evidence only; no actor, owner or policy authority is created. */
public final class ManagedCardReservationAuditEvidence {
    private ManagedCardReservationAuditEvidence() { }
    public static AuditEvent event(ManagedCardReservationGraph graph,AuditActor actor,String policyRevision){
        Objects.requireNonNull(graph);var a=graph.action();
        return new AuditEvent(a.eventId(),new AuditStream(a.tenantId(),"core-banking-cards","managed-card",a.managedCardId()),actor,
            "MANAGED_CARD_RESERVATION_RECORDED",AuditEvent.Outcome.COMPLETED,a.actionId(),a.receiptId(),a.operationId(),a.recordedAt(),digest(graph),policyRevision);
    }
    public static String digest(ManagedCardReservationGraph graph){
        Objects.requireNonNull(graph);var a=graph.action();
        try{
            var bytes=new ByteArrayOutputStream();var out=new DataOutputStream(bytes);text(out,"cards.managed.reservation.evidence");out.writeInt(1);
            for(UUID value:List.of(a.tenantId(),a.actorId(),a.partyId(),a.accountId(),a.contractId(),a.productId(),a.managedCardId(),a.enrollmentId(),a.actionId(),a.receiptId(),a.accountFenceReceiptId(),a.operationId(),a.eventId(),a.outboxId())){out.writeLong(value.getMostSignificantBits());out.writeLong(value.getLeastSignificantBits());}
            out.writeLong(a.accountVersion());out.writeLong(a.productVersion());out.writeLong(a.acceptNotAfterEpochMillis());out.writeLong(a.progressVersion());out.writeLong(a.recordedAt().getEpochSecond());out.writeInt(a.recordedAt().getNano());
            // The graph constructor enforces all cross-row identities/times and fixed local reservation labels.
            for(String value:List.of(a.issuerAdapterId(),a.processorAdapterId(),a.reasonCode(),a.intentDigest(),a.state(),graph.card().restriction(),graph.event().kind(),graph.outbox().kind(),graph.outbox().state()))text(out,value);
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes.toByteArray()));
        }catch(IOException|java.security.NoSuchAlgorithmException failure){throw new IllegalStateException("Retained Cards audit evidence unavailable",failure);}
    }
    private static void text(DataOutputStream out,String value)throws IOException {byte[] raw=value.getBytes(StandardCharsets.UTF_8);out.writeInt(raw.length);out.write(raw);}
}
