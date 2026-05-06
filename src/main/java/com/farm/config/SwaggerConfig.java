package com.farm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI farmerManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Farmer Management System API")
                .description("REST API for managing farmers — crop types, land records, state-wise data")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Your Name")
                    .email("your@email.com")));
    }
}