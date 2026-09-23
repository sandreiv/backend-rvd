/**
 * Aplicación: rvd
 * Archivo: SessionCookieService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Construye la cookie de sesión opaca (HttpOnly, Secure, SameSite) y su
 * versión de borrado. El path se limita al context-path de RVD.
 */
@Component
public class SessionCookieService {

    private final SessionCookieProperties properties;
    private final String cookiePath;

    public SessionCookieService(
            SessionCookieProperties properties,
            @Value("${server.servlet.context-path:/}") String contextPath) {
        this.properties = properties;
        this.cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
    }

    public String cookieName() {
        return properties.cookieName();
    }

    public String cookiePath() {
        return cookiePath;
    }

    /** Cookie de creación: vive hasta {@code expiresAt}. */
    public ResponseCookie create(String sessionId, Instant expiresAt) {
        Duration maxAge = Duration.between(Instant.now(), expiresAt);
        return base(sessionId)
                .maxAge(maxAge.isNegative() ? Duration.ZERO : maxAge)
                .build();
    }

    /** Cookie de borrado: valor vacío y {@code Max-Age=0}. */
    public ResponseCookie clear() {
        return base("")
                .maxAge(Duration.ZERO)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(properties.cookieName(), value)
                .httpOnly(true)
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .path(cookiePath);
    }
}
