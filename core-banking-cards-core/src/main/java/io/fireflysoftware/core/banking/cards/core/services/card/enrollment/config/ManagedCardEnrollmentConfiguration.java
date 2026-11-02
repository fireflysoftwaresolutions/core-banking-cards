/* Copyright 2026 Firefly Software Foundation. Licensed under the Apache License, Version 2.0. */
package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.config;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.*;
import io.fireflysoftware.core.banking.cards.core.services.card.enrollment.impl.ManagedCardEnrollmentServiceImpl;
import java.time.Clock;
import java.util.UUID;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import org.fireflyframework.security.spi.SecurityContextPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/** No session/native/owner/audit-admission defaults: enabling requires separately qualified real bindings. */
@Configuration(proxyBeanMethods=false)
@ConditionalOnProperty(name="firefly.cards.managed-enrollment.enabled",havingValue="true")
public class ManagedCardEnrollmentConfiguration {
    @Bean
    @ConditionalOnMissingBean(ManagedCardEnrollmentService.class)
    ManagedCardEnrollmentService managedCardEnrollmentService(SecurityContextPort principals,ManagedCardEnrollmentAuthority authority,ManagedCardReservationAuditAdmission admission,
            @Qualifier("managedCardCommandSession") ManagedCardEnrollmentSession command,
            @Qualifier("managedCardReaderSession") ManagedCardEnrollmentSession reader) {
        var clock=Clock.systemUTC();LongSupplier nanos=System::nanoTime;Supplier<UUID> identifiers=UUID::randomUUID;
        var audit=command.reservationAudit(principals,authority,admission,clock,nanos,identifiers);
        return new ManagedCardEnrollmentServiceImpl(principals,authority,command,reader,clock,nanos,identifiers,audit);
    }
}
