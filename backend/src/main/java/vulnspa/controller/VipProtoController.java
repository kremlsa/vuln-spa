package vulnspa.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.User;
import vulnspa.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vip")
public class VipProtoController {

    private final UserRepository userRepo;

    public VipProtoController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/status")
    public Map<String, Object> getVipStatus() {
        // 1. Достаём authentication из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        // 2. Берём username и грузим сущность
        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        // 3. Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("isVip", user.isVip());
        return response;
    }
}