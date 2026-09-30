/**
 * Aplicación: rvd
 * Archivo: HistorialNovedadResumenDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Historial de novedades del docente
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record HistorialNovedadResumenDTO(
    Long idNovedad,
    String tipo,
    String descripcion,
    String accion,
    String componente,
    LocalDateTime fecha,
    String estadoNovedad,
    String vigente
) {}
