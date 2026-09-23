/**
 * Aplicación: rvd
 * Archivo: CookieBearerTokenResolver.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Traduce la cookie opaca al JWT guardado en {@link OpaqueSessionStore}.
 *
 * <p>Con cookie presente, solo se acepta lo que haya en el store (cookie
 * desconocida o vencida → sin token → 401). Sin cookie, y si
 * {@code allowBearer=true}, se acepta {@code Authorization: Bearer}.</p>
 */
@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final OpaqueSessionStore sessionStore;
    private final SessionCookieProperties properties;
    private final BearerTokenResolver headerResolver;

    public CookieBearerTokenResolver(
            OpaqueSessionStore sessionStore,
            SessionCookieProperties properties) {
        this(sessionStore, properties, new DefaultBearerTokenResolver());
    }

    CookieBearerTokenResolver(
            OpaqueSessionStore sessionStore,
            SessionCookieProperties properties,
            BearerTokenResolver headerResolver) {
        this.sessionStore = sessionStore;
        this.properties = properties;
        this.headerResolver = headerResolver;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, properties.cookieName());
        if (cookie != null) {
            return sessionStore.find(cookie.getValue())
                    .map(OpaqueSession::accessToken)
                    .orElse(null);
        }
        if (properties.allowBearer()) {
            return headerResolver.resolve(request);
        }
        return null;
    }
}
