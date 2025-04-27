package vulnspa.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class WafFilter implements Filter {

    @Value("${waf.enabled:true}")
    private boolean wafEnabled;

    private final Pattern sqlInjectionPattern = Pattern.compile(
            "('|--|;|/\\*|\\*/|\\b(SELECT|UNION|INSERT|UPDATE|DELETE|DROP|OR|AND)\\b)",
            Pattern.CASE_INSENSITIVE
    );

    private final Pattern xssPattern = Pattern.compile(
            "(?i)(<script.*?>|</script>|onerror\\s*=|onload\\s*=|javascript:|alert\\()"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!wafEnabled) {
            chain.doFilter(request, response);
            return;
        }

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();

        // Пропустить страницу блокировки
        if (uri != null && uri.startsWith("/static/waf_blocked.html")) {
            chain.doFilter(request, response);
            return;
        }

        // Оборачиваем запрос чтобы можно было читать тело
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpReq);
        System.out.println("[WAF] Incoming request method: " + httpReq.getMethod());
        System.out.println("[WAF] Incoming request URI: " + httpReq.getRequestURI());
        System.out.println("[WAF] Incoming request content-type: " + httpReq.getContentType());
        System.out.println("[WAF] Incoming request content-length: " + httpReq.getContentLength());

        if (isMalicious(cachedRequest)) {
            System.out.println("[WAF] Заблокирован подозрительный запрос: " + uri);

            if (uri.startsWith("/api/")) {
                httpResp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResp.setContentType("application/json");
                httpResp.setCharacterEncoding("UTF-8");
                httpResp.getWriter().write("{\"error\": \"WAF: Blocked for security reason\"}");
                httpResp.flushBuffer();
                return;
            } else {
                httpResp.sendRedirect("/static/waf_blocked.html");
            }
            return;
        }

        // Всё чисто ➔ пропускаем дальше
        chain.doFilter(cachedRequest, response);
    }

    private boolean isMalicious(CachedBodyHttpServletRequest request) throws IOException {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String body = "";

        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getContentType() != null && request.getContentType().contains("application/json")) {
            body = request.getCachedBodyAsString();
        }

        System.out.println("[WAF] Проверка запроса:");
        System.out.println("URI: " + uri);
        System.out.println("QUERY: " + query);
        System.out.println("BODY: " + body);

        return (query != null && (sqlInjectionPattern.matcher(query).find() || xssPattern.matcher(query).find())) ||
                (uri != null && (sqlInjectionPattern.matcher(uri).find() || xssPattern.matcher(uri).find())) ||
                (!body.isEmpty() && (sqlInjectionPattern.matcher(body).find() || xssPattern.matcher(body).find()));
    }
}
