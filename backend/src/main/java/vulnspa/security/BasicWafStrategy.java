package vulnspa.security;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class BasicWafStrategy implements WafStrategy {
    private final Pattern sql = Pattern.compile( "\\bOR\\s*1\\s*=\\s*1\\b", Pattern.CASE_INSENSITIVE);
    private final Pattern xss = Pattern.compile("<script>", Pattern.CASE_INSENSITIVE);
    private final Pattern path = Pattern.compile("\\.\\./", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest req) throws IOException {
        return match(req, sql) || match(req, xss) || match(req, path);
    }

    private boolean match(CachedBodyHttpServletRequest req, Pattern p) throws IOException {
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String decodedQuery = query != null ? URLDecoder.decode(query, StandardCharsets.UTF_8) : "";
        String body = "";

        if ("POST".equalsIgnoreCase(req.getMethod()) &&
                req.getContentType() != null &&
                req.getContentType().contains("application/json")) {
            body = req.getCachedBodyAsString();
        }

        return (p.matcher(decodedQuery).find()) ||
                (uri != null && p.matcher(uri).find()) ||
                (!body.isEmpty() && p.matcher(body).find());
    }
}
