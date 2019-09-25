package org.otaibe.at.flight.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity001 {
    String prop001 = UUID.randomUUID().toString();
    String prop002 = UUID.randomUUID().toString();

    @Autowired
    Entity002 entity002;
}
