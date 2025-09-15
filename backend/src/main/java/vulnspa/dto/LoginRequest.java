package vulnspa.dto;

/**
 * DTO с учетными данными для входа пользователя.
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Возвращает имя пользователя.
     *
     * @return логин.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username логин.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль в открытом виде.
     *
     * @return пароль.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль.
     *
     * @param password пароль в открытом виде.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
