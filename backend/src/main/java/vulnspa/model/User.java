package vulnspa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

/**
 * Сущность пользователя, используемая Spring Security.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String username;
    private String password;
    private boolean isVip;
    private boolean enabled = true;

    // Геттеры и сеттеры нужны для JSON сериализации

    /**
     * Возвращает признак VIP-пользователя.
     *
     * @return {@code true}, если пользователь VIP.
     */
    public boolean isVip() {
        return isVip;
    }

    /**
     * Устанавливает флаг VIP.
     *
     * @param vip новое значение флага.
     */
    public void setIsVip(boolean vip) {
        isVip = vip;
    }

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
     * Возвращает пароль пользователя.
     *
     * @return пароль (используется только для записи при сериализации).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password пароль в хешированном виде.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает, включен ли пользователь.
     *
     * @return {@code true}, если учетная запись активна.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Устанавливает статус активности пользователя.
     *
     * @param enabled флаг активности.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
