package com.precisioncast.erp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://nonprovided-remotely-harold.ngrok-free.dev");
        server.setDescription("Ngrok HTTPS Server");
        return new OpenAPI()
                .info(new Info()
                        .title("Precision Cast ERP API | Total APIs 44")
                        .version("1.0")
                        .description("ERP Backend APIs for Auth, Sales Order, Delivery Challan and Sales Invoice"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))

                .servers(List.of(server));

    }
}
