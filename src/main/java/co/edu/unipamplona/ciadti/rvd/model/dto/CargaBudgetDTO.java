/**
 * Aplicación: rvd
 * Archivo: CargaBudgetDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record CargaBudgetDTO(
        BigDecimal valorCarga,
        BigDecimal valorAutorizado,
        List<CargaBudgetDocenteDTO> docentes
) {}
