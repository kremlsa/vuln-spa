package vulnspa.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Собственный обработчик ошибок, который возвращает JSON вместо HTML.
 * Используется для того, чтобы фронтенд получал структурированный ответ при ошибках.
 */
@RestController
public class CustomErrorController implements ErrorController {

    /**
     * Формирует JSON-ответ с описанием возникшей ошибки.
     *
     * @param request запрос, содержащий атрибуты ошибки.
     * @return карта с HTTP-статусом, текстом ошибки, сообщением и путем запроса.
     */
    @RequestMapping("/error")
    public Map<String, Object> handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = (statusCode != null) ? Integer.parseInt(statusCode.toString()) : 500;

        String errorText = HttpStatus.resolve(status) != null
                ? HttpStatus.valueOf(status).getReasonPhrase()
                : "Unknown Error";

        return Map.of(
                "status", status,
                "error", errorText,
                "message", message != null ? message : "",
                "path", uri
        );
    }
}