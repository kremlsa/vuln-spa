package vulnspa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация доступа к статическим ресурсам.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Регистрирует обработчики ресурсов для SPA.
     *
     * @param registry реестр обработчиков.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
