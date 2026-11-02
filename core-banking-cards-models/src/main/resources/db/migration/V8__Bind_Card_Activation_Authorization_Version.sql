-- Bind the actual currently authorized issuer version after acquiring native row locks.
-- V7 remains unchanged; current application repositories use these explicit-version overloads.
CREATE FUNCTION public.reserve_card_activation(
    p_action UUID, p_card UUID, p_tenant UUID, p_actor UUID,
    p_party UUID, p_account UUID, p_digest TEXT, p_grant_expires TIMESTAMPTZ, p_expected_binding_version BIGINT
) RETURNS SETOF card_activation_action LANGUAGE plpgsql SET search_path=pg_catalog,public,pg_temp AS $$
DECLARE
    c card%ROWTYPE;
    b card_issuer_instrument%ROWTYPE;
    p card_activation_proof%ROWTYPE;
    a card_activation_action%ROWTYPE;
BEGIN
    SELECT * INTO c FROM card WHERE card_id = p_card FOR UPDATE;
    IF NOT FOUND THEN RAISE EXCEPTION 'Activation is not permitted'; END IF;
    SELECT * INTO b FROM card_issuer_instrument WHERE card_id = p_card FOR UPDATE;
    IF NOT FOUND THEN RAISE EXCEPTION 'Activation is not permitted'; END IF;
    SELECT * INTO p FROM card_activation_proof WHERE card_id = p_card FOR UPDATE;
    IF NOT FOUND THEN RAISE EXCEPTION 'Activation is not permitted'; END IF;
    SELECT * INTO a FROM card_activation_action WHERE action_id = p_action FOR UPDATE;
    IF p_grant_expires IS NULL OR p_grant_expires <= clock_timestamp()
       OR p_expected_binding_version IS NULL OR p_expected_binding_version <= 0
       OR b.binding_version IS DISTINCT FROM p_expected_binding_version
       OR b.tenant_id IS DISTINCT FROM p_tenant OR b.party_id IS DISTINCT FROM p_party
       OR b.account_id IS DISTINCT FROM p_account OR c.party_id IS DISTINCT FROM b.party_id
       OR c.account_id IS DISTINCT FROM b.account_id OR p_actor IS NULL THEN
        RAISE EXCEPTION 'Activation is not permitted';
    END IF;
    IF a.action_id IS NOT NULL THEN
        IF a.card_id IS DISTINCT FROM p_card OR a.tenant_id IS DISTINCT FROM p_tenant
           OR a.actor_id IS DISTINCT FROM p_actor OR a.proof_digest IS DISTINCT FROM p_digest
           OR a.binding_version IS DISTINCT FROM b.binding_version THEN
            RAISE EXCEPTION 'Activation action conflicts with an existing request';
        END IF;
        RETURN NEXT a;
        RETURN;
    END IF;
    -- clock_timestamp is evaluated only after the locks above have been acquired.
    IF c.card_status IS DISTINCT FROM 'DRAFT'::card_status_enum
       OR c.is_active IS DISTINCT FROM false OR c.is_locked IS DISTINCT FROM true
       OR c.activation_date IS NOT NULL OR c.pin IS NOT NULL OR c.cvv IS NOT NULL
       OR NOT card_activation_has_valid_expiry(c.expiration_date,c.expiration_year,c.expiration_month,clock_timestamp())
       OR p.proof_digest IS DISTINCT FROM p_digest OR p.binding_version <> b.binding_version
       OR p.expires_at <= clock_timestamp() OR p.consumed_at IS NOT NULL
       OR p.reserved_action_id IS NOT NULL THEN
        RAISE EXCEPTION 'Activation is not permitted';
    END IF;
    INSERT INTO card_activation_action(action_id,card_id,tenant_id,actor_id,binding_version,proof_digest,state)
        VALUES (p_action,p_card,p_tenant,p_actor,b.binding_version,p_digest,'RESERVED') RETURNING * INTO a;
    UPDATE card_activation_proof SET reserved_action_id = p_action WHERE card_id = p_card;
    RETURN NEXT a;
END;
$$;

CREATE FUNCTION public.finish_card_activation(
    p_action UUID, p_tenant UUID, p_actor UUID, p_party UUID, p_account UUID,
    p_grant_expires TIMESTAMPTZ, p_expected_binding_version BIGINT, p_outcome TEXT, p_receipt TEXT
) RETURNS SETOF card_activation_action LANGUAGE plpgsql SET search_path=pg_catalog,public,pg_temp AS $$
DECLARE
    c card%ROWTYPE;
    b card_issuer_instrument%ROWTYPE;
    p card_activation_proof%ROWTYPE;
    a card_activation_action%ROWTYPE;
    card_key UUID;
    activated TIMESTAMPTZ;
BEGIN
    SELECT card_id INTO card_key FROM card_activation_action WHERE action_id = p_action;
    IF NOT FOUND THEN RAISE EXCEPTION 'Activation is not permitted'; END IF;
    SELECT * INTO c FROM card WHERE card_id = card_key FOR UPDATE;
    SELECT * INTO b FROM card_issuer_instrument WHERE card_id = card_key FOR UPDATE;
    SELECT * INTO p FROM card_activation_proof WHERE card_id = card_key FOR UPDATE;
    SELECT * INTO a FROM card_activation_action WHERE action_id = p_action FOR UPDATE;
    IF p_grant_expires IS NULL OR p_grant_expires <= clock_timestamp()
       OR p_expected_binding_version IS NULL OR p_expected_binding_version <= 0
       OR b.binding_version IS DISTINCT FROM p_expected_binding_version
       OR a.tenant_id IS DISTINCT FROM p_tenant OR a.actor_id IS DISTINCT FROM p_actor
       OR b.tenant_id IS DISTINCT FROM p_tenant OR b.party_id IS DISTINCT FROM p_party
       OR b.account_id IS DISTINCT FROM p_account THEN
        RAISE EXCEPTION 'Activation is not permitted';
    END IF;
    IF a.state IN ('CONFIRMED','REJECTED','RECONCILIATION_REQUIRED') THEN RETURN NEXT a; RETURN; END IF;
    IF p_outcome NOT IN ('CONFIRMED','REJECTED','UNCERTAIN') THEN
        RAISE EXCEPTION 'Invalid issuer observation';
    END IF;
    IF p_outcome = 'CONFIRMED' THEN
        IF p_receipt IS NULL OR length(p_receipt) = 0 THEN RAISE EXCEPTION 'Invalid issuer observation'; END IF;
        IF c.card_status IS DISTINCT FROM 'DRAFT'::card_status_enum
           OR c.is_active IS DISTINCT FROM false OR c.is_locked IS DISTINCT FROM true
           OR c.activation_date IS NOT NULL OR c.pin IS NOT NULL OR c.cvv IS NOT NULL OR c.party_id IS DISTINCT FROM b.party_id
           OR c.account_id IS DISTINCT FROM b.account_id OR a.binding_version <> b.binding_version
           OR p.binding_version <> b.binding_version OR p.reserved_action_id IS DISTINCT FROM p_action
           OR p.proof_digest IS DISTINCT FROM a.proof_digest OR p.consumed_at IS NOT NULL
           OR NOT card_activation_has_valid_expiry(c.expiration_date,c.expiration_year,c.expiration_month,clock_timestamp()) THEN
            p_outcome := 'RECONCILIATION_REQUIRED';
        ELSE
            activated := clock_timestamp();
            UPDATE card_activation_proof SET consumed_at=activated WHERE card_id=card_key;
            UPDATE card_activation_action SET state='CONFIRMED',issuer_receipt=p_receipt,
                activation_time=activated,updated_at=activated WHERE action_id=p_action;
            UPDATE card SET card_status='ACTIVE',is_active=true,is_locked=false,
                activation_date=activated AT TIME ZONE 'UTC',date_updated=activated AT TIME ZONE 'UTC'
                WHERE card_id=card_key;
        END IF;
    END IF;
    UPDATE card_activation_action SET state=p_outcome,issuer_receipt=p_receipt,
        activation_time=activated,updated_at=clock_timestamp() WHERE action_id=p_action RETURNING * INTO a;
    RETURN NEXT a;
END;
$$;

REVOKE ALL ON FUNCTION public.reserve_card_activation(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TIMESTAMPTZ,BIGINT) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.finish_card_activation(UUID,UUID,UUID,UUID,UUID,TIMESTAMPTZ,BIGINT,TEXT,TEXT) FROM PUBLIC;
