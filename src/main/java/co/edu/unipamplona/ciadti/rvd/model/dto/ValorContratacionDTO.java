/**
 * Aplicación: rvd
 * Archivo: ValorContratacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/08/2026
 * Modificaciones:
 * 03/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ValorContratacionDTO(
    BigDecimal valorVacaciones,
    BigDecimal valorCesantias,
    BigDecimal valorIntereses,
    BigDecimal valorPrimaLegal,
    BigDecimal totalPrestaciones,
    BigDecimal valorContrato,
    BigDecimal totalContrato
) {}
