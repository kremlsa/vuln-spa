package vulnspa.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import vulnspa.model.ErrorResponse;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(HttpServletRequest request) {
        return new ErrorResponse("Forbidden", "Access is denied", request.getRequestURI());
    }

    @ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(HttpServletRequest request) {
        return new ErrorResponse("Unauthorized", "Authentication is required", request.getRequestURI());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();


        if (uri.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Not Found\",\"message\":\"Resource not found\",\"path\":\"" + uri + "\"}");
        } else {
            request.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralError(HttpServletRequest request, Exception ex) {
        System.out.println("🔥 EXCEPTION: " + ex.getClass().getName() + " - " + ex.getMessage());
        return new ErrorResponse("Internal Server Error", ex.getMessage(), request.getRequestURI());
    }
}
