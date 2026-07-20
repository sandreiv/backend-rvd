package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
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
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;

public interface CoordinacionService {

    List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria, Long idUsuario);

    void savePreload(RelacionConvocatoriaCoordinacionDTO dto);

    List<DocentePlantaCoordinacionDTO> listCareerProfessors(Long idCoordinacion);

    List<DocentePreasignacionDTO> searchProfessor(String nombre, String documento, Long idModalidadContratacion);

    List<FechaModalidadFormularioDTO> getWorkDate(Long coorId, Long mocoId);

    ValorPuntosPrecargaDTO getValuePointsPreload(Long anio, Long idCategoriaCatedratico, Long idPersonaGeneral);

    List<CategoriaCatedraticoDTO> listProfessorCategory(Long idModalidadContratacion);

    void addProfessor(CargaDocenteFormularioDTO dto);

    List<DocenteCoordinacionDTO> listProfessors(Long idCoordinacion, Long idModalidadContratacion);

    void updateProfessor(Long idCargaDocente, CargaDocenteFormularioDTO dto);

    void deleteProfessor(Long idCargaDocente);

    List<UnidadDTO> listRegionalUnits(Long idCoordinacion);

    List<ProgramaDTO> listProgramsByRegionalUnit(Long idCoordinacion, Long idUnidadRegional, Long idNivelEducativo);

    List<TipoActividadCriterioDTO> listCriteria(Long idTipoActividad);

    List<TipoActividadDTO> listActivityTypes();

    List<MateriaDTO> listSubjects(Long idPrograma, Long idCoordinacion);

    List<GrupoDTO> listSubjectGroup(String codigoMateria, Long idPeriodoUniversidad);

    List<ProyectoDTO> listProjectsProfessor(Long idPersonaGeneral);

    void saveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto);

    List<DetalleCargaDocenteDTO> listDetailProfessorPreload(Long idCargaDocente);

    void updateDetailProfessorPreload(DetalleCargaDocenteDTO dto);

    void saveCareerProfessorPreload(CargaDocentePlantaDTO dto);

    void deleteProfessorActivity(Long idDetalleCargaDocente);

    TotalPreasignacionDTO getTotalPreassignment(Long idCarga);

    List<CoordinacionBusquedaDTO> searchCoordination(String nombre);

    void saveCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto);

    List<CoordinacionRestriccionDTO> listCoordinationRestriction(Long idConvocatoria);

    void updateCoordinationRestriction(Long id, CoordinacionRestriccionFormularioDTO dto);

    void deleteCoordinationRestriction(Long id, CoordinacionRestriccionDTO dto);

    void bulkDeleteCoordinationRestriction(List<CoordinacionRestriccionDTO> restricciones);
}
