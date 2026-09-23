/**
 * Aplicación: rvd
 * Archivo: SpaCsrfTokenRequestHandler.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.csrf
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (CSRF para SPA Angular)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.csrf;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handler CSRF (double submit) para el SPA Angular.
 *
 * <p>Usa el token en claro (sin XOR) para que el valor que RVD entrega en
 * {@code /api/auth/bootstrap} y {@code /api/auth/csrf} coincida con la
 * cookie {@code XSRF-TOKEN} y con el header {@code X-XSRF-TOKEN} que envía
 * el front. El XOR protege HTML comprimido (BREACH); aquí el token viaja
 * en un JSON pequeño, no aplica. Además fuerza la carga del token en cada
 * request para que la cookie se emita también en GET.</p>
 */
public final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Supplier<CsrfToken> csrfToken) {
        super.handle(request, response, csrfToken);
        csrfToken.get();
    }
}
