CREATE TABLE card_activation_dispatch (
    action_id UUID PRIMARY KEY REFERENCES card_activation_action(action_id),
    started_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp()
);

-- Claim before sending. A crash after this commit is uncertain and cannot authorize another send.
CREATE FUNCTION public.claim_card_activation_dispatch(
    p_action UUID, p_tenant UUID, p_actor UUID, p_party UUID, p_account UUID,
    p_grant_expires TIMESTAMPTZ, p_expected_binding_version BIGINT
) RETURNS BOOLEAN LANGUAGE plpgsql SET search_path=pg_catalog,public,pg_temp AS $$
DECLARE
    c card%ROWTYPE;
    b card_issuer_instrument%ROWTYPE;
    p card_activation_proof%ROWTYPE;
    a card_activation_action%ROWTYPE;
    card_key UUID;
BEGIN
    SELECT card_id INTO card_key FROM card_activation_action WHERE action_id=p_action;
    IF NOT FOUND THEN RAISE EXCEPTION 'Activation is not permitted'; END IF;
    SELECT * INTO c FROM card WHERE card_id=card_key FOR UPDATE;
    SELECT * INTO b FROM card_issuer_instrument WHERE card_id=card_key FOR UPDATE;
    SELECT * INTO p FROM card_activation_proof WHERE card_id=card_key FOR UPDATE;
    SELECT * INTO a FROM card_activation_action WHERE action_id=p_action FOR UPDATE;
    IF p_grant_expires IS NULL OR p_grant_expires<=clock_timestamp()
       OR p_expected_binding_version IS NULL OR p_expected_binding_version<=0
       OR a.tenant_id IS DISTINCT FROM p_tenant OR a.actor_id IS DISTINCT FROM p_actor
       OR b.tenant_id IS DISTINCT FROM p_tenant OR b.party_id IS DISTINCT FROM p_party
       OR b.account_id IS DISTINCT FROM p_account OR c.party_id IS DISTINCT FROM b.party_id
       OR c.account_id IS DISTINCT FROM b.account_id
       OR b.binding_version IS DISTINCT FROM p_expected_binding_version
       OR a.binding_version IS DISTINCT FROM b.binding_version
       OR p.binding_version IS DISTINCT FROM b.binding_version
       OR p.reserved_action_id IS DISTINCT FROM p_action
       OR p.proof_digest IS DISTINCT FROM a.proof_digest THEN
        RAISE EXCEPTION 'Activation is not permitted';
    END IF;
    IF EXISTS (SELECT 1 FROM card_activation_dispatch WHERE action_id=p_action)
       OR a.state<>'RESERVED' THEN RETURN false; END IF;
    IF c.card_status IS DISTINCT FROM 'DRAFT'::card_status_enum
       OR c.is_active IS DISTINCT FROM false OR c.is_locked IS DISTINCT FROM true
       OR c.activation_date IS NOT NULL OR c.pin IS NOT NULL OR c.cvv IS NOT NULL
       OR p.consumed_at IS NOT NULL OR p.expires_at<=clock_timestamp()
       OR NOT card_activation_has_valid_expiry(c.expiration_date,c.expiration_year,c.expiration_month,clock_timestamp()) THEN
        RAISE EXCEPTION 'Activation is not permitted';
    END IF;
    INSERT INTO card_activation_dispatch(action_id) VALUES(p_action);
    UPDATE card_activation_action SET state='UNCERTAIN',updated_at=clock_timestamp() WHERE action_id=p_action;
    RETURN true;
END;
$$;

REVOKE ALL ON FUNCTION public.claim_card_activation_dispatch(UUID,UUID,UUID,UUID,UUID,TIMESTAMPTZ,BIGINT) FROM PUBLIC;
