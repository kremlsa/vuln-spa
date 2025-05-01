package vulnspa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;

    private final UserRepository userRepository;

    public AuthController(UserService userService, AuthenticationManager authManager,
                          UserRepository userRepository) {
        this.userService = userService;
        this.authManager = authManager;
        this.userRepository = userRepository;
    }

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // make sure it's not anonymous
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getName());
        if (auth == null
                || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(); // or handle however you like

        Map<String,Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("roles", auth.getAuthorities());
        body.put("isVip", user.isVip());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered");
    }
}