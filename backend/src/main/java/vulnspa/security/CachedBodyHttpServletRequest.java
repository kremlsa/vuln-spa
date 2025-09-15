package vulnspa.security;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Обертка над {@link HttpServletRequest}, позволяющая многократно читать тело запроса.
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    /**
     * Считывает тело запроса и сохраняет его для повторного использования.
     *
     * @param request исходный HTTP-запрос.
     * @throws IOException при ошибке чтения тела.
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = request.getInputStream().readAllBytes(); // Считываем тело один раз
    }

    /**
     * Возвращает поток, читающийся из кэшированного массива.
     *
     * @return поток для повторного чтения тела запроса.
     */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);

        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // ничего не делаем
            }
        };
    }

    /**
     * Возвращает {@link BufferedReader} поверх кэшированного тела.
     *
     * @return читатель тела запроса.
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Возвращает кэшированное тело в виде строки.
     *
     * @return тело запроса.
     */
    public String getCachedBodyAsString() {
        return new String(cachedBody, StandardCharsets.UTF_8);
    }
}
