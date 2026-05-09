package vulnspa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Базовая конфигурация OpenAPI/Swagger UI.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vulnSpaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vuln SPA API")
                        .description("Документация REST API для backend-модуля")
                        .version("v1")
                        .contact(new Contact().name("Vuln SPA Team"))
                        .license(new License().name("MIT")));
    }
}
