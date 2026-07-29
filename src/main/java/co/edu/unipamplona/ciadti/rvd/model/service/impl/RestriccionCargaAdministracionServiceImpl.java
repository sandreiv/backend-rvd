/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaAdministracionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para administrar restricciones de carga por modalidad.
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaModalidadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaModalidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadModalidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CategoriaCatedraticoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CategoriaModalidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadModalidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.RestriccionCargaAdministracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestriccionCargaAdministracionServiceImpl implements RestriccionCargaAdministracionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";

    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final RestriccionCargaRepository restriccionCargaRepository;
    private final CategoriaCatedraticoRepository categoriaCatedraticoRepository;
    private final CategoriaModalidadRepository categoriaModalidadRepository;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final TipoActividadModalidadRepository tipoActividadModalidadRepository;
    private final ProgramaRepository programaRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final ObjectMapper objectMapper;

    /**
     * Lista las modalidades de contratación disponibles para configurar restricción de carga.
     *
     * @return lista de modalidades con nombre, sigla y estado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestriccionCargaModalidadListadoDTO> listModalities() {
        log.debug("listModalities ===> Consultando modalidades de contratación para restricción de carga");

        List<RestriccionCargaModalidadListadoDTO> result = modalidadContratacionRepository.findAllModalities()
                .stream()
                .map(this::toListDto)
                .toList();

        log.info("listModalities ===> Modalidades consultadas. total={}", result.size());

        return result;
    }

    /**
     * Consulta los catálogos requeridos por el formulario de restricción de carga.
     *
     * @return catálogos de categorías, tipos de actividad, programas y personas.
     */
    @Override
    @Transactional(readOnly = true)
    public RestriccionCargaCatalogosDTO getCatalogs() {
        log.debug("getCatalogs ===> Consultando catálogos de restricción de carga");

        RestriccionCargaCatalogosDTO result = new RestriccionCargaCatalogosDTO(
                categoriaCatedraticoRepository.findAllForLoadRestriction()
                        .stream()
                        .map(item -> new CatalogoAdministracionDTO(
                                item.getId(),
                                item.getDescripcion(),
                                null
                        ))
                        .toList(),
                tipoActividadesRepository.findAdministrationParentList()
                        .stream()
                        .map(this::mapTipoActividadCatalog)
                        .toList(),
                mapCatalog(programaRepository.findAdministrationOptions()),
                mapCatalog(personaGeneralRepository.findAdministrationOptions())
        );

        log.info("getCatalogs ===> Catálogos de restricción de carga consultados");

        return result;
    }

    /**
     * Consulta la restricción de carga configurada para una modalidad de contratación.
     *
     * @param idModalidadContratacion identificador de la modalidad.
     * @return detalle de la restricción configurada.
     */
    @Override
    @Transactional(readOnly = true)
    public RestriccionCargaDetalleDTO getRestriction(Long idModalidadContratacion) {
        log.debug("getRestriction ===> Consultando restricción de carga. idModalidad={}",
                idModalidadContratacion);

        validateModalityExists(idModalidadContratacion);

        RestriccionCargaEntity restriction = restriccionCargaRepository
                .findById(idModalidadContratacion)
                .orElse(null);

        CategoriaModalidadEntity categoriaModalidad = categoriaModalidadRepository
                .findByIdModalidadContratacion(idModalidadContratacion)
                .orElse(null);

        TipoActividadModalidadEntity tipoActividadModalidad = tipoActividadModalidadRepository
                .findByIdModalidadContratacion(idModalidadContratacion)
                .orElse(null);

        RestriccionExcepcionDTO excepcion = parseExcepcion(
        restriction != null ? restriction.getExcepcion() : null
        );

        RestriccionCargaDetalleDTO result = new RestriccionCargaDetalleDTO(
                idModalidadContratacion,
                restriction != null ? restriction.getMinimo() : null,
                restriction != null ? restriction.getMaximo() : null,
                restriction != null ? restriction.getInvestigacion() : null,
                restriction != null ? restriction.getFormaPago() : null,
                restriction != null ? restriction.getTipoContrato() : null,
                restriction != null ? restriction.getTipoHoras() : null,
                excepcion != null ? cleanIds(excepcion.programas()) : List.of(),
                excepcion != null ? cleanIds(excepcion.personas()) : List.of(),
                categoriaModalidad != null ? categoriaModalidad.getIdCategoriaCatedratico() : null,
                tipoActividadModalidad != null ? tipoActividadModalidad.getIdTipoActividades() : null
        );

        log.info("getRestriction ===> Restricción de carga consultada. idModalidad={}",
                idModalidadContratacion);

        return result;
    }

    /**
     * Registra o actualiza la restricción de carga de una modalidad.
     * También sincroniza las relaciones de categoría y tipo de actividad.
     *
     * @param dto datos enviados desde el formulario.
     */
    @Override
    @Transactional
    public void saveRestriction(RestriccionCargaFormularioDTO dto) {
        log.info("saveRestriction ===> Guardando restricción de carga. idModalidad={}",
                dto != null ? dto.idModalidadContratacion() : null);

        validateRestriction(dto);

        RestriccionCargaEntity entity = restriccionCargaRepository
                .findById(dto.idModalidadContratacion())
                .orElseGet(() -> {
                    RestriccionCargaEntity created = new RestriccionCargaEntity();
                    created.setIdModalidadContratacion(dto.idModalidadContratacion());
                    return created;
                });

        entity.setMinimo(clean(dto.minimo()));
        entity.setMaximo(clean(dto.maximo()));
        entity.setInvestigacion(isMarked(dto.investigacion()) ? "1" : "0");
        entity.setFormaPago(clean(dto.formaPago()));
        entity.setTipoContrato(clean(dto.tipoContrato()));
        entity.setTipoHoras(clean(dto.tipoHoras()));
        entity.setExcepcion(buildExcepcion(dto));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        restriccionCargaRepository.save(entity);

        syncCategoriaModalidad(dto.idModalidadContratacion(), dto.idCategoriaCatedratico());
        syncTipoActividadModalidad(dto.idModalidadContratacion(), dto.idTipoActividad());

        log.info("saveRestriction ===> Restricción de carga guardada. idModalidad={}",
                dto.idModalidadContratacion());
    }

    private RestriccionCargaModalidadListadoDTO toListDto(ModalidadContratacionEntity entity) {
        return new RestriccionCargaModalidadListadoDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getSigla(),
                entity.getEstado()
        );
    }

    private CatalogoAdministracionDTO mapTipoActividadCatalog(
            TipoActividadAdministracionListadoProjection item) {
        return new CatalogoAdministracionDTO(
                item.getId(),
                item.getNombre(),
                item.getCodigo()
        );
    }

    private List<CatalogoAdministracionDTO> mapCatalog(
            List<CatalogoAdministracionProjection> projections) {
        return projections.stream()
                .map(item -> new CatalogoAdministracionDTO(
                        item.getId(),
                        item.getLabel(),
                        item.getCodigo()
                ))
                .toList();
    }

    private void validateRestriction(RestriccionCargaFormularioDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La restricción de carga es obligatoria");
        }

        validateModalityExists(dto.idModalidadContratacion());
        validateHours(dto.minimo(), dto.maximo());
        validateTipoContrato(dto.tipoContrato());
        validateExcepcion(dto);
        validateCategoria(dto.idCategoriaCatedratico());
        validateTipoActividad(dto.idTipoActividad());
    }

    private void validateModalityExists(Long idModalidadContratacion) {
        if (idModalidadContratacion == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La modalidad de contratación es obligatoria");
        }

        if (!modalidadContratacionRepository.existsById(idModalidadContratacion)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la modalidad de contratación");
        }
    }

    private void validateHours(String minimo, String maximo) {
        if (!StringUtils.hasText(minimo) || !StringUtils.hasText(maximo)) {
            return;
        }

        BigDecimal minimoNumber = parseHours(minimo, "El mínimo de horas debe ser numérico");
        BigDecimal maximoNumber = parseHours(maximo, "El máximo de horas debe ser numérico");

        if (maximoNumber.compareTo(minimoNumber) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El máximo de horas debe ser mayor o igual que el mínimo");
        }
    }

    private void validateTipoContrato(String tipoContrato) {
        String value = normalize(tipoContrato);

        if (!StringUtils.hasText(value)) {
            return;
        }

        if (!List.of("CONTRATO", "NORMA").contains(value)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de contrato debe ser Contrato o Norma"
            );
        }
    }

    private BigDecimal parseHours(String value, String message) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private void validateExcepcion(RestriccionCargaFormularioDTO dto) {
        List<Long> idsProgramas = cleanIds(dto.idsProgramasExcepcion());
        List<Long> idsPersonas = cleanIds(dto.idsPersonasExcepcion());

        for (Long idPrograma : idsProgramas) {
            if (!programaRepository.existsById(idPrograma)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe uno de los programas seleccionados para la excepción");
            }
        }

        for (Long idPersona : idsPersonas) {
            if (!personaGeneralRepository.existsById(idPersona)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe una de las personas seleccionadas para la excepción");
            }
        }
    }

    private void validateCategoria(Long idCategoriaCatedratico) {
        if (idCategoriaCatedratico == null) {
            return;
        }

        if (!categoriaCatedraticoRepository.existsById(idCategoriaCatedratico)) {
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "No existe la categoría seleccionada");
        }
    }

    private void validateTipoActividad(Long idTipoActividad) {
        if (idTipoActividad == null) {
            return;
        }

        if (!tipoActividadesRepository.existsById(idTipoActividad)) {
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "No existe el tipo de actividad seleccionado");
        }
    }

    private void syncCategoriaModalidad(
            Long idModalidadContratacion,
            Long idCategoriaCatedratico) {
        categoriaModalidadRepository.deleteByIdModalidadContratacion(idModalidadContratacion);

        if (idCategoriaCatedratico == null) {
            return;
        }

        CategoriaModalidadEntity entity = new CategoriaModalidadEntity();
        entity.setIdModalidadContratacion(idModalidadContratacion);
        entity.setIdCategoriaCatedratico(idCategoriaCatedratico);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        categoriaModalidadRepository.save(entity);
    }

    private void syncTipoActividadModalidad(
            Long idModalidadContratacion,
            Long idTipoActividad) {
        tipoActividadModalidadRepository.deleteByIdModalidadContratacion(idModalidadContratacion);

        if (idTipoActividad == null) {
            return;
        }

        TipoActividadModalidadEntity entity = new TipoActividadModalidadEntity();
        entity.setIdModalidadContratacion(idModalidadContratacion);
        entity.setIdTipoActividades(idTipoActividad);
        entity.setOrden("1");
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());

        tipoActividadModalidadRepository.save(entity);
    }

    private String buildExcepcion(RestriccionCargaFormularioDTO dto) {
        List<Long> idsProgramas = cleanIds(dto.idsProgramasExcepcion());
        List<Long> idsPersonas = cleanIds(dto.idsPersonasExcepcion());

        if (idsProgramas.isEmpty() && idsPersonas.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(
                    new RestriccionExcepcionDTO(idsProgramas, idsPersonas)
            );
        } catch (JsonProcessingException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible construir la excepción de la restricción");
        }
    }

    private RestriccionExcepcionDTO parseExcepcion(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return objectMapper.readValue(value, RestriccionExcepcionDTO.class);
        } catch (JsonProcessingException ex) {
            log.warn("parseExcepcion ===> No fue posible leer la excepción configurada. value={}",
                    value);
            return null;
        }
    }

    private boolean isMarked(String value) {
        String normalized = normalize(value);
        return "1".equals(normalized)
                || "S".equals(normalized)
                || "SI".equals(normalized)
                || "TRUE".equals(normalized)
                || "ACTIVO".equals(normalized);
    }

    private List<Long> cleanIds(List<Long> ids) {
        if (ids == null) {
            return List.of();
        }

        return ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private String clean(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String normalize(String value) {
        return clean(value) == null
                ? ""
                : clean(value).toUpperCase(Locale.ROOT);
    }

    private record RestriccionExcepcionDTO(
            List<Long> programas,
            List<Long> personas
    ) {}
}