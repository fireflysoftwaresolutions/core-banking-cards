/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.qualification.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/** Native HTTP admission for existing credential tests; grants no issuer or business ownership policy. */
final class CardBoundaryTestAuthority implements AutoCloseable {
    private final HttpServer server;
    private final String token;
    CardBoundaryTestAuthority() throws Exception {
        var key=new RSAKeyGenerator(2048).keyID("controlled-card-boundary").generate();
        server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
        String endpoint="http://127.0.0.1:"+server.getAddress().getPort(),issuer=endpoint+"/issuer";
        server.createContext("/jwks",exchange->{try(exchange){var bytes=new JWKSet(key.toPublicJWK()).toString().getBytes(StandardCharsets.UTF_8);exchange.getResponseHeaders().set("Content-Type","application/json");exchange.sendResponseHeaders(200,bytes.length);exchange.getResponseBody().write(bytes);}});
        server.createContext("/v1/data/cards/allow",exchange->{try(exchange){
            var input=new ObjectMapper().readTree(exchange.getRequestBody()).path("input");var subject=input.path("subject");var action=input.path("action").asText();var resource=input.path("resource").asText();
            boolean route="POST".equals(action)&&resource.equals("/api/v1/cards")
                    || Set.of("GET","PUT").contains(action)&&resource.matches("/api/v1/cards/[0-9a-f-]{36}")
                    || "POST".equals(action)&&resource.matches("/api/v1/cards/[0-9a-f-]{36}/activation")
                    || "GET".equals(action)&&resource.matches("/api/v1/cards/[0-9a-f-]{36}/activation/[0-9a-f-]{36}");
            boolean allowed=route&&"controlled-boundary-staff".equals(subject.path("subject").asText())&&issuer.equals(subject.path("issuer").asText())&&"controlled-boundary-tenant".equals(subject.path("tenantId").asText())&&subject.path("scopes").toString().contains("\"cards:boundary-test\"");
            var bytes=new ObjectMapper().writeValueAsBytes(Map.of("result",allowed));exchange.getResponseHeaders().set("Content-Type","application/json");exchange.sendResponseHeaders(200,bytes.length);exchange.getResponseBody().write(bytes);
        }});server.start();
        var claims=new JWTClaimsSet.Builder().issuer(issuer).subject("controlled-boundary-staff").audience("core-card-boundary").claim("tenant","controlled-boundary-tenant").claim("scope","cards:boundary-test").notBeforeTime(Date.from(Instant.now().minusSeconds(1))).expirationTime(Date.from(Instant.now().plusSeconds(180))).build();
        var jwt=new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyID()).build(),claims);jwt.sign(new RSASSASigner(key));token=jwt.serialize();
    }
    String bearer(){return "Bearer "+token;}
    void configure(ProcessBuilder process){
        String endpoint="http://127.0.0.1:"+server.getAddress().getPort();
        process.command().addAll(List.of("--firefly.security.resource-server.enabled=true","--firefly.security.resource-server.strict-policy=true","--firefly.security.resource-server.issuer="+endpoint+"/issuer","--firefly.security.resource-server.jwk-set-uri="+endpoint+"/jwks","--firefly.security.resource-server.audiences=core-card-boundary","--firefly.security.resource-server.tenant-claim-paths=tenant","--firefly.security.resource-server.scope-claim-paths=scope","--firefly.security.resource-server.tenant-required=true","--firefly.security.resource-server.permit-matchers=/v3/api-docs,/actuator/health","--firefly.cards.authorization.opa.enabled=true","--firefly.cards.authorization.opa.endpoint="+endpoint,"--firefly.cards.authorization.opa.decision-path=cards/allow","--firefly.cards.authorization.opa.evaluation-path=cards/evaluation","--firefly.cards.authorization.opa.allow-loopback-http=true"));
    }
    @Override public void close(){server.stop(0);}
    @Override public String toString(){return "CardBoundaryTestAuthority[redacted]";}
}
