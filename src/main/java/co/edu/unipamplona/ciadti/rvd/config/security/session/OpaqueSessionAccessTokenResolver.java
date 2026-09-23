/**
 * Aplicación: rvd
 * Archivo: OpaqueSessionAccessTokenResolver.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Cookie/store (o Bearer de respaldo) → JWT
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Fuente de verdad del JWT de SecurityAuth: {@link OpaqueSessionStore}.
 *
 * <p>No usa {@code Authentication.getCredentials()}: Spring llama
 * {@code eraseCredentials()} y el {@code Jwt} queda en {@code null}.</p>
 */
@Component
public class OpaqueSessionAccessTokenResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final OpaqueSessionStore sessionStore;
    private final SessionCookieProperties properties;

    public OpaqueSessionAccessTokenResolver(
            OpaqueSessionStore sessionStore,
            SessionCookieProperties properties) {
        this.sessionStore = sessionStore;
        this.properties = properties;
    }

    /**
     * Cookie válida → JWT del store. Cookie presente pero desconocida o
     * vencida → vacío (no cae al header). Sin cookie y
     * {@code allowBearer=true} → {@code Authorization: Bearer}.
     */
    public Optional<String> resolveAccessToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, properties.cookieName());
        if (cookie != null) {
            return sessionStore.find(cookie.getValue())
                    .map(OpaqueSession::accessToken)
                    .filter(StringUtils::hasText);
        }
        return resolveBearerFallback(request);
    }

    /**
     * Sesión opaca de la cookie, si existe. Sin cookie o id desconocido →
     * vacío (el fallback Bearer no tiene fila en el store).
     */
    public Optional<OpaqueSession> findSession(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, properties.cookieName());
        if (cookie == null) {
            return Optional.empty();
        }
        return sessionStore.find(cookie.getValue());
    }

    private Optional<String> resolveBearerFallback(HttpServletRequest request) {
        if (!properties.allowBearer()) {
            return Optional.empty();
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        String token = header.substring(BEARER_PREFIX.length()).trim();
        return StringUtils.hasText(token) ? Optional.of(token) : Optional.empty();
    }
}
