/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionServiceImpl.java
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
import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.AsignarCentroCostoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CoordinacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsignarCentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MetodologiaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.UnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionAdministracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinacionAdministracionServiceImpl implements CoordinacionAdministracionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";

    private final CoordinacionRepository coordinacionRepository;
    private final UnidadRepository unidadRepository;
    private final ModalidadRepository modalidadRepository;
    private final MetodologiaRepository metodologiaRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final AsignarCentroCostoRepository asignarCentroCostoRepository;

    @Override
    @Transactional(readOnly = true)
    public CoordinacionAdministracionCatalogosDTO getCatalogs() {
        log.debug("getCatalogs ===> Consultando catálogos para administración de coordinaciones");

        CoordinacionAdministracionCatalogosDTO result = new CoordinacionAdministracionCatalogosDTO(
                mapCatalog(modalidadRepository.findAdministrationOptions()),
                mapCatalog(metodologiaRepository.findAdministrationOptions()),
                mapCatalog(centroCostoRepository.findAdministrationOptions())
        );

        log.info("getCatalogs ===> Catálogos de administración de coordinaciones consultados");
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoAdministracionDTO> searchUnits(String term) {
        String normalizedTerm = StringUtils.hasText(term) ? term.trim() : null;

        log.debug("searchUnits ===> Buscando unidades para administración de coordinaciones. term={}",
                normalizedTerm);

        List<CatalogoAdministracionDTO> result = mapCatalog(
                unidadRepository.searchAdministrationUnits(normalizedTerm)
        );

        log.info("searchUnits ===> Unidades encontradas. term={}, total={}",
                normalizedTerm, result.size());

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listParentCoordinations() {
        log.debug("listParentCoordinations ===> Listando coordinaciones padre");

        List<CoordinacionAdministracionListadoDTO> result = coordinacionRepository.findAdministrationParentCoordinations()
                .stream()
                .map(this::mapToDto)
                .toList();

        log.info("listParentCoordinations ===> Coordinaciones padre listadas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listChildCoordinations(Long idPadre) {
        log.debug("listChildCoordinations ===> Listando coordinaciones hijas. idPadre={}", idPadre);

        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            log.warn("listChildCoordinations ===> Coordinación padre no encontrada. idPadre={}", idPadre);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        List<CoordinacionAdministracionListadoDTO> result = coordinacionRepository.findAdministrationChildCoordinations(idPadre)
                .stream()
                .map(this::mapToDto)
                .toList();

        log.info("listChildCoordinations ===> Coordinaciones hijas listadas. idPadre={}, total={}",
                idPadre, result.size());

        return result;
    }

    @Override
    @Transactional
    public void saveParentCoordination(CoordinacionAdministracionFormularioDTO dto) {
        log.info("saveParentCoordination ===> Guardando coordinación padre. nombre={}, idCentroCosto={}",
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);

        saveCoordinationWithParent(null, dto);

        log.info("saveParentCoordination ===> Coordinación padre guardada. nombre={}, idCentroCosto={}",
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);
    }

    @Override
    @Transactional
    public void saveChildCoordination(Long idPadre, CoordinacionAdministracionFormularioDTO dto) {
        log.info("saveChildCoordination ===> Guardando coordinación hija. idPadre={}, nombre={}, idCentroCosto={}",
                idPadre,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);

        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            log.warn("saveChildCoordination ===> Coordinación padre no encontrada. idPadre={}", idPadre);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        saveCoordinationWithParent(idPadre, dto);

        log.info("saveChildCoordination ===> Coordinación hija guardada. idPadre={}, nombre={}, idCentroCosto={}",
                idPadre,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);
    }

    @Override
    @Transactional
    public void updateCoordination(Long id, CoordinacionAdministracionFormularioDTO dto) {
        log.info("updateCoordination ===> Actualizando coordinación. id={}, nombre={}, idCentroCosto={}",
                id,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCoordination(dto);

        CoordinacionesEntity entity = coordinacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("updateCoordination ===> Coordinación no encontrada. id={}", id);
                    return new ApiException(
                            HttpStatus.NOT_FOUND,
                            "No existe la coordinación con id " + id
                    );
                });

        /*
        * No cambiamos COOR_IDPADRE en edición.
        * Si era padre, sigue padre.
        * Si era hija, sigue hija.
        */
        fillCoordination(entity, dto);

        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        saveOrUpdateCostCenter(saved.getId(), dto.idCentroCosto());

        log.info("updateCoordination ===> Coordinación actualizada. id={}, idCentroCosto={}",
                saved.getId(), dto.idCentroCosto());
    }

    @Override
    @Transactional
    public void deleteCoordination(Long id) {
        log.info("deleteCoordination ===> Eliminando coordinación. id={}", id);

        CoordinacionesEntity entity = coordinacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("deleteCoordination ===> Coordinación no encontrada. id={}", id);
                    return new ApiException(
                            HttpStatus.NOT_FOUND,
                            "No existe la coordinación con id " + id
                    );
                });

        if (coordinacionRepository.existsByIdCoordinacionPadre(entity.getId())) {
            log.warn("deleteCoordination ===> Eliminación bloqueada. La coordinación tiene hijas asociadas. id={}",
                    entity.getId());
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar la coordinación porque tiene coordinaciones hijas asociadas"
            );
        }

        /*
        * Primero eliminamos por procedimiento las asignaciones de centro de costo
        * asociadas a esta coordinación.
        */
        List<Long> costCenterAssignmentIds = asignarCentroCostoRepository.findIdsByIdCoordinacion(id);

        log.debug("deleteCoordination ===> Asignaciones de centro de costo encontradas para eliminar. idCoordinacion={}, total={}",
                id, costCenterAssignmentIds.size());

        for (Long assignmentId : costCenterAssignmentIds) {
            BigDecimal assignmentResult = asignarCentroCostoRepository.deleteByProcedure(
                    assignmentId,
                    REGISTRADO_POR
            );

            if (assignmentResult == null || BigDecimal.ONE.compareTo(assignmentResult) != 0) {
                log.warn("deleteCoordination ===> Procedimiento de eliminación de centro costo falló. idCoordinacion={}, idAsignacion={}, resultado={}",
                        id, assignmentId, assignmentResult);
            }

            validateProcedureResult(
                    assignmentResult,
                    "No se pudo eliminar el centro de costo asignado a la coordinación"
            );

            log.info("deleteCoordination ===> Centro de costo asignado eliminado. idCoordinacion={}, idAsignacion={}",
                    id, assignmentId);
        }

        /*
        * Luego eliminamos la coordinación por procedimiento.
        */
        BigDecimal result = coordinacionRepository.deleteByProcedure(id, REGISTRADO_POR);

        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            log.warn("deleteCoordination ===> Procedimiento de eliminación de coordinación falló. id={}, resultado={}",
                    id, result);
        }

        validateProcedureResult(result, "No se pudo eliminar la coordinación");

        log.info("deleteCoordination ===> Coordinación eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteBulkCoordinations(List<Long> ids) {
        log.info("deleteBulkCoordinations ===> Eliminación masiva de coordinaciones. total={}",
                ids != null ? ids.size() : 0);

        if (ids == null || ids.isEmpty()) {
            log.debug("deleteBulkCoordinations ===> Lista vacía. No se realiza eliminación");
            return;
        }

        for (Long id : ids) {
            deleteCoordination(id);
        }

        log.info("deleteBulkCoordinations ===> Eliminación masiva de coordinaciones finalizada. total={}",
                ids.size());
    }

    private void saveCoordinationWithParent(
            Long idPadre,
            CoordinacionAdministracionFormularioDTO dto
    ) {
        log.debug("saveCoordinationWithParent ===> Iniciando creación de coordinación. idPadre={}, nombre={}, idCentroCosto={}",
                idPadre,
                dto != null ? dto.nombre() : null,
                dto != null ? dto.idCentroCosto() : null);

        validateCoordination(dto);

        CoordinacionesEntity entity = new CoordinacionesEntity();
        entity.setIdCoordinacionPadre(idPadre);

        fillCoordination(entity, dto);

        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        saveOrUpdateCostCenter(saved.getId(), dto.idCentroCosto());

        log.info("saveCoordinationWithParent ===> Coordinación creada. id={}, idPadre={}, nombre={}, idCentroCosto={}",
                saved.getId(),
                idPadre,
                saved.getNombre(),
                dto.idCentroCosto());
    }

    private void fillCoordination(
            CoordinacionesEntity entity,
            CoordinacionAdministracionFormularioDTO dto
    ) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion().trim());

        entity.setIdUnidadPadre(dto.idUnidadPadre());
        entity.setIdRegional(dto.idUnidadRegional());
        entity.setIdArea(dto.idUnidad());
        entity.setIdModalidad(dto.idModalidad());
        entity.setIdMetodologia(dto.idMetodologia());

        entity.setCodigo(StringUtils.hasText(dto.codigo()) ? dto.codigo().trim() : null);
        entity.setEsAcademica(normalizeCheck(dto.esAcademica()));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
    }

    private void saveOrUpdateCostCenter(Long idCoordinacion, Long idCentroCosto) {
        log.debug("saveOrUpdateCostCenter ===> Guardando centro de costo de coordinación. idCoordinacion={}, idCentroCosto={}",
                idCoordinacion, idCentroCosto);

        AsignarCentroCostoEntity entity = asignarCentroCostoRepository
                .findFirstByIdCoordinacion(idCoordinacion)
                .orElseGet(AsignarCentroCostoEntity::new);

        entity.setIdCoordinacion(idCoordinacion);
        entity.setIdCentroCosto(idCentroCosto);
        entity.setEstado(1L);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        AsignarCentroCostoEntity saved = asignarCentroCostoRepository.save(entity);

        log.info("saveOrUpdateCostCenter ===> Centro de costo de coordinación guardado. idAsignacion={}, idCoordinacion={}, idCentroCosto={}",
                saved.getId(),
                idCoordinacion,
                idCentroCosto);
    }

    private void validateCoordination(CoordinacionAdministracionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la coordinación es obligatoria");
        }

        if (!StringUtils.hasText(dto.nombre())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (!StringUtils.hasText(dto.descripcion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }

        if (dto.idUnidadRegional() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La unidad regional es obligatoria");
        }

        if (dto.idUnidad() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La unidad es obligatoria");
        }

        if (dto.idModalidad() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La modalidad es obligatoria");
        }

        if (dto.idMetodologia() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La metodología es obligatoria");
        }

        if (dto.idCentroCosto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo es obligatorio");
        }

        if (!unidadRepository.existsById(dto.idUnidadRegional())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La unidad regional no existe");
        }

        if (!unidadRepository.existsById(dto.idUnidad())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La unidad no existe");
        }

        if (dto.idUnidadPadre() != null && !unidadRepository.existsById(dto.idUnidadPadre())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La unidad padre no existe");
        }

        if (!modalidadRepository.existsById(dto.idModalidad())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La modalidad no existe");
        }

        if (!metodologiaRepository.existsById(dto.idMetodologia())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "La metodología no existe");
        }

        if (!centroCostoRepository.existsById(dto.idCentroCosto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El centro de costo no existe");
        }
    }

    private CoordinacionAdministracionListadoDTO mapToDto(
            CoordinacionAdministracionListadoProjection item
    ) {
        return new CoordinacionAdministracionListadoDTO(
                item.getId(),
                item.getIdCoordinacionPadre(),
                item.getNombre(),
                item.getDescripcion(),
                item.getIdUnidadPadre(),
                item.getUnidadPadre(),
                item.getIdUnidadRegional(),
                item.getUnidadRegional(),
                item.getIdUnidad(),
                item.getUnidad(),
                item.getIdModalidad(),
                item.getModalidad(),
                item.getIdMetodologia(),
                item.getMetodologia(),
                item.getIdCentroCosto(),
                item.getCentroCosto(),
                item.getCodigo(),
                item.getEsAcademica()
        );
    }

    private List<CatalogoAdministracionDTO> mapCatalog(
            List<CatalogoAdministracionProjection> items
    ) {
        return items.stream()
                .map(item -> new CatalogoAdministracionDTO(
                        item.getId(),
                        item.getLabel(),
                        item.getCodigo()
                ))
                .toList();
    }

    private String normalizeCheck(String value) {
        if (!StringUtils.hasText(value)) {
            return "0";
        }

        String normalized = value.trim().toUpperCase();

        return "1".equals(normalized)
                || "ACTIVO".equals(normalized)
                || "TRUE".equals(normalized)
                || "SI".equals(normalized)
                || "S".equals(normalized)
                ? "1"
                : "0";
    }


    private void validateProcedureResult(BigDecimal result, String message) {
        if (result == null || BigDecimal.ONE.compareTo(result) != 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, message);
        }
    }
}

/* 17/07/2026 @:Daniel Arias */
