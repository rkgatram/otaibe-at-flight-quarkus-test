package org.otaibe.at.flight.test.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
@Getter
@Slf4j
public class ServiceTests {

    @Inject
    Service service;

    @Test
    public void serviceTest() {
        log.info("service consume start");
        getService().consume();
        log.info("service consume end");
    }
}
