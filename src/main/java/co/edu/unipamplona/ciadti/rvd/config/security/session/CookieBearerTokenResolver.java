/**
 * Aplicación: rvd
 * Archivo: CookieBearerTokenResolver.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 * 23/09/2026 - Sebastian Jaimes - Delega en OpaqueSessionAccessTokenResolver
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Traduce la cookie opaca al JWT guardado en {@link OpaqueSessionStore}.
 *
 * <p>La lógica vive en {@link OpaqueSessionAccessTokenResolver} para que
 * {@code GET /api/auth/menu} use el mismo criterio (Spring borra
 * {@code Authentication.getCredentials()}).</p>
 */
@Component
@RequiredArgsConstructor
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final OpaqueSessionAccessTokenResolver accessTokenResolver;

    @Override
    public String resolve(HttpServletRequest request) {
        return accessTokenResolver.resolveAccessToken(request).orElse(null);
    }
}
