/**
 * Aplicación: rvd
 * Archivo: AdministracionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionClaveDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionListadoDTO;

public interface AdministracionService {

    AsociacionCoordinacionCatalogosDTO getAssociationCatalogs();

    List<AsociacionCoordinacionListadoDTO> listCoordinationAssociations();

    void saveCoordinationAssociation(AsociacionCoordinacionFormularioDTO dto);

    void updateCoordinationAssociation(Long id, AsociacionCoordinacionFormularioDTO dto);

    void deleteCoordinationAssociation(Long id);

    void deleteBulkCoordinationAssociations(List<Long> ids);

    List<CentroCostoAsignadoListadoDTO> listCostCenterAssignments();

    void saveCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto);

    void updateCostCenterAssignment(Long id, CentroCostoAsignadoFormularioDTO dto);

    void deleteCostCenterAssignment(Long id);

    void deleteBulkCostCenterAssignments(List<Long> ids);

    List<PersonaCoordinacionListadoDTO> listPeopleCoordinations();

    void savePeopleCoordination(PersonaCoordinacionFormularioDTO dto);

    void updatePeopleCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto);

    void deletePeopleCoordination(Long idPersonaGeneral, Long idCoordinacion);

    void deleteBulkPeopleCoordinations(List<PersonaCoordinacionClaveDTO> registros);

    List<PersonaCoordinacionListadoDTO> listPlantProfessorCoordinations();

    void savePlantProfessorCoordination(PersonaCoordinacionFormularioDTO dto);

    void updatePlantProfessorCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto);

    void deletePlantProfessorCoordination(Long idPersonaGeneral, Long idCoordinacion);

    void deleteBulkPlantProfessorCoordinations(List<PersonaCoordinacionClaveDTO> registros);
    
}

/* 17/07/2026 @:Daniel Arias */
