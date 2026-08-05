/**
 * Aplicación: rvd
 * Archivo: EncabezadoCargaReporteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record EncabezadoCargaReporteDTO(
    Long idCarga,
    Long idCoordinacion,
    String unidad,
    String facultad,
    String coordinacion,
    Long idPeriodoUniversidad,
    String periodoAcademico,
    Long idConvocatoria,
    String convocatoria
) {}
