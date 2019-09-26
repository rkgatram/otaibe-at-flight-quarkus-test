package org.otaibe.at.flight.test.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.at.flight.test.config.SpringConfig;
import org.otaibe.at.flight.test.domain.Entity001;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
public class Processor001 extends BaseProcessor {
    @Autowired
    @Qualifier(SpringConfig.ENTITY_001_SPRING)
    Entity001 entity001Spring;
}
