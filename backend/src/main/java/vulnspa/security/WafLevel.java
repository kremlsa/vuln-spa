package vulnspa.security;

/**
 * Уровни работы демонстрационного веб-фаервола.
 */
public enum WafLevel {
    /**
     * WAF отключен.
     */
    NONE,
    /**
     * Простые регулярные выражения.
     */
    BASIC,
    /**
     * Более агрессивные проверки.
     */
    ADVANCED
}
