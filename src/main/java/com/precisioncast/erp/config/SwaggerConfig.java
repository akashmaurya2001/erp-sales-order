package com.precisioncast.erp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Precision Cast ERP API")
                        .version("1.0")
                        .description("ERP Backend APIs for Auth, Sales Order, Delivery Challan and Sales Invoice"));


    }
}
