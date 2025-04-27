package vulnspa.security;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"waf.enabled=true"})
class AuthenticationAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoginWithUser() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testLoginWithAdmin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testLoginWithNonexistentUser() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "nonexistent")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Nested
    class WhenNotAuthenticated {

        @Test
        void testGetNotesWithoutAuthentication() throws Exception {
            mockMvc.perform(get("/api/notes"))
                    .andExpect(status().isOk()); // 200
        }

        @Test
        void testCreateNoteWithoutAuthentication() throws Exception {
            String json = "{\"content\": \"test note\"}";

            mockMvc.perform(post("/api/notes")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testDeleteNoteWithoutAuthentication() throws Exception {
            mockMvc.perform(delete("/api/notes/1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class WhenAuthenticated {

        @Test
        @WithMockUser(username = "testuser")
        void testGetNotesWithAuthentication() throws Exception {
            mockMvc.perform(get("/api/notes"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "testuser")
        void testCreateNoteWithAuthentication() throws Exception {
            String json = "{\"content\": \"test note\"}";

            mockMvc.perform(post("/api/notes")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "testuser")
        void testDeleteNoteWithAuthentication() throws Exception {
            mockMvc.perform(delete("/api/notes/1"))
                    .andExpect(status().isOk());
        }
    }
}