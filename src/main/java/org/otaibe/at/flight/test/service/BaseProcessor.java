package org.otaibe.at.flight.test.service;

import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@ToString
@Slf4j
public abstract class BaseProcessor {

    public static final Collection<BaseProcessor> PROCESSORS = new ConcurrentLinkedQueue();

    public void init(@Observes StartupEvent event) {
        log.info("init started: {}", this.getClass().getSimpleName());
        PROCESSORS.add(this);
    }
}
