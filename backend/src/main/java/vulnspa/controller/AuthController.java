package vulnspa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.dto.LoginRequest;
import vulnspa.model.User;
import vulnspa.repository.UserRepository;
import vulnspa.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер аутентификации и регистрации пользователей.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;

    private final UserRepository userRepository;

    /**
     * Создает контроллер, объединяющий сервис пользователя и менеджер аутентификации.
     *
     * @param userService сервис работы с пользователями.
     * @param authManager менеджер аутентификации Spring Security.
     * @param userRepository репозиторий пользователей.
     */
    public AuthController(UserService userService, AuthenticationManager authManager,
                          UserRepository userRepository) {
        this.userService = userService;
        this.authManager = authManager;
        this.userRepository = userRepository;
    }

    /**
     * Выполняет аутентификацию пользователя и сохраняет SecurityContext в сессии.
     *
     * @param loginRequest учетные данные пользователя.
     * @param request текущий HTTP-запрос.
     * @param response HTTP-ответ, в который сохраняется контекст.
     * @return {@code 200 OK}, если вход выполнен успешно, иначе {@code 401 Unauthorized}.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );
        try {
            // 1) Аутентифицируем пользователя
            Authentication auth = authManager.authenticate(authToken);

            // 2) Кладём Authentication в новый SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(auth);
            SecurityContextHolder.setContext(securityContext);

            // 3) Гарантированно создаём HTTP-сессию
            request.getSession(true);

            // 4) Сохраняем SecurityContext в сессии и заставляем Spring выдать Set-Cookie
            new HttpSessionSecurityContextRepository()
                    .saveContext(securityContext, request, response);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    /**
     * Демонстрация небезопасного выхода: сессия не инвалидируется.
     *
     * @return редирект на страницу входа.
     */
    @GetMapping("/logout")
    public String insecureLogout() {
        return "redirect:/login";
    }

//    // ✅ Безопасный logout — инвалидирует сессию
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        request.getSession(false).invalidate();
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok().build();
//    }

    /**
     * Возвращает информацию о текущем пользователе.
     *
     * @return объект со сводными данными профиля и списком ролей.
     */
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Not authenticated"));
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String,Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("isVip", user.isVip());
        // сюда добавляем роли
        List<String> roles = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        body.put("roles", roles);

        return ResponseEntity.ok(body);
    }

    /**
     * Регистрирует нового пользователя и назначает базовую роль.
     *
     * @param user регистрируемый пользователь.
     * @return сообщение об успешной регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered");
    }
}