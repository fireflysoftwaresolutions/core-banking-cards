package io.fireflysoftware.core.banking.cards.core.services.card.enrollment.store;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
class ManagedCardEnrollmentWiringTest {
    static final String PREFIX="io.fireflysoftware.core.banking.cards.core.services.card.enrollment.";
    Class<?> type(String name) { return assertDoesNotThrow(()->Class.forName(PREFIX+name)); }
    @Test void functionalServiceHasAnInterfaceAndImplPackage() {
        Class<?> api=type("ManagedCardEnrollmentService");assertTrue(api.isInterface());assertTrue(api.isAssignableFrom(type("impl.ManagedCardEnrollmentServiceImpl")));
    }
    @Test void disabledFeatureHasNoServiceAndNeedsNoAuthorityOrDatabaseBean() {
        new ApplicationContextRunner().withUserConfiguration(type("config.ManagedCardEnrollmentConfiguration")).run(context->{assertNull(context.getStartupFailure());assertEquals(0,context.getBeanNamesForType(type("ManagedCardEnrollmentService")).length);});
    }
    @Test void enablingWithoutRealBindingsCannotManufactureAnAvailableService() {
        new ApplicationContextRunner().withUserConfiguration(type("config.ManagedCardEnrollmentConfiguration")).withPropertyValues("firefly.cards.managed-enrollment.enabled=true").run(context->assertNotNull(context.getStartupFailure()));
    }
}
