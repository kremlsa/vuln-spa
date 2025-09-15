package vulnspa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import vulnspa.model.User;
import vulnspa.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * REST-контроллер для проверки VIP-статуса текущего пользователя.
 */
@RestController
@RequestMapping("/api/vip")
public class VipProtoController {

    private final UserRepository userRepo;

    /**
     * Создает контроллер с необходимым репозиторием пользователей.
     *
     * @param userRepo репозиторий пользователей.
     */
    public VipProtoController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Возвращает признак VIP для авторизованного пользователя.
     *
     * @return карта с именем пользователя и флагом {@code isVip}.
     */
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