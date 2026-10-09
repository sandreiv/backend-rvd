/**
 * Aplicación: rvd
 * Archivo: DocenteSecurity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.docente
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security.docente;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;

public final class DocenteSecurity {

    private DocenteSecurity() {
    }

    public static Long requireIdPersonaGeneral() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof DocentePrincipal principal
                && principal.idPersonaGeneral() != null) {
            return principal.idPersonaGeneral();
        }
        throw new ApiException(HttpStatus.UNAUTHORIZED, "No autenticado");
    }
}
