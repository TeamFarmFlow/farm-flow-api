package com.example.app.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
        @Bean
        OpenAPI openAPI() {
                String jwt = "JWT";
                SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
                SecurityScheme securityScheme = new SecurityScheme().name(jwt)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT");

                Components securityComponents = new Components()
                                .addSecuritySchemes(jwt, securityScheme);

                Info info = new Info()
                                .title("FarmFlow API")
                                .description("Farm Flow API Document")
                                .version("1.0");

                return new OpenAPI()
                                .info(info)
                                .addSecurityItem(securityRequirement)
                                .components(securityComponents);
        }
}