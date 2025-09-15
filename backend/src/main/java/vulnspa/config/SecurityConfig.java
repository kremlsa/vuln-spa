package vulnspa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import vulnspa.security.Md5PasswordEncoder;

import javax.sql.DataSource;

import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.config.Customizer;


/**
 * Конфигурация Spring Security для демо-приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final Md5PasswordEncoder passwordEncoder;

    /**
     * Создает конфигурацию безопасности.
     *
     * @param dataSource источник данных с таблицами пользователей.
     * @param passwordEncoder используемый кодировщик паролей.
     */
    public SecurityConfig(DataSource dataSource,
                          Md5PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Конфигурирует {@link JdbcUserDetailsManager} для работы с таблицами Spring Security.
     *
     * @return настроенный менеджер пользователей.
     */
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * Создает {@link DaoAuthenticationProvider} с MD5-кодировщиком.
     *
     * @param uds менеджер пользователей.
     * @return настроенный провайдер аутентификации.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(JdbcUserDetailsManager uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Сборка {@link AuthenticationManager} с кастомным провайдером.
     *
     * @param http текущая {@link HttpSecurity}.
     * @param authProvider провайдер аутентификации.
     * @return менеджер аутентификации.
     * @throws Exception при ошибке конфигурации.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       DaoAuthenticationProvider authProvider)
            throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(authProvider);
        return auth.build();
    }

    /**
     * Основная цепочка фильтров безопасности для REST API.
     *
     * @param http объект конфигурации безопасности.
     * @param authProvider провайдер аутентификации.
     * @return настроенная цепочка фильтров.
     * @throws Exception при ошибке конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider) throws Exception {
        http
                .authenticationProvider(authProvider)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        // открываем весь /api/auth/* для вашего REST-контроллера
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2-консоль
                        .requestMatchers("/h2-console/**").permitAll()
                        // заметки
                        .requestMatchers(HttpMethod.POST,   "/api/notes/**")
                        .hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/notes/**")
                        .hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/vip/**").authenticated()
                        .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/api/actuator/**").permitAll()
                        // всё остальное — открыто
                        .anyRequest().permitAll()
                )
                // (optional) HTTP Basic для curl/Postman
                .httpBasic(Customizer.withDefaults())
                // чтобы H2-консоль работала в iframe
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .logout(logout -> logout.disable()) // отключаем автоматический logout для демонстрации уязвимости
        ;

        return http.build();
    }
}