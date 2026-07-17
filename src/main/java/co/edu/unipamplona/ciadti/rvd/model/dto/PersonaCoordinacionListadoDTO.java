/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaCoordinacionListadoDTO(
        Long idPersonaGeneral,
        String persona,
        String documentoIdentidad,
        Long idCoordinacion,
        String coordinacion,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */