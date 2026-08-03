/**
 * Aplicación: rvd
 * Archivo: ActividadHorasResumenDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/08/2026
 * Modificaciones:
 * 03/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ActividadHorasResumenDTO(
    String tipo,
    String codigo,
    String nombre,
    BigDecimal totalHoras
) {}
