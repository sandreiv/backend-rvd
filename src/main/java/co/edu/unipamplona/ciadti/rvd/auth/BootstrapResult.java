/**
 * Aplicación: rvd
 * Archivo: BootstrapResult.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import java.time.Instant;

import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;

/**
 * Resultado del bootstrap: id opaco para la cookie, vencimiento y body.
 *
 * @param sessionId id aleatorio guardado en el store de sesiones
 * @param expiresAt vencimiento efectivo de la sesión
 * @param response  body de respuesta (sin tokens)
 */
public record BootstrapResult(
        String sessionId,
        Instant expiresAt,
        JwtAuthResponseDTO response) {
}
