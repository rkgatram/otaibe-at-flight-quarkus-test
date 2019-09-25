package org.otaibe.at.flight.test.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/uapi")
public class UApiController {

    @GetMapping
    public String hello() {
        return "hello";
    }
}