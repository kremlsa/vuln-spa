package vulnspa.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Очень слабый MD5-хэш для демонстрации.
 */
@Component
public class Md5PasswordEncoder implements PasswordEncoder {

    /**
     * Вычисляет MD5-хеш от переданной строки.
     *
     * @param rawPassword исходный пароль.
     * @return шестнадцатеричная строка MD5.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(rawPassword.toString()
                    .getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }

    /**
     * Сравнивает исходный пароль с MD5-хешем.
     *
     * @param rawPassword исходный пароль.
     * @param encodedPassword ранее сохраненный хеш.
     * @return {@code true}, если хеши совпадают.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}