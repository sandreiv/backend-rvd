/**
 * Aplicación: rvd
 * Archivo: ParametrosContratacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record ParametrosContratacionDTO(
    String unidadRegional,
    String coordinacion,
    String modalidadContratacion,
    String categorizacion,
    String puntosAsignados,
    String estadoCategorizacion,
    BigDecimal asignacionSalarial,
    BigDecimal valorHora,
    String horasTrabajo
) {}
