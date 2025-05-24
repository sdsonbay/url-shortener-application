package swe.urlshortener.url_shortener.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shortenerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener Service API")
                        .description("URL kısaltma servisi için REST API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("URL Shortener Team")
                                .email("info@urlshortener.com")));
    }
}