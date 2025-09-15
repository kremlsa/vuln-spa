package vulnspa.controller;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты {@link vulnspa.controller.AuthController}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"waf.enabled=true"})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Набор тестов для сценариев с авторизованным пользователем.
     */
    @Nested
    class WhenAuthenticated {

        /**
         * Проверяет, что {@code /api/auth/me} возвращает данные пользователя.
         */
        @Test
        @WithMockUser(username = "user", roles = {"USER"})
        void testGetCurrentUserAuthenticated() throws Exception {
            mockMvc.perform(get("/api/auth/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.roles").exists());
        }
    }

    /**
     * Набор тестов для анонимных запросов.
     */
    @Nested
    class WhenNotAuthenticated {

        /**
         * Проверяет, что анонимный запрос получает 401.
         */
        @Test
        void testGetCurrentUserNotAuthenticated() throws Exception {
            mockMvc.perform(get("/api/auth/me"))
                    .andExpect(status().isUnauthorized());
        }
    }
}

/*
@Nested создаёт вложенные классы внутри тестового класса.

Это позволяет группировать связанные тесты по логическому смыслу.

Каждый вложенный класс может иметь свою аннотацию @Test, @BeforeEach, @WithMockUser и другие настройки.

При запуске тестов IDE и отчеты (например, Maven или CI) показывают структуру красивее.
 */