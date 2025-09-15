package vulnspa.security;

import java.io.IOException;

/**
 * Контракт для стратегий веб-фаервола.
 */
public interface WafStrategy {
    /**
     * Проверяет запрос на наличие злонамеренных признаков.
     *
     * @param request кэшированный запрос.
     * @return {@code true}, если запрос следует заблокировать.
     * @throws IOException при ошибке чтения данных запроса.
     */
    boolean isMalicious(CachedBodyHttpServletRequest request) throws IOException;
}
