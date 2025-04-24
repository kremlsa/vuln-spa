package vulnspa.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class WafFilter implements Filter {

    private final Pattern sqlInjectionPattern = Pattern.compile(
            "('|--|;|/\\*|\\*/|\\b(SELECT|UNION|INSERT|UPDATE|DELETE|DROP|OR|AND)\\b)",
            Pattern.CASE_INSENSITIVE
    );

    private final Pattern xssPattern = Pattern.compile(
            "<script|onerror=|onload=|javascript:",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();
        String query = httpReq.getQueryString();

        // Исключаем страницу с предупреждением, чтобы не зациклиться
        if (uri != null && uri.startsWith("/static/waf_blocked.html")) {
            chain.doFilter(request, response);
            return;
        }

        // Проверка на опасные паттерны
        boolean isMalicious = (query != null && (sqlInjectionPattern.matcher(query).find() || xssPattern.matcher(query).find())) ||
                (uri != null && (sqlInjectionPattern.matcher(uri).find() || xssPattern.matcher(uri).find()));

        if (isMalicious) {
            System.out.println("[WAF] Заблокирован подозрительный запрос: " + uri + "?" + query);

            if (uri.startsWith("/api/")) {
                httpResp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResp.setContentType("application/json");
                httpResp.getWriter().write("{\"error\": \"WAF: Blocked for security reason\"}");
            } else {
                httpResp.sendRedirect("/static/waf_blocked.html");
            }
            return;
        }

        chain.doFilter(request, response);
    }
}
