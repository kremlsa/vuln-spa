package vulnspa.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.User;
import vulnspa.repository.UserRepository;
import vulnspa.security.Md5PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * REST-контроллер для работы с профилем текущего пользователя.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepo;
    private final Md5PasswordEncoder passwordEncoder;

    /**
     * Создает контроллер для работы с профилями.
     *
     * @param userRepo репозиторий пользователей.
     * @param passwordEncoder небезопасный демонстрационный кодировщик паролей.
     */
    public ProfileController(UserRepository userRepo, Md5PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Возвращает основные данные о текущем пользователе.
     *
     * @return карта с логином, статусом и VIP-флагом пользователя.
     */
    @GetMapping
    public Map<String, Object> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("enabled", user.isEnabled());
        result.put("isVip", user.isVip());
        return result;
    }

    /**
     * Обновляет пароль текущего пользователя после проверки старого пароля.
     *
     * @param body карта со старым и новым паролем.
     * @return код {@code 200 OK}, если пароль обновлён, либо описание ошибки.
     */
    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Старый пароль неверен");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseEntity.ok().build();
    }
}
