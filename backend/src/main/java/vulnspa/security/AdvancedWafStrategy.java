package vulnspa.security;

import java.io.IOException;
import java.util.regex.Pattern;

public class AdvancedWafStrategy implements WafStrategy {
    private final Pattern sql = Pattern.compile("('|--|;|/\\*|\\*/|\\b(SELECT|UNION|INSERT|UPDATE|DELETE|DROP|OR|AND)\\b)", Pattern.CASE_INSENSITIVE);
    private final Pattern xss = Pattern.compile("(?i)(<script.*?>|</script>|onerror\\s*=|onload\\s*=|javascript:|alert\\()");
    private final Pattern path = Pattern.compile("((\\.\\./)+|%2e%2e%2f|%2e%2e/|\\.%2e/|/\\.%2e)", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest req) throws IOException {
        return match(req, sql) || match(req, xss) || match(req, path);
    }

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
