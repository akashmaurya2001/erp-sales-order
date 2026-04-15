package com.precisioncast.erp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Hidden
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Precision Cast ERP Backend is running",
                "swagger", "/swagger-ui/index.html",
                "health", "/api/health"
        );
    }
}
