/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation;

/** An absent external adapter has not made an issuer request. */
public final class CardIssuerUnavailableException extends RuntimeException {
    public enum Execution { NOT_EXECUTED }
    public CardIssuerUnavailableException() { super("Card issuer NOT_CONFIGURED: NOT_EXECUTED"); }
    public CardIssuerPort.Configuration configuration() { return CardIssuerPort.Configuration.NOT_CONFIGURED; }
    public Execution execution() { return Execution.NOT_EXECUTED; }
}
