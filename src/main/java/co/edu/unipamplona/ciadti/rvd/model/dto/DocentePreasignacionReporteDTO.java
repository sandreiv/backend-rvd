/**
 * Aplicación: rvd
 * Archivo: DocentePreasignacionReporteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 04/08/2026 - Sebastian Jaimes - Fila plana por docente con horas por código
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record DocentePreasignacionReporteDTO(
    Long idCargaDocente,
    String nombreCompleto,
    String documento,
    String puntos,
    String categoria,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String semanas,
    BigDecimal asignacionSalarial,
    ValorContratacionDTO valorContratacion,
    Map<String, BigDecimal> horasPorCodigo,
    String errorContrato
) {}
