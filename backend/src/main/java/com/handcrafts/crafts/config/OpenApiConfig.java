package com.handcrafts.crafts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Crafts Role-Based Auth API")
                        .version("1.0")
                        .description("API documentation for multi-role login system: USER, SELLER, ADMIN")
                        .contact(new Contact()
                                .name("Arya Pawar")
                                .email("arya@example.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecuritySchemeTypeEnum.HTTP.getSwaggerType())
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token from login here")));
    }
}
