package vulnspa.model;

/**
 * Унифицированный ответ об ошибке для REST-клиентов.
 */
public class ErrorResponse {
    private String error;
    private String message;
    private String path;

    /**
     * Пустой конструктор для сериализации.
     */
    public ErrorResponse() {}

    /**
     * Создает объект с заданными параметрами ошибки.
     *
     * @param error краткое описание ошибки.
     * @param message подробное сообщение.
     * @param path путь запроса, где произошла ошибка.
     */
    public ErrorResponse(String error, String message, String path) {
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Возвращает краткое описание ошибки.
     *
     * @return текстовая метка ошибки.
     */
    public String getError() {
        return error;
    }

    /**
     * Устанавливает краткое описание ошибки.
     *
     * @param error текстовая метка ошибки.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Возвращает подробное сообщение об ошибке.
     *
     * @return сообщение об ошибке.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает подробное сообщение об ошибке.
     *
     * @param message сообщение об ошибке.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Возвращает путь запроса, где произошла ошибка.
     *
     * @return URI запроса.
     */
    public String getPath() {
        return path;
    }

    /**
     * Устанавливает путь запроса, где произошла ошибка.
     *
     * @param path URI запроса.
     */
    public void setPath(String path) {
        this.path = path;
    }
}