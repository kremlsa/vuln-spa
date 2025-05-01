package vulnspa.service;// src/main/java/.../service/UserService.java
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.User;
import vulnspa.repository.UserRepository;
import vulnspa.security.Md5PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepo;
    private Md5PasswordEncoder passwordEncoder;
    private JdbcTemplate jdbc;

    public UserService(UserRepository userRepo,
                       Md5PasswordEncoder passwordEncoder,
                       JdbcTemplate jdbc) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jdbc        = jdbc;
    }

    public void register(User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists"
            );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        // Вставляем роль USER
        jdbc.update(
                "INSERT INTO AUTHORITIES(username, authority) VALUES (?, ?)",
                user.getUsername(), "ROLE_USER"
        );
    }

    public Map<String, Object> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("roles", auth.getAuthorities());
        result.put("isVip", user.isVip());
        return result;
    }
}
