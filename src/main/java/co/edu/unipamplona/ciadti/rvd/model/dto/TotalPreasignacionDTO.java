/**
 * Aplicación: rvd
 * Archivo: TotalPreasignacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/07/2026
 * Modificaciones:
 * 20/07/2026 - Sebastian Jaimes - Creación inicial
 * 20/07/2026 - Sebastian Jaimes - Totales monetarios a BigDecimal
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record TotalPreasignacionDTO(
    Long totalDocentes,
    BigDecimal totalPrestaciones,
    BigDecimal totalContratos,
    BigDecimal totalPreasignacion,
    List<TotalHorasPreasignacionDTO> totalHorasPreasignacion,
    BigDecimal totalHoras
) {}
