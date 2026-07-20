/**
 * Aplicación: rvd
 * Archivo: TotalHorasPreasignacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/07/2026
 * Modificaciones:
 * 20/07/2026 - Sebastian Jaimes - Creación inicial
 * 20/07/2026 - Sebastian Jaimes - Horas a BigDecimal
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record TotalHorasPreasignacionDTO(
    Long idTipoActividad,
    String codigo,
    String nombre,
    BigDecimal horas
) {}
