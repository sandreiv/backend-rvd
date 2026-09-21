/**
 * Aplicación: rvd
 * Archivo: CargaBudgetService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - computeInclusive para contratación
 * 18/09/2026 - Sebastian Jaimes - preasignación iguala valor y autorizado
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.math.BigDecimal;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;

public interface CargaBudgetService {

    CargaBudgetDTO getBudget(Long idCarga);

    BigDecimal preview(Long idCarga, CargaBudgetOverlay overlay);

    ValorContratacionDTO compute(CargaBudgetOverlay overlay);

    ValorContratacionDTO computeInclusive(CargaBudgetOverlay overlay);

    void assertNotExceedsAuthorized(
            Long idCarga,
            CargaBudgetOverlay overlay);

    void refreshCargValor(Long idCarga);

    void refreshPreassignmentTotals(Long idCarga);
}
