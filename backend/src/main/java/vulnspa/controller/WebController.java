package vulnspa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    // Обрабатываем всё, кроме /api/** и файлов с расширениями (например .js, .css, .png)
    @RequestMapping(value = { "/{path:^(?!api|static|h2-console|favicon\\.ico|.*\\..*).*}" })
    public String redirect() {
        return "forward:/index.html";
    }
}
