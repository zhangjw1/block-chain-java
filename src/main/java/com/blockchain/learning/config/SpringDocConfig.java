package com.blockchain.learning.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Java Blockchain Learning API")
                        .version("1.0.0")
                        .description("An API for interacting with a blockchain network, managing wallets, and deploying/interacting with smart contracts.")
                        .termsOfService("http://example.com/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
