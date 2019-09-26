package org.otaibe.at.flight.test.config;

import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity002;
import org.otaibe.at.flight.test.domain.Entity003;
import org.otaibe.at.flight.test.domain.Entity004;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringConfig {

    public static final String ENTITY_001_SPRING = "entity001-spring";
    public static final String ENTITY_002_SPRING = "entity002-spring";
    public static final String ENTITY_004_SPRING = "entity004-spring";

    @Bean(name = ENTITY_001_SPRING)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Entity001 entity001(@Qualifier(SpringConfig.ENTITY_002_SPRING) Entity002 entity002) {
        Entity001 result = new Entity001();
        result.setEntity002(entity002);
        return result;
    }

    @Bean(name = ENTITY_002_SPRING)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Entity002 entity002() {
        return new Entity002();
    }

    @Bean(name = ENTITY_004_SPRING)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Entity004 entity004(Entity003 entity003, @Qualifier(SpringConfig.ENTITY_001_SPRING) Entity001 entity) {
        Entity004 result = new Entity004();
        result.setEntity001(entity);
        result.setEntity003(entity003);
        return result;
    }
}
