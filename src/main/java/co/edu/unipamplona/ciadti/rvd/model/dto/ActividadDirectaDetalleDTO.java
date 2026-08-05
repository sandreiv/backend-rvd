/**
 * Aplicación: rvd
 * Archivo: ActividadDirectaDetalleDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/08/2026
 * Modificaciones:
 * 03/08/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ActividadDirectaDetalleDTO(
    String unidad,
    String programa,
    String materia,
    String grupo,
    BigDecimal horas
) {}
