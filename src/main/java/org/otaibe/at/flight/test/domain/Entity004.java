package org.otaibe.at.flight.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity004 {
    String prop001 = UUID.randomUUID().toString();
    String prop002 = UUID.randomUUID().toString();

    Entity001 entity001;
    Entity003 entity003;
}
