package org.otaibe.at.flight.test.utils;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.otaibe.at.flight.test.config.QuarkusConfig;
import org.otaibe.at.flight.test.config.SpringConfig;
import org.otaibe.at.flight.test.domain.Entity001;
import org.otaibe.at.flight.test.domain.Entity004;
import org.otaibe.at.flight.test.service.BaseProcessor;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Getter
@Slf4j
public class BeanManagerUtilsTests {

    @Inject
    BeanManager beanManager;
    @Inject
    BeanManagerUtils beanManagerUtils;

    @Test
    public void beanManagerTest() throws InterruptedException {

        Entity004 entity004 = getBeanManagerUtils()
                .createBean(getBeanManager(), SpringConfig.ENTITY_004_SPRING, Entity004.class);
        log.info("entity004: {}", entity004);
        assertNotNull(entity004);

        //RACE CONDITION THERE - so will have to put the delay in order to prevent it
        Thread.sleep(300);
        List<BaseProcessor> baseProcessorList = getBeanManagerUtils().getReferences(getBeanManager(), BaseProcessor.class);
        log.info("BaseProcessor List: {}", baseProcessorList);
        assertEquals(3, baseProcessorList.size());

        log.info("will assure that the prototype(dependent) beans loaded through BeanManager are always different");
        assertFalse(
                getBeanManagerUtils()
                        .createBean(getBeanManager(), SpringConfig.ENTITY_001_SPRING, Entity001.class)
                        .equals(
                                getBeanManagerUtils()
                                        .createBean(getBeanManager(), SpringConfig.ENTITY_001_SPRING, Entity001.class)
                        )
        );
        assertFalse(
                getBeanManagerUtils()
                        .createBean(getBeanManager(), QuarkusConfig.ENTITY_001_QUARKUS, Entity001.class)
                        .equals(
                                getBeanManagerUtils()
                                        .createBean(getBeanManager(), QuarkusConfig.ENTITY_001_QUARKUS, Entity001.class)
                        )
        );
        log.info("assured");
    }

}
