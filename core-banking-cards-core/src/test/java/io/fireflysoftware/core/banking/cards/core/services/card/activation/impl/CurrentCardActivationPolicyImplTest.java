/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.fireflysoftware.core.banking.cards.models.entities.card.CardIssuerInstrument;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.fireflyframework.security.api.domain.*;
import org.fireflyframework.security.spi.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/** Controlled native-principal/PDP collaborators; not signed issuer or real ownership acceptance. */
class CurrentCardActivationPolicyImplTest {
    private static final Instant NOW=Instant.parse("2026-11-02T08:00:00Z");
    private static final UUID CARD=UUID.randomUUID(),TENANT=UUID.randomUUID(),PARTY=UUID.randomUUID(),ACTOR=UUID.randomUUID(),ACCOUNT=UUID.randomUUID();
    private final CardIssuerInstrumentRepository bindings=mock(CardIssuerInstrumentRepository.class);
    private final PolicyEvaluationPort policies=mock(PolicyEvaluationPort.class);
    private final CurrentCardActivationPolicyImpl.Profile profile=new CurrentCardActivationPolicyImpl.Profile(
            "https://identity.example.test/realm","customer-namespace",TENANT,"core-banking-cards",Set.of("domain-cards"),
            "client_id","actor_id","party_id","principal_kind");
    private SecurityPrincipal principal(Map<String,Object> changes) {
        var claims=new HashMap<String,Object>(Map.of("exp",NOW.plusSeconds(60),"nbf",NOW.minusSeconds(1),
                "aud",List.of("core-banking-cards"),"client_id","domain-cards","actor_id",ACTOR.toString(),
                "party_id",PARTY.toString(),"principal_kind","HUMAN"));claims.putAll(changes);
        return new SecurityPrincipal("opaque-customer-subject",profile.issuer(),profile.platformTenant(),Set.of(),Set.of("cards:activate"),claims,NOW.minusSeconds(60),null,Set.of(),Map.of());
    }
    private CurrentCardActivationPolicyImpl service(SecurityContextPort security) {
        when(bindings.findByCardId(CARD)).thenReturn(Mono.just(new CardIssuerInstrument(CARD,TENANT,PARTY,ACCOUNT,"owned-issuer","instrument-77",3)));
        when(policies.evaluate(any(),anyString(),anyString(),anyMap())).thenReturn(Mono.just(new PolicyEvaluation(Decision.permit(),Optional.of(new PolicyEvaluationProvenance("policy-r7","decision-1")))));
        return new CurrentCardActivationPolicyImpl(security,policies,bindings,profile,Clock.fixed(NOW,ZoneOffset.UTC));
    }
    private static void refused(Mono<?> result) {
        StepVerifier.create(result).expectErrorSatisfies(error->assertEquals(403,assertInstanceOf(ResponseStatusException.class,error).getStatusCode().value())).verify();
    }
    @Test void exactCurrentCustomerAndOwnerBindingProduceOnlyFiveSecondScope() {
        var nativePrincipal=principal(Map.of());var value=service(()->Mono.just(nativePrincipal)).authorize(CARD).block();
        assertEquals(TENANT,value.tenantId());assertEquals(ACTOR,value.actorId());assertEquals(PARTY,value.partyId());assertEquals(ACCOUNT,value.accountId());
        assertEquals(NOW.plusSeconds(5).atOffset(ZoneOffset.UTC),value.expiresAt());assertEquals(3,value.bindingVersion());
        verify(policies).evaluate(eq(nativePrincipal),eq("cards.activate"),eq("cards/card/"+CARD),argThat(context->TENANT.toString().equals(context.get("tenantId"))&&ACCOUNT.toString().equals(context.get("accountId"))&&Long.valueOf(3).equals(context.get("bindingVersion"))));
    }
    @Test void tokenExpiryIsNeverExtended() {var p=principal(Map.of("exp",NOW.plusSeconds(2)));assertEquals(NOW.plusSeconds(2).atOffset(ZoneOffset.UTC),service(()->Mono.just(p)).authorize(CARD).block().expiresAt());}
    @Test void anotherPartyIsRejectedBeforePolicy() {var s=service(()->Mono.just(principal(Map.of("party_id",UUID.randomUUID().toString()))));refused(s.authorize(CARD));verifyNoInteractions(policies);}
    @Test void wrongNativeClientIsRejectedBeforeOwnerRead() {var s=service(()->Mono.just(principal(Map.of("client_id","another-client"))));refused(s.authorize(CARD));verifyNoInteractions(bindings,policies);}
    @Test void wrongAudienceIsRejected() {refused(service(()->Mono.just(principal(Map.of("aud",List.of("domain-cards"))))).authorize(CARD));}
    @Test void futureNotBeforeIsRejected() {refused(service(()->Mono.just(principal(Map.of("nbf",NOW.plusSeconds(1))))).authorize(CARD));}
    @Test void expiredPrincipalIsRejected() {refused(service(()->Mono.just(principal(Map.of("exp",NOW)))).authorize(CARD));}
    @Test void missingNativeIdentityNeverReadsOwner() {var s=service(Mono::empty);refused(s.authorize(CARD));verifyNoInteractions(bindings,policies);}
    @Test void changedPrincipalAfterPolicyCannotReturnScope() {var calls=new AtomicInteger();var first=principal(Map.of());var changed=principal(Map.of("actor_id",UUID.randomUUID().toString()));refused(service(()->Mono.just(calls.getAndIncrement()==0?first:changed)).authorize(CARD));}
    @Test void permissionWithoutEngineProvenanceCannotAdmit() {var s=service(()->Mono.just(principal(Map.of())));when(policies.evaluate(any(),anyString(),anyString(),anyMap())).thenReturn(Mono.just(new PolicyEvaluation(Decision.permit(),Optional.empty())));refused(s.authorize(CARD));}
    @Test void missingOwnerBindingCannotAdmit() {var s=service(()->Mono.just(principal(Map.of())));when(bindings.findByCardId(CARD)).thenReturn(Mono.empty());refused(s.authorize(CARD));verifyNoInteractions(policies);}
    @Test void missingPolicyObservationCannotAdmit() {var s=service(()->Mono.just(principal(Map.of())));when(policies.evaluate(any(),anyString(),anyString(),anyMap())).thenReturn(Mono.empty());refused(s.authorize(CARD));}
}
