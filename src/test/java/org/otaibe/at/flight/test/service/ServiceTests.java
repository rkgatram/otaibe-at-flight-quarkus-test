package org.otaibe.at.flight.test.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@Getter
@Slf4j
public class ServiceTests {

    @Inject
    Service service;

    @Test
    public void serviceTest() {
        assertTrue(getService().getProcessors().size() > 0);

        log.info("service consume start");
        getService().consume();
        log.info("service consume end");

        log.info("will assure that the prototype(dependent) beans loaded in ApplicationScoped service are not changed");
        assertEquals(getService().getEntity001Spring(), getService().getEntity001Spring());
        assertEquals(getService().getEntity001Quarkus(), getService().getEntity001Quarkus());
        log.info("assured");

    }

}
