package org.otaibe.at.flight.test.config;

import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity002;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Dependent
public class QuarkusConfig {

    public static final String ENTITY_001_QUARKUS = "entity001-quarkus";

    @Produces
    @Named(ENTITY_001_QUARKUS)
    public Entity001 entity001(Entity002 entity002) {
        Entity001 result = new Entity001();
        return result;
    }
}
