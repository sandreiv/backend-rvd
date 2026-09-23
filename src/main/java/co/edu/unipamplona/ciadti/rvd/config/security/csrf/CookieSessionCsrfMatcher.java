/**
 * Aplicación: rvd
 * Archivo: CookieSessionCsrfMatcher.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.csrf
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (CSRF para SPA Angular)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.csrf;

import java.util.Set;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Exige CSRF solo cuando la petición es mutadora y se autentica con la
 * cookie de sesión opaca. Un {@code Authorization: Bearer} (Swagger,
 * Postman) no es vulnerable a CSRF porque el navegador no lo adjunta solo.
 */
public final class CookieSessionCsrfMatcher implements RequestMatcher {

    private static final Set<String> SAFE_METHODS =
            Set.of("GET", "HEAD", "TRACE", "OPTIONS");

    private final String sessionCookieName;

    public CookieSessionCsrfMatcher(String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (SAFE_METHODS.contains(request.getMethod())) {
            return false;
        }
        return WebUtils.getCookie(request, sessionCookieName) != null;
    }
}
