package org.otaibe.at.flight.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.otaibe.at.flight.test.config.JsonConfig;
import org.otaibe.at.flight.test.domain.Settings1;
import org.otaibe.at.flight.test.domain.Settings2;
import org.otaibe.at.flight.test.domain.Settings3;
import org.otaibe.at.flight.test.utils.JsonUtils;
import org.otaibe.at.flight.test.utils.MapWrapper;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class ConfigService {
    @Inject
    Vertx vertx;
    @Inject
    JsonConfig jsonConfig;
    @Inject
    MapWrapper mapWrapper;
    @Inject
    JsonUtils jsonUtils;
    @Inject
    ObjectMapper objectMapper;

    @ConfigProperty(name = "service.config.files")
    List<String> configFiles;

    private ObjectMapper yamlMapper;
    private Map<String, Object> allSettings = new HashMap<>();
    private Settings1 settings1;
    private Settings2 settings2;
    private Settings3 settings3;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);


    @PostConstruct
    public void init() {

        Flux.interval(Duration.ofMillis(1))
                .retry()
                .filter(aLong -> getJsonConfig().getIsInitialized().get())
                .next()
                .doOnNext(aLong -> {
                    yamlMapper = getJsonConfig().getYamlMapper();

                    Flux.fromIterable(getConfigFiles())
                            .flatMap(s -> readYmlMap(s)
                                    .doOnNext(map -> log.info("file {} was loaded", s))
                                    .map(map -> Tuples.of(s, map))
                            )
                            .collectMap(objects -> objects.getT1(), objects -> objects.getT2())
                            .map(map -> getConfigFiles().stream()
                                    .map(s -> map.get(s))
                                    .collect(Collectors.toList())
                            )
                            //.doOnNext(maps -> log.info("yml config list: {}", getJsonUtils().toStringLazy(maps, getObjectMapper())))
                            .map(maps -> maps
                                    .stream()
                                    .reduce(allSettings, (map, map2) -> getMapWrapper().mergeStringObjectMap(map, map2))
                            )
                            .doOnNext(map -> {
                                //log.info("yml config: {}", getJsonUtils().toStringLazy(map, getObjectMapper()));
                                settings1 = settings1(map);
                                settings2 = settings2(map);
                                settings3 = settings3(map);
                                setAllSettings(map);
                                getIsInitialized().set(true);
                            })
                            .subscribe();

                })
                .subscribe()

        ;
    }

    private Settings1 settings1(Map<String, Object> allSettings) {
        return getSettings(allSettings, Settings1.class, Settings1.ROOT_PATH);
    }

    private Settings2 settings2(Map<String, Object> allSettings) {
        return getSettings(allSettings, Settings2.class, Settings2.ROOT_PATH);
    }

    private Settings3 settings3(Map<String, Object> allSettings) {
        return getSettings(allSettings, Settings3.class, Settings3.ROOT_PATH);
    }

    private <T> T getSettings(Map<String, Object> allSettings1, Class<T> clazz, String... path) {
        return Optional.ofNullable(getMapWrapper().getObjectValue(allSettings1, path))
                .map(o -> getJsonUtils().toStringLazy(o, getObjectMapper()).toString())
                .flatMap(s -> getJsonUtils().readValue(s, clazz, getObjectMapper()))
                .orElseThrow(() -> new RuntimeException(
                        MessageFormat.format("Unable find default config! Path: {0}, Class: {1}",
                                path, clazz.getName())));
    }

    public Mono<Map<String, Object>> readYmlMap(String path) {
        return RxJava2Adapter.singleToMono(
                getVertx().fileSystem().rxReadFile(path)
        )
                .map(Buffer::getBytes)
                //.doOnNext(bytes -> log.info("fileName: {}", path))
                //.doOnNext(bytes -> log.info("file: {}", new String(bytes)))
                .map(bytes -> {
                    try {
                        return getYamlMapper().readValue(bytes, Map.class);
                    } catch (Exception e) {
                        log.error("unable to read from file:" + path, e);
                        throw new RuntimeException(e);
                    }
                });
    }

}
