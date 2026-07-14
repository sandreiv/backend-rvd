package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaCatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.AsignarCentroCostoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.AsociacionCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsignarCentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsociacionCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MateriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaCatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.AdministracionService;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionClaveDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntityId;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntityId;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocentesPlantaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministracionServiceImpl implements AdministracionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";

    private final AsociacionCoordinacionRepository asociacionCoordinacionRepository;
    private final AsignarCentroCostoRepository asignarCentroCostoRepository;
    private final CoordinacionRepository coordinacionRepository;
    private final ProgramaRepository programaRepository;
    private final MateriaRepository materiaRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PersonaCoordinacionRepository personaCoordinacionRepository;
    private final DocentesPlantaCoordinacionRepository docentesPlantaCoordinacionRepository;

    @Override
    @Transactional(readOnly = true)
    public AsociacionCoordinacionCatalogosDTO getAssociationCatalogs() {
        return new AsociacionCoordinacionCatalogosDTO(
                mapCatalog(coordinacionRepository.findAdministrationOptions()),
                mapCatalog(programaRepository.findAdministrationOptions()),
                mapSubjectCatalog(materiaRepository.findAdministrationOptions()),
                mapCatalog(centroCostoRepository.findAdministrationOptions()),
                mapCatalog(personaGeneralRepository.findAdministrationOptions())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociacionCoordinacionListadoDTO> listCoordinationAssociations() {
        return asociacionCoordinacionRepository.findAdministrationList()
                .stream()
                .map(item -> new AsociacionCoordinacionListadoDTO(
                        item.getId(),
                        item.getIdCoordinacion(),
                        item.getCoordinacion(),
                        item.getIdPrograma(),
                        item.getPrograma(),
                        item.getCodigoMateria(),
                        item.getMateria(),
                        item.getIdCentroCosto(),
                        item.getCentroCosto(),
                        item.getEstado()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void saveCoordinationAssociation(AsociacionCoordinacionFormularioDTO dto) {
        validateAssociation(dto);

        AsociacionCoordinacionEntity association = new AsociacionCoordinacionEntity();
        fillAssociation(association, dto);
        asociacionCoordinacionRepository.save(association);
    }

    @Override
    @Transactional
    public void updateCoordinationAssociation(Long id, AsociacionCoordinacionFormularioDTO dto) {
        validateAssociation(dto);

        AsociacionCoordinacionEntity association = asociacionCoordinacionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id));

        fillAssociation(association, dto);
        asociacionCoordinacionRepository.save(association);
    }

    @Override
    @Transactional
    public void deleteCoordinationAssociation(Long id) {
        if (!asociacionCoordinacionRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id);
        }

        asociacionCoordinacionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkCoordinationAssociations(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        asociacionCoordinacionRepository.deleteAllById(ids);
    }

    private void fillAssociation(AsociacionCoordinacionEntity association, AsociacionCoordinacionFormularioDTO dto) {
        String codigoMateria = normalizeText(dto.codigoMateria());
        boolean hasProgram = dto.idPrograma() != null;
        boolean hasSubject = StringUtils.hasText(codigoMateria);

        association.setIdCoordinacion(dto.idCoordinacion());
        association.setIdPrograma(hasProgram ? dto.idPrograma() : null);
        association.setCodigoMateria(hasSubject ? codigoMateria : null);
        association.setIdCentroCosto(hasProgram ? dto.idCentroCosto() : null);

        association.setEstado(normalizeStatus(dto.estado()));
        association.setRegistradoPor(REGISTRADO_POR);
        association.setFechaCambio(new Date());
    }

    private void validateAssociation(AsociacionCoordinacionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la asociación es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        String codigoMateria = normalizeText(dto.codigoMateria());

        if (dto.idPrograma() != null && StringUtils.hasText(codigoMateria)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debe seleccionar programa o materia, no ambos");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }

        if (dto.idPrograma() != null && !programaRepository.existsById(dto.idPrograma())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El programa no existe");
        }

        if (StringUtils.hasText(codigoMateria) && !materiaRepository.existsById(codigoMateria)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La materia no existe");
        }

        if (dto.idCentroCosto() != null && !centroCostoRepository.existsById(dto.idCentroCosto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El centro de costo no existe");
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "1";
        }

        String normalized = status.trim().toUpperCase();

        return "0".equals(normalized) || "INACTIVO".equals(normalized) || "I".equals(normalized) ? "0" : "1";
    }

    private String normalizeText(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private List<CatalogoAdministracionDTO> mapCatalog(List<CatalogoAdministracionProjection> projections) {
        return projections.stream()
                .map(item -> new CatalogoAdministracionDTO(item.getId(), item.getLabel(), item.getCodigo()))
                .toList();
    }

    private List<MateriaCatalogoAdministracionDTO> mapSubjectCatalog(List<MateriaCatalogoAdministracionProjection> projections) {
        return projections.stream()
                .map(item -> new MateriaCatalogoAdministracionDTO(item.getCodigoMateria(), item.getLabel()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCostoAsignadoListadoDTO> listCostCenterAssignments() {
        return asignarCentroCostoRepository.findAdministrationList()
                .stream()
                .map(item -> new CentroCostoAsignadoListadoDTO(
                        item.getId(),
                        item.getIdCoordinacion(),
                        item.getCoordinacion(),
                        item.getIdCentroCosto(),
                        item.getCentroCosto(),
                        item.getEstado()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void saveCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto) {
        validateCostCenterAssignment(dto);

        AsignarCentroCostoEntity entity = new AsignarCentroCostoEntity();
        fillCostCenterAssignment(entity, dto);
        asignarCentroCostoRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateCostCenterAssignment(Long id, CentroCostoAsignadoFormularioDTO dto) {
        validateCostCenterAssignment(dto);

        AsignarCentroCostoEntity entity = asignarCentroCostoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id));

        fillCostCenterAssignment(entity, dto);
        asignarCentroCostoRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteCostCenterAssignment(Long id) {
        if (!asignarCentroCostoRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id);
        }

        asignarCentroCostoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkCostCenterAssignments(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        asignarCentroCostoRepository.deleteAllById(ids);
    }

    private void fillCostCenterAssignment(
            AsignarCentroCostoEntity entity,
            CentroCostoAsignadoFormularioDTO dto
    ) {
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setIdCentroCosto(dto.idCentroCosto());
        entity.setEstado("1".equals(normalizeStatus(dto.estado())) ? 1L : 0L);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validateCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del centro de costo es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        if (dto.idCentroCosto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo es obligatorio");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }

        if (!centroCostoRepository.existsById(dto.idCentroCosto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El centro de costo no existe");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaCoordinacionListadoDTO> listPeopleCoordinations() {
        return personaCoordinacionRepository.findAdministrationList()
                .stream()
                .map(item -> new PersonaCoordinacionListadoDTO(
                        item.getIdPersonaGeneral(),
                        item.getPersona(),
                        item.getDocumentoIdentidad(),
                        item.getIdCoordinacion(),
                        item.getCoordinacion(),
                        item.getEstado()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void savePeopleCoordination(PersonaCoordinacionFormularioDTO dto) {
        validatePersonCoordination(dto);

        PersonaCoordinacionEntity entity = new PersonaCoordinacionEntity();
        fillPeopleCoordination(entity, dto);
        personaCoordinacionRepository.save(entity);
    }

    @Override
    @Transactional
    public void updatePeopleCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto) {
        validatePersonCoordination(dto);

        PersonaCoordinacionEntityId oldId = personaCoordinationId(oldIdPersonaGeneral, oldIdCoordinacion);

        if (!personaCoordinacionRepository.existsById(oldId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asociada a la coordinación");
        }

        boolean keyChanged = !oldIdPersonaGeneral.equals(dto.idPersonaGeneral())
                || !oldIdCoordinacion.equals(dto.idCoordinacion());

        if (keyChanged) {
            personaCoordinacionRepository.deleteById(oldId);
        }

        PersonaCoordinacionEntity entity = new PersonaCoordinacionEntity();
        fillPeopleCoordination(entity, dto);
        personaCoordinacionRepository.save(entity);
    }

    @Override
    @Transactional
    public void deletePeopleCoordination(Long idPersonaGeneral, Long idCoordinacion) {
        PersonaCoordinacionEntityId id = personaCoordinationId(idPersonaGeneral, idCoordinacion);

        if (!personaCoordinacionRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asociada a la coordinación");
        }

        personaCoordinacionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkPeopleCoordinations(List<PersonaCoordinacionClaveDTO> registros) {
        if (registros == null || registros.isEmpty()) {
            return;
        }

        personaCoordinacionRepository.deleteAllById(
                registros.stream()
                        .map(item -> personaCoordinationId(item.idPersonaGeneral(), item.idCoordinacion()))
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaCoordinacionListadoDTO> listPlantProfessorCoordinations() {
        return docentesPlantaCoordinacionRepository.findAdministrationList()
                .stream()
                .map(item -> new PersonaCoordinacionListadoDTO(
                        item.getIdPersonaGeneral(),
                        item.getPersona(),
                        item.getDocumentoIdentidad(),
                        item.getIdCoordinacion(),
                        item.getCoordinacion(),
                        item.getEstado()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void savePlantProfessorCoordination(PersonaCoordinacionFormularioDTO dto) {
        validatePersonCoordination(dto);

        DocentesPlantaCoordinacionEntity entity = new DocentesPlantaCoordinacionEntity();
        fillPlantProfessorCoordination(entity, dto);
        docentesPlantaCoordinacionRepository.save(entity);
    }

    @Override
    @Transactional
    public void updatePlantProfessorCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto) {
        validatePersonCoordination(dto);

        DocentesPlantaCoordinacionEntityId oldId = plantProfessorCoordinationId(oldIdPersonaGeneral, oldIdCoordinacion);

        if (!docentesPlantaCoordinacionRepository.existsById(oldId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el docente planta asociado a la coordinación");
        }

        boolean keyChanged = !oldIdPersonaGeneral.equals(dto.idPersonaGeneral())
                || !oldIdCoordinacion.equals(dto.idCoordinacion());

        if (keyChanged) {
            docentesPlantaCoordinacionRepository.deleteById(oldId);
        }

        DocentesPlantaCoordinacionEntity entity = new DocentesPlantaCoordinacionEntity();
        fillPlantProfessorCoordination(entity, dto);
        docentesPlantaCoordinacionRepository.save(entity);
    }

    @Override
    @Transactional
    public void deletePlantProfessorCoordination(Long idPersonaGeneral, Long idCoordinacion) {
        DocentesPlantaCoordinacionEntityId id = plantProfessorCoordinationId(idPersonaGeneral, idCoordinacion);

        if (!docentesPlantaCoordinacionRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el docente planta asociado a la coordinación");
        }

        docentesPlantaCoordinacionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkPlantProfessorCoordinations(List<PersonaCoordinacionClaveDTO> registros) {
        if (registros == null || registros.isEmpty()) {
            return;
        }

        docentesPlantaCoordinacionRepository.deleteAllById(
                registros.stream()
                        .map(item -> plantProfessorCoordinationId(item.idPersonaGeneral(), item.idCoordinacion()))
                        .toList()
        );
    }

    private void fillPeopleCoordination(PersonaCoordinacionEntity entity, PersonaCoordinacionFormularioDTO dto) {
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setEstado("1".equals(normalizeStatus(dto.estado())) ? 1L : 0L);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void fillPlantProfessorCoordination(DocentesPlantaCoordinacionEntity entity, PersonaCoordinacionFormularioDTO dto) {
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setEstado(normalizeStatus(dto.estado()));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validatePersonCoordination(PersonaCoordinacionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información es obligatoria");
        }

        if (dto.idPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La persona no existe");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La coordinación no existe");
        }
    }

    private PersonaCoordinacionEntityId personaCoordinationId(Long idPersonaGeneral, Long idCoordinacion) {
        PersonaCoordinacionEntityId id = new PersonaCoordinacionEntityId();
        id.setIdPersonaGeneral(idPersonaGeneral);
        id.setIdCoordinacion(idCoordinacion);
        return id;
    }

    private DocentesPlantaCoordinacionEntityId plantProfessorCoordinationId(Long idPersonaGeneral, Long idCoordinacion) {
        DocentesPlantaCoordinacionEntityId id = new DocentesPlantaCoordinacionEntityId();
        id.setIdPersonaGeneral(idPersonaGeneral);
        id.setIdCoordinacion(idCoordinacion);
        return id;
    }    



}