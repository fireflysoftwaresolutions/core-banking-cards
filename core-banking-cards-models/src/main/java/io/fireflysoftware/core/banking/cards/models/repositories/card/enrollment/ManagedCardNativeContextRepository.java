/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.models.repositories.card.enrollment;

import io.fireflysoftware.core.banking.cards.models.entities.card.enrollment.managed.ManagedCardReservationGraph.Action;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

/** Technical V8 admission only; business persistence remains mapped in the reservation repository. */
@NoRepositoryBean
public interface ManagedCardNativeContextRepository extends Repository<Action,UUID> {
    record Installed(UUID contextNonce,UUID actionId,String purpose,String scopeDigest,Long dbNowMillis,
            Long authorityUntilMillis,Long commandUntilMillis) { }
    @Query("SELECT * FROM public.cards_managed_enrollment_enter(:nonce,:tenant,:party,:account,:actor,:action,:purpose,:issuer,:subject,:tenantKey,:fence,:intent,:scope,:authorityUntil,:commandUntil)")
    Mono<Installed> enter(@Param("nonce") UUID nonce,@Param("tenant") UUID tenant,@Param("party") UUID party,
            @Param("account") UUID account,@Param("actor") UUID actor,@Param("action") UUID action,
            @Param("purpose") String purpose,@Param("issuer") String issuer,@Param("subject") String subject,
            @Param("tenantKey") String tenantKey,@Param("fence") UUID fence,@Param("intent") String intent,
            @Param("scope") String scope,@Param("authorityUntil") OffsetDateTime authorityUntil,
            @Param("commandUntil") OffsetDateTime commandUntil);
    @Query("SELECT public.cards_managed_enrollment_complete(:nonce) AS nonce")
    Mono<UUID> complete(@Param("nonce") UUID nonce);
}
