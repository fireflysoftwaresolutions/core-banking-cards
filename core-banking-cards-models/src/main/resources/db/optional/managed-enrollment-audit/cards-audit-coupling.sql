-- Explicit owner capture after Cards V8 and the byte-derived audit134 deployment.
-- Historical reservation rows remain audit UNVERIFIED; no backfill or validation is claimed.
-- Native pairing is technical consistency; current profile/owner/PDP authority stays mandatory in Java.
BEGIN;
DO $$ BEGIN
 IF NOT pg_catalog.pg_has_role(current_user,'firefly_audit_guard','SET') OR NOT pg_catalog.pg_has_role(current_user,'cards_managed_guard','SET')
    OR NOT pg_catalog.has_schema_privilege(current_user,'public','CREATE WITH GRANT OPTION')
    OR pg_catalog.has_schema_privilege('firefly_audit_guard','public','CREATE') THEN
  RAISE EXCEPTION 'Separately delegated Cards and audit installer required' USING ERRCODE='42501';
 END IF;
END $$;
GRANT CREATE ON SCHEMA public TO firefly_audit_guard;
SET LOCAL ROLE firefly_audit_guard;
GRANT USAGE ON SCHEMA firefly_audit TO cards_managed_guard;
GRANT REFERENCES(resource_tenant_id,event_id) ON firefly_audit.audit_event TO cards_managed_guard;
CREATE FUNCTION public.firefly_cards_require_original_audit(a public.managed_card_enrollment_action,
 p_issuer TEXT,p_subject TEXT,p_platform TEXT,p_client TEXT,p_realm TEXT,p_staff UUID,p_policy TEXT,p_evidence TEXT,p_disposition TEXT) RETURNS VOID
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c firefly_audit_private.context%ROWTYPE;e firefly_audit.audit_event%ROWTYPE;
BEGIN
 SELECT * INTO c FROM firefly_audit_private.context t WHERE t.xid=pg_catalog.pg_current_xact_id_if_assigned()
  AND t.backend_pid=pg_catalog.pg_backend_pid() AND t.db_session_user=session_user
  AND t.resource_tenant_id=a.tenant_id AND t.event_id=a.event_id AND t.service='core-banking-cards'
  AND t.resource_type='managed-card' AND t.resource_id=a.managed_card_id;
 IF NOT FOUND OR p_disposition IS NULL OR p_disposition NOT IN('NEW_RESERVATION','EXACT_RESERVE_REPLAY') THEN
  RAISE EXCEPTION 'Original Cards audit scope required' USING ERRCODE='23514';
 END IF;
 c:=firefly_audit_private.context_for(c.nonce);
 PERFORM firefly_audit_private.require_graph(c);
 IF c.new_event<>(p_disposition='NEW_RESERVATION') OR NOT EXISTS(SELECT 1 FROM firefly_audit_private.completion done WHERE done.nonce=c.nonce) THEN
  RAISE EXCEPTION 'Completed original Cards audit append required' USING ERRCODE='23514';
 END IF;
 SELECT * INTO e FROM firefly_audit.audit_event t WHERE t.resource_tenant_id=a.tenant_id AND t.event_id=a.event_id;
 IF NOT FOUND OR ROW(e.service,e.resource_type,e.resource_id,e.action,e.outcome,e.process_id,e.correlation_id,e.causation_id,
    e.actor_issuer,e.actor_subject,e.actor_platform_tenant_id,e.actor_client_id,e.actor_staff_realm,e.actor_staff_id,e.policy_revision,e.evidence_digest,
    e.occurred_seconds,e.occurred_nanos)
  IS DISTINCT FROM ROW('core-banking-cards'::text,'managed-card'::text,a.managed_card_id,'MANAGED_CARD_RESERVATION_RECORDED'::text,'COMPLETED'::text,a.action_id,a.receipt_id,a.operation_id,
    p_issuer,p_subject,p_platform,p_client,p_realm,p_staff,p_policy,p_evidence,
    pg_catalog.floor(extract(epoch FROM a.recorded_at))::bigint,((extract(epoch FROM a.recorded_at)-pg_catalog.floor(extract(epoch FROM a.recorded_at)))*1000000000)::integer) THEN
  RAISE EXCEPTION 'Exact retained Cards audit event required' USING ERRCODE='23514';
 END IF;
END $$;
REVOKE ALL ON FUNCTION public.firefly_cards_require_original_audit(public.managed_card_enrollment_action,TEXT,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TEXT) FROM PUBLIC,cards_managed_command,cards_managed_reader;
GRANT EXECUTE ON FUNCTION public.firefly_cards_require_original_audit(public.managed_card_enrollment_action,TEXT,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TEXT) TO cards_managed_guard;
RESET ROLE;
REVOKE CREATE ON SCHEMA public FROM firefly_audit_guard;
SET LOCAL ROLE cards_managed_guard;
ALTER TABLE public.managed_card_enrollment_action ADD CONSTRAINT managed_card_permanent_audit_fk
 FOREIGN KEY(tenant_id,event_id) REFERENCES firefly_audit.audit_event(resource_tenant_id,event_id)
 DEFERRABLE INITIALLY DEFERRED NOT VALID;
CREATE TABLE cards_managed_private.audit_pair (
 nonce UUID PRIMARY KEY REFERENCES cards_managed_private.context(nonce),event_id UUID NOT NULL,
 actor_client_id TEXT NOT NULL,actor_staff_realm TEXT,actor_staff_id UUID,policy_revision TEXT NOT NULL,evidence_digest TEXT NOT NULL,
 disposition TEXT NOT NULL CHECK(disposition IN('NEW_RESERVATION','EXACT_RESERVE_REPLAY'))
);
REVOKE ALL ON cards_managed_private.audit_pair FROM PUBLIC,cards_managed_command,cards_managed_reader;
CREATE TRIGGER immutable_original_audit_pair BEFORE UPDATE OR DELETE ON cards_managed_private.audit_pair
 FOR EACH ROW EXECUTE FUNCTION cards_managed_private.immutable_business_row();
CREATE FUNCTION cards_managed_private.audit_text(p_value TEXT) RETURNS BYTEA
LANGUAGE SQL IMMUTABLE STRICT SET search_path=pg_catalog AS $$
 SELECT pg_catalog.int4send(pg_catalog.octet_length(pg_catalog.convert_to(p_value,'UTF8')))||pg_catalog.convert_to(p_value,'UTF8');
$$;
CREATE FUNCTION cards_managed_private.audit_evidence(a public.managed_card_enrollment_action) RETURNS TEXT
LANGUAGE SQL IMMUTABLE STRICT SET search_path=pg_catalog AS $$
 SELECT pg_catalog.encode(pg_catalog.sha256(
  cards_managed_private.audit_text('cards.managed.reservation.evidence')||pg_catalog.int4send(1)
  ||pg_catalog.uuid_send(a.tenant_id)||pg_catalog.uuid_send(a.actor_id)||pg_catalog.uuid_send(a.party_id)||pg_catalog.uuid_send(a.account_id)
  ||pg_catalog.uuid_send(a.contract_id)||pg_catalog.uuid_send(a.product_id)||pg_catalog.uuid_send(a.managed_card_id)||pg_catalog.uuid_send(a.enrollment_id)
  ||pg_catalog.uuid_send(a.action_id)||pg_catalog.uuid_send(a.receipt_id)||pg_catalog.uuid_send(a.account_fence_receipt_id)||pg_catalog.uuid_send(a.operation_id)
  ||pg_catalog.uuid_send(a.event_id)||pg_catalog.uuid_send(a.outbox_id)
  ||pg_catalog.int8send(a.account_version)||pg_catalog.int8send(a.product_version)||pg_catalog.int8send(a.accept_not_after_epoch_millis)||pg_catalog.int8send(a.progress_version)
  ||pg_catalog.int8send(pg_catalog.floor(extract(epoch FROM a.recorded_at))::bigint)
  ||pg_catalog.int4send(((extract(epoch FROM a.recorded_at)-pg_catalog.floor(extract(epoch FROM a.recorded_at)))*1000000000)::integer)
  ||cards_managed_private.audit_text(a.issuer_adapter_id)||cards_managed_private.audit_text(a.processor_adapter_id)||cards_managed_private.audit_text(a.reason_code)
  ||cards_managed_private.audit_text(a.intent_digest)||cards_managed_private.audit_text(a.state)||cards_managed_private.audit_text('RETAINED')
  ||cards_managed_private.audit_text('RESERVATION_RECORDED')||cards_managed_private.audit_text('COORDINATE_INACTIVE_ENROLLMENT')||cards_managed_private.audit_text('COORDINATOR_PENDING')
 ),'hex');
$$;
CREATE FUNCTION cards_managed_private.require_original_audit() RETURNS TRIGGER
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;a public.managed_card_enrollment_action%ROWTYPE;p cards_managed_private.audit_pair%ROWTYPE;
BEGIN
 c:=cards_managed_private.current_context();
 IF c.purpose<>'RESERVE' OR NOT EXISTS(SELECT 1 FROM cards_managed_private.completion done WHERE done.nonce=c.nonce) THEN
  RAISE EXCEPTION 'Completed original Cards reservation required' USING ERRCODE='23514';
 END IF;
 PERFORM cards_managed_private.require_graph(c);
 SELECT * INTO a FROM public.managed_card_enrollment_action t WHERE t.tenant_id=c.tenant_id AND t.action_id=c.action_id;
 SELECT * INTO p FROM cards_managed_private.audit_pair t WHERE t.nonce=c.nonce;
 IF NOT FOUND OR p.event_id<>a.event_id OR p.evidence_digest IS DISTINCT FROM cards_managed_private.audit_evidence(a) THEN
  RAISE EXCEPTION 'Exact original Cards audit pair required' USING ERRCODE='23514';
 END IF;
 PERFORM public.firefly_cards_require_original_audit(a,c.caller_issuer,c.caller_subject,c.caller_tenant_key,p.actor_client_id,p.actor_staff_realm,p.actor_staff_id,p.policy_revision,p.evidence_digest,p.disposition);
 RETURN NULL;
END $$;
CREATE CONSTRAINT TRIGGER original_cards_audit AFTER INSERT ON public.managed_card_enrollment_action
 DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION cards_managed_private.require_original_audit();
CREATE CONSTRAINT TRIGGER original_cards_audit_pair AFTER INSERT ON cards_managed_private.audit_pair
 DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION cards_managed_private.require_original_audit();
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA cards_managed_private FROM PUBLIC,cards_managed_command,cards_managed_reader;
RESET ROLE;
GRANT CREATE ON SCHEMA public TO cards_managed_guard;
SET LOCAL ROLE cards_managed_guard;
CREATE FUNCTION public.cards_managed_original_audit_pair(p_nonce UUID,p_event UUID,p_client TEXT,p_realm TEXT,p_staff UUID,p_policy TEXT,p_evidence TEXT,p_disposition TEXT) RETURNS UUID
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;a public.managed_card_enrollment_action%ROWTYPE;
BEGIN
 c:=cards_managed_private.current_context();
 IF c.purpose<>'RESERVE' OR c.nonce IS DISTINCT FROM p_nonce OR p_policy IS NULL OR length(p_policy) NOT BETWEEN 1 AND 2048 THEN
  RAISE EXCEPTION 'Original admitted Cards audit pair required' USING ERRCODE='42501';
 END IF;
 PERFORM cards_managed_private.require_graph(c);
 SELECT * INTO a FROM public.managed_card_enrollment_action t WHERE t.tenant_id=c.tenant_id AND t.action_id=c.action_id;
 IF a.event_id IS DISTINCT FROM p_event OR cards_managed_private.audit_evidence(a) IS DISTINCT FROM p_evidence THEN
  RAISE EXCEPTION 'Exact retained Cards audit body required' USING ERRCODE='23514';
 END IF;
 PERFORM public.firefly_cards_require_original_audit(a,c.caller_issuer,c.caller_subject,c.caller_tenant_key,p_client,p_realm,p_staff,p_policy,p_evidence,p_disposition);
 INSERT INTO cards_managed_private.audit_pair VALUES(c.nonce,p_event,p_client,p_realm,p_staff,p_policy,p_evidence,p_disposition);
 RETURN c.nonce;
END $$;
REVOKE ALL ON FUNCTION public.cards_managed_original_audit_pair(UUID,UUID,TEXT,TEXT,UUID,TEXT,TEXT,TEXT) FROM PUBLIC,cards_managed_reader;
GRANT EXECUTE ON FUNCTION public.cards_managed_original_audit_pair(UUID,UUID,TEXT,TEXT,UUID,TEXT,TEXT,TEXT) TO cards_managed_command;
RESET ROLE;
REVOKE CREATE ON SCHEMA public FROM cards_managed_guard;
SET LOCAL ROLE firefly_audit_guard;
REVOKE REFERENCES(resource_tenant_id,event_id) ON firefly_audit.audit_event FROM cards_managed_guard;
REVOKE USAGE ON SCHEMA firefly_audit FROM cards_managed_guard;
RESET ROLE;
DO $$ BEGIN
 IF pg_catalog.has_schema_privilege('firefly_audit_guard','public','CREATE') OR pg_catalog.has_schema_privilege('cards_managed_guard','public','CREATE') THEN
  RAISE EXCEPTION 'Owner audit temporary installation grant cleanup required' USING ERRCODE='42501';
 END IF;
END $$;
COMMIT;
