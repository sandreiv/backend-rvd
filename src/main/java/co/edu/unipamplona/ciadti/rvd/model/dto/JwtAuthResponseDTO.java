/**
 * Aplicación: rvd
 * Archivo: JwtAuthResponseDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JwtAuthResponseDTO {
    private String accessToken;
    @JsonProperty("type")
    private String tokenType = "Bearer";
    private String refreshToken;
    private String username;
    private String idPersona;
    private List<String> roles;
    private UsuarioSesionDTO usuario;
}
