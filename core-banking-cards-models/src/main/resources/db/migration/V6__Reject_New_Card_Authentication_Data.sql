-- Retain historical records without permitting new plaintext authentication data.
CREATE FUNCTION reject_new_card_authentication_data() RETURNS trigger
LANGUAGE plpgsql AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.pin IS NOT NULL OR NEW.cvv IS NOT NULL THEN
            RAISE EXCEPTION 'PIN and CVV are not accepted by card record storage'
                USING ERRCODE = '23514';
        END IF;
    ELSIF (NEW.pin IS NOT NULL AND NEW.pin IS DISTINCT FROM OLD.pin)
       OR (NEW.cvv IS NOT NULL AND NEW.cvv IS DISTINCT FROM OLD.cvv) THEN
        RAISE EXCEPTION 'PIN and CVV are not accepted by card record storage'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER card_authentication_data_insert_guard
BEFORE INSERT ON card
FOR EACH ROW EXECUTE FUNCTION reject_new_card_authentication_data();

CREATE TRIGGER card_authentication_data_update_guard
BEFORE UPDATE OF pin, cvv ON card
FOR EACH ROW EXECUTE FUNCTION reject_new_card_authentication_data();
