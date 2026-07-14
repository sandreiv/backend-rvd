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
        return new CoordinacionAdministracionCatalogosDTO(
                mapCatalog(modalidadRepository.findAdministrationOptions()),
                mapCatalog(metodologiaRepository.findAdministrationOptions()),
                mapCatalog(centroCostoRepository.findAdministrationOptions())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoAdministracionDTO> searchUnits(String term) {
        String normalizedTerm = StringUtils.hasText(term) ? term.trim() : null;

        return mapCatalog(unidadRepository.searchAdministrationUnits(normalizedTerm));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listParentCoordinations() {
        return coordinacionRepository.findAdministrationParentCoordinations()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionAdministracionListadoDTO> listChildCoordinations(Long idPadre) {
        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        return coordinacionRepository.findAdministrationChildCoordinations(idPadre)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void saveParentCoordination(CoordinacionAdministracionFormularioDTO dto) {
        saveCoordinationWithParent(null, dto);
    }

    @Override
    @Transactional
    public void saveChildCoordination(Long idPadre, CoordinacionAdministracionFormularioDTO dto) {
        if (idPadre == null || !coordinacionRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinación padre");
        }

        saveCoordinationWithParent(idPadre, dto);
    }

    @Override
    @Transactional
    public void updateCoordination(Long id, CoordinacionAdministracionFormularioDTO dto) {
        validateCoordination(dto);

        CoordinacionesEntity entity = coordinacionRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la coordinación con id " + id
                ));

        /*
         * No cambiamos COOR_IDPADRE en edición.
         * Si era padre, sigue padre.
         * Si era hija, sigue hija.
         */
        fillCoordination(entity, dto);

        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        saveOrUpdateCostCenter(saved.getId(), dto.idCentroCosto());
    }

    @Override
    @Transactional
    public void deleteCoordination(Long id) {
        CoordinacionesEntity entity = coordinacionRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la coordinación con id " + id
                ));

        if (coordinacionRepository.existsByIdCoordinacionPadre(entity.getId())) {
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

        for (Long assignmentId : costCenterAssignmentIds) {
            BigDecimal assignmentResult = asignarCentroCostoRepository.deleteByProcedure(
                    assignmentId,
                    REGISTRADO_POR
            );

            validateProcedureResult(
                    assignmentResult,
                    "No se pudo eliminar el centro de costo asignado a la coordinación"
            );
        }

        /*
        * Luego eliminamos la coordinación por procedimiento.
        */
        BigDecimal result = coordinacionRepository.deleteByProcedure(id, REGISTRADO_POR);

        validateProcedureResult(result, "No se pudo eliminar la coordinación");
    }

    @Override
    @Transactional
    public void deleteBulkCoordinations(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            deleteCoordination(id);
        }
    }

    private void saveCoordinationWithParent(
            Long idPadre,
            CoordinacionAdministracionFormularioDTO dto
    ) {
        validateCoordination(dto);

        CoordinacionesEntity entity = new CoordinacionesEntity();
        entity.setIdCoordinacionPadre(idPadre);

        fillCoordination(entity, dto);

        CoordinacionesEntity saved = coordinacionRepository.save(entity);

        saveOrUpdateCostCenter(saved.getId(), dto.idCentroCosto());
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
        AsignarCentroCostoEntity entity = asignarCentroCostoRepository
                .findFirstByIdCoordinacion(idCoordinacion)
                .orElseGet(AsignarCentroCostoEntity::new);

        entity.setIdCoordinacion(idCoordinacion);
        entity.setIdCentroCosto(idCentroCosto);
        entity.setEstado(1L);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        asignarCentroCostoRepository.save(entity);
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