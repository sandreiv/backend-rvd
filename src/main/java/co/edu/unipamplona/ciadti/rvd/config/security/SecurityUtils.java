/**
 * Aplicación: rvd
 * Archivo: SecurityUtils.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
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

    public static Long requireIdPersona() {
        return currentIdPersona().orElseThrow(() -> new IllegalStateException("No hay idPersona en el contexto de seguridad"));
    }
}
