/**
 * Aplicación: rvd
 * Archivo: AsosiacionCoordinacionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record AsociacionCoordinacionFormularioDTO(
        Long id,
        Long idCoordinacion,
        Long idPrograma,
        String codigoMateria,
        Long idCentroCosto,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */