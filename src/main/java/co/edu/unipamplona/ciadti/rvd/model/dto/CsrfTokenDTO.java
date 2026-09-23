/**
 * Aplicación: rvd
 * Archivo: CsrfTokenDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

/**
 * Token CSRF vigente para el SPA (double submit con la cookie XSRF-TOKEN).
 *
 * @param headerName header HTTP que debe enviar el front
 * @param token      valor del token
 */
public record CsrfTokenDTO(String headerName, String token) {
}
