package vulnspa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главная точка входа Spring Boot-приложения.
 * Здесь запускается весь бекенд уязвимого демо-приложения.
 */
@SpringBootApplication
public class VulnspaApplication {

    /**
     * Запускает контекст Spring Boot.
     *
     * @param args аргументы командной строки, пробрасываются в Spring.
     */
    public static void main(String[] args) {
        SpringApplication.run(VulnspaApplication.class, args);
    }
}