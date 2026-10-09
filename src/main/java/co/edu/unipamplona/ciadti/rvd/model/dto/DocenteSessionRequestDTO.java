/**
 * Aplicación: rvd
 * Archivo: DocenteSessionRequestDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocenteSessionRequestDTO(
        @NotBlank @Size(max = 30) String numeroDocumento,
        @NotNull LocalDate fechaExpedicion
) {
}
