package vulnspa.security;

import java.io.IOException;

public interface WafStrategy {
    boolean isMalicious(CachedBodyHttpServletRequest request) throws IOException;
}
