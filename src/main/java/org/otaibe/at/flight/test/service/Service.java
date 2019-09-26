package org.otaibe.at.flight.test.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.at.flight.test.config.QuarkusConfig;
import org.otaibe.at.flight.test.config.SpringConfig;
import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity003;
import org.otaibe.at.flight.test.utils.BeanManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@org.springframework.stereotype.Service
@Getter
@Setter
@Slf4j
@ToString(callSuper = true)
public class Service {

    @Autowired
    @Qualifier(SpringConfig.ENTITY_001_SPRING)
    Entity001 entity001Spring;

    @Dependent
    @Named(QuarkusConfig.ENTITY_001_QUARKUS)
    Entity001 entity001Quarkus;

    @Autowired
    Entity003 entity003;

    @Inject
    BeanManager beanManager;
    @Inject
    BeanManagerUtils beanManagerUtils;

    Collection<BaseProcessor> processors = new ArrayList<>();

    @PostConstruct
    public void init() {
        processors = BaseProcessor.PROCESSORS;
    }

    public void consume() {
        log.info("entity spring print");
        printEntity001(getEntity001Spring());
        log.info("entity quarkus print", getEntity001Quarkus());
        printEntity001(getEntity001Quarkus());
        log.info("beanManager->entity spring print");
        printEntity(SpringConfig.ENTITY_001_SPRING, Entity001.class);
        log.info("beanManager->entity quarkus print");
        printEntity(QuarkusConfig.ENTITY_001_QUARKUS, Entity001.class);
        log.info("beanManager->entity003 print");
        printEntity(Entity003.ENTITY_003, Entity003.class);
        log.info("beanManager->service print");
        printService();

    }

    private void printEntity001(Entity001 entity001) {
        for (int i = 0; i < 3; i++) {
            log.info("entity {}: {}", i, entity001);
        }
    }

    private <T> void printEntity(String name, Class<T> clazz) {
        for (int i = 0; i < 3; i++) {
            Object reference = StringUtils.hasLength(name) ?
                    getBeanManagerUtils().createBean(getBeanManager(), name, clazz) : getBeanManagerUtils().createBean(getBeanManager(), clazz);
            log.info("entity {}: {}", i, reference);
        }
    }

    private void printService() {
        for (int i = 0; i < 3; i++) {
            Object reference = getBeanManagerUtils().createBean(getBeanManager(), Service.class);
            log.info("service {}: {}", i, reference);
        }
    }


}
