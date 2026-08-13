/**
 * Aplicación: rvd
 * Archivo: ProyectosServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaProyectosMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectosMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionConvocatoriasEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaProyectosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProyectosRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionConvocatoriasRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ConvocatoriaProyectosListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectosListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProyectosServiceImpl implements ProyectosService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";
    private static final String ESTADO_ACTIVO = "1";

    private final ProyectosRepository proyectosRepository;
    private final TipoProyectoRepository tipoProyectoRepository;
    private final ConvocatoriaProyectosRepository convocatoriaProyectosRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final RelacionConvocatoriasRepository relacionConvocatoriasRepository;
    private final CoordinacionRepository coordinacionRepository;
    private final PersonaProyectoRepository personaProyectoRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final ProyectosMapper proyectosMapper;
    private final TipoProyectoMapper tipoProyectoMapper;
    private final ConvocatoriaProyectosMapper convocatoriaProyectosMapper;
    private final PersonaProyectoMapper personaProyectoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProjects() {
        log.debug("listProjects ===> Listando proyectos padre");

        List<ProyectosListaProjection> projects =
                proyectosRepository.findParentProjectsList();
        List<ProyectosListaDTO> result =
                proyectosMapper.toProyectosListaDTOList(projects);

        log.info("listProjects ===> Proyectos padre listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectosListaDTO> listProducts(Long idProyecto) {
        log.debug("listProducts ===> Listando productos. idProyecto={}", idProyecto);

        if (idProyecto == null || !proyectosRepository.existsById(idProyecto)) {
            log.warn("listProducts ===> Proyecto padre no encontrado. idProyecto={}", idProyecto);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto padre");
        }

        List<ProyectosListaProjection> products =
                proyectosRepository.findProductsByParentProjectId(idProyecto);
        List<ProyectosListaDTO> result = proyectosMapper.toProyectosListaDTOList(products);

        log.info("listProducts ===> Productos listados. idProyecto={}, total={}",
                idProyecto, result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveProject(ProyectosFormularioDTO dto) {
        log.info("saveProject ===> Guardando proyecto. nombre={}, idProyectoPadre={}",
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idProyectoPadre() : null);

        validateProject(dto);

        ProyectosEntity entity = new ProyectosEntity();
        fillProject(entity, dto);
        ProyectosEntity saved = proyectosRepository.save(entity);

        log.info("saveProject ===> Proyecto guardado. id={}, idProyectoPadre={}", saved.getId(), saved.getIdProyectoPadre());
    }

    @Override
    @Transactional
    public void updateProject(Long id, ProyectosFormularioDTO dto) {
        log.info("updateProject ===> Actualizando proyecto. id={}, nombre={}",
                id, dto != null ? dto.nombre() : null);

        validateProject(dto);

        ProyectosEntity entity = proyectosRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateProject ===> Proyecto no encontrado. id={}", id);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto con id " + id
                    );
                });

        fillProject(entity, dto);
        proyectosRepository.save(entity);

        log.info("updateProject ===> Proyecto actualizado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        log.info("deleteProject ===> Eliminando proyecto. id={}", id);

        if (id == null || !proyectosRepository.existsById(id)) {
            log.warn("deleteProject ===> Proyecto no encontrado. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto con id " + id
            );
        }

        if (proyectosRepository.existsByIdProyectoPadre(id)) {
            log.warn("deleteProject ===> Eliminación bloqueada. Tiene productos. id={}", id);
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar el proyecto porque tiene productos asociados"
            );
        }

        if (personaProyectoRepository.existsByIdProyecto(id)) {
            log.warn("deleteProject ===> Eliminación bloqueada. Tiene personas. id={}", id);
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar el proyecto porque tiene personas asociadas"
            );
        }

        BigDecimal result = proyectosRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteProject ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar el proyecto");

        log.info("deleteProject ===> Proyecto eliminado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkProjects(List<Long> ids) {
        log.info("deleteBulkProjects ===> Eliminación masiva de proyectos. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkProjects ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteProject(id);
        }

        log.info("deleteBulkProjects ===> Eliminación masiva finalizada. total={}", ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaProyectoListaDTO> listProjectPersons(Long idProyecto) {
        log.debug("listProjectPersons ===> Listando personas. idProyecto={}", idProyecto);

        if (idProyecto == null || !proyectosRepository.existsById(idProyecto)) {
            log.warn("listProjectPersons ===> Proyecto no encontrado. idProyecto={}", idProyecto);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto");
        }

        List<PersonaProyectoListaProjection> persons = personaProyectoRepository.findByIdProyecto(idProyecto);
        List<PersonaProyectoListaDTO> result = personaProyectoMapper.toPersonaProyectoListaDTOList(persons);

        log.info("listProjectPersons ===> Personas listadas. idProyecto={}, total={}", idProyecto, result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveProjectPerson(PersonaProyectoFormularioDTO dto) {
        log.info("saveProjectPerson ===> Guardando persona. idProyecto={}, idPersonaGeneral={}",
                dto != null ? dto.idProyecto() : null,
                dto != null ? dto.idPersonaGeneral() : null);

        validateProjectPerson(dto);

        PersonaProyectoEntity entity = new PersonaProyectoEntity();
        fillProjectPerson(entity, dto);
        personaProyectoRepository.save(entity);

        log.info("saveProjectPerson ===> Persona guardada. idProyecto={}, idPersonaGeneral={}", dto.idProyecto(), dto.idPersonaGeneral());
    }

    @Override
    @Transactional
    public void updateProjectPerson(Long id, PersonaProyectoFormularioDTO dto) {
        log.info("updateProjectPerson ===> Actualizando persona. id={}", id);

        validateProjectPerson(dto);

        PersonaProyectoEntity entity = personaProyectoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateProjectPerson ===> Persona no encontrada. id={}", id);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe la persona del proyecto con id " + id
                    );
                });

        fillProjectPerson(entity, dto);
        personaProyectoRepository.save(entity);

        log.info("updateProjectPerson ===> Persona actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteProjectPerson(Long id) {
        log.info("deleteProjectPerson ===> Eliminando persona. id={}", id);

        if (id == null || !personaProyectoRepository.existsById(id)) {
            log.warn("deleteProjectPerson ===> Persona no encontrada. id={}", id);
            throw new ApiException( HttpStatus.NOT_FOUND, "No existe la persona del proyecto con id " + id
            );
        }

        BigDecimal result = personaProyectoRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteProjectPerson ===> Procedimiento falló. id={}, resultado={}", id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la persona del proyecto");

        log.info("deleteProjectPerson ===> Persona eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkProjectPersons(List<Long> ids) {
        log.info("deleteBulkProjectPersons ===> Eliminación masiva. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkProjectPersons ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteProjectPerson(id);
        }

        log.info("deleteBulkProjectPersons ===> Eliminación masiva finalizada. total={}",
                ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoProyectoListaDTO> listProjectTypes() {
        log.debug("listProjectTypes ===> Listando tipos de proyecto");

        List<TipoProyectoEntity> projectTypes = tipoProyectoRepository.findAllProjectTypes();
        List<TipoProyectoListaDTO> result = tipoProyectoMapper.toTipoProyectoListaDTOList(projectTypes);

        log.info("listProjectTypes ===> Tipos de proyecto listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveProjectType(TipoProyectoFormularioDTO dto) {
        log.info("saveProjectType ===> Guardando tipo de proyecto. nombre={}, tipo={}", dto != null ? dto.nombre() : null, dto != null ? dto.tipo() : null);

        validateProjectType(dto);

        TipoProyectoEntity entity = new TipoProyectoEntity();
        fillProjectType(entity, dto);
        tipoProyectoRepository.save(entity);

        log.info("saveProjectType ===> Tipo de proyecto guardado. nombre={}, tipo={}", dto.nombre(), dto.tipo());
    }

    @Override
    @Transactional
    public void updateProjectType(Long id, TipoProyectoFormularioDTO dto) {
        log.info("updateProjectType ===> Actualizando tipo de proyecto. id={}, nombre={}, tipo={}",
                id,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.tipo() : null);

        validateProjectType(dto);

        TipoProyectoEntity entity = tipoProyectoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateProjectType ===> Tipo de proyecto no encontrado. id={}", id);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de proyecto con id " + id);
                });

        fillProjectType(entity, dto);
        tipoProyectoRepository.save(entity);

        log.info("updateProjectType ===> Tipo de proyecto actualizado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteProjectType(Long id) {
        log.info("deleteProjectType ===> Eliminando tipo de proyecto. id={}", id);

        if (id == null || !tipoProyectoRepository.existsById(id)) {
            log.warn("deleteProjectType ===> Tipo de proyecto no encontrado. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de proyecto con id " + id
            );
        }

        if (proyectosRepository.existsByIdTipoProyecto(id)) {
            log.warn("deleteProjectType ===> Eliminación bloqueada. Tiene proyectos asociados. id={}", id);
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar el tipo de proyecto porque tiene proyectos asociados"
            );
        }

        BigDecimal result = tipoProyectoRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteProjectType ===> Procedimiento de eliminación falló. id={}, resultado={}", id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar el tipo de proyecto");

        log.info("deleteProjectType ===> Tipo de proyecto eliminado. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkProjectTypes(List<Long> ids) {
        log.info("deleteBulkProjectTypes ===> Eliminación masiva de tipos de proyecto. total={}", ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkProjectTypes ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteProjectType(id);
        }

        log.info("deleteBulkProjectTypes ===> Eliminación masiva finalizada. total={}", ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaProyectosListaDTO> listProjectCalls() {
        log.debug("listProjectCalls ===> Listando convocatorias de proyectos");

        List<ConvocatoriaProyectosListaProjection> projectCalls = convocatoriaProyectosRepository.findAllProjectCalls();
        List<ConvocatoriaProyectosListaDTO> result = convocatoriaProyectosMapper.toConvocatoriaProyectosListaDTOList(projectCalls);

        log.info("listProjectCalls ===> Convocatorias de proyectos listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveProjectCall(ConvocatoriaProyectosFormularioDTO dto) {
        log.info("saveProjectCall ===> Guardando convocatoria de proyecto. nombre={}, codigo={}, idConvocatoria={}",
                dto != null ? dto.nombre() : null,
                dto != null ? dto.codigo() : null,
                dto != null ? dto.idConvocatoria() : null);

        validateProjectCall(dto);
        validateProjectCallRelation(dto);

        ConvocatoriaProyectosEntity entity = new ConvocatoriaProyectosEntity();
        fillProjectCall(entity, dto);
        ConvocatoriaProyectosEntity saved = convocatoriaProyectosRepository.save(entity);

        RelacionConvocatoriasEntity relation = new RelacionConvocatoriasEntity();
        relation.setIdConvocatoriaProyectos(saved.getId());
        relation.setIdConvocatoria(dto.idConvocatoria());
        relation.setEstado(ESTADO_ACTIVO);
        relation.setRegistradoPor(REGISTRADO_POR);
        relation.setFechaCambio(new Date());
        relacionConvocatoriasRepository.save(relation);

        log.info("saveProjectCall ===> Convocatoria de proyecto guardada. id={}, idConvocatoria={}",
                saved.getId(), dto.idConvocatoria());
    }

    @Override
    @Transactional
    public void updateProjectCall(
            Long id,
            ConvocatoriaProyectosFormularioDTO dto) {

        log.info(
                "updateProjectCall ===> Actualizando convocatoria de proyecto. id={}, nombre={}, codigo={}, idConvocatoria={}",
                id,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.codigo() : null,
                dto != null ? dto.idConvocatoria() : null
        );

        validateProjectCall(dto);
        validateProjectCallRelation(dto);

        ConvocatoriaProyectosEntity entity =
                convocatoriaProyectosRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn(
                                    "updateProjectCall ===> Convocatoria de proyecto no encontrada. id={}",
                                    id
                            );

                            return new ApiException(
                                    HttpStatus.NOT_FOUND,
                                    "No existe la convocatoria de proyecto con id " + id
                            );
                        });

        fillProjectCall(entity, dto);
        convocatoriaProyectosRepository.save(entity);

        syncProjectCallRelation(
                id,
                dto.idConvocatoria()
        );

        log.info(
                "updateProjectCall ===> Convocatoria de proyecto actualizada. id={}, idConvocatoria={}",
                id,
                dto.idConvocatoria()
        );
    }

    @Override
    @Transactional
    public void deleteProjectCall(Long id) {
        log.info("deleteProjectCall ===> Eliminando convocatoria de proyecto. id={}", id);

        if (id == null || !convocatoriaProyectosRepository.existsById(id)) {
            log.warn("deleteProjectCall ===> Convocatoria no encontrada. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la convocatoria de proyecto con id " + id
            );
        }

        if (proyectosRepository.existsByIdConvocatoriaProyectos(id)) {
            log.warn("deleteProjectCall ===> Eliminación bloqueada. Tiene proyectos asociados. id={}", id);
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se puede eliminar la convocatoria porque tiene proyectos asociados"
            );
        }

        BigDecimal result = convocatoriaProyectosRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteProjectCall ===> Procedimiento de eliminación falló. id={}, resultado={}", id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la convocatoria de proyecto");

        log.info("deleteProjectCall ===> Convocatoria de proyecto eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkProjectCalls(List<Long> ids) {
        log.info("deleteBulkProjectCalls ===> Eliminación masiva de convocatorias. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkProjectCalls ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteProjectCall(id);
        }

        log.info("deleteBulkProjectCalls ===> Eliminación masiva finalizada. total={}", ids.size());
    }

    private void syncProjectCallRelation(
            Long idConvocatoriaProyectos,
            Long idConvocatoria) {

        List<RelacionConvocatoriasEntity> relations =
                relacionConvocatoriasRepository
                        .findByIdConvocatoriaProyectos(
                                idConvocatoriaProyectos
                        );

        RelacionConvocatoriasEntity targetRelation = null;

        for (RelacionConvocatoriasEntity relation : relations) {

            boolean isTarget =
                    Objects.equals(
                            relation.getIdConvocatoria(),
                            idConvocatoria
                    );

            relation.setEstado(
                    isTarget ? ESTADO_ACTIVO : "0"
            );

            relation.setRegistradoPor(REGISTRADO_POR);
            relation.setFechaCambio(new Date());

            if (isTarget) {
                targetRelation = relation;
            }
        }

        if (!relations.isEmpty()) {
            relacionConvocatoriasRepository.saveAll(relations);
        }

        if (targetRelation != null) {
            return;
        }

        RelacionConvocatoriasEntity newRelation =
                new RelacionConvocatoriasEntity();

        newRelation.setIdConvocatoriaProyectos(
                idConvocatoriaProyectos
        );

        newRelation.setIdConvocatoria(
                idConvocatoria
        );

        newRelation.setEstado(ESTADO_ACTIVO);
        newRelation.setRegistradoPor(REGISTRADO_POR);
        newRelation.setFechaCambio(new Date());

        relacionConvocatoriasRepository.save(newRelation);
    }

    private void fillProject(ProyectosEntity entity, ProyectosFormularioDTO dto) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(normalizeOptional(dto.descripcion()));
        entity.setMonto(normalizeOptional(dto.monto()));
        entity.setFechaInicio(dto.fechaInicio());
        entity.setFechaFin(dto.fechaFin());
        entity.setIdConvocatoriaProyectos(dto.idConvocatoriaProyectos());
        entity.setIdTipoProyecto(dto.idTipoProyecto());
        entity.setIdCoordinacion(dto.idCoordinacion());
        entity.setIdProyectoPadre(dto.idProyectoPadre());
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validateProject(ProyectosFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del proyecto es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (dto.idConvocatoriaProyectos() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria de proyecto es obligatoria"
            );
        }

        if (!convocatoriaProyectosRepository.existsById(dto.idConvocatoriaProyectos())) {
            throw new ApiException( HttpStatus.NOT_FOUND, "No existe la convocatoria de proyecto con id " + dto.idConvocatoriaProyectos()
            );
        }

        if (dto.idTipoProyecto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de proyecto es obligatorio"
            );
        }

        if (!tipoProyectoRepository.existsById(dto.idTipoProyecto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de proyecto con id " + dto.idTipoProyecto()
            );
        }

        if (dto.idCoordinacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria"
            );
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación con id " + dto.idCoordinacion()
            );
        }

        if (dto.idProyectoPadre() != null
                && !proyectosRepository.existsById(dto.idProyectoPadre())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto padre con id " + dto.idProyectoPadre()
            );
        }

        if (dto.fechaInicio() != null
                && dto.fechaFin() != null
                && !dto.fechaFin().after(dto.fechaInicio())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La fecha fin debe ser posterior a la fecha inicio"
            );
        }
    }

    private void fillProjectPerson(
            PersonaProyectoEntity entity,
            PersonaProyectoFormularioDTO dto) {
        entity.setIdProyecto(dto.idProyecto());
        entity.setIdPersonaGeneral(dto.idPersonaGeneral());
        entity.setIdTipoActividad(dto.idTipoActividad());
        entity.setTipo(normalizeOptional(dto.tipo()));
        entity.setHoras(normalizeOptional(dto.horas()));
        entity.setObservacion(normalizeOptional(dto.observacion()));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validateProjectPerson(PersonaProyectoFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la persona del proyecto es obligatoria"
            );
        }

        if (dto.idProyecto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El proyecto es obligatorio");
        }

        if (!proyectosRepository.existsById(dto.idProyecto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el proyecto con id " + dto.idProyecto()
            );
        }

        if (dto.idPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.NOT_FOUND,"No existe la persona con id " + dto.idPersonaGeneral()
            );
        }

        if (dto.idTipoActividad() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad es obligatorio"
            );
        }

        if (!tipoActividadesRepository.existsById(dto.idTipoActividad())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de actividad con id " + dto.idTipoActividad()
            );
        }

        if (!StringUtils.hasText(dto.tipo())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo es obligatorio");
        }

        if (!StringUtils.hasText(dto.horas())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas son obligatorias");
        }
    }

    private void fillProjectCall(ConvocatoriaProyectosEntity entity, ConvocatoriaProyectosFormularioDTO dto) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion().trim());
        entity.setCodigo(dto.codigo().trim().toUpperCase());
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validateProjectCall(ConvocatoriaProyectosFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la convocatoria de proyecto es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (!StringUtils.hasText(dto.descripcion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }

        if (!StringUtils.hasText(dto.codigo())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El código es obligatorio");
        }
    }

    private void validateProjectCallRelation(ConvocatoriaProyectosFormularioDTO dto) {
        if (dto.idConvocatoria() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria es obligatoria");
        }

        if (!convocatoriaRepository.existsById(dto.idConvocatoria())) {
            log.warn("validateProjectCallRelation ===> Convocatoria no encontrada. idConvocatoria={}", dto.idConvocatoria());
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la convocatoria con id " + dto.idConvocatoria()
            );
        }
    }

    private void fillProjectType(TipoProyectoEntity entity, TipoProyectoFormularioDTO dto) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion().trim());
        entity.setMinimoParticipantes(normalizeOptional(dto.minimoParticipantes()));
        entity.setMaximoParticipantes(normalizeOptional(dto.maximoParticipantes()));
        entity.setMontoMaximo(normalizeOptional(dto.montoMaximo()));
        entity.setMinimoProductos(normalizeOptional(dto.minimoProductos()));
        entity.setMinimoConocimientoTi(normalizeOptional(dto.minimoConocimientoTi()));
        entity.setTipo(dto.tipo().trim());
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void validateProjectType(TipoProyectoFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información del tipo de proyecto es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (!StringUtils.hasText(dto.descripcion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }

        if (!StringUtils.hasText(dto.tipo())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo es obligatorio");
        }

        validateNumericRange(
                dto.minimoParticipantes(),
                dto.maximoParticipantes(),
                "participantes"
        );
    }

    private void validateNumericRange(String minimo, String maximo, String campo) {
        Integer minValue = parseOptionalInteger(minimo, "El mínimo de " + campo);
        Integer maxValue = parseOptionalInteger(maximo, "El máximo de " + campo);

        if (minValue != null && minValue < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El mínimo de " + campo + " no puede ser negativo"
            );
        }

        if (maxValue != null && maxValue < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El máximo de " + campo + " no puede ser negativo"
            );
        }

        if (minValue != null && maxValue != null && maxValue < minValue) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El máximo de " + campo + " debe ser mayor o igual al mínimo"
            );
        }
    }

    private Integer parseOptionalInteger(String value, String fieldLabel) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, fieldLabel + " debe ser un número entero"
            );
        }
    }

    private String normalizeOptional(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private void validateProcedureResult(BigDecimal result, String message) {
        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, message);
        }
    }

}
