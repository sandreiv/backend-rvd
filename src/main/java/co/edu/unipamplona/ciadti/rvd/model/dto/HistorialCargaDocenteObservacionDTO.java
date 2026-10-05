/**
 * Aplicación: rvd
 * Archivo: HistorialCargaDocenteObservacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record HistorialCargaDocenteObservacionDTO(
    Long idHistorial,
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String rolPersonaGeneral,
    String observacion,
    LocalDateTime fecha,
    String estado
) {}