/**
 * Aplicación: rvd
 * Archivo: ActividadPtdDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ActividadPtdDTO(
    Long id,
    String codigo,
    List<DetalleActividadPtdDTO> detalles,
    String criterio,
    String tipo,
    BigDecimal totalHoras
) {}