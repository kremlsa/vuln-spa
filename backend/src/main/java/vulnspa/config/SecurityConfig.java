package vulnspa.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import vulnspa.security.Md5PasswordEncoder;

import javax.sql.DataSource;

import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final Md5PasswordEncoder passwordEncoder;

    public SecurityConfig(DataSource dataSource,
                          Md5PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== 1) бин JdbcUserDetailsManager =====
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    // ===== 2) бин DaoAuthenticationProvider =====
    @Bean
    public DaoAuthenticationProvider authenticationProvider(JdbcUserDetailsManager uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // ===== 3) бин AuthenticationManager =====
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       DaoAuthenticationProvider authProvider)
            throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(authProvider);
        return auth.build();
    }

    // ===== 4) SecurityFilterChain без formLogin =====
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

                        // статика и фронтовые роуты
                        .requestMatchers("/", "/index.html", "/static/**",
                                "/favicon.ico", "/logo.png", "/login")
                        .permitAll()

                        // H2-консоль
                        .requestMatchers("/h2-console/**").permitAll()

                        // заметки
                        .requestMatchers(HttpMethod.GET,    "/api/notes/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/notes/**")
                        .hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/notes/**")
                        .hasAnyRole("USER","ADMIN")

                        // всё остальное — под защищённый доступ
                        .anyRequest().authenticated()
                )
                // (optional) HTTP Basic для curl/Postman
                .httpBasic(Customizer.withDefaults())
                // чтобы H2-консоль работала в iframe
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}