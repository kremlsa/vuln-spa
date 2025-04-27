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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"waf.enabled=true"})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class WhenAuthenticated {

        @Test
        @WithMockUser(username = "user", roles = {"USER"})
        void testGetCurrentUserAuthenticated() throws Exception {
            mockMvc.perform(get("/api/auth/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.roles").exists());
        }
    }

    @Nested
    class WhenNotAuthenticated {

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