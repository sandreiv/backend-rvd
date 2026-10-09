/**
 * Aplicación: rvd
 * Archivo: DocenteSessionResponseDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record DocenteSessionResponseDTO(
        String accessToken,
        DocenteSesionDTO docente
) {
}
