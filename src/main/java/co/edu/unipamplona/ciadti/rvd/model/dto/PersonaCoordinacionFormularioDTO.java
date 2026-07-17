/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaCoordinacionFormularioDTO(
        Long idPersonaGeneral,
        Long idCoordinacion,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */