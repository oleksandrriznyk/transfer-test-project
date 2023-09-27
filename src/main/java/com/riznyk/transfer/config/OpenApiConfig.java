package com.riznyk.transfer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("account")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI transferMoneySystemApi() {
        return new OpenAPI()
                .info(new Info().title("Transfer money system")
                        .description("Use this system to transfer money to your friends")
                        .version("v0.0.1"));
    }

}
