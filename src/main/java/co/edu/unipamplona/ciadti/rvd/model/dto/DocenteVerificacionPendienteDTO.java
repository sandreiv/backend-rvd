/**
 * Aplicación: rvd
 * Archivo: DocenteVerificacionPendienteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/09/2026
 * Modificaciones:
 * 07/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record DocenteVerificacionPendienteDTO(
        Long idCargaDocente,
        String nombreCompleto,
        Long idPeriodoUniversidad,
        Long idConvocatoria,
        Long idCoordinacion,
        String nombreCoordinacion
) {}
