package org.otaibe.at.flight.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component(Entity003.ENTITY_003)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity003 {
    public static final String ENTITY_003 = "entity003";
    String prop001 = UUID.randomUUID().toString();
    String prop002 = UUID.randomUUID().toString();

    @PostConstruct
    public void init() {
        setProp002("init-" + getProp002());
    }
}
