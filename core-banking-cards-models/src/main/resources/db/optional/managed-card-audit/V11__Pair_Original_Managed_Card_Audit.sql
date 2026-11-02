-- Explicit owning deployment after V10 and the original released immutable-audit
-- postgresql.sql + completed-append.sql. Installer grants the original completed
-- accessor to cards_managed_guard and firefly_audit_writer before this migration.
BEGIN;
SET LOCAL statement_timeout='5s';
SET LOCAL lock_timeout='5s';
DO $$ BEGIN
    IF NOT EXISTS(SELECT FROM pg_catalog.pg_roles WHERE rolname='firefly_audit_writer' AND rolcanlogin
        AND NOT rolsuper AND NOT rolbypassrls AND NOT rolinherit AND NOT rolcreatedb AND NOT rolcreaterole AND NOT rolreplication)
       OR pg_catalog.pg_has_role('firefly_audit_writer','firefly_audit_guard','MEMBER')
       OR pg_catalog.pg_has_role('firefly_audit_writer','cards_managed_guard','MEMBER')
       OR pg_catalog.has_schema_privilege('firefly_audit_writer','public','CREATE')
       OR NOT EXISTS(SELECT FROM pg_catalog.pg_proc p JOIN pg_catalog.pg_roles r ON r.oid=p.proowner
            WHERE p.oid=to_regprocedure('public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean)')
                AND p.prosecdef AND p.provolatile='v' AND p.proconfig=ARRAY['search_path=pg_catalog'] AND r.rolname='firefly_audit_guard')
       OR NOT pg_catalog.has_function_privilege('cards_managed_guard','public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean)','EXECUTE')
       OR NOT pg_catalog.has_function_privilege('firefly_audit_writer','public.firefly_audit_require_completed(uuid,uuid,bigint,text,boolean)','EXECUTE') THEN
        RAISE EXCEPTION 'Original restricted completed audit deployment required' USING ERRCODE='42501';
    END IF;
END $$;

CREATE TABLE public.managed_card_reservation_audit (
    action_id UUID PRIMARY KEY REFERENCES public.managed_card_enrollment_action(action_id),
    tenant_id UUID NOT NULL,event_id UUID NOT NULL,audit_context_nonce UUID NOT NULL,
    audit_sequence BIGINT NOT NULL CHECK(audit_sequence>0),
    audit_record_hash TEXT NOT NULL CHECK((audit_record_hash COLLATE "C") ~ '^[0-9a-f]{64}$'),
    UNIQUE(tenant_id,event_id),
    FOREIGN KEY(tenant_id,event_id) REFERENCES firefly_audit.audit_event(resource_tenant_id,event_id),
    CHECK('00000000-0000-0000-0000-000000000000'::uuid<>ALL(ARRAY[action_id,tenant_id,event_id,audit_context_nonce]))
);
ALTER TABLE public.managed_card_reservation_audit ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_reservation_audit FORCE ROW LEVEL SECURITY;
REVOKE ALL ON public.managed_card_reservation_audit FROM PUBLIC,cards_managed_command,cards_managed_reader;
GRANT SELECT ON public.managed_card_reservation_audit TO cards_managed_guard,cards_managed_reader;
GRANT SELECT,INSERT ON public.managed_card_reservation_audit TO firefly_audit_writer;
CREATE POLICY managed_audit_guard ON public.managed_card_reservation_audit FOR SELECT TO cards_managed_guard USING(true);
CREATE POLICY managed_audit_read ON public.managed_card_reservation_audit FOR SELECT TO cards_managed_reader,firefly_audit_writer
    USING(public.cards_managed_action_matches(tenant_id,action_id));

ALTER TABLE cards_managed_private.context ADD COLUMN audit_paired BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN original_action_existed BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN original_binding_existed BOOLEAN NOT NULL DEFAULT false;
CREATE FUNCTION cards_managed_private.capture_original_audit_disposition() RETURNS TRIGGER
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog AS $$
BEGIN
    NEW.audit_paired:=false;
    NEW.original_action_existed:=EXISTS(SELECT FROM public.managed_card_enrollment_action a WHERE a.action_id=NEW.action_id);
    NEW.original_binding_existed:=EXISTS(SELECT FROM public.managed_card_reservation_audit b WHERE b.action_id=NEW.action_id);
    RETURN NEW;
END $$;
ALTER FUNCTION cards_managed_private.capture_original_audit_disposition() OWNER TO cards_managed_guard;
REVOKE ALL ON FUNCTION cards_managed_private.capture_original_audit_disposition() FROM PUBLIC;
CREATE TRIGGER managed_original_audit_disposition BEFORE INSERT ON cards_managed_private.context
    FOR EACH ROW EXECUTE FUNCTION cards_managed_private.capture_original_audit_disposition();

CREATE FUNCTION public.cards_managed_audit_can_append(p_tenant UUID,p_action UUID) RETURNS BOOLEAN
LANGUAGE sql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
    SELECT session_user='firefly_audit_writer' AND public.cards_managed_action_matches(p_tenant,p_action)
        AND EXISTS(SELECT FROM cards_managed_private.context c WHERE c.xid=pg_current_xact_id_if_assigned()
            AND c.backend_pid=pg_backend_pid() AND c.login_name=session_user AND c.purpose='RESERVE'
            AND NOT c.original_action_existed AND NOT c.original_binding_existed AND NOT c.audit_paired)
$$;
CREATE POLICY managed_audit_append ON public.managed_card_reservation_audit FOR INSERT TO firefly_audit_writer
    WITH CHECK(public.cards_managed_audit_can_append(tenant_id,action_id));

-- Pure canonical evidence encoding matches the original Java DataOutputStream
-- contract. Business rows remain ordinary mapped Spring Data writes.
CREATE FUNCTION cards_managed_private.audit_text(p_text TEXT) RETURNS BYTEA
LANGUAGE sql IMMUTABLE SET search_path=pg_catalog AS $$
    SELECT int4send(octet_length(convert_to(p_text,'UTF8')))||convert_to(p_text,'UTF8')
$$;
CREATE FUNCTION cards_managed_private.reservation_evidence(a public.managed_card_enrollment_action) RETURNS TEXT
LANGUAGE sql IMMUTABLE SET search_path=pg_catalog AS $$
    SELECT encode(sha256(cards_managed_private.audit_text('cards.managed.reservation.evidence')||int4send(1)
        ||uuid_send(a.tenant_id)||uuid_send(a.actor_id)||uuid_send(a.party_id)||uuid_send(a.account_id)
        ||uuid_send(a.contract_id)||uuid_send(a.product_id)||uuid_send(a.managed_card_id)||uuid_send(a.enrollment_id)
        ||uuid_send(a.action_id)||uuid_send(a.receipt_id)||uuid_send(a.account_fence_receipt_id)||uuid_send(a.operation_id)||uuid_send(a.event_id)||uuid_send(a.outbox_id)
        ||int8send(a.account_version)||int8send(a.product_version)||int8send(a.accept_not_after_epoch_millis)||int8send(a.progress_version)
        ||int8send(floor(extract(epoch FROM a.recorded_at))::bigint)
        ||int4send(((extract(epoch FROM a.recorded_at)-floor(extract(epoch FROM a.recorded_at)))*1000000000)::integer)
        ||cards_managed_private.audit_text(a.issuer_adapter_id)||cards_managed_private.audit_text(a.processor_adapter_id)
        ||cards_managed_private.audit_text(a.reason_code)||cards_managed_private.audit_text(a.intent_digest)||cards_managed_private.audit_text(a.state)
        ||cards_managed_private.audit_text('RETAINED')||cards_managed_private.audit_text('RESERVATION_RECORDED')
        ||cards_managed_private.audit_text('COORDINATE_INACTIVE_ENROLLMENT')||cards_managed_private.audit_text('COORDINATOR_PENDING')),'hex')
$$;
ALTER FUNCTION cards_managed_private.audit_text(TEXT) OWNER TO cards_managed_guard;
ALTER FUNCTION cards_managed_private.reservation_evidence(public.managed_card_enrollment_action) OWNER TO cards_managed_guard;
REVOKE ALL ON FUNCTION cards_managed_private.audit_text(TEXT),cards_managed_private.reservation_evidence(public.managed_card_enrollment_action) FROM PUBLIC;

CREATE FUNCTION public.cards_managed_original_audit_pair(p_nonce UUID,p_event UUID,p_audit_nonce UUID,p_sequence BIGINT,p_hash TEXT,
    p_client TEXT,p_realm TEXT,p_staff UUID,p_policy TEXT,p_evidence TEXT,p_disposition TEXT) RETURNS UUID
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;a public.managed_card_enrollment_action%ROWTYPE;
    b public.managed_card_reservation_audit%ROWTYPE;completed RECORD;is_replay BOOLEAN;seconds BIGINT;nanos INTEGER;
BEGIN
    SELECT * INTO c FROM cards_managed_private.context WHERE xid=pg_current_xact_id_if_assigned()
        AND backend_pid=pg_backend_pid() AND login_name=session_user AND nonce=p_nonce;
    IF NOT FOUND OR session_user<>'firefly_audit_writer' OR c.purpose<>'RESERVE' OR c.audit_paired
        OR c.authority_until<=clock_timestamp() OR c.command_until<=clock_timestamp()
        OR p_disposition IS NULL OR p_disposition NOT IN ('NEW_RESERVATION','EXACT_RESERVE_REPLAY')
        OR c.original_action_existed<>c.original_binding_existed THEN
        RAISE EXCEPTION 'Original managed reservation audit scope required' USING ERRCODE='42501';
    END IF;
    is_replay:=p_disposition='EXACT_RESERVE_REPLAY';
    IF is_replay<>c.original_action_existed THEN
        RAISE EXCEPTION 'Original business and audit disposition required' USING ERRCODE='23514';
    END IF;
    SELECT * INTO a FROM public.managed_card_enrollment_action WHERE action_id=c.action_id AND tenant_id=c.tenant_id
        AND party_id=c.party_id AND account_id=c.account_id AND actor_id=c.actor_id
        AND account_fence_receipt_id=c.fence AND intent_digest=c.intent AND event_id=p_event;
    IF NOT FOUND THEN RAISE EXCEPTION 'Original managed reservation evidence required' USING ERRCODE='23514';END IF;
    SELECT * INTO b FROM public.managed_card_reservation_audit WHERE action_id=a.action_id AND tenant_id=a.tenant_id
        AND event_id=a.event_id AND audit_sequence=p_sequence AND audit_record_hash=p_hash
        AND (is_replay OR audit_context_nonce=p_audit_nonce);
    IF NOT FOUND OR p_evidence IS DISTINCT FROM cards_managed_private.reservation_evidence(a) THEN
        RAISE EXCEPTION 'Original immutable managed audit binding required' USING ERRCODE='23514';
    END IF;
    SELECT * INTO STRICT completed FROM public.firefly_audit_require_completed(p_audit_nonce,p_event,p_sequence,p_hash,is_replay);
    seconds:=floor(extract(epoch FROM a.recorded_at))::bigint;
    nanos:=((extract(epoch FROM a.recorded_at)-seconds)*1000000000)::integer;
    IF (completed.resource_tenant_id,completed.service,completed.resource_type,completed.resource_id,completed.event_id,
        completed.actor_issuer,completed.actor_subject,completed.actor_client_id,completed.actor_platform_tenant_id,completed.actor_staff_realm,completed.actor_staff_id,
        completed.action,completed.outcome,completed.process_id,completed.correlation_id,completed.causation_id,completed.occurred_seconds,completed.occurred_nanos,completed.evidence_digest,completed.policy_revision)
        IS DISTINCT FROM (a.tenant_id,'core-banking-cards'::text,'managed-card'::text,a.managed_card_id,a.event_id,
            c.issuer,c.subject,p_client,c.tenant_key,p_realm,p_staff,'MANAGED_CARD_RESERVATION_RECORDED'::text,'COMPLETED'::text,
            a.action_id,a.receipt_id,a.operation_id,seconds,nanos,p_evidence,p_policy) THEN
        RAISE EXCEPTION 'Exact completed original managed audit record required' USING ERRCODE='23514';
    END IF;
    IF c.authority_until<=clock_timestamp() OR c.command_until<=clock_timestamp() THEN
        RAISE EXCEPTION 'Original managed reservation audit authority expired' USING ERRCODE='42501';
    END IF;
    UPDATE cards_managed_private.context SET audit_paired=true WHERE xid=c.xid AND nonce=c.nonce;
    RETURN p_nonce;
END $$;

ALTER FUNCTION public.cards_managed_enrollment_complete(UUID) RENAME TO cards_managed_enrollment_storage_complete;
REVOKE ALL ON FUNCTION public.cards_managed_enrollment_storage_complete(UUID) FROM PUBLIC,cards_managed_command,cards_managed_reader,firefly_audit_writer;
CREATE FUNCTION public.cards_managed_enrollment_complete(p_nonce UUID) RETURNS UUID
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;
BEGIN
    SELECT * INTO c FROM cards_managed_private.context WHERE xid=pg_current_xact_id_if_assigned()
        AND backend_pid=pg_backend_pid() AND login_name=session_user AND nonce=p_nonce;
    IF NOT FOUND OR (c.purpose='RESERVE' AND NOT c.audit_paired)
        OR (c.purpose='RECOVER' AND (NOT c.original_action_existed OR NOT c.original_binding_existed)) THEN
        RAISE EXCEPTION 'Original completed managed audit pairing required' USING ERRCODE='42501';
    END IF;
    RETURN public.cards_managed_enrollment_storage_complete(p_nonce);
END $$;

GRANT CREATE ON SCHEMA public TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_audit_can_append(UUID,UUID) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_original_audit_pair(UUID,UUID,UUID,BIGINT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TEXT) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_enrollment_complete(UUID) OWNER TO cards_managed_guard;
REVOKE CREATE ON SCHEMA public FROM cards_managed_guard;
REVOKE ALL ON FUNCTION public.cards_managed_audit_can_append(UUID,UUID),public.cards_managed_original_audit_pair(UUID,UUID,UUID,BIGINT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TEXT),
    public.cards_managed_enrollment_complete(UUID) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.cards_managed_audit_can_append(UUID,UUID),public.cards_managed_original_audit_pair(UUID,UUID,UUID,BIGINT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TEXT)
    TO firefly_audit_writer;
GRANT EXECUTE ON FUNCTION public.cards_managed_enrollment_complete(UUID) TO cards_managed_command,cards_managed_reader,firefly_audit_writer;

-- Exact audit login stays NOINHERIT and cannot SET ROLE to the technical command
-- role. Explicit least-privilege business grants do not alter audit provenance.
GRANT cards_managed_command TO firefly_audit_writer WITH INHERIT FALSE, SET FALSE, ADMIN FALSE;
GRANT USAGE ON SCHEMA public TO firefly_audit_writer;
GRANT SELECT,INSERT ON public.managed_card,public.managed_card_enrollment_action,public.managed_card_provider_operation,
    public.managed_card_enrollment_event,public.managed_card_enrollment_outbox TO firefly_audit_writer;
ALTER POLICY managed_card_read ON public.managed_card TO cards_managed_command,cards_managed_reader,firefly_audit_writer;
ALTER POLICY managed_card_append ON public.managed_card TO cards_managed_command,firefly_audit_writer;
ALTER POLICY managed_action_read ON public.managed_card_enrollment_action TO cards_managed_command,cards_managed_reader,firefly_audit_writer;
ALTER POLICY managed_action_append ON public.managed_card_enrollment_action TO cards_managed_command,firefly_audit_writer;
ALTER POLICY managed_operation_read ON public.managed_card_provider_operation TO cards_managed_command,cards_managed_reader,firefly_audit_writer;
ALTER POLICY managed_operation_append ON public.managed_card_provider_operation TO cards_managed_command,firefly_audit_writer;
ALTER POLICY managed_event_read ON public.managed_card_enrollment_event TO cards_managed_command,cards_managed_reader,firefly_audit_writer;
ALTER POLICY managed_event_append ON public.managed_card_enrollment_event TO cards_managed_command,firefly_audit_writer;
ALTER POLICY managed_outbox_read ON public.managed_card_enrollment_outbox TO cards_managed_command,cards_managed_reader,firefly_audit_writer;
ALTER POLICY managed_outbox_append ON public.managed_card_enrollment_outbox TO cards_managed_command,firefly_audit_writer;
GRANT EXECUTE ON FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ),
    public.cards_managed_scope_matches(UUID,UUID,UUID,UUID,UUID,BOOLEAN,TIMESTAMPTZ),public.cards_managed_action_matches(UUID,UUID,BOOLEAN,TIMESTAMPTZ),
    public.cards_managed_card_visible(UUID,UUID,UUID,UUID,TIMESTAMPTZ),public.cards_managed_reservation_intent_matches(UUID,TEXT,BIGINT) TO firefly_audit_writer;
COMMIT;
