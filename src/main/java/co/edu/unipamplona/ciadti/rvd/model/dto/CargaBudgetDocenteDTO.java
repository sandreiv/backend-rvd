/**
 * Aplicación: rvd
 * Archivo: CargaBudgetDocenteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;

public record CargaBudgetDocenteDTO(
        Long idCargaDocente,
        BigDecimal totalContrato
) {}
