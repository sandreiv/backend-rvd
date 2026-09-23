/**
 * Aplicación: rvd
 * Archivo: OpaqueSession.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import java.time.Instant;

/**
 * Estado servidor de una sesión BFF: el JWT de SecurityAuth nunca sale al
 * navegador; solo viaja el id opaco de la cookie.
 *
 * @param accessToken  JWT RS256 emitido por SecurityAuth
 * @param refreshToken refresh token (reservado para renovación futura)
 * @param expiresAt    {@code exp} del JWT; puede ser {@code null}
 */
public record OpaqueSession(
        String accessToken,
        String refreshToken,
        Instant expiresAt) {
}
