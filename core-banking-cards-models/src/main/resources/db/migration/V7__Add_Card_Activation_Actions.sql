-- Existing cards are not assigned an issuer or tenant by inference.
CREATE TABLE card_issuer_instrument (
    card_id UUID PRIMARY KEY REFERENCES card(card_id),
    tenant_id UUID NOT NULL,
    party_id UUID NOT NULL,
    account_id UUID NOT NULL,
    adapter_id VARCHAR(100) NOT NULL,
    instrument_reference VARCHAR(255) NOT NULL,
    issuance_reference VARCHAR(255) NOT NULL,
    binding_version BIGINT NOT NULL DEFAULT 1 CHECK (binding_version > 0),
    issued_inactive BOOLEAN NOT NULL CHECK (issued_inactive),
    UNIQUE (tenant_id, adapter_id, instrument_reference)
);

CREATE TABLE card_activation_proof (
    card_id UUID PRIMARY KEY REFERENCES card_issuer_instrument(card_id),
    proof_digest TEXT NOT NULL CHECK (proof_digest ~ '^[0-9a-f]{64}$'),
    binding_version BIGINT NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    reserved_action_id UUID,
    consumed_at TIMESTAMPTZ
);

CREATE TABLE card_activation_action (
    action_id UUID PRIMARY KEY,
    card_id UUID NOT NULL REFERENCES card(card_id),
    tenant_id UUID NOT NULL,
    actor_id UUID NOT NULL,
    binding_version BIGINT NOT NULL,
    proof_digest TEXT NOT NULL,
    state VARCHAR(40) NOT NULL CHECK (state IN
        ('RESERVED','CONFIRMED','REJECTED','UNCERTAIN','RECONCILIATION_REQUIRED')),
    issuer_receipt VARCHAR(255),
    activation_time TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp()
);

ALTER TABLE card_activation_proof ADD CONSTRAINT card_activation_reserved_action_fk
    FOREIGN KEY (reserved_action_id) REFERENCES card_activation_action(action_id);

CREATE FUNCTION card_activation_has_valid_expiry(p_expiry TIMESTAMP, p_year INTEGER, p_month INTEGER, p_now TIMESTAMPTZ)
RETURNS BOOLEAN LANGUAGE plpgsql IMMUTABLE AS $$
BEGIN
    IF p_expiry IS NOT NULL AND p_expiry <= p_now AT TIME ZONE 'UTC' THEN RETURN false; END IF;
    IF p_year IS NULL AND p_month IS NULL THEN RETURN p_expiry IS NOT NULL; END IF;
    IF p_year IS NULL OR p_month IS NULL OR p_year NOT BETWEEN 1 AND 9998 OR p_month NOT BETWEEN 1 AND 12 THEN
        RETURN false;
    END IF;
    IF make_date(p_year,p_month,1)+interval '1 month' <= p_now AT TIME ZONE 'UTC' THEN RETURN false; END IF;
    IF p_expiry IS NOT NULL AND (extract(year FROM p_expiry) <> p_year OR extract(month FROM p_expiry) <> p_month) THEN
        RETURN false;
    END IF;
    RETURN true;
END;
$$;

CREATE FUNCTION reserve_card_activation(
    p_action UUID, p_card UUID, p_tenant UUID, p_actor UUID,
    p_party UUID, p_account UUID, p_digest TEXT, p_grant_expires TIMESTAMPTZ
) RETURNS SETOF card_activation_action LANGUAGE plpgsql AS $$
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

CREATE FUNCTION finish_card_activation(
    p_action UUID, p_tenant UUID, p_actor UUID, p_party UUID, p_account UUID,
    p_grant_expires TIMESTAMPTZ, p_outcome TEXT, p_receipt TEXT
) RETURNS SETOF card_activation_action LANGUAGE plpgsql AS $$
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

-- These functions are internal persistence operations, not public database APIs.
REVOKE ALL ON FUNCTION reserve_card_activation(UUID,UUID,UUID,UUID,UUID,UUID,TEXT,TIMESTAMPTZ) FROM PUBLIC;
REVOKE ALL ON FUNCTION finish_card_activation(UUID,UUID,UUID,UUID,UUID,TIMESTAMPTZ,TEXT,TEXT) FROM PUBLIC;

-- Enforce the same restriction against stale read/replace writes, including native writes.
CREATE FUNCTION protect_card_activation_boundary() RETURNS trigger LANGUAGE plpgsql
SECURITY DEFINER SET search_path = pg_catalog, public, pg_temp AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF (NEW.card_status IS NOT NULL AND NEW.card_status <> 'DRAFT') OR NEW.activation_date IS NOT NULL THEN
            RAISE EXCEPTION 'New card records must be inactive drafts';
        END IF;
        NEW.card_status := 'DRAFT';
        NEW.is_active := false;
        NEW.is_locked := true;
        RETURN NEW;
    END IF;
    IF OLD.activation_date IS NOT NULL AND NEW.activation_date IS DISTINCT FROM OLD.activation_date THEN
        RAISE EXCEPTION 'Committed activation history cannot be changed';
    END IF;
    IF EXISTS (SELECT 1 FROM card_issuer_instrument b WHERE b.card_id=OLD.card_id
               AND (b.party_id IS DISTINCT FROM NEW.party_id OR b.account_id IS DISTINCT FROM NEW.account_id)) THEN
        RAISE EXCEPTION 'Issuer-bound ownership cannot be changed';
    END IF;
    IF (NEW.card_status='ACTIVE' AND OLD.card_status IS DISTINCT FROM 'ACTIVE'::card_status_enum)
       OR (NEW.is_active IS true AND OLD.is_active IS DISTINCT FROM true)
       OR (NEW.is_locked IS false AND OLD.is_locked IS true)
       OR NEW.activation_date IS DISTINCT FROM OLD.activation_date THEN
        IF OLD.card_status IS DISTINCT FROM 'DRAFT'::card_status_enum OR OLD.activation_date IS NOT NULL
           OR NEW.card_status IS DISTINCT FROM 'ACTIVE'::card_status_enum OR NEW.activation_date IS NULL
           OR NOT EXISTS (
                SELECT 1 FROM card_activation_action a
                JOIN card_activation_proof p ON p.card_id=a.card_id AND p.reserved_action_id=a.action_id
                JOIN card_issuer_instrument b ON b.card_id=a.card_id AND b.binding_version=a.binding_version
                WHERE a.card_id=OLD.card_id AND a.state='CONFIRMED'
                  AND a.tenant_id=b.tenant_id AND a.activation_time=p.consumed_at
                  AND a.activation_time AT TIME ZONE 'UTC'=NEW.activation_date) THEN
            RAISE EXCEPTION 'Card activation and restriction removal require a verified lifecycle action';
        END IF;
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER card_activation_boundary_guard BEFORE INSERT OR UPDATE ON card
FOR EACH ROW EXECUTE FUNCTION protect_card_activation_boundary();

REVOKE ALL ON FUNCTION protect_card_activation_boundary() FROM PUBLIC;
