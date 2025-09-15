package vulnspa.security;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Базовая стратегия WAF: проверяет несколько простых паттернов.
 */
public class BasicWafStrategy implements WafStrategy {
    private final Pattern sql = Pattern.compile("\\bOR\\s*1\\s*=\\s*1\\b", Pattern.CASE_INSENSITIVE);
    private final Pattern xss = Pattern.compile("<script>", Pattern.CASE_INSENSITIVE);
    private final Pattern path = Pattern.compile("(?<!%|\\\\)\\.\\./", Pattern.CASE_INSENSITIVE);


    /**
     * Выполняет проверки запроса на SQLi, XSS и обход каталогов.
     *
     * @param req кэшированный HTTP-запрос.
     * @return {@code true}, если найден подозрительный шаблон.
     * @throws IOException при ошибке чтения тела запроса.
     */
    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest req) throws IOException {
        return match(req, sql) || match(req, xss) || match(req, path);
    }

    /**
     * Проверяет URI, query string и тело запроса регулярным выражением.
     *
     * @param req HTTP-запрос.
     * @param p проверяемый паттерн.
     * @return {@code true}, если паттерн найден.
     * @throws IOException при ошибке чтения тела запроса.
     */
    private boolean match(CachedBodyHttpServletRequest req, Pattern p) throws IOException {
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String body = "";

        if ("POST".equalsIgnoreCase(req.getMethod()) &&
                req.getContentType() != null &&
                req.getContentType().contains("application/json")) {
            body = req.getCachedBodyAsString();
        }

        return (query != null && p.matcher(query).find()) ||
                (uri != null && p.matcher(uri).find()) ||
                (!body.isEmpty() && p.matcher(body).find());
    }
}
