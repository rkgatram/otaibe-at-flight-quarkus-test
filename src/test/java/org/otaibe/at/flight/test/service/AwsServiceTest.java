package org.otaibe.at.flight.test.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.UUID;

@QuarkusTest
@Getter
@Slf4j
public class AwsServiceTest {

    public static final String PROVIDER_AVAIL_CACHE_FLIGHT = "/provider/avail/cache/flight/";
    @Inject
    AwsService service;

    @Test
    public void readWriteTest() {
        String fileName = UUID.randomUUID().toString();
        String text = UUID.randomUUID().toString();
        String key = PROVIDER_AVAIL_CACHE_FLIGHT + fileName;
        log.info("key: {}, text: {}", key, text);
        getService().write(key, text)
                .map(aBoolean -> Assert.assertTrue(aBoolean))
                .then(getService().read(key))
                .doOnNext(s -> log.info("read result key: {}, text: {}", key, s))
                .map(s -> StringUtils.equals(text, s))
                .map(aBoolean -> Assert.assertTrue(aBoolean))
                .block();
        String notExistingKey = PROVIDER_AVAIL_CACHE_FLIGHT + UUID.randomUUID().toString();
        getService().read(notExistingKey)
                .flatMap(s -> Mono.<Boolean>error(new RuntimeException("should be empty")))
                .switchIfEmpty(Mono.just(Boolean.TRUE))
                .map(aBoolean -> Assert.assertTrue(aBoolean))
                .doOnSubscribe(subscription -> log.info("will try to read from not existing key: {}", notExistingKey))
                .doOnNext(aBoolean -> log.info("not found result: {}", aBoolean))
                .block();
    }
}
