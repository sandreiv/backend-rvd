/**
 * Aplicación: rvd
 * Archivo: SecurityConfig.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/03/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Resource Server SecurityAuth (solo Vortal)
 * 23/09/2026 - Sebastian Jaimes - BFF: cookie opaca + CSRF double submit
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import co.edu.unipamplona.ciadti.rvd.config.cors.CorsConfig;
import co.edu.unipamplona.ciadti.rvd.config.security.csrf.CookieSessionCsrfMatcher;
import co.edu.unipamplona.ciadti.rvd.config.security.csrf.SpaCsrfTokenRequestHandler;
import co.edu.unipamplona.ciadti.rvd.config.security.jwt.JwtAccessDeniedHandler;
import co.edu.unipamplona.ciadti.rvd.config.security.jwt.JwtAuthEntryPoint;
import co.edu.unipamplona.ciadti.rvd.config.security.session.CookieBearerTokenResolver;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/bootstrap",
            "/api/auth/logout",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    private static final String[] SESSION_ENDPOINTS = {
            "/api/auth/me",
            "/api/auth/menu",
            "/api/auth/csrf"
    };

    private final SecurityAuthTokenValidator securityAuthTokenValidator;
    private final SecurityAuthJwtAuthenticationConverter jwtAuthenticationConverter;
    private final FuncionalidadAuthorizationManager funcionalidadAuthorizationManager;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsConfig corsConfig;
    private final CookieBearerTokenResolver cookieBearerTokenResolver;
    private final SessionCookieProperties sessionCookieProperties;
    private final SessionCookieService sessionCookieService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(
                    corsConfig.corsConfigurationSource()))
            .csrf(csrf -> csrf
                    .csrfTokenRepository(csrfTokenRepository())
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                    .requireCsrfProtectionMatcher(new CookieSessionCsrfMatcher(
                            sessionCookieProperties.cookieName()))
                    .ignoringRequestMatchers(
                            "/api/auth/bootstrap",
                            "/api/auth/logout"))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuthEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .requestMatchers(SESSION_ENDPOINTS).authenticated()
                    .anyRequest().access(funcionalidadAuthorizationManager))
            .oauth2ResourceServer(oauth2 -> oauth2
                    .bearerTokenResolver(cookieBearerTokenResolver)
                    .jwt(jwt -> jwt
                            .decoder(securityAuthTokenValidator.decoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter))
                    .authenticationEntryPoint(jwtAuthEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler));
        return http.build();
    }

    /**
     * Cookie {@code XSRF-TOKEN} legible por JS (double submit) acotada al
     * context-path y con los mismos atributos Secure/SameSite de la sesión.
     */
    private CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookiePath(sessionCookieService.cookiePath());
        repository.setCookieCustomizer(cookie -> cookie
                .secure(sessionCookieProperties.secure())
                .sameSite(sessionCookieProperties.sameSite()));
        return repository;
    }
}
