/**
 * Aplicación: rvd
 * Archivo: JwtAuthResponseDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 23/09/2026 - Sebastian Jaimes - BFF: sin tokens en el body; expiresAt y CSRF
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;

/**
 * Respuesta de sesión del BFF. El JWT de SecurityAuth no viaja aquí: queda
 * en el servidor y el navegador solo recibe la cookie opaca HttpOnly.
 */
@Data
public class JwtAuthResponseDTO {
    private String username;
    private String nombreCompleto;
    private String idPersona;
    private List<String> roles;
    private UsuarioSesionDTO usuario;
    /** Vencimiento de la sesión (exp del JWT o TTL por defecto). */
    private Instant expiresAt;
    /** Header que debe enviar el SPA en peticiones mutadoras. */
    private String csrfHeaderName;
    /** Valor CSRF (double submit) vigente para esta sesión. */
    private String csrfToken;
}
