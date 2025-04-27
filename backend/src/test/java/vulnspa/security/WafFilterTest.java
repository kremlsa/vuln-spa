package vulnspa.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"waf.enabled=true"})
class WafFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser")
    void testBlockXssInPostBody() throws Exception {
        String maliciousJson = "{\"content\":\"<script>alert('xss')</script>\"}";

        mockMvc.perform(post("/api/notes")
                        .contentType("application/json")
                        .content(maliciousJson))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("WAF: Blocked for security reason"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testAllowCleanRequest() throws Exception {
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testBlockXssInUri() throws Exception {
        mockMvc.perform(get("/api/notes/<script>alert(1)</script>"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("WAF: Blocked for security reason"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testBlockSqlInjectionInQuery() throws Exception {
        mockMvc.perform(get("/api/notes?q=SELECT * FROM users"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("WAF: Blocked for security reason"));
    }

}