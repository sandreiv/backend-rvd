/**
 * Aplicación: rvd
 * Archivo: HorasActividadesCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Sebastian Jaimes:  Creación inicial
 * 27/08/2026 - Agrupación por actividad padre
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record HorasActividadesCargaDTO(
    List<HorasActividadPadreDTO> totalHorasPreasignacion,
    BigDecimal totalHoras
) {}
