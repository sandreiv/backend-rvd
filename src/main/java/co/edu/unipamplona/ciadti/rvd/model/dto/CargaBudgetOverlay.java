/**
 * Aplicación: rvd
 * Archivo: CargaBudgetOverlay.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CargaBudgetOverlay(
        Long idCargaDocente,
        Long idModalidadContratacion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal asignacionSalarial,
        BigDecimal valorHora,
        BigDecimal semanas,
        BigDecimal horasActividades,
        String puntos,
        BigDecimal valorPunto
) {}
