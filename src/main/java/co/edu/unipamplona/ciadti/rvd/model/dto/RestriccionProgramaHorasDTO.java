/**
 * Aplicación: rvd
 * Archivo: RestriccionProgramaHorasDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/08/2026
 * Modificaciones:
 * 06/08/2026 - Sebastian Jaimes
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record RestriccionProgramaHorasDTO(
        Long idPrograma,
        String maximoHoras,
        BigDecimal horasAsignadas,
        BigDecimal horasDisponibles
) {}
