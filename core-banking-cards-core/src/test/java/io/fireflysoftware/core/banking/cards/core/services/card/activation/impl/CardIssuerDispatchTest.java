/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Unit boundaries only: configured observations below are not issuer-effect acceptance. */
class CardIssuerDispatchTest {
    static final UUID CARD=new UUID(0,1), ACTION=new UUID(0,2), TENANT=new UUID(0,3),
            ACTOR=new UUID(0,4), PARTY=new UUID(0,5), ACCOUNT=new UUID(0,6);
    final CardActivationStoreImpl store=mock(CardActivationStoreImpl.class);
    final CardIssuerPort issuer=mock(CardIssuerPort.class);
    final CardActivationPolicy.AuthorizedScope scope=new CardActivationPolicy.AuthorizedScope(
            TENANT,ACTOR,PARTY,ACCOUNT,OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(30),1);
    final CardIssuerPort.Binding binding=new CardIssuerPort.Binding(CARD,TENANT,PARTY,ACCOUNT,"configured-test","instrument",1);
    final CardActivationReceipt reserved=new CardActivationReceipt(ACTION,CARD,CardActivationReceipt.State.RESERVED,null);
    final CardActivationReceipt uncertain=new CardActivationReceipt(ACTION,CARD,CardActivationReceipt.State.UNCERTAIN,null);
    final CardActivationReceipt confirmed=new CardActivationReceipt(ACTION,CARD,CardActivationReceipt.State.CONFIRMED,OffsetDateTime.now());
    CardActivationRequest request(){var r=new CardActivationRequest();r.setActivationProof("a".repeat(43));return r;}
    CardActivationServiceImpl service(CardIssuerPort port){return new CardActivationServiceImpl(store,Optional.of(card->Mono.just(scope)),Optional.of(port));}
    void configured(){
        when(issuer.configuration()).thenReturn(CardIssuerPort.Configuration.CONFIGURED);
        when(store.reserve(eq(ACTION),eq(CARD),eq(scope),anyString())).thenReturn(Mono.just(reserved));
        when(store.binding(CARD,1)).thenReturn(Mono.just(binding));
        when(store.claimDispatch(ACTION,scope)).thenReturn(Mono.just(true));
        when(store.finish(ACTION,scope,"CONFIRMED","unit-observation")).thenReturn(Mono.just(confirmed));
        when(store.finish(ACTION,scope,"UNCERTAIN",null)).thenReturn(Mono.just(uncertain));
    }
    CardIssuerPort.Observation activated(){return new CardIssuerPort.Observation(ACTION,TENANT,"configured-test","instrument",1,CardIssuerPort.Outcome.ACTIVATED,"unit-observation");}

    @Test void noOpExplicitlyReportsNotConfiguredAndNeverExecutes(){
        var port=new NoOpCardIssuerPortImpl();
        assertEquals(CardIssuerPort.Configuration.NOT_CONFIGURED,port.configuration());
        for(var call:List.of(port.initiateActivation(binding,ACTION,scope.expiresAt().toInstant()),port.observeActivation(binding,ACTION))){
            var error=assertThrows(CardIssuerUnavailableException.class,call::block);
            assertEquals(CardIssuerPort.Configuration.NOT_CONFIGURED,error.configuration());
            assertEquals(CardIssuerUnavailableException.Execution.NOT_EXECUTED,error.execution());
        }
    }
    @Test void noOpCannotReserveProofOrPersistAnAction(){
        var error=assertThrows(ResponseStatusException.class,()->service(new NoOpCardIssuerPortImpl()).activate(CARD,ACTION,request()).block());
        assertEquals(503,error.getStatusCode().value());assertTrue(error.getReason().contains("NOT_EXECUTED"));verifyNoInteractions(store);
    }
    @Test void durableClaimPrecedesTheSingleInitiation(){
        configured();when(issuer.initiateActivation(eq(binding),eq(ACTION),any())).thenReturn(Mono.just(activated()));
        assertEquals(confirmed,service(issuer).activate(CARD,ACTION,request()).block());
        var order=inOrder(store,issuer);order.verify(store).claimDispatch(ACTION,scope);order.verify(issuer).initiateActivation(eq(binding),eq(ACTION),any());
        verify(issuer,never()).observeActivation(any(),any());
    }
    @Test void losingTheDurableClaimOnlyObservesTheOriginalAction(){
        configured();when(store.claimDispatch(ACTION,scope)).thenReturn(Mono.just(false));when(issuer.observeActivation(binding,ACTION)).thenReturn(Mono.just(activated()));
        assertEquals(confirmed,service(issuer).activate(CARD,ACTION,request()).block());verify(issuer,never()).initiateActivation(any(),any(),any());
    }
    @Test void ambiguousInitiationNeverRetriesOrClaimsActivation(){
        configured();when(issuer.initiateActivation(eq(binding),eq(ACTION),any())).thenReturn(Mono.error(new IllegalStateException("connection lost")));
        assertEquals(uncertain,service(issuer).activate(CARD,ACTION,request()).block());verify(issuer,times(1)).initiateActivation(eq(binding),eq(ACTION),any());verify(issuer,never()).observeActivation(any(),any());
    }
    @Test void mismatchedIssuerObservationCannotConfirmTheAction(){
        configured();var other=new CardIssuerPort.Observation(new UUID(0,99),TENANT,"configured-test","instrument",1,CardIssuerPort.Outcome.ACTIVATED,"unit-observation");
        when(issuer.initiateActivation(eq(binding),eq(ACTION),any())).thenReturn(Mono.just(other));assertEquals(uncertain,service(issuer).activate(CARD,ACTION,request()).block());
    }
    @Test void revokedAuthorityAfterIssuerCallCannotFinalize(){
        configured();when(issuer.initiateActivation(eq(binding),eq(ACTION),any())).thenReturn(Mono.just(activated()));var calls=new AtomicInteger();
        CardActivationPolicy policy=card->calls.incrementAndGet()<3?Mono.just(scope):Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN));
        var service=new CardActivationServiceImpl(store,Optional.of(policy),Optional.of(issuer));assertThrows(ResponseStatusException.class,()->service.activate(CARD,ACTION,request()).block());
        verify(store,never()).finish(any(),any(),anyString(),any());
    }
    @Test void receiptRecoveryNeverInitiatesObservesOrConsumesProof(){
        when(store.currentReceipt(ACTION,CARD,scope)).thenReturn(Mono.just(uncertain));assertEquals(uncertain,service(issuer).recover(CARD,ACTION).block());
        verifyNoInteractions(issuer);verify(store).currentReceipt(ACTION,CARD,scope);verifyNoMoreInteractions(store);
    }
}
