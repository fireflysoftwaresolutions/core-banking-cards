-- The original immutable reservation graph is separate from an issued instrument.
-- No PAN, issuer result, activation, owner eligibility or account fence is inferred here.
DO $$ BEGIN
    IF NOT EXISTS(SELECT FROM pg_roles WHERE rolname='cards_managed_guard') THEN
        CREATE ROLE cards_managed_guard NOLOGIN NOINHERIT NOSUPERUSER NOBYPASSRLS;
    END IF;
    IF NOT EXISTS(SELECT FROM pg_roles WHERE rolname='cards_managed_command') THEN
        CREATE ROLE cards_managed_command NOLOGIN NOSUPERUSER NOBYPASSRLS;
    END IF;
    IF NOT EXISTS(SELECT FROM pg_roles WHERE rolname='cards_managed_reader') THEN
        CREATE ROLE cards_managed_reader NOLOGIN NOSUPERUSER NOBYPASSRLS;
    END IF;
    IF EXISTS(SELECT FROM pg_roles WHERE rolname IN ('cards_managed_guard','cards_managed_command','cards_managed_reader')
        AND (rolsuper OR rolbypassrls OR rolcanlogin)) THEN
        RAISE EXCEPTION 'Dedicated non-login managed Cards roles required';
    END IF;
END $$;
CREATE SCHEMA cards_managed_private AUTHORIZATION cards_managed_guard;
REVOKE ALL ON SCHEMA cards_managed_private FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO cards_managed_guard,cards_managed_command,cards_managed_reader;

CREATE TABLE public.managed_card (
    managed_card_id UUID PRIMARY KEY, tenant_id UUID NOT NULL, party_id UUID NOT NULL, account_id UUID NOT NULL,
    contract_id UUID NOT NULL, product_id UUID NOT NULL, enrollment_id UUID NOT NULL UNIQUE,
    action_id UUID NOT NULL UNIQUE, restriction TEXT NOT NULL CHECK(restriction='RETAINED'), recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,managed_card_id),
    CHECK(recorded_at=date_trunc('milliseconds',recorded_at) AND recorded_at>'epoch'::timestamptz)
);
CREATE TABLE public.managed_card_enrollment_action (
    action_id UUID PRIMARY KEY, tenant_id UUID NOT NULL, actor_id UUID NOT NULL, party_id UUID NOT NULL, account_id UUID NOT NULL,
    contract_id UUID NOT NULL, product_id UUID NOT NULL, managed_card_id UUID NOT NULL UNIQUE, enrollment_id UUID NOT NULL UNIQUE,
    account_version BIGINT NOT NULL CHECK(account_version>=0), product_version BIGINT NOT NULL CHECK(product_version>=0),
    issuer_adapter_id TEXT NOT NULL CHECK(issuer_adapter_id ~ '^[A-Za-z][A-Za-z0-9_.-]{0,63}$'),
    processor_adapter_id TEXT NOT NULL CHECK(processor_adapter_id ~ '^[A-Za-z][A-Za-z0-9_.-]{0,63}$'),
    reason_code TEXT NOT NULL CHECK(reason_code ~ '^[A-Z][A-Z0-9_]{0,63}$'), accept_not_after_epoch_millis BIGINT NOT NULL,
    intent_digest TEXT NOT NULL CHECK(intent_digest ~ '^[0-9a-f]{64}$'), receipt_id UUID NOT NULL UNIQUE,
    account_fence_receipt_id UUID NOT NULL, operation_id UUID NOT NULL UNIQUE, event_id UUID NOT NULL UNIQUE, outbox_id UUID NOT NULL UNIQUE,
    progress_version BIGINT NOT NULL CHECK(progress_version=0), state TEXT NOT NULL CHECK(state='RESERVED'), recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,managed_card_id),
    FOREIGN KEY(tenant_id,action_id,managed_card_id) REFERENCES public.managed_card(tenant_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED,
    CHECK(recorded_at=date_trunc('milliseconds',recorded_at) AND recorded_at>'epoch'::timestamptz
        AND extract(epoch FROM recorded_at)*1000<accept_not_after_epoch_millis)
);
ALTER TABLE public.managed_card ADD FOREIGN KEY(tenant_id,action_id,managed_card_id)
    REFERENCES public.managed_card_enrollment_action(tenant_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED;
CREATE TABLE public.managed_card_provider_operation (
    operation_id UUID PRIMARY KEY, tenant_id UUID NOT NULL, action_id UUID NOT NULL UNIQUE, managed_card_id UUID NOT NULL,
    intent_digest TEXT NOT NULL CHECK(intent_digest ~ '^[0-9a-f]{64}$'), accept_not_after_epoch_millis BIGINT NOT NULL, recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,operation_id),
    FOREIGN KEY(tenant_id,action_id,managed_card_id) REFERENCES public.managed_card_enrollment_action(tenant_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED
);
CREATE TABLE public.managed_card_enrollment_event (
    event_id UUID PRIMARY KEY, tenant_id UUID NOT NULL, action_id UUID NOT NULL UNIQUE, managed_card_id UUID NOT NULL,
    operation_id UUID NOT NULL, intent_digest TEXT NOT NULL CHECK(intent_digest ~ '^[0-9a-f]{64}$'),
    kind TEXT NOT NULL CHECK(kind='RESERVATION_RECORDED'), progress_version BIGINT NOT NULL CHECK(progress_version=0), recorded_at TIMESTAMPTZ NOT NULL,
    UNIQUE(tenant_id,action_id,event_id),
    FOREIGN KEY(tenant_id,action_id,managed_card_id) REFERENCES public.managed_card_enrollment_action(tenant_id,action_id,managed_card_id) DEFERRABLE INITIALLY DEFERRED,
    FOREIGN KEY(tenant_id,action_id,operation_id) REFERENCES public.managed_card_provider_operation(tenant_id,action_id,operation_id) DEFERRABLE INITIALLY DEFERRED
);
CREATE TABLE public.managed_card_enrollment_outbox (
    outbox_id UUID PRIMARY KEY, tenant_id UUID NOT NULL, action_id UUID NOT NULL UNIQUE, operation_id UUID NOT NULL, event_id UUID NOT NULL,
    kind TEXT NOT NULL CHECK(kind='COORDINATE_INACTIVE_ENROLLMENT'), state TEXT NOT NULL CHECK(state='COORDINATOR_PENDING'),
    progress_version BIGINT NOT NULL CHECK(progress_version=0), recorded_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY(tenant_id,action_id,operation_id) REFERENCES public.managed_card_provider_operation(tenant_id,action_id,operation_id) DEFERRABLE INITIALLY DEFERRED,
    FOREIGN KEY(tenant_id,action_id,event_id) REFERENCES public.managed_card_enrollment_event(tenant_id,action_id,event_id) DEFERRABLE INITIALLY DEFERRED
);
ALTER TABLE public.managed_card_enrollment_action
    ADD FOREIGN KEY(operation_id) REFERENCES public.managed_card_provider_operation(operation_id) DEFERRABLE INITIALLY DEFERRED,
    ADD FOREIGN KEY(event_id) REFERENCES public.managed_card_enrollment_event(event_id) DEFERRABLE INITIALLY DEFERRED,
    ADD FOREIGN KEY(outbox_id) REFERENCES public.managed_card_enrollment_outbox(outbox_id) DEFERRABLE INITIALLY DEFERRED;

-- Only the fixed technical admission functions can install context. Caller-set GUCs grant nothing.
CREATE TABLE cards_managed_private.context (
    xid XID8 PRIMARY KEY, backend_pid INTEGER NOT NULL, login_name NAME NOT NULL, nonce UUID NOT NULL UNIQUE,
    tenant_id UUID NOT NULL, party_id UUID NOT NULL, account_id UUID NOT NULL, actor_id UUID NOT NULL, action_id UUID NOT NULL,
    purpose TEXT NOT NULL CHECK(purpose IN ('RESERVE','RECOVER')), issuer TEXT NOT NULL, subject TEXT NOT NULL, tenant_key TEXT NOT NULL,
    fence UUID, intent TEXT, scope_digest TEXT NOT NULL, db_now TIMESTAMPTZ NOT NULL, authority_until TIMESTAMPTZ NOT NULL, command_until TIMESTAMPTZ NOT NULL
);
ALTER TABLE cards_managed_private.context OWNER TO cards_managed_guard;
REVOKE ALL ON cards_managed_private.context FROM PUBLIC,cards_managed_command,cards_managed_reader;

CREATE FUNCTION public.cards_managed_enrollment_enter(p_nonce UUID,p_tenant UUID,p_party UUID,p_account UUID,p_actor UUID,p_action UUID,
    p_purpose TEXT,p_issuer TEXT,p_subject TEXT,p_tenant_key TEXT,p_fence UUID,p_intent TEXT,p_scope TEXT,p_authority_until TIMESTAMPTZ,p_command_until TIMESTAMPTZ)
RETURNS TABLE(context_nonce UUID,action_id UUID,purpose TEXT,scope_digest TEXT,db_now_millis BIGINT,authority_until_millis BIGINT,command_until_millis BIGINT)
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
DECLARE now_at TIMESTAMPTZ; xid_at XID8:=pg_current_xact_id();
BEGIN
    IF p_nonce IS NULL OR p_tenant IS NULL OR p_party IS NULL OR p_account IS NULL OR p_actor IS NULL OR p_action IS NULL
       OR '00000000-0000-0000-0000-000000000000'::uuid=ANY(ARRAY[p_nonce,p_tenant,p_party,p_account,p_actor,p_action])
       OR p_purpose IS NULL OR p_purpose NOT IN ('RESERVE','RECOVER')
       OR p_issuer IS NULL OR length(p_issuer) NOT BETWEEN 1 AND 2048 OR p_subject IS NULL OR length(p_subject) NOT BETWEEN 1 AND 512
       OR p_tenant_key IS NULL OR length(p_tenant_key) NOT BETWEEN 1 AND 512 OR p_scope IS NULL OR p_scope !~ '^[0-9a-f]{64}$'
       OR p_authority_until IS NULL OR p_command_until IS NULL OR p_command_until>p_authority_until
       OR EXISTS(SELECT FROM cards_managed_private.context c WHERE c.xid=xid_at)
       OR (p_purpose='RESERVE' AND (NOT pg_has_role(session_user,'cards_managed_command','MEMBER')
           OR p_fence IS NULL OR p_fence='00000000-0000-0000-0000-000000000000'::uuid OR p_intent IS NULL OR p_intent !~ '^[0-9a-f]{64}$'))
       OR (p_purpose='RECOVER' AND (NOT pg_has_role(session_user,'cards_managed_reader','MEMBER') OR p_fence IS NOT NULL OR p_intent IS NOT NULL)) THEN
        RAISE EXCEPTION 'Managed reservation context refused' USING ERRCODE='42501';
    END IF;
    PERFORM pg_advisory_xact_lock_shared(hashtextextended('cards.managed.tenant:'||p_tenant,0));
    PERFORM pg_advisory_xact_lock(hashtextextended('cards.managed.account:'||p_tenant||':'||p_account,0));
    PERFORM pg_advisory_xact_lock(hashtextextended('cards.managed.action:'||p_action,0));
    now_at:=date_trunc('milliseconds',clock_timestamp());
    IF p_command_until<=clock_timestamp() OR p_authority_until<=clock_timestamp() THEN
        RAISE EXCEPTION 'Managed reservation authority expired' USING ERRCODE='42501';
    END IF;
    INSERT INTO cards_managed_private.context VALUES(xid_at,pg_backend_pid(),session_user,p_nonce,p_tenant,p_party,p_account,p_actor,p_action,
        p_purpose,p_issuer,p_subject,p_tenant_key,p_fence,p_intent,p_scope,now_at,p_authority_until,p_command_until);
    RETURN QUERY SELECT p_nonce,p_action,p_purpose,p_scope,(extract(epoch FROM now_at)*1000)::bigint,
        floor(extract(epoch FROM p_authority_until)*1000)::bigint,floor(extract(epoch FROM p_command_until)*1000)::bigint;
END $$;

CREATE FUNCTION public.cards_managed_scope_matches(p_tenant UUID,p_party UUID,p_account UUID,p_action UUID,p_actor UUID DEFAULT NULL,p_write BOOLEAN DEFAULT false,p_recorded TIMESTAMPTZ DEFAULT NULL)
RETURNS BOOLEAN LANGUAGE sql VOLATILE SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
    SELECT EXISTS(SELECT FROM cards_managed_private.context c WHERE c.xid=pg_current_xact_id_if_assigned()
        AND c.backend_pid=pg_backend_pid() AND c.login_name=session_user
        AND c.tenant_id=p_tenant AND c.party_id=p_party AND c.account_id=p_account AND c.action_id=p_action
        AND (p_actor IS NULL OR c.actor_id=p_actor) AND c.authority_until>clock_timestamp() AND c.command_until>clock_timestamp()
        AND (NOT p_write OR (c.purpose='RESERVE' AND c.db_now=p_recorded)))
$$;
CREATE FUNCTION public.cards_managed_action_matches(p_tenant UUID,p_action UUID,p_write BOOLEAN DEFAULT false,p_recorded TIMESTAMPTZ DEFAULT NULL)
RETURNS BOOLEAN LANGUAGE sql VOLATILE SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
    SELECT EXISTS(SELECT FROM public.managed_card_enrollment_action a WHERE a.tenant_id=p_tenant AND a.action_id=p_action
        AND public.cards_managed_scope_matches(a.tenant_id,a.party_id,a.account_id,a.action_id,a.actor_id,p_write,p_recorded))
$$;
-- INSERT RETURNING must see the just-inserted mapped Card before its Action exists.
-- Retained cards always require the original Action actor as well as the account scope.
CREATE FUNCTION public.cards_managed_card_visible(p_tenant UUID,p_party UUID,p_account UUID,p_action UUID,p_recorded TIMESTAMPTZ)
RETURNS BOOLEAN LANGUAGE sql VOLATILE SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
    SELECT CASE WHEN EXISTS(SELECT FROM public.managed_card_enrollment_action a WHERE a.action_id=p_action)
        THEN public.cards_managed_action_matches(p_tenant,p_action)
        ELSE public.cards_managed_scope_matches(p_tenant,p_party,p_account,p_action,NULL,true,p_recorded) END
$$;
CREATE FUNCTION public.cards_managed_reservation_intent_matches(p_fence UUID,p_intent TEXT,p_deadline BIGINT)
RETURNS BOOLEAN LANGUAGE sql VOLATILE SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
    SELECT EXISTS(SELECT FROM cards_managed_private.context c WHERE c.xid=pg_current_xact_id_if_assigned()
        AND c.backend_pid=pg_backend_pid() AND c.login_name=session_user AND c.purpose='RESERVE'
        AND c.fence=p_fence AND c.intent=p_intent AND extract(epoch FROM c.command_until)*1000<=p_deadline)
$$;

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
CREATE POLICY managed_card_read ON public.managed_card FOR SELECT TO cards_managed_command,cards_managed_reader
    USING(public.cards_managed_card_visible(tenant_id,party_id,account_id,action_id,recorded_at));
CREATE POLICY managed_card_append ON public.managed_card FOR INSERT TO cards_managed_command
    WITH CHECK(public.cards_managed_scope_matches(tenant_id,party_id,account_id,action_id,NULL,true,recorded_at));
CREATE POLICY managed_action_read ON public.managed_card_enrollment_action FOR SELECT TO cards_managed_command,cards_managed_reader
    USING(public.cards_managed_scope_matches(tenant_id,party_id,account_id,action_id,actor_id));
CREATE POLICY managed_action_append ON public.managed_card_enrollment_action FOR INSERT TO cards_managed_command
    WITH CHECK(public.cards_managed_scope_matches(tenant_id,party_id,account_id,action_id,actor_id,true,recorded_at)
        AND public.cards_managed_reservation_intent_matches(account_fence_receipt_id,intent_digest,accept_not_after_epoch_millis));
CREATE POLICY managed_operation_read ON public.managed_card_provider_operation FOR SELECT TO cards_managed_command,cards_managed_reader
    USING(public.cards_managed_action_matches(tenant_id,action_id));
CREATE POLICY managed_operation_append ON public.managed_card_provider_operation FOR INSERT TO cards_managed_command
    WITH CHECK(public.cards_managed_action_matches(tenant_id,action_id,true,recorded_at));
CREATE POLICY managed_event_read ON public.managed_card_enrollment_event FOR SELECT TO cards_managed_command,cards_managed_reader
    USING(public.cards_managed_action_matches(tenant_id,action_id));
CREATE POLICY managed_event_append ON public.managed_card_enrollment_event FOR INSERT TO cards_managed_command
    WITH CHECK(public.cards_managed_action_matches(tenant_id,action_id,true,recorded_at));
CREATE POLICY managed_outbox_read ON public.managed_card_enrollment_outbox FOR SELECT TO cards_managed_command,cards_managed_reader
    USING(public.cards_managed_action_matches(tenant_id,action_id));
CREATE POLICY managed_outbox_append ON public.managed_card_enrollment_outbox FOR INSERT TO cards_managed_command
    WITH CHECK(public.cards_managed_action_matches(tenant_id,action_id,true,recorded_at));
CREATE POLICY managed_card_guard ON public.managed_card FOR SELECT TO cards_managed_guard USING(true);
CREATE POLICY managed_action_guard ON public.managed_card_enrollment_action FOR SELECT TO cards_managed_guard USING(true);
CREATE POLICY managed_operation_guard ON public.managed_card_provider_operation FOR SELECT TO cards_managed_guard USING(true);
CREATE POLICY managed_event_guard ON public.managed_card_enrollment_event FOR SELECT TO cards_managed_guard USING(true);
CREATE POLICY managed_outbox_guard ON public.managed_card_enrollment_outbox FOR SELECT TO cards_managed_guard USING(true);
REVOKE ALL ON public.managed_card,public.managed_card_enrollment_action,public.managed_card_provider_operation,
    public.managed_card_enrollment_event,public.managed_card_enrollment_outbox FROM PUBLIC;
GRANT SELECT,INSERT ON public.managed_card,public.managed_card_enrollment_action,public.managed_card_provider_operation,
    public.managed_card_enrollment_event,public.managed_card_enrollment_outbox TO cards_managed_command;
GRANT SELECT ON public.managed_card,public.managed_card_enrollment_action,public.managed_card_provider_operation,
    public.managed_card_enrollment_event,public.managed_card_enrollment_outbox TO cards_managed_reader,cards_managed_guard;

CREATE FUNCTION public.cards_managed_enrollment_complete(p_nonce UUID) RETURNS UUID
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
DECLARE c cards_managed_private.context%ROWTYPE;
BEGIN
    SELECT * INTO c FROM cards_managed_private.context WHERE xid=pg_current_xact_id_if_assigned()
        AND backend_pid=pg_backend_pid() AND login_name=session_user AND nonce=p_nonce;
    IF NOT FOUND OR c.authority_until<=clock_timestamp() OR c.command_until<=clock_timestamp() THEN
        RAISE EXCEPTION 'Managed reservation context unavailable' USING ERRCODE='42501';
    END IF;
    IF NOT EXISTS(SELECT FROM public.managed_card_enrollment_action a
        JOIN public.managed_card m ON m.managed_card_id=a.managed_card_id AND m.tenant_id=a.tenant_id AND m.action_id=a.action_id
        JOIN public.managed_card_provider_operation o ON o.operation_id=a.operation_id AND o.tenant_id=a.tenant_id AND o.action_id=a.action_id
        JOIN public.managed_card_enrollment_event e ON e.event_id=a.event_id AND e.tenant_id=a.tenant_id AND e.action_id=a.action_id
        JOIN public.managed_card_enrollment_outbox b ON b.outbox_id=a.outbox_id AND b.tenant_id=a.tenant_id AND b.action_id=a.action_id
        WHERE a.tenant_id=c.tenant_id AND a.party_id=c.party_id AND a.account_id=c.account_id AND a.actor_id=c.actor_id AND a.action_id=c.action_id
        AND (c.purpose='RECOVER' OR (a.intent_digest=c.intent AND a.account_fence_receipt_id=c.fence))
        AND m.party_id=a.party_id AND m.account_id=a.account_id AND m.contract_id=a.contract_id AND m.product_id=a.product_id AND m.enrollment_id=a.enrollment_id
        AND o.managed_card_id=a.managed_card_id AND e.managed_card_id=a.managed_card_id AND e.operation_id=a.operation_id
        AND b.operation_id=a.operation_id AND b.event_id=a.event_id AND o.intent_digest=a.intent_digest AND e.intent_digest=a.intent_digest
        AND o.accept_not_after_epoch_millis=a.accept_not_after_epoch_millis
        AND m.recorded_at=a.recorded_at AND o.recorded_at=a.recorded_at AND e.recorded_at=a.recorded_at AND b.recorded_at=a.recorded_at) THEN
        RAISE EXCEPTION 'Complete original managed reservation required' USING ERRCODE='23514';
    END IF;
    DELETE FROM cards_managed_private.context WHERE xid=c.xid;
    RETURN p_nonce;
END $$;
CREATE FUNCTION cards_managed_private.require_completed_context() RETURNS TRIGGER
LANGUAGE plpgsql SECURITY DEFINER SET search_path=pg_catalog,public,pg_temp AS $$
BEGIN
    IF EXISTS(SELECT FROM cards_managed_private.context WHERE xid=NEW.xid AND nonce=NEW.nonce) THEN
        RAISE EXCEPTION 'Managed reservation context was not completed' USING ERRCODE='23514';
    END IF;
    RETURN NULL;
END $$;
CREATE CONSTRAINT TRIGGER managed_context_completion AFTER INSERT ON cards_managed_private.context
    DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE FUNCTION cards_managed_private.require_completed_context();

GRANT CREATE ON SCHEMA public TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_scope_matches(UUID,UUID,UUID,UUID,UUID,BOOLEAN,TIMESTAMPTZ) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_action_matches(UUID,UUID,BOOLEAN,TIMESTAMPTZ) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_card_visible(UUID,UUID,UUID,UUID,TIMESTAMPTZ) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_reservation_intent_matches(UUID,TEXT,BIGINT) OWNER TO cards_managed_guard;
ALTER FUNCTION public.cards_managed_enrollment_complete(UUID) OWNER TO cards_managed_guard;
ALTER FUNCTION cards_managed_private.require_completed_context() OWNER TO cards_managed_guard;
REVOKE CREATE ON SCHEMA public FROM cards_managed_guard;
REVOKE ALL ON FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ),
    public.cards_managed_scope_matches(UUID,UUID,UUID,UUID,UUID,BOOLEAN,TIMESTAMPTZ),public.cards_managed_action_matches(UUID,UUID,BOOLEAN,TIMESTAMPTZ),
    public.cards_managed_card_visible(UUID,UUID,UUID,UUID,TIMESTAMPTZ),public.cards_managed_reservation_intent_matches(UUID,TEXT,BIGINT),public.cards_managed_enrollment_complete(UUID),cards_managed_private.require_completed_context() FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.cards_managed_enrollment_enter(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TEXT,TEXT,TEXT,UUID,TEXT,TEXT,TIMESTAMPTZ,TIMESTAMPTZ),
    public.cards_managed_scope_matches(UUID,UUID,UUID,UUID,UUID,BOOLEAN,TIMESTAMPTZ),public.cards_managed_action_matches(UUID,UUID,BOOLEAN,TIMESTAMPTZ),
    public.cards_managed_card_visible(UUID,UUID,UUID,UUID,TIMESTAMPTZ),public.cards_managed_reservation_intent_matches(UUID,TEXT,BIGINT),public.cards_managed_enrollment_complete(UUID) TO cards_managed_command,cards_managed_reader;
