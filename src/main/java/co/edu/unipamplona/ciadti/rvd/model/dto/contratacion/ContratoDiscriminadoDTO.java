/**
 * Aplicación: rvd
 * Archivo: ContratoDiscriminadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto.contratacion
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (generación del anexo de contratación en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto.contratacion;

import java.math.BigDecimal;

public record ContratoDiscriminadoDTO(
    BigDecimal salarios,
    BigDecimal cesantias,
    BigDecimal vacaciones,
    BigDecimal prima
) {}
