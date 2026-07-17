/**
 * Aplicación: rvd
 * Archivo: AsosiacionCoordinacionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record AsociacionCoordinacionListadoDTO(
        Long id,
        Long idCoordinacion,
        String coordinacion,
        Long idPrograma,
        String programa,
        String codigoMateria,
        String materia,
        Long idCentroCosto,
        String centroCosto,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */