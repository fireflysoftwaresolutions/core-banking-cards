-- Opt-in immutable managed reservation only. Default V1-V7 selection remains unchanged.
-- Technical scope does not establish caller, owner, account-fence or provider authority.
DO $$ BEGIN
    IF NOT pg_catalog.pg_has_role(current_user,'cards_managed_guard','SET')
        OR NOT pg_catalog.has_schema_privilege(current_user,'public','CREATE WITH GRANT OPTION')
        OR pg_catalog.has_schema_privilege('cards_managed_guard','public','CREATE') THEN
        RAISE EXCEPTION 'Separately delegated managed Cards migrator required' USING ERRCODE='42501';
    END IF;
    IF (SELECT count(*) FROM pg_catalog.pg_roles r WHERE r.rolname IN('cards_managed_guard','cards_managed_command','cards_managed_reader')
        AND NOT r.rolinherit AND NOT r.rolsuper AND NOT r.rolbypassrls AND NOT r.rolcreatedb AND NOT r.rolcreaterole AND NOT r.rolreplication
        AND r.rolcanlogin=(r.rolname<>'cards_managed_guard'))<>3
        OR pg_catalog.pg_has_role('cards_managed_command','cards_managed_guard','MEMBER')
        OR pg_catalog.pg_has_role('cards_managed_reader','cards_managed_guard','MEMBER') THEN
        RAISE EXCEPTION 'Exact preprovisioned managed Cards roles required' USING ERRCODE='42501';
    END IF;
END $$;
CREATE SCHEMA cards_managed_private AUTHORIZATION cards_managed_guard;
GRANT CREATE ON SCHEMA public TO cards_managed_guard;
SET LOCAL ROLE cards_managed_guard;
REVOKE ALL ON SCHEMA cards_managed_private FROM PUBLIC,cards_managed_command,cards_managed_reader;

CREATE TABLE public.managed_card (
    managed_card_id UUID PRIMARY KEY,tenant_id UUID NOT NULL,party_id UUID NOT NULL,account_id UUID NOT NULL,
    contract_id UUID NOT NULL,product_id UUID NOT NULL,enrollment_id UUID NOT NULL,action_id UUID NOT NULL UNIQUE,
    restriction TEXT NOT NULL CHECK(restriction='RETAINED'),recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,party_id,account_id,action_id,managed_card_id),UNIQUE(tenant_id,enrollment_id),
    CHECK(managed_card_id<>'00000000-0000-0000-0000-000000000000'::uuid AND isfinite(recorded_at))
);
CREATE TABLE public.managed_card_enrollment_action (
    action_id UUID PRIMARY KEY,tenant_id UUID NOT NULL,actor_id UUID NOT NULL,party_id UUID NOT NULL,account_id UUID NOT NULL,
    contract_id UUID NOT NULL,product_id UUID NOT NULL,managed_card_id UUID NOT NULL UNIQUE,enrollment_id UUID NOT NULL,
    account_version BIGINT NOT NULL CHECK(account_version>=0),product_version BIGINT NOT NULL CHECK(product_version>=0),
    issuer_adapter_id TEXT NOT NULL CHECK(issuer_adapter_id ~ '^[A-Za-z][A-Za-z0-9_.-]{0,63}$'),
    processor_adapter_id TEXT NOT NULL CHECK(processor_adapter_id ~ '^[A-Za-z][A-Za-z0-9_.-]{0,63}$'),
    reason_code TEXT NOT NULL CHECK(reason_code ~ '^[A-Z][A-Z0-9_]{0,63}$'),accept_not_after_epoch_millis BIGINT NOT NULL,
    intent_digest TEXT NOT NULL CHECK(length(intent_digest)=64 AND intent_digest ~ '^[0-9a-f]{64}$'),
    receipt_id UUID NOT NULL UNIQUE,account_fence_receipt_id UUID NOT NULL,
    operation_id UUID NOT NULL UNIQUE,event_id UUID NOT NULL UNIQUE,outbox_id UUID NOT NULL UNIQUE,
    progress_version BIGINT NOT NULL CHECK(progress_version=0),state TEXT NOT NULL CHECK(state='RESERVED'),recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id),UNIQUE(tenant_id,party_id,account_id,action_id,managed_card_id),
    CHECK(action_id<>'00000000-0000-0000-0000-000000000000'::uuid AND isfinite(recorded_at)
        AND recorded_at=pg_catalog.date_trunc('milliseconds',recorded_at)
        AND pg_catalog.floor(extract(epoch FROM recorded_at)*1000)::bigint<accept_not_after_epoch_millis)
);
CREATE TABLE public.managed_card_provider_operation (
    operation_id UUID PRIMARY KEY,tenant_id UUID NOT NULL,action_id UUID NOT NULL UNIQUE,managed_card_id UUID NOT NULL,
    intent_digest TEXT NOT NULL,accept_not_after_epoch_millis BIGINT NOT NULL,recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,operation_id),
    FOREIGN KEY(tenant_id,action_id) REFERENCES public.managed_card_enrollment_action(tenant_id,action_id) DEFERRABLE INITIALLY DEFERRED
);
CREATE TABLE public.managed_card_enrollment_event (
    event_id UUID PRIMARY KEY,tenant_id UUID NOT NULL,action_id UUID NOT NULL UNIQUE,managed_card_id UUID NOT NULL,
    operation_id UUID NOT NULL,intent_digest TEXT NOT NULL,kind TEXT NOT NULL CHECK(kind='RESERVATION_RECORDED'),
    progress_version BIGINT NOT NULL CHECK(progress_version=0),recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,event_id),
    FOREIGN KEY(tenant_id,action_id,operation_id) REFERENCES public.managed_card_provider_operation(tenant_id,action_id,operation_id) DEFERRABLE INITIALLY DEFERRED
);
CREATE TABLE public.managed_card_enrollment_outbox (
    outbox_id UUID PRIMARY KEY,tenant_id UUID NOT NULL,action_id UUID NOT NULL UNIQUE,operation_id UUID NOT NULL,event_id UUID NOT NULL,
    kind TEXT NOT NULL CHECK(kind='COORDINATE_INACTIVE_ENROLLMENT'),state TEXT NOT NULL CHECK(state='COORDINATOR_PENDING'),
    progress_version BIGINT NOT NULL CHECK(progress_version=0),recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,outbox_id),
    FOREIGN KEY(tenant_id,action_id,event_id) REFERENCES public.managed_card_enrollment_event(tenant_id,action_id,event_id) DEFERRABLE INITIALLY DEFERRED
);
ALTER TABLE public.managed_card ADD CONSTRAINT managed_card_action_fk
    FOREIGN KEY(tenant_id,party_id,account_id,action_id,managed_card_id)
    REFERENCES public.managed_card_enrollment_action(tenant_id,party_id,account_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.managed_card_enrollment_action ADD CONSTRAINT managed_action_card_fk
    FOREIGN KEY(tenant_id,party_id,account_id,action_id,managed_card_id)
    REFERENCES public.managed_card(tenant_id,party_id,account_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.managed_card_enrollment_action ADD CONSTRAINT managed_action_operation_fk
    FOREIGN KEY(tenant_id,action_id,operation_id) REFERENCES public.managed_card_provider_operation(tenant_id,action_id,operation_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.managed_card_enrollment_action ADD CONSTRAINT managed_action_event_fk
    FOREIGN KEY(tenant_id,action_id,event_id) REFERENCES public.managed_card_enrollment_event(tenant_id,action_id,event_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.managed_card_enrollment_action ADD CONSTRAINT managed_action_outbox_fk
    FOREIGN KEY(tenant_id,action_id,outbox_id) REFERENCES public.managed_card_enrollment_outbox(tenant_id,action_id,outbox_id) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE cards_managed_private.context (
    nonce UUID PRIMARY KEY,xid XID8 NOT NULL,backend_pid INTEGER NOT NULL,db_session_user NAME NOT NULL,
    tenant_id UUID NOT NULL,party_id UUID NOT NULL,account_id UUID NOT NULL,actor_id UUID NOT NULL,action_id UUID NOT NULL,
    purpose TEXT NOT NULL CHECK(purpose IN('RESERVE','RECOVER')),caller_issuer TEXT NOT NULL,caller_subject TEXT NOT NULL,caller_tenant_key TEXT NOT NULL,
    account_fence_receipt_id UUID,intent_digest TEXT,scope_digest TEXT NOT NULL,
    installed_at TIMESTAMPTZ NOT NULL,authority_until TIMESTAMPTZ NOT NULL,command_until TIMESTAMPTZ NOT NULL,
    UNIQUE(xid,backend_pid,db_session_user),CHECK(isfinite(installed_at) AND isfinite(authority_until) AND isfinite(command_until)
        AND command_until>installed_at AND command_until<=authority_until AND command_until<=installed_at+INTERVAL '5 seconds')
);
CREATE TABLE cards_managed_private.completion (nonce UUID PRIMARY KEY REFERENCES cards_managed_private.context(nonce));
REVOKE ALL ON ALL TABLES IN SCHEMA cards_managed_private FROM PUBLIC,cards_managed_command,cards_managed_reader;

CREATE FUNCTION cards_managed_private.current_context() RETURNS cards_managed_private.context
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE; observed TIMESTAMPTZ;
BEGIN
    SELECT * INTO c FROM cards_managed_private.context t WHERE t.xid=pg_catalog.pg_current_xact_id_if_assigned()
        AND t.backend_pid=pg_catalog.pg_backend_pid() AND t.db_session_user=session_user;
    IF NOT FOUND THEN RAISE EXCEPTION 'Installed managed Cards context required' USING ERRCODE='42501'; END IF;
    observed:=pg_catalog.clock_timestamp();
    IF observed<c.installed_at OR observed>=c.authority_until OR observed>=c.command_until
        OR NOT((session_user='cards_managed_command' AND c.purpose='RESERVE') OR(session_user='cards_managed_reader' AND c.purpose='RECOVER')) THEN
        RAISE EXCEPTION 'Original managed Cards window required' USING ERRCODE='42501';
    END IF;
    RETURN c;
END $$;
CREATE FUNCTION public.cards_managed_enrollment_scope_matches(p_tenant UUID,p_action UUID) RETURNS BOOLEAN
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;
BEGIN c:=cards_managed_private.current_context();RETURN c.tenant_id=p_tenant AND c.action_id=p_action;END $$;

CREATE FUNCTION public.cards_managed_enrollment_enter(p_nonce UUID,p_tenant UUID,p_party UUID,p_account UUID,p_actor UUID,p_action UUID,
    p_purpose TEXT,p_issuer TEXT,p_subject TEXT,p_tenant_key TEXT,p_fence UUID,p_intent TEXT,p_scope TEXT,p_authority_until TIMESTAMPTZ,p_command_until TIMESTAMPTZ)
RETURNS TABLE(context_nonce UUID,action_id UUID,purpose TEXT,scope_digest TEXT,db_now_millis BIGINT,authority_until_millis BIGINT,command_until_millis BIGINT)
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE observed TIMESTAMPTZ; cap TIMESTAMPTZ; runtime_role pg_catalog.pg_roles%ROWTYPE;
BEGIN
    SELECT * INTO runtime_role FROM pg_catalog.pg_roles r WHERE r.rolname=session_user;
    IF runtime_role.rolsuper OR runtime_role.rolbypassrls OR runtime_role.rolcreatedb OR runtime_role.rolcreaterole OR runtime_role.rolreplication OR runtime_role.rolinherit
        OR pg_catalog.pg_has_role(session_user,'cards_managed_guard','MEMBER') OR pg_catalog.has_schema_privilege(session_user,'public','CREATE')
        OR pg_catalog.has_schema_privilege(session_user,'cards_managed_private','USAGE')
        OR NOT((session_user='cards_managed_command' AND p_purpose='RESERVE') OR(session_user='cards_managed_reader' AND p_purpose='RECOVER')) THEN
        RAISE EXCEPTION 'Restricted managed Cards runtime required' USING ERRCODE='42501';
    END IF;
    IF p_nonce IS NULL OR p_nonce='00000000-0000-0000-0000-000000000000'::uuid
        OR p_tenant IS NULL OR p_party IS NULL OR p_account IS NULL OR p_actor IS NULL OR p_action IS NULL
        OR p_tenant='00000000-0000-0000-0000-000000000000'::uuid OR p_party='00000000-0000-0000-0000-000000000000'::uuid
        OR p_account='00000000-0000-0000-0000-000000000000'::uuid OR p_actor='00000000-0000-0000-0000-000000000000'::uuid OR p_action='00000000-0000-0000-0000-000000000000'::uuid
        OR p_issuer IS NULL OR length(p_issuer) NOT BETWEEN 1 AND 2048 OR p_subject IS NULL OR length(p_subject) NOT BETWEEN 1 AND 2048
        OR p_tenant_key IS NULL OR length(p_tenant_key) NOT BETWEEN 1 AND 2048 OR p_scope IS NULL OR length(p_scope)<>64 OR p_scope !~ '^[0-9a-f]{64}$'
        OR p_authority_until IS NULL OR NOT isfinite(p_authority_until) OR p_command_until IS NULL OR NOT isfinite(p_command_until)
        OR(p_purpose='RESERVE' AND(p_fence IS NULL OR p_fence='00000000-0000-0000-0000-000000000000'::uuid OR p_intent IS NULL OR length(p_intent)<>64 OR p_intent !~ '^[0-9a-f]{64}$'))
        OR(p_purpose='RECOVER' AND(p_fence IS NOT NULL OR p_intent IS NOT NULL)) THEN
        RAISE EXCEPTION 'Exact managed Cards scope required' USING ERRCODE='42501';
    END IF;
    -- Same tenant, then account, then action order on every command and recovery path.
    PERFORM pg_catalog.pg_advisory_xact_lock(pg_catalog.hashtextextended('cards-managed-tenant:'||p_tenant::text,0));
    PERFORM pg_catalog.pg_advisory_xact_lock(pg_catalog.hashtextextended('cards-managed-account:'||p_tenant::text||':'||p_account::text,0));
    PERFORM pg_catalog.pg_advisory_xact_lock(pg_catalog.hashtextextended('cards-managed-action:'||p_tenant::text||':'||p_action::text,0));
    observed:=pg_catalog.date_trunc('milliseconds',pg_catalog.clock_timestamp());
    cap:=LEAST(p_authority_until,p_command_until,observed+INTERVAL '5 seconds');
    IF cap<=observed THEN RAISE EXCEPTION 'Original managed Cards window required' USING ERRCODE='42501'; END IF;
    INSERT INTO cards_managed_private.context VALUES(p_nonce,pg_catalog.pg_current_xact_id(),pg_catalog.pg_backend_pid(),session_user,
        p_tenant,p_party,p_account,p_actor,p_action,p_purpose,p_issuer,p_subject,p_tenant_key,p_fence,p_intent,p_scope,observed,p_authority_until,cap);
    RETURN QUERY SELECT p_nonce,p_action,p_purpose,p_scope,pg_catalog.floor(extract(epoch FROM observed)*1000)::bigint,
        pg_catalog.floor(extract(epoch FROM p_authority_until)*1000)::bigint,pg_catalog.floor(extract(epoch FROM cap)*1000)::bigint;
END $$;

CREATE FUNCTION cards_managed_private.require_graph(c cards_managed_private.context) RETURNS VOID
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE a public.managed_card_enrollment_action%ROWTYPE; good BOOLEAN;
BEGIN
    SELECT * INTO a FROM public.managed_card_enrollment_action t WHERE t.action_id=c.action_id AND t.tenant_id=c.tenant_id AND t.party_id=c.party_id AND t.account_id=c.account_id;
    IF NOT FOUND THEN RAISE EXCEPTION 'Complete managed Cards graph required' USING ERRCODE='23514'; END IF;
    IF c.purpose='RESERVE' AND(a.actor_id<>c.actor_id OR a.account_fence_receipt_id IS DISTINCT FROM c.account_fence_receipt_id OR a.intent_digest IS DISTINCT FROM c.intent_digest) THEN
        RAISE EXCEPTION 'Exact managed Cards reservation required' USING ERRCODE='23514';
    END IF;
    SELECT EXISTS(SELECT 1 FROM public.managed_card m
        JOIN public.managed_card_provider_operation o ON o.operation_id=a.operation_id AND o.tenant_id=a.tenant_id AND o.action_id=a.action_id
        JOIN public.managed_card_enrollment_event e ON e.event_id=a.event_id AND e.tenant_id=a.tenant_id AND e.action_id=a.action_id
        JOIN public.managed_card_enrollment_outbox b ON b.outbox_id=a.outbox_id AND b.tenant_id=a.tenant_id AND b.action_id=a.action_id
        WHERE m.managed_card_id=a.managed_card_id AND m.action_id=a.action_id AND m.tenant_id=a.tenant_id AND m.party_id=a.party_id AND m.account_id=a.account_id
        AND m.contract_id=a.contract_id AND m.product_id=a.product_id AND m.enrollment_id=a.enrollment_id AND m.restriction='RETAINED'
        AND m.recorded_at=a.recorded_at AND o.managed_card_id=a.managed_card_id AND o.intent_digest=a.intent_digest
        AND o.accept_not_after_epoch_millis=a.accept_not_after_epoch_millis AND o.recorded_at=a.recorded_at
        AND e.managed_card_id=a.managed_card_id AND e.operation_id=a.operation_id AND e.intent_digest=a.intent_digest AND e.kind='RESERVATION_RECORDED'
        AND e.progress_version=0 AND e.recorded_at=a.recorded_at AND b.operation_id=a.operation_id AND b.event_id=a.event_id
        AND b.kind='COORDINATE_INACTIVE_ENROLLMENT' AND b.state='COORDINATOR_PENDING' AND b.progress_version=0 AND b.recorded_at=a.recorded_at) INTO good;
    IF NOT good THEN RAISE EXCEPTION 'Complete managed Cards graph required' USING ERRCODE='23514'; END IF;
END $$;
CREATE FUNCTION public.cards_managed_enrollment_complete(p_nonce UUID) RETURNS UUID
LANGUAGE plpgsql VOLATILE SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;
BEGIN
    c:=cards_managed_private.current_context();
    IF p_nonce IS DISTINCT FROM c.nonce THEN RAISE EXCEPTION 'Exact managed Cards completion required' USING ERRCODE='42501'; END IF;
    PERFORM cards_managed_private.require_graph(c);
    INSERT INTO cards_managed_private.completion VALUES(c.nonce);
    PERFORM cards_managed_private.current_context();RETURN c.nonce;
END $$;
CREATE FUNCTION cards_managed_private.require_deferred_graph() RETURNS TRIGGER
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog AS $$
DECLARE c cards_managed_private.context%ROWTYPE;
BEGIN
    c:=cards_managed_private.current_context();
    IF c.purpose<>'RESERVE' OR NOT EXISTS(SELECT 1 FROM cards_managed_private.completion done WHERE done.nonce=c.nonce) THEN
        RAISE EXCEPTION 'Completed managed Cards reservation required' USING ERRCODE='23514';
    END IF;
    PERFORM cards_managed_private.require_graph(c);RETURN NULL;
END $$;
CREATE FUNCTION cards_managed_private.immutable_business_row() RETURNS TRIGGER
LANGUAGE plpgsql SET search_path=pg_catalog AS $$
BEGIN RAISE EXCEPTION 'Immutable managed Cards reservation required' USING ERRCODE='42501'; END $$;

ALTER TABLE public.managed_card ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card FORCE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_action ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_action FORCE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_provider_operation ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_provider_operation FORCE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_event ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_event FORCE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_outbox ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.managed_card_enrollment_outbox FORCE ROW LEVEL SECURITY;
DO $$ DECLARE relation TEXT;
BEGIN
    FOREACH relation IN ARRAY ARRAY['managed_card','managed_card_enrollment_action','managed_card_provider_operation','managed_card_enrollment_event','managed_card_enrollment_outbox'] LOOP
        EXECUTE pg_catalog.format('REVOKE ALL ON public.%I FROM PUBLIC,cards_managed_command,cards_managed_reader',relation);
        EXECUTE pg_catalog.format('CREATE POLICY managed_integrity ON public.%I TO cards_managed_guard USING(true) WITH CHECK(true)',relation);
        EXECUTE pg_catalog.format('CREATE POLICY managed_scoped_read ON public.%I FOR SELECT TO cards_managed_command,cards_managed_reader USING(public.cards_managed_enrollment_scope_matches(tenant_id,action_id))',relation);
        EXECUTE pg_catalog.format('CREATE POLICY managed_scoped_insert ON public.%I FOR INSERT TO cards_managed_command WITH CHECK(public.cards_managed_enrollment_scope_matches(tenant_id,action_id))',relation);
        EXECUTE pg_catalog.format('CREATE TRIGGER managed_immutable BEFORE UPDATE OR DELETE ON public.%I FOR EACH ROW EXECUTE FUNCTION cards_managed_private.immutable_business_row()',relation);
        EXECUTE pg_catalog.format('CREATE CONSTRAINT TRIGGER managed_complete AFTER INSERT ON public.%I DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION cards_managed_private.require_deferred_graph()',relation);
        EXECUTE pg_catalog.format('GRANT SELECT,INSERT ON public.%I TO cards_managed_command',relation);
        EXECUTE pg_catalog.format('GRANT SELECT ON public.%I TO cards_managed_reader',relation);
    END LOOP;
END $$;
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA cards_managed_private FROM PUBLIC,cards_managed_command,cards_managed_reader;
REVOKE ALL ON FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ),
    public.cards_managed_enrollment_complete(UUID),public.cards_managed_enrollment_scope_matches(UUID,UUID) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ),
    public.cards_managed_enrollment_complete(UUID),public.cards_managed_enrollment_scope_matches(UUID,UUID) TO cards_managed_command,cards_managed_reader;
RESET ROLE;
REVOKE CREATE ON SCHEMA public FROM cards_managed_guard;
DO $$ BEGIN
    IF pg_catalog.has_schema_privilege('cards_managed_guard','public','CREATE') THEN
        RAISE EXCEPTION 'Managed Cards temporary CREATE cleanup required' USING ERRCODE='42501';
    END IF;
END $$;
