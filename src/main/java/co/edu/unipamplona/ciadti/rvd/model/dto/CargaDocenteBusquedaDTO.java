/**
 * Aplicación: rvd
 * Archivo: CargaDocenteBusquedaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CargaDocenteBusquedaDTO(
    Long id,
    Long idCarga,
    Long idConvocatoria,
    Long idCoordinacion,
    Long idModalidadContratacion,
    Long idFechasConvocatoria
) {}

/* 17/07/2026 @:Daniel Arias */