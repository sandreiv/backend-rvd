/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaAdministracionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para administrar restricciones de carga por modalidad.
 * 06/08/2026 - Excepciones de programa con máximo de horas (idPrograma + maximoHoras).
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaPersonaExcepcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaProgramaExcepcionDTO;
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
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.RestriccionCargaAdministracionService;
import lombok.RequiredArgsConstructor;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestriccionCargaAdministracionServiceImpl implements RestriccionCargaAdministracionService {

    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final RestriccionCargaRepository restriccionCargaRepository;
    private final CargaDocenteRepository cargaDocenteRepository;
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

        List<CategoriaModalidadEntity> categoriasModalidad = categoriaModalidadRepository
            .findAllByIdModalidadContratacion(idModalidadContratacion);

        List<TipoActividadModalidadEntity> tiposActividadModalidad = tipoActividadModalidadRepository
            .findAllByIdModalidadContratacion(idModalidadContratacion);

        RestriccionExcepcionDTO excepcion = parseExcepcion(
        restriction != null ? restriction.getExcepcion() : null
        );

        List<RestriccionCargaProgramaExcepcionDTO> programasExcepcion =
                excepcion != null
                        ? cleanProgramasExcepcion(excepcion.programas())
                        : List.of();
        List<RestriccionCargaPersonaExcepcionDTO> personasExcepcion =
                excepcion != null
                        ? cleanPersonasExcepcion(excepcion.personas())
                        : List.of();

        RestriccionCargaDetalleDTO result = new RestriccionCargaDetalleDTO(
                idModalidadContratacion,
                restriction != null ? restriction.getMinimo() : null,
                restriction != null ? restriction.getMaximo() : null,
                restriction != null ? restriction.getInvestigacion() : null,
                restriction != null ? restriction.getFormaPago() : null,
                restriction != null ? restriction.getTipoContrato() : null,
                restriction != null ? restriction.getTipoHoras() : null,
                programasExcepcion.stream()
                        .map(RestriccionCargaProgramaExcepcionDTO::idPrograma)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList(),
                programasExcepcion,
                personasExcepcion.stream()
                        .map(RestriccionCargaPersonaExcepcionDTO::idPersona)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList(),
                personasExcepcion,
                categoriasModalidad.stream()
                        .map(CategoriaModalidadEntity::getIdCategoriaCatedratico)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList(),
                tiposActividadModalidad.stream()
                        .map(TipoActividadModalidadEntity::getIdTipoActividades)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
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

        var restrictionExistente = restriccionCargaRepository
        .findById(dto.idModalidadContratacion());

        boolean isNewRestriction = restrictionExistente.isEmpty();

        RestriccionCargaEntity entity = restrictionExistente
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
        entity.setRegistradoPor(
        RegistradoPorUtils.value(
                        isNewRestriction ? Accion.INSERT : Accion.UPDATE
                )
        );
        entity.setFechaCambio(new Date());

        restriccionCargaRepository.save(entity);

        syncHorasDeExcepcionCargaDocente(dto);
        syncCategoriasModalidad(dto.idModalidadContratacion(), dto.idsCategoriasCatedratico());
        syncTiposActividadModalidad(dto.idModalidadContratacion(), dto.idsTiposActividad());

        log.info("saveRestriction ===> Restricción de carga guardada. idModalidad={}", dto.idModalidadContratacion());
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
        validateInvestigacion(dto.investigacion());
        validateFormaPago(dto.formaPago());
        validateTipoContrato(dto.tipoContrato());
        validateTipoHoras(dto.tipoHoras());
        validateExcepcion(dto);
        validateCategorias(dto.idsCategoriasCatedratico());
        validateTiposActividad(dto.idsTiposActividad());
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
        if (!StringUtils.hasText(minimo)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El mínimo de horas es obligatorio");
        }

        if (!StringUtils.hasText(maximo)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El máximo de horas es obligatorio");
        }

        BigDecimal minimoNumber = parseHours(minimo, "El mínimo de horas debe ser numérico");
        BigDecimal maximoNumber = parseHours(maximo, "El máximo de horas debe ser numérico");

        if (maximoNumber.compareTo(minimoNumber) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El máximo de horas debe ser mayor o igual que el mínimo");
        }
    }

    private void validateInvestigacion(String investigacion) {
        String value = normalize(investigacion);

        if (!StringUtils.hasText(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La investigación es obligatoria");
        }

        if (!List.of("0", "1", "S", "N", "SI", "NO", "TRUE", "FALSE").contains(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La investigación debe ser Sí o No");
        }
    }

    private void validateFormaPago(String formaPago) {
        String value = normalize(formaPago);

        if (!StringUtils.hasText(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La forma de pago es obligatoria");
        }

        if (!List.of("SALARIO", "CATEDRA").contains(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La forma de pago debe ser Salario o Cátedra");
        }
    }

    private void validateTipoHoras(String tipoHoras) {
        String value = normalize(tipoHoras);

        if (!StringUtils.hasText(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El tipo de horas es obligatorio");
        }

        if (!List.of("SEMANAL", "SEMESTRAL").contains(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El tipo de horas debe ser Semanal o Semestral");
        }
    }

    private void validateTipoContrato(String tipoContrato) {
        String value = normalize(tipoContrato);

        if (!StringUtils.hasText(value)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "El tipo de contrato es obligatorio");
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
        List<RestriccionCargaProgramaExcepcionDTO> programas =
                cleanProgramasExcepcion(
                        dto.programasExcepcion(),
                        dto.idsProgramasExcepcion());
        List<RestriccionCargaPersonaExcepcionDTO> personas =
                cleanPersonasExcepcion(dto.personasExcepcion(), dto.idsPersonasExcepcion());

        for (RestriccionCargaProgramaExcepcionDTO programa : programas) {
            if (!programaRepository.existsById(programa.idPrograma())) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe uno de los programas seleccionados para la excepción");
            }

            validateMaximoHorasExcepcion(programa.maximoHoras());
        }

        for (RestriccionCargaPersonaExcepcionDTO persona : personas) {
            if (!personaGeneralRepository.existsById(persona.idPersona())) {
                throw new ApiException(HttpStatus.NOT_FOUND,"No existe una de las personas seleccionadas para la excepción");
            }

            validateMaximoHorasExcepcion(persona.maximoHoras());
        }
    }

    private void validateMaximoHorasExcepcion(String maximoHoras) {
        if (!StringUtils.hasText(maximoHoras)) {
            return;
        }

        BigDecimal maximoHorasNumber = parseHours(
                maximoHoras,
                "Las horas máximas de excepción deben ser numéricas"
        );

        if (maximoHorasNumber.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Las horas máximas de excepción deben ser mayores a cero");
        }
    }

    private void validateCategorias(List<Long> idsCategoriasCatedratico) {
        for (Long idCategoriaCatedratico : cleanIds(idsCategoriasCatedratico)) {
            if (!categoriaCatedraticoRepository.existsById(idCategoriaCatedratico)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe una de las categorías seleccionadas");
            }
        }
    }

    private void validateTiposActividad(List<Long> idsTiposActividad) {
        for (Long idTipoActividad : cleanIds(idsTiposActividad)) {
            if (!tipoActividadesRepository.existsById(idTipoActividad)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe uno de los tipos de actividad seleccionados");
            }
        }
    }

    private void syncHorasDeExcepcionCargaDocente(RestriccionCargaFormularioDTO dto) {
        Long idModalidadContratacion = dto.idModalidadContratacion();

        int cleared = cargaDocenteRepository.clearHorasDeExcepcionByModalidad(
                idModalidadContratacion,
                RegistradoPorUtils.value(Accion.UPDATE)
        );

        int updated = 0;

        List<RestriccionCargaPersonaExcepcionDTO> personas = cleanPersonasExcepcion(dto.personasExcepcion(), dto.idsPersonasExcepcion());

        for (RestriccionCargaPersonaExcepcionDTO persona : personas) {
            String maximoHoras = clean(persona.maximoHoras());

            if (!StringUtils.hasText(maximoHoras)) {
                continue;
            }

            updated += cargaDocenteRepository.updateHorasDeExcepcionByModalidadAndPersona(
                    idModalidadContratacion,
                    persona.idPersona(),
                    maximoHoras,
                    RegistradoPorUtils.value(Accion.UPDATE)
            );
        }

        log.info(
                "syncHorasDeExcepcionCargaDocente ===> Horas de excepción sincronizadas. idModalidad={}, limpiados={}, actualizados={}",
                idModalidadContratacion,
                cleared,
                updated
        );
    }

    private void syncCategoriasModalidad(
            Long idModalidadContratacion,
            List<Long> idsCategoriasCatedratico) {
        categoriaModalidadRepository.deleteByIdModalidadContratacion(idModalidadContratacion);

        for (Long idCategoriaCatedratico : cleanIds(idsCategoriasCatedratico)) {
            CategoriaModalidadEntity entity = new CategoriaModalidadEntity();
            entity.setIdModalidadContratacion(idModalidadContratacion);
            entity.setIdCategoriaCatedratico(idCategoriaCatedratico);
            entity.setRegistradoPor(
                    RegistradoPorUtils.value(Accion.INSERT)
            );
            entity.setFechaCambio(new Date());

            categoriaModalidadRepository.save(entity);
        }
    }

    private void syncTiposActividadModalidad(
            Long idModalidadContratacion,
            List<Long> idsTiposActividad) {
        tipoActividadModalidadRepository.deleteByIdModalidadContratacion(idModalidadContratacion);

        int orden = 1;

        for (Long idTipoActividad : cleanIds(idsTiposActividad)) {
            TipoActividadModalidadEntity entity = new TipoActividadModalidadEntity();
            entity.setIdModalidadContratacion(idModalidadContratacion);
            entity.setIdTipoActividades(idTipoActividad);
            entity.setOrden(String.valueOf(orden++));
            entity.setRegistradoPor(
                    RegistradoPorUtils.value(Accion.INSERT)
            );
            entity.setFechaCambio(new Date());

            tipoActividadModalidadRepository.save(entity);
        }
    }

    private String buildExcepcion(RestriccionCargaFormularioDTO dto) {
        List<RestriccionCargaProgramaExcepcionDTO> programas =cleanProgramasExcepcion(dto.programasExcepcion(), dto.idsProgramasExcepcion());
        List<RestriccionCargaPersonaExcepcionDTO> personas = cleanPersonasExcepcion(dto.personasExcepcion(), dto.idsPersonasExcepcion());

        if (programas.isEmpty() && personas.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(new RestriccionExcepcionDTO(programas, personas));
        } catch (JsonProcessingException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible construir la excepción de la restricción");
        }
    }

    private RestriccionExcepcionDTO parseExcepcion(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(value);

            return new RestriccionExcepcionDTO(
                    parseProgramasExcepcion(root.get("programas")),
                    parsePersonasExcepcion(root.get("personas"))
            );
        } catch (JsonProcessingException ex) {
            log.warn("parseExcepcion ===> No fue posible leer la excepción configurada. value={}",
                    value);
            return null;
        }
    }

    private List<RestriccionCargaProgramaExcepcionDTO> parseProgramasExcepcion(
            JsonNode node) {
        if (node == null || !node.isArray()) {
            return List.of();
        }

        List<RestriccionCargaProgramaExcepcionDTO> programas = new ArrayList<>();

        for (JsonNode item : node) {
            if (item == null || item.isNull()) {
                continue;
            }

            if (item.isObject()) {
                Long idPrograma = parseLongNode(item.get("idPrograma"));

                if (idPrograma == null) {
                    idPrograma = parseLongNode(item.get("id"));
                }

                if (idPrograma == null) {
                    continue;
                }

                programas.add(new RestriccionCargaProgramaExcepcionDTO(
                        idPrograma,
                        parseTextNode(item.get("maximoHoras"))
                ));
                continue;
            }

            Long idPrograma = parseLongNode(item);

            if (idPrograma != null) {
                programas.add(new RestriccionCargaProgramaExcepcionDTO(
                        idPrograma,
                        null
                ));
            }
        }

        return cleanProgramasExcepcion(programas);
    }

    private List<RestriccionCargaPersonaExcepcionDTO> parsePersonasExcepcion(JsonNode node) {
        if (node == null || !node.isArray()) {
            return List.of();
        }

        List<RestriccionCargaPersonaExcepcionDTO> personas = new ArrayList<>();

        for (JsonNode item : node) {
            if (item == null || item.isNull()) {
                continue;
            }

            if (item.isObject()) {
                Long idPersona = parseLongNode(item.get("idPersona"));

                if (idPersona == null) {
                    idPersona = parseLongNode(item.get("id"));
                }

                if (idPersona == null) {
                    continue;
                }

                personas.add(new RestriccionCargaPersonaExcepcionDTO(
                        idPersona,
                        parseTextNode(item.get("maximoHoras"))
                ));
                continue;
            }

            Long idPersona = parseLongNode(item);

            if (idPersona != null) {
                personas.add(new RestriccionCargaPersonaExcepcionDTO(
                        idPersona,
                        null
                ));
            }
        }

        return cleanPersonasExcepcion(personas);
    }

    private Long parseLongNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isNumber()) {
            return node.longValue();
        }

        if (node.isTextual() && StringUtils.hasText(node.asText())) {
            try {
                return Long.valueOf(node.asText().trim());
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }

    private String parseTextNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        return clean(node.asText());
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

    private List<RestriccionCargaProgramaExcepcionDTO> cleanProgramasExcepcion(
            List<RestriccionCargaProgramaExcepcionDTO> programas) {
        return cleanProgramasExcepcion(programas, List.of());
    }

    private List<RestriccionCargaProgramaExcepcionDTO> cleanProgramasExcepcion(
            List<RestriccionCargaProgramaExcepcionDTO> programas,
            List<Long> idsProgramasFallback) {
        Map<Long, RestriccionCargaProgramaExcepcionDTO> result = new LinkedHashMap<>();

        if (programas != null) {
            for (RestriccionCargaProgramaExcepcionDTO programa : programas) {
                if (programa == null || programa.idPrograma() == null) {
                    continue;
                }

                result.putIfAbsent(
                        programa.idPrograma(),
                        new RestriccionCargaProgramaExcepcionDTO(
                                programa.idPrograma(),
                                clean(programa.maximoHoras())
                        )
                );
            }
        }

        if (result.isEmpty()) {
            for (Long idPrograma : cleanIds(idsProgramasFallback)) {
                result.putIfAbsent(
                        idPrograma,
                        new RestriccionCargaProgramaExcepcionDTO(
                                idPrograma,
                                null
                        )
                );
            }
        }

        return result.values().stream().toList();
    }

    private List<RestriccionCargaPersonaExcepcionDTO> cleanPersonasExcepcion(
            List<RestriccionCargaPersonaExcepcionDTO> personas) {
        return cleanPersonasExcepcion(personas, List.of());
    }

    private List<RestriccionCargaPersonaExcepcionDTO> cleanPersonasExcepcion(
            List<RestriccionCargaPersonaExcepcionDTO> personas,
            List<Long> idsPersonasFallback) {
        Map<Long, RestriccionCargaPersonaExcepcionDTO> result = new LinkedHashMap<>();

        if (personas != null) {
            for (RestriccionCargaPersonaExcepcionDTO persona : personas) {
                if (persona == null || persona.idPersona() == null) {
                    continue;
                }

                result.putIfAbsent(
                        persona.idPersona(),
                        new RestriccionCargaPersonaExcepcionDTO(
                                persona.idPersona(),
                                clean(persona.maximoHoras())
                        )
                );
            }
        }

        if (result.isEmpty()) {
            for (Long idPersona : cleanIds(idsPersonasFallback)) {
                result.putIfAbsent(
                        idPersona,
                        new RestriccionCargaPersonaExcepcionDTO(
                                idPersona,
                                null
                        )
                );
            }
        }

        return result.values().stream().toList();
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
            List<RestriccionCargaProgramaExcepcionDTO> programas,
            List<RestriccionCargaPersonaExcepcionDTO> personas
    ) {}
}