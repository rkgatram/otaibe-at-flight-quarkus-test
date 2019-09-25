package org.otaibe.at.flight.test.service;

import io.quarkus.arc.CreationalContextImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.at.flight.test.config.QuarkusConfig;
import org.otaibe.at.flight.test.config.SpringConfig;
import org.otaibe.at.flight.test.domain.Entity001;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Set;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class Service {

    @Autowired
    @Qualifier(SpringConfig.ENTITY_001_SPRING)
    Entity001 entity001Spring;

    @Dependent
    @Named(QuarkusConfig.ENTITY_001_QUARKUS)
    Entity001 entity001Quarkus;

    @Inject
    BeanManager beanManager;

    public void consume() {
        log.info("entity spring print");
        printEntity001(getEntity001Spring());
        log.info("entity quarkus print", getEntity001Quarkus());
        printEntity001(getEntity001Quarkus());
        log.info("beanManager->entity spring print");
        printEntity(SpringConfig.ENTITY_001_SPRING);
        log.info("beanManager->entity quarkus print");
        printEntity(QuarkusConfig.ENTITY_001_QUARKUS);
        log.info("beanManager->service print");
        printService();

    }

    private void printEntity001(Entity001 entity001) {
        for (int i = 0; i < 3; i++) {
            log.info("entity {}: {}", i, entity001);
        }
    }

    private void printEntity(String name) {
        Set<Bean<?>> beans = getBeanManager().getBeans(name);
        Bean<?> bean = beans.stream().findFirst().get();
        CreationalContext<?> creationalContext = getBeanManager().createCreationalContext(bean);
//        Set<InjectionPoint> injectionPoints = bean.getInjectionPoints();
        for (int i = 0; i < 3; i++) {
            Object reference = getBeanManager().getReference(
                    bean,
                    Entity001.class,
                    creationalContext);

//            injectionPoints.forEach(injectionPoint -> {
//                Object injectableReference = getBeanManager().getInjectableReference(injectionPoint, creationalContext);
//                Field field = (Field) injectionPoint.getMember();
//                try {
//                    field.setAccessible(true);
//                    field.set(reference, injectableReference);
//                } catch (Exception e) {
//                    log.error("unable to fill injectable reference", e);
//                }
//            });

            log.info("entity {}: {}", i, reference);
        }
    }

    private void printService() {
        Set<Bean<?>> beans = getBeanManager().getBeans(Service.class);
        Bean<?> bean = beans.stream().findFirst().get();
        for (int i = 0; i < 3; i++) {
            Object reference = getBeanManager().getReference(
                    bean,
                    Service.class,
                    new CreationalContextImpl<>(bean));
            log.info("service {}: {}", i, reference);
        }
    }
}
