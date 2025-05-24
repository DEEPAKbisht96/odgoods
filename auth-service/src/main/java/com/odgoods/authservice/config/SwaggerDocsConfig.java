package com.odgoods.authservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerDocsConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ODGoods Auth Microservice API")
                        .version("v1")
                        .description("This microservice handles authentication, JWT, and user profiles.")
                        .contact(new Contact()
                                .name("ODGoods Dev Team")
                                .email("support@odgoods.com")
                                .url("https://odgoods.com"))
                );
    }
}
