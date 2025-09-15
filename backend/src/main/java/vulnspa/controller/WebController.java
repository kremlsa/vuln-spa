package vulnspa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер, который перенаправляет все неизвестные пути на SPA-фронтенд.
 */
@Controller
public class WebController {

    /**
     * Возвращает индексный файл для маршрутов, которые должны обрабатываться фронтендом.
     *
     * @return строка для форварда на {@code index.html}.
     */
    @RequestMapping(value = {"/{path:^(?!api|static|h2-console|favicon\\.ico|.*\\..*).*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
