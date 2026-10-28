/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;

import java.util.UUID;
import org.fireflyframework.core.audit.AuditRows;
import org.springframework.data.repository.*;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

/** Explicit owner deployment only; no CRUD or automatic repository bean. */
@NoRepositoryBean
public interface ManagedCardReservationAuditNativeRepository extends Repository<AuditRows.Event,UUID> {
    @Query("SELECT public.cards_managed_original_audit_pair(:nonce,:event,:client,:realm,:staff,:policy,:evidence,:disposition) AS nonce")
    Mono<UUID> pair(@Param("nonce")UUID nonce,@Param("event")UUID event,@Param("client")String client,@Param("realm")String realm,@Param("staff")UUID staff,
            @Param("policy")String policy,@Param("evidence")String evidence,@Param("disposition")String disposition);
}
