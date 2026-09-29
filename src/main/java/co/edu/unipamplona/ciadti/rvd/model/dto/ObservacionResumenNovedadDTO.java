/**
 * Aplicación: rvd
 * Archivo: ObservacionResumenNovedadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Observación de RVD.OBSERVACIONES en el resumen
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record ObservacionResumenNovedadDTO(
    Long idObservacion,
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String observacion,
    LocalDateTime fecha
) {}
