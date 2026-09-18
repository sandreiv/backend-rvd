package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.DetalleCargaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DetalleNovedadCargaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RelacionCargaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GuardarNovedadesDetallesProyectosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsignarCentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsociacionCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleNovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionCargaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasProgramaProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadCargaDocenteService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadCargaDocenteServiceImpl
        implements NovedadCargaDocenteService {

    private static final String COMPONENT_ASSIGN_NAME_NN = "asign-name-nn";

    private static final String COMPONENT_CONTRACT_MODALITY = "change-contract-modality";

    private static final String COMPONENT_CHANGE_PROJECT_ACTIVITIES = "change-project-activities";

    private static final String ESTADO_NOVEDAD_REVISION = "0";

    private static final String PREASIGNACION_SOLO_LECTURA = "La convocatoria tiene restricción activa y esta coordinación no está habilitada para edición en las fechas permitidas.";

    private static final Set<String> CODIGOS_CENTRO_COSTO_ESPECIAL = Set.of("CTEI", "ISU");

    private final CargaDocenteRepository cargaDocenteRepository;

    private final CargaRepository cargaRepository;

    private final ConvocatoriaRepository convocatoriaRepository;

    private final RestriccionPorCoordinacionRepository restriccionPorCoordinacionRepository;

    private final RestriccionCargaRepository restriccionCargaRepository;

    private final AsignarCentroCostoRepository asignarCentroCostoRepository;

    private final  AsociacionCoordinacionRepository asociacionCoordinacionRepository;

    private final PersonaProyectoRepository personaProyectoRepository;

    private final RelacionCargaProyectoRepository relacionCargaProyectoRepository;

    private final NovedadCargaDocenteRepository novedadCargaDocenteRepository;

    private final DetalleNovedadCargaDocenteRepository detalleNovedadCargaDocenteRepository;

    private final NovedadRepository novedadRepository;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    private final DetalleNovedadCargaDocenteMapper detalleNovedadCargaDocenteMapper;

    private final DetalleCargaDocenteMapper detalleCargaDocenteMapper;

    private final RelacionCargaProyectoMapper relacionCargaProyectoMapper;

    @Override
    @Transactional
    public void assignNameToNn(AsignarNombreNnDTO dto) {
        validateRequest(dto);
        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                        .findById(dto.idCargaDocente())
                        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"No existe la carga docente seleccionada"));

        NovedadEntity novedad =
                novedadRepository
                        .findById(dto.idNovedad())
                        .orElseThrow(
                                () -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad seleccionada"));

        validateNoveltyType(novedad);
        /*
         * Una carga no debe recibir otra novedad
         * mientras tenga una pendiente de revisión.
         */
        if (novedadCargaDocenteRepository.countNoveltyInReview(dto.idCargaDocente()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT,"El docente tiene una novedad en revisión");
        }
        /*
         * El método agregado por el equipo determina
         * si existe una fotografía válida anterior.
         */
        Optional<NovedadCargaDocenteEntity> previous = novedadCargaDocenteRepository.findProfessorRecordToDuplicateNovelty(dto.idCargaDocente());

        /*
         * Asignar nombre a NN solamente se puede ejecutar
         * cuando el estado efectivo todavía no tiene persona.
         */
        Long currentPersonId =previous.map(NovedadCargaDocenteEntity::getIdPersonaGeneral).orElse(cargaDocente.getIdPersonaGeneral());

        if (currentPersonId != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga seleccionada ya tiene un docente asignado");
        }

        String registradoPor =RegistradoPorUtils.value(Accion.INSERT);

        int inserted;

        if (previous.isPresent()) {

            inserted = novedadCargaDocenteRepository.insertAssignNameNnFromNovelty(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor);

        } else {
            inserted = novedadCargaDocenteRepository.insertAssignNameNnFromCargaDocente(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor);
        }

        if (inserted != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No fue posible registrar la novedad");
        }

        log.info(
                "assignNameToNn ===> Novedad registrada. cadoId={}, noveId={}, nuevoPegeId={}",
                dto.idCargaDocente(),
                dto.idNovedad(),
                dto.idPersonaGeneral()
        );
    }

    @Override
    @Transactional
    public void saveContractModalityProfessor(CambioModalidadHoraCatedraticoDTO dto) {

        log.info(
                "saveContractModalityProfessor ===> Guardando novedad. idCargaDocente={}, idNovedad={}",
                dto != null ? dto.idCargaDocente() : null,
                dto != null ? dto.idNovedad() : null
        );

        validateContractModalityRequest(dto);

        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                        .findById(dto.idCargaDocente())
                        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"No existe la carga docente con id " + dto.idCargaDocente()));

        validateCargaDocenteMatch(cargaDocente, dto);

        NovedadEntity novedad =
                novedadRepository
                        .findById(dto.idNovedad())
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la novedad seleccionada"
                                )
                        );

        validateContractModalityType(novedad);

        Optional<NovedadCargaDocenteEntity> existing =
                novedadCargaDocenteRepository
                        .findByIdCargaDocente(
                                dto.idCargaDocente()
                        );

        validateNoOtherNoveltyInReview(existing, dto.idNovedad());

        boolean isNew = existing.isEmpty();
        NovedadCargaDocenteEntity entity =
                existing.orElseGet(
                        NovedadCargaDocenteEntity::new
                );

        fillNovedad(
                entity,
                dto,
                cargaDocente
        );

        persistNovedad(
                entity,
                isNew
        );
        replaceDetails(
                dto.idCargaDocente(),
                dto.detalles()
        );

        log.info(
                "saveContractModalityProfessor ===> Novedad guardada. idCargaDocente={}",
                dto.idCargaDocente()
        );
    }

    @Override
    @Transactional
    public void saveNoveltyProjectActivities(GuardarNovedadesDetallesProyectosDTO dto) {
        // Los campos de idCargaDocente o detallesCargaDocente ya vienen correctamente como novedad u original segun sea el caso
        log.info("saveNoveltyProjectActivities ===> Guardando novedad detalle precarga docente. idCargaDocente={}", dto.idCargaDocente());

        NovedadEntity novedad = novedadRepository.findById(dto.idNovedad())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad seleccionada"));
        validateChangeProjectActivitiesType(novedad);

        if (novedadCargaDocenteRepository.countNoveltyInReview(dto.idCargaDocente()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT,"El docente tiene una novedad en revisión");
        }

        Optional<NovedadCargaDocenteEntity> previous = novedadCargaDocenteRepository.findProfessorRecordToDuplicateNovelty(dto.idCargaDocente());
        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        int inserted;

        // Falta revisar los nuevos valores de los contratos porque se cambiaron actividades. De momento solo se copian del de referencia, luego se deben pasar como campos
        if (previous.isPresent()) {
            inserted = novedadCargaDocenteRepository.insertChangeProjectActivitiesFromNovelty(dto.idCargaDocente(), dto.idNovedad(), registradoPor);
        } else {
            inserted = novedadCargaDocenteRepository.insertChangeProjectActivitiesFromCargaDocente(dto.idCargaDocente(), dto.idNovedad(), registradoPor);
        }
        if (inserted != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No fue posible registrar la novedad");
        }


        Long idCoordinacion = resolveIdCoordinacionByNovedadCargaDocente(dto.idCargaDocente());
        validatePreassignmentWriteAllowedByNovedadCargaDocente(dto.idCargaDocente());

        if (dto.detallesNuevos() != null && !dto.detallesNuevos().isEmpty()) {
            agregarDetalleNovedad(dto.idCargaDocente(), dto.detallesNuevos(), idCoordinacion);
        }
        if (dto.detallesActualizados() != null && !dto.detallesActualizados().isEmpty()) {
            for (DetalleCargaDocenteDTO detalle: dto.detallesActualizados()) {
                actualizarDetalleNovedad(detalle, idCoordinacion);
            }
        }

        log.info("saveNoveltyProjectActivities ===> Novedad detalle precarga docente guardado. idCargaDocente={}", dto.idCargaDocente());
    }

    @Override
    @Transactional
    public void deleteProfessorActivityNovelty(Long idDetalleNovedadCargaDocente) {
        // Los campos de idCargaDocente o detallesCargaDocente ya vienen correctamente como novedad u original segun sea el caso
        log.info("deleteProfessorActivityNovelty ===> Eliminando actividad docente. idDetalle={}", idDetalleNovedadCargaDocente);

        validatePreassignmentWriteAllowedByNovedadDetalle(idDetalleNovedadCargaDocente);

        detalleNovedadCargaDocenteRepository.deleteByProcedure(
                idDetalleNovedadCargaDocente,
                RegistradoPorUtils.value(Accion.DELETE));

        log.info("deleteProfessorActivityNovelty ===> Actividad docente eliminada. idDetalle={}", idDetalleNovedadCargaDocente);
    }


    private void validateRequest(
            AsignarNombreNnDTO dto
    ) {

        if (
            dto == null ||
            dto.idCargaDocente() == null ||
            dto.idNovedad() == null ||
            dto.idPersonaGeneral() == null
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la novedad y el docente son obligatorios"
            );
        }
    }

    private void validateNoveltyType(
            NovedadEntity novedad
    ) {

        String component =
                novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_ASSIGN_NAME_NN
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Asignar nombre a NN"
            );
        }
    }

    private void validateContractModalityRequest(
            CambioModalidadHoraCatedraticoDTO dto
    ) {

        if (
            dto == null ||
            dto.idCargaDocente() == null ||
            dto.idCarga() == null ||
            dto.idNovedad() == null ||
            dto.idModalidadContratacion() == null
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la carga, la novedad y la modalidad son obligatorias"
            );
        }

        FechasConvocatoriaFormularioDTO fechas =
                dto.fechasConvocatoria();

        if (
            fechas == null ||
            fechas.id() == null
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las fechas de convocatoria son obligatorias"
            );
        }

        if (
            dto.detalles() == null ||
            dto.detalles().isEmpty()
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Los detalles de la novedad son obligatorios"
            );
        }

        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            validateDetalleItem(detalle);
        }
    }

    private void validateCargaDocenteMatch(
            CargaDocenteEntity cargaDocente,
            CambioModalidadHoraCatedraticoDTO dto
    ) {

        if (
            cargaDocente.getIdCarga() == null ||
            !cargaDocente.getIdCarga().equals(dto.idCarga())
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga enviada no corresponde a la carga docente"
            );
        }
    }

    private void validateContractModalityType(
            NovedadEntity novedad
    ) {

        String component =
                novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_CONTRACT_MODALITY
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a cambio de modalidad"
            );
        }
    }

    private void validateChangeProjectActivitiesType(
            NovedadEntity novedad
    ) {

        String component =
                novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_CHANGE_PROJECT_ACTIVITIES
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Reasignación de proyectos docente"
            );
        }
    }

    private void validateNoOtherNoveltyInReview(
            Optional<NovedadCargaDocenteEntity> existing,
            Long idNovedad
    ) {

        if (existing.isEmpty()) {
            return;
        }

        NovedadCargaDocenteEntity current =
                existing.get();

        if (
            ESTADO_NOVEDAD_REVISION
                    .equals(current.getEstadoNovedad()) &&
            current.getIdNovedadCatalogo() != null &&
            !idNovedad.equals(
                    current.getIdNovedadCatalogo()
            )
        ) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente tiene una novedad en revisión"
            );
        }
    }

    private void validateDetalleItem(
            DetalleCargaDocenteItemDTO detalle
    ) {

        if (detalle == null || detalle.horas() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas del detalle son obligatorias"
            );
        }

        if (detalle.idCentroCosto() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El centro de costo del detalle es obligatorio"
            );
        }

        Long tipoActividad =
                detalle.idTipoActividadHija() != null
                        ? detalle.idTipoActividadHija()
                        : detalle.idTipoActividad();

        if (tipoActividad == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de actividad del detalle es obligatorio"
            );
        }
    }

    private void fillNovedad(
            NovedadCargaDocenteEntity entity,
            CambioModalidadHoraCatedraticoDTO dto,
            CargaDocenteEntity cargaDocente
    ) {

        FechasConvocatoriaFormularioDTO fechas =
                dto.fechasConvocatoria();
        boolean isNew = entity.getIdCargaDocente() == null;
        Date now = new Date();

        entity.setIdCargaDocente(dto.idCargaDocente());
        entity.setIdCarga(dto.idCarga());
        entity.setIdPersonaGeneral(
                dto.idPersonaGeneral() != null
                        ? dto.idPersonaGeneral()
                        : cargaDocente.getIdPersonaGeneral()
        );
        entity.setIdModalidadContratacion(
                dto.idModalidadContratacion()
        );
        entity.setIdCategoriaCatedratico(
                dto.idCategoriaCatedratico()
        );
        entity.setIdFechasConvocatoria(fechas.id());
        entity.setIdNovedadCatalogo(dto.idNovedad());
        entity.setFechaNovedad(now);
        entity.setFechaInicio(fechas.fechaInicio());
        entity.setFechaFin(fechas.fechaFin());
        copyContractValues(entity, dto, cargaDocente);
        entity.setEstadoNovedad(ESTADO_NOVEDAD_REVISION);
        entity.setRegistradoPor(
                RegistradoPorUtils.value(
                        isNew ? Accion.INSERT : Accion.UPDATE
                )
        );
        entity.setFechaCambio(now);
    }

    private void copyContractValues(
            NovedadCargaDocenteEntity entity,
            CambioModalidadHoraCatedraticoDTO dto,
            CargaDocenteEntity cargaDocente
    ) {

        entity.setValorContrato(dto.valorContrato());
        entity.setValorPrestaciones(dto.valorPrestaciones());
        entity.setSalario(dto.asignacionSalarial());
        entity.setTotalContrato(dto.totalContrato());
        entity.setValorHora(dto.valorHora());
        entity.setValorPunto(dto.valorPunto());
        entity.setPuntos(trimToNull(dto.puntos()));
        entity.setSemanas(trimToNull(dto.semanas()));
        entity.setHoras(trimToNull(dto.horas()));
        entity.setHorasDeExcepcion(
                trimToNull(dto.horasDeExcepcion())
        );
        entity.setOnceMeses(resolveOnceMeses(dto));
        entity.setEstado(cargaDocente.getEstado());
        entity.setVigente(cargaDocente.getVigente());
        entity.setNivelFormacion(
                cargaDocente.getNivelFormacion()
        );
        entity.setMomento(cargaDocente.getMomento());
    }

    private String resolveOnceMeses(
            CambioModalidadHoraCatedraticoDTO dto
    ) {

        if (StringUtils.hasText(dto.onceMeses())) {
            return dto.onceMeses().trim();
        }

        return FechasConvocatoriaCalculator
                .calcularOnceMesesPorSemanas(dto.semanas());
    }

    private void persistNovedad(
            NovedadCargaDocenteEntity entity,
            boolean isNew
    ) {

        if (isNew) {
            entityManager.persist(entity);
        } else {
            novedadCargaDocenteRepository.save(entity);
        }

        entityManager.flush();
    }

    private void replaceDetails(
            Long idCargaDocente,
            List<DetalleCargaDocenteItemDTO> detalles
    ) {

        deleteExistingDetails(idCargaDocente);

        for (DetalleCargaDocenteItemDTO detalle : detalles) {
            DetalleNovedadCargaDocenteEntity entity =
                    new DetalleNovedadCargaDocenteEntity();
            fillDetalle(
                    entity,
                    idCargaDocente,
                    detalle
            );
            detalleNovedadCargaDocenteRepository.save(entity);
        }
    }

    private void deleteExistingDetails(
            Long idCargaDocente
    ) {

        List<DetalleNovedadCargaDocenteEntity> actuales =
                detalleNovedadCargaDocenteRepository
                        .findByIdNovedadCargaDocente(
                                idCargaDocente
                        );

        String registradoPor =
                RegistradoPorUtils.value(Accion.DELETE);

        for (DetalleNovedadCargaDocenteEntity actual : actuales) {
            BigDecimal result =
                    detalleNovedadCargaDocenteRepository
                            .deleteByProcedure(
                                    actual.getId(),
                                    registradoPor
                            );
            validateProcedureResult(
                    result,
                    "No se pudo eliminar el detalle de la novedad"
            );
        }
    }

    private void fillDetalle(
            DetalleNovedadCargaDocenteEntity entity,
            Long idCargaDocente,
            DetalleCargaDocenteItemDTO detalle
    ) {

        Long tipoActividad =
                detalle.idTipoActividadHija() != null
                        ? detalle.idTipoActividadHija()
                        : detalle.idTipoActividad();

        entity.setIdNovedadCargaDocente(idCargaDocente);
        entity.setIdPrograma(detalle.idPrograma());
        entity.setIdGrupo(detalle.idGrupo());
        entity.setIdTipoActividad(tipoActividad);
        entity.setIdCentroCosto(detalle.idCentroCosto());
        entity.setHoras(detalle.horas().toString());
        entity.setRegistradoPor(
                RegistradoPorUtils.value(Accion.INSERT)
        );
        entity.setFechaCambio(new Date());
    }

    private void validateProcedureResult(
            BigDecimal result,
            String message
    ) {

        if (
            result == null ||
            BigDecimal.ONE.compareTo(result) != 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    message
            );
        }
    }

    private String trimToNull(String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }

    // Similar a SaveDetailProfessorPreload pero enfocado en novedades
    private void agregarDetalleNovedad(Long idNovedadCargaDocente, List<DetalleCargaDocenteItemDTO> detalles, Long idCoordinacion) {
        validateProgramHourRestrictionOnSaveNovelty(detalles, idNovedadCargaDocente);

        for (DetalleCargaDocenteItemDTO detalle : detalles) {
            validateDetalleItem(detalle, idCoordinacion);

            Long idCentroCosto = resolveIdCentroCosto(detalle, idCoordinacion);
            DetalleNovedadCargaDocenteEntity entity = detalleNovedadCargaDocenteMapper.toEntity(idNovedadCargaDocente, detalle, idCentroCosto);
            
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            entity.setFechaCambio(new Date());
            DetalleNovedadCargaDocenteEntity saved = detalleNovedadCargaDocenteRepository.save(entity);
            saveRelacionesCargaProyectoNovedad(saved.getId(), detalle.relacionCargaProyecto());
        }
    }

    // Similar a UpdateDetailProfessorPreload pero enfocado en novedades
    private void actualizarDetalleNovedad(DetalleCargaDocenteDTO dto, Long idCoordinacion) {
        validateUpdateNoveltyDetailProfessorPreload(dto, idCoordinacion);

        Long idDetalleNovedadCargaDocente = dto.idDetalleCargaDocente();
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        DetalleNovedadCargaDocenteEntity detallePersistido = detalleNovedadCargaDocenteRepository.findById(idDetalleNovedadCargaDocente).orElseThrow();
        Long idCentroCosto = resolveIdCentroCostoFromActividad(actividad, idCoordinacion);
        DetalleNovedadCargaDocenteEntity entity = detalleNovedadCargaDocenteMapper.toEntityFromDto(dto, idCentroCosto);
        entity.setIdTipoActividad(detalleCargaDocenteMapper
                .resolveTipoActividadFromActividad(
                        actividad,
                        detallePersistido.getIdTipoActividad()));
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());
        detalleNovedadCargaDocenteRepository.save(entity);

        relacionCargaProyectoRepository.deleteByIdDetalleNovedadCargaDocente(
                idDetalleNovedadCargaDocente);
        saveRelacionesCargaProyectoNovedad(
                idDetalleNovedadCargaDocente,
                detalleCargaDocenteMapper.toRelacionesCargaProyecto(
                        actividad.relacionCargaProyecto()));
    }

    private Long resolveIdCoordinacionByNovedadCargaDocente(Long idNovedadCargaDocente) {
        if (idNovedadCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La novedad carga docente es obligatoria");
        }
        NovedadCargaDocenteEntity novedadCargaDocente = novedadCargaDocenteRepository.findByIdCargaDocente(idNovedadCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la novedad carga docente con id " + idNovedadCargaDocente));
        if (novedadCargaDocente.getIdCarga() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La novedad carga docente no tiene carga asociada");
        }
        CargaEntity carga = cargaRepository.findById(novedadCargaDocente.getIdCarga())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la novedad carga con id " + novedadCargaDocente.getIdCarga()));
        return carga.getIdCoordinacion();
    }

    private void validatePreassignmentWriteAllowedByNovedadDetalle(Long idDetalleNovedadCargaDocente) {
        if (idDetalleNovedadCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle novedad de novedad carga docente es obligatorio");
        }

        DetalleNovedadCargaDocenteEntity detallNovedad = detalleNovedadCargaDocenteRepository.findById(idDetalleNovedadCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el detalle novedad de novedad carga docente con id " + idDetalleNovedadCargaDocente));

        validatePreassignmentWriteAllowedByNovedadCargaDocente(detallNovedad.getIdNovedadCargaDocente());
    }

    private void validatePreassignmentWriteAllowedByNovedadCargaDocente(Long idNovedadCargaDocente) {
        if (idNovedadCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la novedad carga docente es obligatorio");
        }

        NovedadCargaDocenteEntity novedadCargaDocente = novedadCargaDocenteRepository.findByIdCargaDocente(idNovedadCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad carga docente con id " + idNovedadCargaDocente));

        validatePreassignmentWriteAllowedByCarga(novedadCargaDocente.getIdCarga());
    }

    private void validatePreassignmentWriteAllowedByCarga(Long idCarga) {
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }

        CargaEntity carga = cargaRepository.findById(idCarga)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga));

        validatePreassignmentWriteAllowed(carga);
    }

    private void validatePreassignmentWriteAllowed(CargaEntity carga) {
        if (carga == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga de la preasignación");
        }

        if (isPreassignmentWriteAllowed(carga.getIdConvocatoria(), carga.getIdCoordinacion())) {
            return;
        }

        log.warn("validatePreassignmentWriteAllowed ===> Escritura bloqueada. idCarga={}, idConvocatoria={}, idCoordinacion={}",
                carga.getId(), carga.getIdConvocatoria(), carga.getIdCoordinacion());

        throw new ApiException(HttpStatus.FORBIDDEN, PREASIGNACION_SOLO_LECTURA);
    }

    private boolean isPreassignmentWriteAllowed(Long idConvocatoria, Long idCoordinacion) {
        if (idConvocatoria == null || idCoordinacion == null) {
            return false;
        }

        var convocatoria = convocatoriaRepository.findById(idConvocatoria)
                .orElse(null);

        if (convocatoria == null) {
            return false;
        }

        Long totalRestricciones = restriccionPorCoordinacionRepository.countActiveNonExpiredRestrictionsByConvocatoria(idConvocatoria);

        boolean tieneRestriccionesNoVencidas = totalRestricciones != null && totalRestricciones > 0;

        if (!tieneRestriccionesNoVencidas) {
            return "1".equals(convocatoria.getEstado());
        }

        Long totalRestriccionesEditables = restriccionPorCoordinacionRepository
                .countEditableRestrictionsByConvocatoriaAndCoordinacion(
                        idConvocatoria,
                        idCoordinacion);

        return totalRestriccionesEditables != null && totalRestriccionesEditables > 0;
    }

    private void validateProgramHourRestrictionOnSaveNovelty(List<DetalleCargaDocenteItemDTO> detalles, Long idCargaDocente) {
        NovedadCargaDocenteEntity novedadCargaDocente = novedadCargaDocenteRepository
                .findByIdCargaDocente(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + idCargaDocente));

        Map<Long, String> maximos = resolveMaximosHorasPrograma(novedadCargaDocente.getIdModalidadContratacion());
        if (maximos.isEmpty()) {
            return;
        }

        Map<Long, BigDecimal> horasNuevas = new LinkedHashMap<>();
        for (DetalleCargaDocenteItemDTO detalle : detalles) {
            if (detalle.idPrograma() == null || detalle.horas() == null) {
                continue;
            }
            if (!maximos.containsKey(detalle.idPrograma())) {
                continue;
            }
            horasNuevas.merge(
                    detalle.idPrograma(),
                    BigDecimal.valueOf(detalle.horas()),
                    BigDecimal::add);
        }

        if (horasNuevas.isEmpty()) {
            return;
        }

        Map<Long, BigDecimal> horasAsignadas = loadHorasAsignadasPorProgramaEnNovedad(idCargaDocente, null);

        for (Map.Entry<Long, BigDecimal> entry : horasNuevas.entrySet()) {
            assertProgramHoursWithinLimit(
                    entry.getKey(),
                    maximos.get(entry.getKey()),
                    horasAsignadas.getOrDefault(
                            entry.getKey(),
                            BigDecimal.ZERO),
                    entry.getValue());
        }
    }

    private void validateProgramHourRestrictionOnUpdateNovelty(
            DetalleCargaDocenteDTO dto,
            Long idDetallePersistido) {
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        Long idPrograma = actividad.programa() != null
                ? actividad.programa().id()
                : null;
        if (idPrograma == null || !StringUtils.hasText(actividad.horas())) {
            return;
        }

        NovedadCargaDocenteEntity novedadCargaDocente = novedadCargaDocenteRepository
                .findByIdCargaDocente(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + dto.idCargaDocente()));

        Map<Long, String> maximos = resolveMaximosHorasPrograma(
                novedadCargaDocente.getIdModalidadContratacion());
        String maximoHoras = maximos.get(idPrograma);
        if (!StringUtils.hasText(maximoHoras)) {
            return;
        }

        Map<Long, BigDecimal> horasAsignadas = loadHorasAsignadasPorProgramaEnNovedad(
                dto.idCargaDocente(),
                idDetallePersistido
        );

        assertProgramHoursWithinLimit(
                idPrograma,
                maximoHoras,
                horasAsignadas.getOrDefault(idPrograma, BigDecimal.ZERO),
                parseHorasDetalle(actividad.horas()));
    }

    private Map<Long, String> resolveMaximosHorasPrograma(Long idModalidadContratacion) {
        Map<Long, String> result = new LinkedHashMap<>();

        restriccionCargaRepository.findById(idModalidadContratacion)
                .map(RestriccionCargaEntity::getExcepcion)
                .ifPresent(excepcion -> {
                    try {
                        JsonNode root = objectMapper.readTree(excepcion);
                        JsonNode programas = root.get("programas");
                        if (programas == null || !programas.isArray()) {
                            return;
                        }
                        for (JsonNode programa : programas) {
                            if (programa == null || programa.isNull()) {
                                continue;
                            }

                            Long idPrograma;
                            String maximoHoras = null;

                            if (programa.isObject()) {
                                idPrograma = parseLongNode(
                                        programa.get("idPrograma"));
                                if (idPrograma == null) {
                                    idPrograma = parseLongNode(
                                            programa.get("id"));
                                }
                                JsonNode maximoNode =
                                        programa.get("maximoHoras");
                                if (maximoNode != null
                                        && StringUtils.hasText(
                                                maximoNode.asText())) {
                                    maximoHoras = maximoNode.asText().trim();
                                }
                            } else {
                                idPrograma = parseLongNode(programa);
                            }

                            if (idPrograma != null
                                    && StringUtils.hasText(maximoHoras)) {
                                result.putIfAbsent(idPrograma, maximoHoras);
                            }
                        }
                    } catch (JsonProcessingException ex) {
                        log.warn(
                                "resolveMaximosHorasPrograma ===> No fue posible leer excepciones de programa. idModalidad={}",
                                idModalidadContratacion);
                    }
                });

        return result;
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

    private Map<Long, BigDecimal> loadHorasAsignadasPorProgramaEnNovedad(
            Long idNovedadCargaDocente,
            Long idDetalleExcluido) {
        Map<Long, BigDecimal> result = new LinkedHashMap<>();
        List<HorasProgramaProjection> rows =
                detalleNovedadCargaDocenteRepository
                        .findHorasByProgramaAndCargaDocente(
                                idNovedadCargaDocente,
                                idDetalleExcluido);
        for (HorasProgramaProjection row : rows) {
            if (row.getIdPrograma() == null) {
                continue;
            }
            result.put(
                    row.getIdPrograma(),
                    row.getTotalHoras() != null
                            ? row.getTotalHoras()
                            : BigDecimal.ZERO);
        }
        return result;
    }

    private void assertProgramHoursWithinLimit(
            Long idPrograma,
            String maximoHoras,
            BigDecimal horasAsignadas,
            BigDecimal horasNuevas) {
        BigDecimal maximo = parseHorasDetalle(maximoHoras);
        BigDecimal total = horasAsignadas.add(horasNuevas);
        if (total.compareTo(maximo) > 0) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas del programa " + idPrograma
                            + " exceden el máximo permitido de "
                            + maximoHoras
                            + " (asignadas: " + horasAsignadas
                            + ", nuevas: " + horasNuevas + ")");
        }
    }

    private BigDecimal parseHorasDetalle(String horas) {
        if (!StringUtils.hasText(horas)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(horas.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private void validateDetalleItem(
            DetalleCargaDocenteItemDTO detalle,
            Long idCoordinacion) {
        if (detalle.horas() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        Long idCentroCostoResuelto = resolveIdCentroCosto(detalle, idCoordinacion);
        if (idCentroCostoResuelto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo del detalle es obligatorio");
        }
        Long tipoActividad = detalle.idTipoActividadHija() != null ? detalle.idTipoActividadHija() : detalle.idTipoActividad();
        if (tipoActividad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad del detalle es obligatorio");
        }
        if (detalle.relacionCargaProyecto() != null) {
            for (RelacionCargaProyectoDTO relacion : detalle.relacionCargaProyecto()) {
                validateRelacionCargaProyecto(relacion);
            }
        }
    }

    private Long resolveIdCentroCosto(
            DetalleCargaDocenteItemDTO detalle,
            Long idCoordinacion) {
        Long idCentroEspecial = findIdCentroCostoByCodigoActividadEspecial(detalle.codigoTipoActividad());
        if (idCentroEspecial != null) {
            // Prioridad 1: CTEI/ISU desde coordinación con el mismo COOR_CODIGO.
            return idCentroEspecial;
        }
        Long idCentroPrograma = findIdCentroCostoProgramaAsociado(idCoordinacion, detalle.idPrograma());
        if (idCentroPrograma != null) {
            // Prioridad 2: centro de costo del programa en ASOCIACIONCOORDINACION.
            return idCentroPrograma;
        }
        if (detalle.materia() != null && detalle.materia().idCentroCosto() != null) {
            // Prioridad 3: centro de costo de la materia (transversales).
            return detalle.materia().idCentroCosto();
        }
        // Prioridad 4: centro de costo enviado en el formulario.
        return detalle.idCentroCosto();
    }

    private Long resolveIdCentroCostoFromActividad(DetalleCargaDocenteActividadDTO actividad, Long idCoordinacion) {
        Long idCentroEspecial = findIdCentroCostoByCodigoActividadEspecial(resolveCodigoTipoActividad(actividad));
        if (idCentroEspecial != null) {
            return idCentroEspecial;
        }
        Long idPrograma = actividad.programa() != null
                ? actividad.programa().id()
                : null;
        Long idCentroPrograma = findIdCentroCostoProgramaAsociado(
                idCoordinacion, idPrograma);
        if (idCentroPrograma != null) {
            return idCentroPrograma;
        }
        // En update no hay materia.idCentroCosto; se usa el del formulario.
        if (actividad.centroCosto() == null) {
            return null;
        }
        return actividad.centroCosto().id();
    }

    private Long findIdCentroCostoByCodigoActividadEspecial(String codigoTipoActividad) {
        if (!StringUtils.hasText(codigoTipoActividad)) {
            return null;
        }
        String codigo = codigoTipoActividad.trim().toUpperCase();
        if (!CODIGOS_CENTRO_COSTO_ESPECIAL.contains(codigo)) {
            return null;
        }
        return asignarCentroCostoRepository
                .findIdCentroCostoByCodigoCoordinacion(codigo)
                .orElse(null);
    }

    private Long findIdCentroCostoProgramaAsociado(Long idCoordinacion, Long idPrograma) {
        if (idCoordinacion == null || idPrograma == null) {
            return null;
        }
        return asociacionCoordinacionRepository
                .findIdCentroCostoByCoordinacionAndPrograma(
                        idCoordinacion, idPrograma)
                .orElse(null);
    }

    private void validateRelacionCargaProyecto(RelacionCargaProyectoDTO relacion) {
        if (relacion.idPersonaProyecto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona proyecto es obligatoria");
        }
        if (relacion.idProyecto() != null && !personaProyectoRepository.existsByIdAndIdProyecto(relacion.idPersonaProyecto(), relacion.idProyecto())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona proyecto no corresponde al proyecto indicado");
        }
        if (!personaProyectoRepository.existsById(relacion.idPersonaProyecto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona proyecto con id " + relacion.idPersonaProyecto());
        }
    }

    private String resolveCodigoTipoActividad(DetalleCargaDocenteActividadDTO actividad) {
        if (actividad.tipoActividadHija() != null) {
            for (TipoActividadDTO hija : actividad.tipoActividadHija()) {
                if (hija != null && StringUtils.hasText(hija.codigo())) {
                    return hija.codigo();
                }
            }
        }
        if (actividad.tipoActividad() != null
                && StringUtils.hasText(actividad.tipoActividad().codigo())) {
            return actividad.tipoActividad().codigo();
        }
        return null;
    }

    private void saveRelacionesCargaProyectoNovedad(Long idDetalleNovedadCargaDocente, List<RelacionCargaProyectoDTO> relaciones) {
        if (relaciones == null || relaciones.isEmpty()) {
            return;
        }
        for (RelacionCargaProyectoDTO relacion : relaciones) {
            RelacionCargaProyectoEntity entity = relacionCargaProyectoMapper.toEntityFromDetalleNovedad(idDetalleNovedadCargaDocente, relacion);
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            entity.setFechaCambio(new Date());
            relacionCargaProyectoRepository.save(entity);
        }
    }

    private void validateUpdateNoveltyDetailProfessorPreload(
            DetalleCargaDocenteDTO dto,
            Long idCoordinacion) {
        if (dto.idDetalleCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle de carga docente es obligatorio");
        }
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,  "La carga docente es obligatoria");
        }
        if (dto.detalles() == null || dto.detalles().size() != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La actualizacion requiere exactamente un detalle");
        }
        if (!cargaDocenteRepository.existsById(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + dto.idCargaDocente());
        }

        DetalleNovedadCargaDocenteEntity detallePersistido = detalleNovedadCargaDocenteRepository
                .findById(dto.idDetalleCargaDocente())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el detalle de carga docente con id " + dto.idDetalleCargaDocente()));

        if (!detallePersistido.getIdNovedadCargaDocente().equals(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El detalle no pertenece a la carga docente enviada");
        }

        validateDetalleActividad(
                dto.detalles().get(0),
                detallePersistido.getIdTipoActividad(),
                idCoordinacion);
        validateProgramHourRestrictionOnUpdateNovelty(dto, detallePersistido.getId());
    }

    private void validateDetalleActividad(
            DetalleCargaDocenteActividadDTO actividad,
            Long idTipoActividadPersistido,
            Long idCoordinacion) {
        if (actividad.horas() == null || actividad.horas().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        Long idCentroCostoResuelto = resolveIdCentroCostoFromActividad(
                actividad, idCoordinacion);
        if (idCentroCostoResuelto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo del detalle es obligatorio");
        }
        Long tipoActividad = detalleCargaDocenteMapper
                .resolveTipoActividadFromActividad(
                        actividad,
                        idTipoActividadPersistido);
        if (tipoActividad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad del detalle es obligatorio");
        }
        if (actividad.relacionCargaProyecto() != null) {
            for (RelacionCargaProyectoListadoDTO relacion
                    : actividad.relacionCargaProyecto()) {
                validateRelacionCargaProyecto(new RelacionCargaProyectoDTO(
                        relacion.idPersonaProyecto(),
                        relacion.idProyecto()));
            }
        }
    }
}
