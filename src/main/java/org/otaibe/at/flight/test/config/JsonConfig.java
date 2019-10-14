package org.otaibe.at.flight.test.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
@Getter
@Slf4j
public class JsonConfig {

    ObjectMapper objectMapper;
    ObjectMapper yamlMapper;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);


    @Produces
    public ObjectMapper objectMapper() {
        //log.info("producing object mapper");
        objectMapper = new ObjectMapper();
        fillObjectMapper(getObjectMapper());
        getObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        //log.info("object mapper is initialized");
        return getObjectMapper();
    }

    @PostConstruct
    public void init() {
        fillObjectMapper(DatabindCodec.mapper());
        fillObjectMapper(DatabindCodec.prettyMapper());

        yamlMapper = new ObjectMapper(new YAMLFactory())
                .configure(SerializationFeature.INDENT_OUTPUT, true)
        ;

        getIsInitialized().set(true);
    }

    private void fillObjectMapper(ObjectMapper objectMapper1) {

        // perform configuration
        objectMapper1
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        ;
        objectMapper1.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
