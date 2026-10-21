/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSaveRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdStatesProfessorsDTO;

public interface PlanTrabajoDigitalService {
    
    void savePtd(PtdSaveRequestDTO dto);

    PtdInfoWithWeightsDTO getInfoPtdWithWeights(Long idPersonaGeneral, Long idCargaDocente, Long idPeriodoUniversitario);

    List<PtdStatesProfessorsDTO> getPtdStatesProfessors(Long idPeriodoUniversitario);
}
