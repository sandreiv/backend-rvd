package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoActividadesAdministracionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoActividadesAdministracionServiceImpl implements TipoActividadesAdministracionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";
    private static final Set<String> CODIGOS_PERMITIDOS = Set.of("CTI", "AC", "FAD", "FAI", "ISU");

    private final TipoActividadesRepository tipoActividadesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadAdministracionListadoDTO> listParentActivityTypes() {
        return tipoActividadesRepository.findAdministrationParentList()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadAdministracionListadoDTO> listChildActivityTypes(Long idPadre) {
        if (idPadre == null || !tipoActividadesRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de actividad padre");
        }

        return tipoActividadesRepository.findAdministrationChildrenList(idPadre)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void saveActivityType(TipoActividadAdministracionFormularioDTO dto) {
        saveActivityTypeWithParent(null, dto);
    }

    @Override
    @Transactional
    public void saveChildActivityType(Long idPadre, TipoActividadAdministracionFormularioDTO dto) {
        if (idPadre == null || !tipoActividadesRepository.existsById(idPadre)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el tipo de actividad padre");
        }

        saveActivityTypeWithParent(idPadre, dto);
    }

    @Override
    @Transactional
    public void updateActivityType(Long id, TipoActividadAdministracionFormularioDTO dto) {
        validateActivityType(dto);

        TipoActividadesEntity entity = tipoActividadesRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de actividad con id " + id
                ));

        /*
         * No se recalcula TIAC_ORDEN.
         * No se cambia TIAC_IDPADRE.
         * Si era padre, sigue padre.
         * Si era hijo, sigue hijo.
         */
        fillActivityType(entity, dto);

        tipoActividadesRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteActivityType(Long id) {
        TipoActividadesEntity entity = tipoActividadesRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el tipo de actividad con id " + id
                ));

        if (tipoActividadesRepository.existsByIdPadre(entity.getId())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar el tipo de actividad porque tiene actividades hijas asociadas"
            );
        }

        tipoActividadesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBulkActivityTypes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            deleteActivityType(id);
        }
    }

    private void saveActivityTypeWithParent(
            Long idPadre,
            TipoActividadAdministracionFormularioDTO dto
    ) {
        validateActivityType(dto);

        TipoActividadesEntity entity = new TipoActividadesEntity();

        /*
         * Si idPadre es null, se crea actividad padre.
         * Si idPadre tiene valor, se crea actividad hija.
         */
        entity.setIdPadre(idPadre);

        /*
         * El orden se calcula según el grupo:
         * - Padres: MAX(TIAC_ORDEN) WHERE TIAC_IDPADRE IS NULL + 1
         * - Hijos:  MAX(TIAC_ORDEN) WHERE TIAC_IDPADRE = idPadre + 1
         */
        entity.setOrden(tipoActividadesRepository.findNextOrderByParent(idPadre));

        fillActivityType(entity, dto);

        tipoActividadesRepository.save(entity);
    }

    private TipoActividadAdministracionListadoDTO mapToDto(
            TipoActividadAdministracionListadoProjection item
    ) {
        return new TipoActividadAdministracionListadoDTO(
                item.getId(),
                item.getNombre(),
                item.getDescripcion(),
                item.getCodigo(),
                item.getMinimoHoras(),
                item.getMaximoHoras(),
                item.getOrden(),
                item.getEstado()
        );
    }

    private void fillActivityType(
            TipoActividadesEntity entity,
            TipoActividadAdministracionFormularioDTO dto
    ) {
        entity.setNombre(dto.nombre().trim());
        entity.setDescripcion(dto.descripcion().trim());
        entity.setCodigo(dto.codigo().trim().toUpperCase());
        entity.setMinimoHoras(String.valueOf(dto.minimoHoras()));
        entity.setMaximoHoras(String.valueOf(dto.maximoHoras()));
        entity.setEstado(normalizeStatus(dto.estado()));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        /*
         * Estos campos no se trabajan en esta pantalla.
         * Se dejan nulos.
         */
        entity.setComponente(null);
        entity.setClase(null);
    }

    private void validateActivityType(TipoActividadAdministracionFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información del tipo de actividad es obligatoria"
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

        String codigo = dto.codigo().trim().toUpperCase();

        if (!CODIGOS_PERMITIDOS.contains(codigo)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El código del tipo de actividad no es válido");
        }

        if (dto.minimoHoras() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El mínimo de horas es obligatorio");
        }

        if (dto.maximoHoras() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El máximo de horas es obligatorio");
        }

        if (dto.minimoHoras() < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El mínimo de horas no puede ser negativo");
        }

        if (dto.maximoHoras() <= dto.minimoHoras()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El máximo de horas debe ser mayor al mínimo de horas"
            );
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "1";
        }

        String normalized = status.trim().toUpperCase();

        return "0".equals(normalized)
                || "INACTIVO".equals(normalized)
                || "I".equals(normalized)
                ? "0"
                : "1";
    }
}