/**
 * Aplicación: rvd
 * Archivo: ValorPuntosPrecargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/06/2026
 * Modificaciones:
 * 23/06/2026 - Sebastian Jaimes - Creación inicial
 * 20/07/2026 - Sebastian Jaimes - Campos monetarios a BigDecimal
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ValorPuntosPrecargaDTO(
    BigDecimal valorHora,
    BigDecimal valorPunto,
    BigDecimal puntosDocente,
    BigDecimal asignacionSalarial
) {}
