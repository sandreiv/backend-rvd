/**
 * Aplicación: rvd
 * Archivo: SecurityConfig.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/03/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Resource Server SecurityAuth (solo Vortal)
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import co.edu.unipamplona.ciadti.rvd.config.cors.CorsConfig;
import co.edu.unipamplona.ciadti.rvd.config.security.jwt.JwtAccessDeniedHandler;
import co.edu.unipamplona.ciadti.rvd.config.security.jwt.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityAuthTokenValidator securityAuthTokenValidator;
    private final SecurityAuthJwtAuthenticationConverter jwtAuthenticationConverter;
    private final FuncionalidadAuthorizationManager funcionalidadAuthorizationManager;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsConfig corsConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(
                    corsConfig.corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuthEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/api/auth/bootstrap",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/api-docs/**",
                            "/v3/api-docs/**")
                    .permitAll()
                    .anyRequest()
                    .access(funcionalidadAuthorizationManager))
            .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt
                            .decoder(securityAuthTokenValidator.decoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter))
                    .authenticationEntryPoint(jwtAuthEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler));
        return http.build();
    }
}
