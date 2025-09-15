package vulnspa.security;

/**
 * Стратегия-заглушка, не выполняющая никакой фильтрации.
 */
public class NoneWafStrategy implements WafStrategy {
    /**
     * Всегда разрешает запрос.
     *
     * @param request проверяемый запрос.
     * @return всегда {@code false}.
     */
    @Override
    public boolean isMalicious(CachedBodyHttpServletRequest request) {
        return false;
    }
}
