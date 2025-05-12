package vulnspa.security;

public class NoneWafStrategy implements WafStrategy {
    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest request) {
        return false;
    }
}
