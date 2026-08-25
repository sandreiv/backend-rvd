/**
 * Aplicación: rvd
 * Archivo: SecurityUtils.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - requireUser para listados por JWT
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<AuthUserDetails> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthUserDetails details)) {
            return Optional.empty();
        }
        return Optional.of(details);
    }

    public static Optional<Long> currentIdPersona() {
        return currentUser().map(AuthUserDetails::getIdPersonaGeneral);
    }

    public static AuthUserDetails requireUser() {
        return currentUser().orElseThrow(() -> new IllegalStateException("No hay usuario autenticado en el contexto de seguridad"));
    }

    public static Long requireIdPersona() {
        Long idPersona = requireUser().getIdPersonaGeneral();
        if (idPersona == null) {
            throw new IllegalStateException("No hay idPersona en el contexto de seguridad");
        }
        return idPersona;
    }
}
