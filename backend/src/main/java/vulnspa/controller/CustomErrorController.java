package vulnspa.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomErrorController  implements ErrorController {

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
                "error", errorText,   // ← теперь будет "Not Found" для 404
                "message", message != null ? message : "",
                "path", uri
                );
        }
    }