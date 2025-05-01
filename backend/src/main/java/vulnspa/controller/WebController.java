package vulnspa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Любые пути, кроме /api/**, возвращают index.html
    @GetMapping({"/", "/login", "/register", "/notes", "/notes/**"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
