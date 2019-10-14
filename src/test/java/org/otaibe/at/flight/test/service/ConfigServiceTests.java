package org.otaibe.at.flight.test.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@Getter
@Slf4j
public class ConfigServiceTests {

    @Inject
    ConfigService service;

    @Test
    public void serviceTest() {
        Flux.interval(Duration.ofMillis(1))
                .retry()
                .filter(aLong -> getService().getIsInitialized().get())
                .next()
                .block();

        assertEquals("prodKey1Value", getService().getSettings1().getKey1());
        assertEquals("commonKey2Value", getService().getSettings2().getKey2());
        assertEquals("testKey3Value", getService().getSettings3().getKey3());

    }

}
