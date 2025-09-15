package vulnspa.security;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Продвинутая стратегия WAF: содержит расширенный набор регулярных выражений.
 */
public class AdvancedWafStrategy implements WafStrategy {
    private final Pattern sql = Pattern.compile("('|--|;|/\\*|\\*/|\\b(SELECT|UNION|INSERT|UPDATE|DELETE|DROP|OR|AND)\\b)", Pattern.CASE_INSENSITIVE);
    private final Pattern xss = Pattern.compile("(?i)(<script.*?>|</script>|onerror\\s*=|onload\\s*=|javascript:|alert\\()");
    private final Pattern path = Pattern.compile("((\\.\\./)+|%2e%2e%2f|%2e%2e/|\\.%2e/|/\\.%2e)", Pattern.CASE_INSENSITIVE);

    /**
     * Проверяет запрос на широкий перечень атак.
     *
     * @param req кэшированный HTTP-запрос.
     * @return {@code true}, если найдены потенциально опасные конструкции.
     * @throws IOException при ошибке чтения тела запроса.
     */
    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest req) throws IOException {
        return match(req, sql) || match(req, xss) || match(req, path);
    }

    /**
     * Выполняет сопоставление регулярного выражения с параметрами запроса.
     *
     * @param req HTTP-запрос.
     * @param p проверяемый паттерн.
     * @return {@code true}, если обнаружен совпадающий фрагмент.
     * @throws IOException при ошибке чтения тела запроса.
     */
    private boolean match(CachedBodyHttpServletRequest req, Pattern p) throws IOException {
        String uri = req.getRequestURI();
        String q = req.getQueryString();
        String body = "";
        if ("POST".equalsIgnoreCase(req.getMethod()) &&
                req.getContentType() != null && req.getContentType().contains("application/json")) {
            body = req.getCachedBodyAsString();
        }

        return (q != null && p.matcher(q).find()) ||
                (uri != null && p.matcher(uri).find()) ||
                (!body.isEmpty() && p.matcher(body).find());
    }
}
