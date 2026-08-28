/**
 * Aplicación: rvd
 * Archivo: CoordinacionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - Listado coordinaciones por JWT (Coordinador/Decano)
 * 27/08/2026 - Horas de actividades por carga
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AprobacionDetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorasActividadesCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionDecanoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionProgramaHorasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;

public interface CoordinacionService {

    List<CoordinacionDTO> findCoordinationsByIdConvocatoria(
            Long idConvocatoria,
            Long idPeriodoUniversidad);

    void savePreload(RelacionConvocatoriaCoordinacionDTO dto);

    List<DocentePlantaCoordinacionDTO> listCareerProfessors(Long idCoordinacion);

    List<DocentePreasignacionDTO> searchProfessor(String nombre, String documento, Long idModalidadContratacion);

    List<FechaModalidadFormularioDTO> getWorkDate(Long idCarga, Long idModalidadContratacion);

    ValorPuntosPrecargaDTO getValuePointsPreload(
            Long anio,
            Long idCategoriaCatedratico,
            Long idPersonaGeneral,
            Long idModalidadContratacion);

    List<CategoriaCatedraticoDTO> listProfessorCategory(Long idModalidadContratacion);

    void addProfessor(CargaDocenteFormularioDTO dto);

    List<DocenteCoordinacionDTO> listProfessors(Long idCarga, Long idModalidadContratacion);

    void updateProfessor(Long idCargaDocente, CargaDocenteFormularioDTO dto);

    void deleteProfessor(Long idCargaDocente);

    void registerProfessorPreloadHistory(Long idCargaDocente);

    List<UnidadDTO> listRegionalUnits(Long idCoordinacion);

    List<ProgramaDTO> listProgramsByRegionalUnit(Long idCoordinacion, Long idUnidadRegional, Long idNivelEducativo);

    List<TipoActividadCriterioDTO> listCriteria(Long idTipoActividad);

    List<TipoActividadDTO> listActivityTypes();

    ActividadModalidadDTO listActivitiesModality(Long idModalidadContratacion);

    List<MateriaDTO> listSubjects(Long idPrograma, Long idCoordinacion);

    List<GrupoDTO> listSubjectGroup(String codigoMateria, Long idPeriodoUniversidad);

    List<ProyectoDTO> listProjectsProfessor(Long idPersonaGeneral, Long idConvocatoria);

    void saveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto);

    List<DetalleCargaDocenteDTO> listDetailProfessorPreload(Long idCargaDocente);

    void updateDetailProfessorPreload(DetalleCargaDocenteDTO dto);

    void saveCareerProfessorPreload(CargaDocentePlantaDTO dto);

    void deleteProfessorActivity(Long idDetalleCargaDocente);

    void approveProfessorPreassignment(Long idCargaDocente);

    void approveProfessorActivityDistribution(AprobacionDetalleCargaDocenteDTO dto);

    TotalPreasignacionDTO getTotalPreload(Long idCarga);

    HorasActividadesCargaDTO getActivitiesHours(Long idCarga);

    ValorContratacionDTO getContractValue(Long idCargaDocente);

    List<ActividadHorasResumenDTO> listActivityHours(Long idCargaDocente);

    List<CentroCostoResumenDTO> listCostCenters(Long idCargaDocente);

    List<ObservacionCargaDTO> listPreloadObservations(Long idCarga);

    void markSeenObservationsByPreload(Long idCarga);

    ResumenCargaDocenteDTO getProfessorLoadSummary(Long idCargaDocente);

    List<RestriccionProgramaHorasDTO> listProgramHourRestrictions(
            Long idModalidadContratacion,
            Long idCargaDocente);

    List<CoordinacionBusquedaDTO> searchCoordination(String nombre);

    List<CoordinacionBusquedaDTO> searchCoordinationForRestriction(String nombre, Long idConvocatoria);

    void saveCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto);

    List<CoordinacionRestriccionDTO> listCoordinationRestriction(Long idConvocatoria);

    void updateCoordinationRestriction(Long id, CoordinacionRestriccionFormularioDTO dto);

    void deleteCoordinationRestriction(Long id, CoordinacionRestriccionDTO dto);

    void bulkDeleteCoordinationRestriction(List<CoordinacionRestriccionDTO> restricciones);

    void endorsePreloadDean(Long idCarga);

    void declinePreload(Long idCarga, ObservacionDecanoDTO dto);

    void approvePreloadDean(Long idCarga);

    void approvePreloadDevelopment(Long idCarga);
    
}
