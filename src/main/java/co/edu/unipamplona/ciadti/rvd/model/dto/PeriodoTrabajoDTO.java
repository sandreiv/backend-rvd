/**
 * Aplicación: rvd
 * Archivo: PeriodoTrabajoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PeriodoTrabajoDTO(
    String unidad,
    String facultad,
    String coordinacion,
    String periodo,
    String convocatoria
) {}
