/**
 * Aplicación: rvd
 * Archivo: FuncionalidadAuthorizationManager.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import java.util.Collection;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import lombok.RequiredArgsConstructor;

/**
 * Autoriza con authorities {@code METHOD:URL} (Ant patterns).
 * Si {@code enforceFuncionalidad=false}, rutas fuera del catálogo del usuario
 * pasan con JWT válido; rutas del catálogo con verbo incorrecto → 403.
 */
@Component
@RequiredArgsConstructor
public class FuncionalidadAuthorizationManager
        implements AuthorizationManager<RequestAuthorizationContext> {

    private final SecurityAuthProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public AuthorizationDecision check(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        String method = context.getRequest().getMethod();
        String path = stripContextPath(
                context.getRequest().getRequestURI(),
                context.getRequest().getContextPath());

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        boolean hasMethodUrl = false;
        boolean methodMatch = false;
        boolean pathKnown = false;

        for (GrantedAuthority ga : authorities) {
            String authority = ga.getAuthority();
            int sep = authority.indexOf(':');
            if (sep <= 0) {
                continue;
            }
            hasMethodUrl = true;
            String authMethod = authority.substring(0, sep);
            String authPath = authority.substring(sep + 1);
            if (!pathMatcher.match(authPath, path)) {
                continue;
            }
            pathKnown = true;
            if (methodMatches(authMethod, method)) {
                methodMatch = true;
                break;
            }
        }

        if (methodMatch) {
            return new AuthorizationDecision(true);
        }
        if (pathKnown) {
            return new AuthorizationDecision(false);
        }
        if (!hasMethodUrl) {
            return new AuthorizationDecision(!properties.enforceFuncionalidad());
        }
        return new AuthorizationDecision(!properties.enforceFuncionalidad());
    }

    private static boolean methodMatches(String authMethod, String requestMethod) {
        if ("*".equals(authMethod)) {
            return true;
        }
        if (authMethod.equalsIgnoreCase(requestMethod)) {
            return true;
        }
        // Compatibilidad ACTUALIZAR→PATCH en catálogo vs PUT legado en APIs.
        if ("PATCH".equalsIgnoreCase(authMethod)
                && "PUT".equalsIgnoreCase(requestMethod)) {
            return true;
        }
        if ("PUT".equalsIgnoreCase(authMethod)
                && "PATCH".equalsIgnoreCase(requestMethod)) {
            return true;
        }
        return false;
    }

    private static String stripContextPath(String uri, String contextPath) {
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            String stripped = uri.substring(contextPath.length());
            return stripped.isEmpty() ? "/" : stripped;
        }
        return uri;
    }
}
