/**
 * Aplicación: rvd
 * Archivo: HorasActividadPadreDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record HorasActividadPadreDTO(
    String codigo,
    String nombre,
    BigDecimal horas
) {}
