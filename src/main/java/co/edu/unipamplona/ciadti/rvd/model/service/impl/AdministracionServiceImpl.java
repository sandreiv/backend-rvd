/**
 * Aplicación: rvd
 * Archivo: AdministracionServiceImpl.java
 * Paquete: Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
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
        log.debug("getAssociationCatalogs ===> Consultando catálogos de administración");

        AsociacionCoordinacionCatalogosDTO result = new AsociacionCoordinacionCatalogosDTO(
                mapCatalog(coordinacionRepository.findAdministrationOptions()),
                mapCatalog(programaRepository.findAdministrationOptions()),
                mapSubjectCatalog(materiaRepository.findAdministrationOptions()),
                mapCatalog(centroCostoRepository.findAdministrationOptions()),
                mapCatalog(personaGeneralRepository.findAdministrationOptions())
        );

        log.info("getAssociationCatalogs ===> Catálogos de administración consultados");
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociacionCoordinacionListadoDTO> listCoordinationAssociations() {
        log.debug("listCoordinationAssociations ===> Listando asociaciones de coordinación");

        List<AsociacionCoordinacionListadoDTO> result = asociacionCoordinacionRepository.findAdministrationList()
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

        log.info("listCoordinationAssociations ===> Asociaciones de coordinación listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveCoordinationAssociation(AsociacionCoordinacionFormularioDTO dto) {
        log.info("saveCoordinationAssociation ===> Guardando asociación de coordinación. idCoordinacion={}, idPrograma={}, codigoMateria={}, idCentroCosto={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idPrograma() : null,
                dto != null ? dto.codigoMateria() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateAssociation(dto);

        AsociacionCoordinacionEntity association = new AsociacionCoordinacionEntity();
        fillAssociation(association, dto);
        AsociacionCoordinacionEntity saved = asociacionCoordinacionRepository.save(association);

        log.info("saveCoordinationAssociation ===> Asociación de coordinación guardada. id={}", saved.getId());
    }

    @Override
    @Transactional
    public void updateCoordinationAssociation(Long id, AsociacionCoordinacionFormularioDTO dto) {
        log.info("updateCoordinationAssociation ===> Actualizando asociación de coordinación. id={}, idCoordinacion={}, idPrograma={}, codigoMateria={}, idCentroCosto={}",
                id,
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idPrograma() : null,
                dto != null ? dto.codigoMateria() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateAssociation(dto);

        AsociacionCoordinacionEntity association = asociacionCoordinacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateCoordinationAssociation ===> Asociación de coordinación no encontrada. id={}", id);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id);
                });

        fillAssociation(association, dto);
        asociacionCoordinacionRepository.save(association);

        log.info("updateCoordinationAssociation ===> Asociación de coordinación actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCoordinationAssociation(Long id) {
        log.info("deleteCoordinationAssociation ===> Eliminando asociación de coordinación. id={}", id);

        if (!asociacionCoordinacionRepository.existsById(id)) {
            log.warn("deleteCoordinationAssociation ===> Asociación de coordinación no encontrada. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la asociación con id " + id);
        }

        BigDecimal result = asociacionCoordinacionRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteCoordinationAssociation ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la asociación coordinación");

        log.info("deleteCoordinationAssociation ===> Asociación de coordinación eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkCoordinationAssociations(List<Long> ids) {
        log.info("deleteBulkCoordinationAssociations ===> Eliminación masiva de asociaciones de coordinación. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCoordinationAssociations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCoordinationAssociation(id);
        }

        log.info("deleteBulkCoordinationAssociations ===> Eliminación masiva de asociaciones finalizada. total={}",
                ids.size());
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
        log.debug("listCostCenterAssignments ===> Listando centros de costo asignados");

        List<CentroCostoAsignadoListadoDTO> result = asignarCentroCostoRepository.findAdministrationList()
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

        log.info("listCostCenterAssignments ===> Centros de costo asignados listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveCostCenterAssignment(CentroCostoAsignadoFormularioDTO dto) {
        log.info("saveCostCenterAssignment ===> Guardando centro de costo asignado. idCoordinacion={}, idCentroCosto={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCostCenterAssignment(dto);

        AsignarCentroCostoEntity entity = new AsignarCentroCostoEntity();
        fillCostCenterAssignment(entity, dto);
        AsignarCentroCostoEntity saved = asignarCentroCostoRepository.save(entity);

        log.info("saveCostCenterAssignment ===> Centro de costo asignado guardado. id={}", saved.getId());
    }

    @Override
    @Transactional
    public void updateCostCenterAssignment(Long id, CentroCostoAsignadoFormularioDTO dto) {
        log.info("updateCostCenterAssignment ===> Actualizando centro de costo asignado. id={}, idCoordinacion={}, idCentroCosto={}",
                id,
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCostCenterAssignment(dto);

        AsignarCentroCostoEntity entity = asignarCentroCostoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateCostCenterAssignment ===> Centro de costo asignado no encontrado. id={}", id);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id);
                });

        fillCostCenterAssignment(entity, dto);
        asignarCentroCostoRepository.save(entity);

        log.info("updateCostCenterAssignment ===> Centro de costo asignado actualizado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCostCenterAssignment(Long id) {
        log.info("deleteCostCenterAssignment ===> Eliminando centro de costo asignado. id={}", id);

        if (!asignarCentroCostoRepository.existsById(id)) {
            log.warn("deleteCostCenterAssignment ===> Centro de costo asignado no encontrado. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el centro de costo asignado con id " + id);
        }

        BigDecimal result = asignarCentroCostoRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteCostCenterAssignment ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar el centro de costo asignado");

        log.info("deleteCostCenterAssignment ===> Centro de costo asignado eliminado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkCostCenterAssignments(List<Long> ids) {
        log.info("deleteBulkCostCenterAssignments ===> Eliminación masiva de centros de costo asignados. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCostCenterAssignments ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCostCenterAssignment(id);
        }

        log.info("deleteBulkCostCenterAssignments ===> Eliminación masiva de centros de costo finalizada. total={}",
                ids.size());
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
        log.debug("listPeopleCoordinations ===> Listando personas asociadas a coordinaciones");

        List<PersonaCoordinacionListadoDTO> result = personaCoordinacionRepository.findAdministrationList()
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

        log.info("listPeopleCoordinations ===> Personas asociadas a coordinaciones listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void savePeopleCoordination(PersonaCoordinacionFormularioDTO dto) {
        log.info("savePeopleCoordination ===> Guardando persona asociada a coordinación. idPersonaGeneral={}, idCoordinacion={}",
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCoordinacion() : null);

        validatePersonCoordination(dto);

        PersonaCoordinacionEntity entity = new PersonaCoordinacionEntity();
        fillPeopleCoordination(entity, dto);
        personaCoordinacionRepository.save(entity);

        log.info("savePeopleCoordination ===> Persona asociada a coordinación guardada. idPersonaGeneral={}, idCoordinacion={}",
                dto.idPersonaGeneral(), dto.idCoordinacion());
    }

    @Override
    @Transactional
    public void updatePeopleCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto) {
        log.info("updatePeopleCoordination ===> Actualizando persona asociada a coordinación. oldIdPersonaGeneral={}, oldIdCoordinacion={}, newIdPersonaGeneral={}, newIdCoordinacion={}",
                oldIdPersonaGeneral,
                oldIdCoordinacion,
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCoordinacion() : null);

        validatePersonCoordination(dto);

        PersonaCoordinacionEntityId oldId = personaCoordinationId(oldIdPersonaGeneral, oldIdCoordinacion);

        if (!personaCoordinacionRepository.existsById(oldId)) {
            log.warn("updatePeopleCoordination ===> Persona asociada a coordinación no encontrada. idPersonaGeneral={}, idCoordinacion={}",
                    oldIdPersonaGeneral, oldIdCoordinacion);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asociada a la coordinación");
        }

        boolean keyChanged = !oldIdPersonaGeneral.equals(dto.idPersonaGeneral())
                || !oldIdCoordinacion.equals(dto.idCoordinacion());

        if (keyChanged) {
            log.info("updatePeopleCoordination ===> Cambio de llave detectado. Eliminando asociación anterior. oldIdPersonaGeneral={}, oldIdCoordinacion={}",
                    oldIdPersonaGeneral, oldIdCoordinacion);

            BigDecimal result = personaCoordinacionRepository.deleteByProcedure(
                    oldIdPersonaGeneral,
                    oldIdCoordinacion,
                    REGISTRADO_POR
            );

            if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
                log.warn("updatePeopleCoordination ===> Procedimiento de eliminación de asociación anterior falló. oldIdPersonaGeneral={}, oldIdCoordinacion={}, resultado={}",
                        oldIdPersonaGeneral, oldIdCoordinacion, result);
            }

            validateProcedureResult(result, "No se pudo eliminar la persona asociada anterior");
        }

        PersonaCoordinacionEntity entity = new PersonaCoordinacionEntity();
        fillPeopleCoordination(entity, dto);
        personaCoordinacionRepository.save(entity);

        log.info("updatePeopleCoordination ===> Persona asociada a coordinación actualizada. idPersonaGeneral={}, idCoordinacion={}",
                dto.idPersonaGeneral(), dto.idCoordinacion());
    }

    @Override
    @Transactional
    public void deletePeopleCoordination(Long idPersonaGeneral, Long idCoordinacion) {
        log.info("deletePeopleCoordination ===> Eliminando persona asociada a coordinación. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);

        PersonaCoordinacionEntityId id = personaCoordinationId(idPersonaGeneral, idCoordinacion);

        if (!personaCoordinacionRepository.existsById(id)) {
            log.warn("deletePeopleCoordination ===> Persona asociada a coordinación no encontrada. idPersonaGeneral={}, idCoordinacion={}",
                    idPersonaGeneral, idCoordinacion);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona asociada a la coordinación");
        }

        BigDecimal result = personaCoordinacionRepository.deleteByProcedure(
                idPersonaGeneral,
                idCoordinacion,
                REGISTRADO_POR
        );

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deletePeopleCoordination ===> Procedimiento de eliminación falló. idPersonaGeneral={}, idCoordinacion={}, resultado={}",
                    idPersonaGeneral, idCoordinacion, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la persona asociada a la coordinación");

        log.info("deletePeopleCoordination ===> Persona asociada a coordinación eliminada. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);
    }

    @Override
    @Transactional
    public void deleteBulkPeopleCoordinations(List<PersonaCoordinacionClaveDTO> registros) {
        log.info("deleteBulkPeopleCoordinations ===> Eliminación masiva de personas asociadas a coordinación. total={}",
                registros != null ? registros.size() : 0);

        if (registros == null || registros.isEmpty()) {
            log.debug("deleteBulkPeopleCoordinations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (PersonaCoordinacionClaveDTO item : registros) {
            deletePeopleCoordination(item.idPersonaGeneral(), item.idCoordinacion());
        }

        log.info("deleteBulkPeopleCoordinations ===> Eliminación masiva de personas asociadas finalizada. total={}",
                registros.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaCoordinacionListadoDTO> listPlantProfessorCoordinations() {
        log.debug("listPlantProfessorCoordinations ===> Listando docentes planta asociados a coordinaciones");

        List<PersonaCoordinacionListadoDTO> result = docentesPlantaCoordinacionRepository.findAdministrationList()
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

        log.info("listPlantProfessorCoordinations ===> Docentes planta asociados listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void savePlantProfessorCoordination(PersonaCoordinacionFormularioDTO dto) {
        log.info("savePlantProfessorCoordination ===> Guardando docente planta asociado a coordinación. idPersonaGeneral={}, idCoordinacion={}",
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCoordinacion() : null);

        validatePersonCoordination(dto);
        
        DocentesPlantaCoordinacionEntity entity = new DocentesPlantaCoordinacionEntity();
        fillPlantProfessorCoordination(entity, dto);
        docentesPlantaCoordinacionRepository.save(entity);

        log.info("savePlantProfessorCoordination ===> Docente planta asociado guardado. idPersonaGeneral={}, idCoordinacion={}",
                dto.idPersonaGeneral(), dto.idCoordinacion());
    }

    @Override
    @Transactional
    public void updatePlantProfessorCoordination(Long oldIdPersonaGeneral, Long oldIdCoordinacion, PersonaCoordinacionFormularioDTO dto) {
        log.info("updatePlantProfessorCoordination ===> Actualizando docente planta asociado. oldIdPersonaGeneral={}, oldIdCoordinacion={}, newIdPersonaGeneral={}, newIdCoordinacion={}",
                oldIdPersonaGeneral,
                oldIdCoordinacion,
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCoordinacion() : null);

        validatePersonCoordination(dto);

        DocentesPlantaCoordinacionEntityId oldId = plantProfessorCoordinationId(oldIdPersonaGeneral, oldIdCoordinacion);

        if (!docentesPlantaCoordinacionRepository.existsById(oldId)) {
            log.warn("updatePlantProfessorCoordination ===> Docente planta asociado no encontrado. idPersonaGeneral={}, idCoordinacion={}",
                    oldIdPersonaGeneral, oldIdCoordinacion);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el docente planta asociado a la coordinación");
        }

        boolean keyChanged = !oldIdPersonaGeneral.equals(dto.idPersonaGeneral())
                || !oldIdCoordinacion.equals(dto.idCoordinacion());

        if (keyChanged) {
            log.info("updatePlantProfessorCoordination ===> Cambio de llave detectado. Eliminando docente planta asociado anterior. oldIdPersonaGeneral={}, oldIdCoordinacion={}",
                    oldIdPersonaGeneral, oldIdCoordinacion);

            BigDecimal result = docentesPlantaCoordinacionRepository.deleteByProcedure(
                    oldIdPersonaGeneral,
                    oldIdCoordinacion,
                    REGISTRADO_POR
            );

            if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
                log.warn("updatePlantProfessorCoordination ===> Procedimiento de eliminación de docente planta anterior falló. oldIdPersonaGeneral={}, oldIdCoordinacion={}, resultado={}",
                        oldIdPersonaGeneral, oldIdCoordinacion, result);
            }

            validateProcedureResult(result, "No se pudo eliminar el docente planta asociado anterior");
        }

        DocentesPlantaCoordinacionEntity entity = new DocentesPlantaCoordinacionEntity();
        fillPlantProfessorCoordination(entity, dto);
        docentesPlantaCoordinacionRepository.save(entity);

        log.info("updatePlantProfessorCoordination ===> Docente planta asociado actualizado. idPersonaGeneral={}, idCoordinacion={}",
                dto.idPersonaGeneral(), dto.idCoordinacion());
    }

    @Override
    @Transactional
    public void deletePlantProfessorCoordination(Long idPersonaGeneral, Long idCoordinacion) {
        log.info("deletePlantProfessorCoordination ===> Eliminando docente planta asociado. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);

        DocentesPlantaCoordinacionEntityId id = plantProfessorCoordinationId(idPersonaGeneral, idCoordinacion);

        if (!docentesPlantaCoordinacionRepository.existsById(id)) {
            log.warn("deletePlantProfessorCoordination ===> Docente planta asociado no encontrado. idPersonaGeneral={}, idCoordinacion={}",
                    idPersonaGeneral, idCoordinacion);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el docente planta asociado a la coordinación");
        }

        BigDecimal result = docentesPlantaCoordinacionRepository.deleteByProcedure(
                idPersonaGeneral,
                idCoordinacion,
                REGISTRADO_POR
        );

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deletePlantProfessorCoordination ===> Procedimiento de eliminación falló. idPersonaGeneral={}, idCoordinacion={}, resultado={}",
                    idPersonaGeneral, idCoordinacion, result);
        }

        validateProcedureResult(result, "No se pudo eliminar el docente planta asociado a la coordinación");

        log.info("deletePlantProfessorCoordination ===> Docente planta asociado eliminado. idPersonaGeneral={}, idCoordinacion={}",
                idPersonaGeneral, idCoordinacion);
    }

    @Override
    @Transactional
    public void deleteBulkPlantProfessorCoordinations(List<PersonaCoordinacionClaveDTO> registros) {
        log.info("deleteBulkPlantProfessorCoordinations ===> Eliminación masiva de docentes planta asociados. total={}",
                registros != null ? registros.size() : 0);

        if (registros == null || registros.isEmpty()) {
            log.debug("deleteBulkPlantProfessorCoordinations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (PersonaCoordinacionClaveDTO item : registros) {
            deletePlantProfessorCoordination(item.idPersonaGeneral(), item.idCoordinacion());
        }

        log.info("deleteBulkPlantProfessorCoordinations ===> Eliminación masiva de docentes planta finalizada. total={}",
                registros.size());
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
    
    private void validateProcedureResult(BigDecimal result, String message) {
        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, message);
        }
    }



}

/* 17/07/2026 @:Daniel Arias */
