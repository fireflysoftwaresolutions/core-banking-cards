/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.activation.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationPolicy;
import io.fireflysoftware.core.banking.cards.models.entities.card.CardIssuerInstrument;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import java.net.URI;
import java.time.*;
import java.util.*;
import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.fireflyframework.security.spi.PolicyEvaluationPort;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Current native customer, owner-stored issuer binding and same-response PDP evaluation. */
public final class CurrentCardActivationPolicyImpl implements CardActivationPolicy {
    public record Profile(String issuer,String platformTenant,UUID resourceTenant,String audience,Set<String> clientIds,
            String clientClaim,String actorClaim,String partyClaim,String kindClaim) {
        public Profile {
            URI trusted=URI.create(Objects.requireNonNull(issuer));
            if(!Set.of("https","http").contains(trusted.getScheme()==null?"":trusted.getScheme())||trusted.getHost()==null||trusted.getUserInfo()!=null||trusted.getQuery()!=null||trusted.getFragment()!=null)throw new IllegalArgumentException("Explicit native identity issuer required");
            for(String text:List.of(platformTenant,audience,clientClaim,actorClaim,partyClaim,kindClaim))requireText(text);
            requireId(resourceTenant);clientIds=Set.copyOf(Objects.requireNonNull(clientIds));
            if(clientIds.isEmpty())throw new IllegalArgumentException("Explicit delegating clients required");
            clientIds.forEach(Profile::requireText);
        }
        private static void requireText(String text) {if(text==null||text.isBlank()||text.length()>255||text.codePoints().anyMatch(Character::isISOControl))throw new IllegalArgumentException("Explicit native identity profile required");}
    }
    private final SecurityContextPort security;
    private final PolicyEvaluationPort policies;
    private final CardIssuerInstrumentRepository bindings;
    private final Profile profile;
    private final Clock clock;
    public CurrentCardActivationPolicyImpl(SecurityContextPort security,PolicyEvaluationPort policies,
            CardIssuerInstrumentRepository bindings,Profile profile,Clock clock) {
        this.security=Objects.requireNonNull(security);this.policies=Objects.requireNonNull(policies);
        this.bindings=Objects.requireNonNull(bindings);this.profile=Objects.requireNonNull(profile);this.clock=Objects.requireNonNull(clock);
    }
    @Override public Mono<AuthorizedScope> authorize(UUID cardId) {return Mono.defer(()->{
        requireId(cardId);Instant observed=clock.instant();
        return security.currentPrincipal().switchIfEmpty(Mono.error(denied())).flatMap(principal->{
            Identity nativeIdentity=identity(principal);Instant until=earliest(nativeIdentity.expires(),observed.plusSeconds(5));
            return bindings.findByCardId(cardId).filter(binding->matches(cardId,binding,nativeIdentity)).switchIfEmpty(Mono.error(denied()))
                .flatMap(binding->policies.evaluate(principal,"cards.activate","cards/card/"+cardId,
                    Map.of("tenantId",binding.tenantId().toString(),"platformTenantId",principal.tenantId(),
                        "cardId",cardId.toString(),"partyId",binding.partyId().toString(),"accountId",binding.accountId().toString(),
                        "actorId",nativeIdentity.actor().toString(),"bindingVersion",binding.bindingVersion(),"adapterId",binding.adapterId()))
                    .filter(evaluation->evaluation.decision().granted()&&evaluation.decision().obligations().isEmpty()&&evaluation.provenance().isPresent())
                    .switchIfEmpty(Mono.error(denied())).flatMap(evaluation->security.currentPrincipal()
                        .filter(principal::equals).switchIfEmpty(Mono.error(denied())).map(current->{
                            if(!identity(current).equals(nativeIdentity)||!clock.instant().isBefore(until)||clock.instant().isBefore(observed))throw denied();
                            return new AuthorizedScope(binding.tenantId(),nativeIdentity.actor(),binding.partyId(),binding.accountId(),until.atOffset(ZoneOffset.UTC),binding.bindingVersion());
                        })));
        }).timeout(Duration.ofSeconds(5));
    });}
    private Identity identity(SecurityPrincipal principal) {
        Instant now=clock.instant();Object expiry=principal.claims().get("exp"),notBefore=principal.claims().get("nbf");
        if(!profile.issuer().equals(principal.issuer())||principal.subject()==null||principal.subject().isBlank()
                ||!profile.platformTenant().equals(principal.tenantId())||!"HUMAN".equals(principal.claims().get(profile.kindClaim()))
                ||!profile.clientIds().contains(text(principal,profile.clientClaim()))
                ||!List.of(profile.audience()).equals(principal.claims().get("aud"))||!principal.scopes().contains("cards:activate")
                ||!(expiry instanceof Instant expires)||!(notBefore instanceof Instant starts)
                ||!now.isBefore(expires)||now.isBefore(starts)||!starts.isBefore(expires))throw denied();
        UUID actor=uuid(text(principal,profile.actorClaim())),party=uuid(text(principal,profile.partyClaim()));
        return new Identity(actor,party,expires);
    }
    private boolean matches(UUID card,CardIssuerInstrument binding,Identity nativeIdentity) {
        return card.equals(binding.cardId())&&profile.resourceTenant().equals(binding.tenantId())&&nativeIdentity.party().equals(binding.partyId())
                &&binding.accountId()!=null&&!binding.accountId().equals(new UUID(0,0))&&binding.bindingVersion()>0
                &&binding.adapterId()!=null&&!binding.adapterId().isBlank()&&binding.instrumentReference()!=null&&!binding.instrumentReference().isBlank();
    }
    private static String text(SecurityPrincipal principal,String claim) {Object value=principal.claims().get(claim);if(!(value instanceof String text)||text.isBlank()||text.length()>255||text.codePoints().anyMatch(Character::isISOControl))throw denied();return text;}
    private static UUID uuid(String text) {if(!text.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))throw denied();UUID value=UUID.fromString(text);requireId(value);return value;}
    private static void requireId(UUID id) {if(id==null||id.equals(new UUID(0,0)))throw denied();}
    private static Instant earliest(Instant a,Instant b) {return a.isBefore(b)?a:b;}
    private record Identity(UUID actor,UUID party,Instant expires) {}
    private static ResponseStatusException denied() {return new ResponseStatusException(HttpStatus.FORBIDDEN,"Current card activation authority required");}
}
