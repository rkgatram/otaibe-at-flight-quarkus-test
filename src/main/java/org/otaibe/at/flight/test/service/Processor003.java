package org.otaibe.at.flight.test.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.at.flight.test.config.QuarkusConfig;
import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity003;
import org.otaibe.at.flight.test.utils.BeanManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

@Service
@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
public class Processor003 extends BaseProcessor {

    @Dependent
    @Named(QuarkusConfig.ENTITY_001_QUARKUS)
    Entity001 entity001Quarkus;

    @Autowired
    Entity003 entity003;

    @Inject
    BeanManager beanManager;
    @Inject
    BeanManagerUtils beanManagerUtils;
}
