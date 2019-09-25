package org.otaibe.at.flight.test.config;

import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity002;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Dependent
public class QuarkusConfig {

    public static final String ENTITY_001_QUARKUS = "entity001-quarkus";

    @Produces
    @Named(ENTITY_001_QUARKUS)
    public Entity001 entity001(@Qualifier(SpringConfig.ENTITY_002_SPRING) Entity002 entity002) {
        Entity001 result = new Entity001();
        result.setEntity002(entity002);
        return result;
    }
}
