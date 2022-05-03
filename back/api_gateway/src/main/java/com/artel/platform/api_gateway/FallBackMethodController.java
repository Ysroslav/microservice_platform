package com.artel.platform.api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    //TODO класс тестовый после реализации удалить

    @GetMapping("/artistServiceFallBack")
    public String artistServiceFallBackMethod() {
        return "Artist Service is taking longer than Expected." +
               " Please try again later";
    }

    @GetMapping("/commonServiceFallBack")
    public String commonServiceFallBackMethod() {
        return "Common Service is taking longer than Expected." +
               " Please try again later";
    }
}
