package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleNovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
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

    private static final String COMPONENT_DELETE_PROFESSOR = "delete-professor";

    private static final String ESTADO_NOVEDAD_REVISION = "0";

    private final CargaDocenteRepository cargaDocenteRepository;

    private final NovedadCargaDocenteRepository novedadCargaDocenteRepository;

    private final DetalleNovedadCargaDocenteRepository detalleNovedadCargaDocenteRepository;

    private final NovedadRepository novedadRepository;

    private final PersonaGeneralRepository personaGeneralRepository;

    private final EntityManager entityManager;

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

        CargaDocenteEntity cargaDocente =
                cargaDocenteRepository
                        .findById(dto.idCargaDocente())
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe la carga docente con id "
                                        + dto.idCargaDocente()));

        validateCargaDocenteMatch(cargaDocente, dto);

        NovedadEntity novedad =
                novedadRepository
                        .findById(dto.idNovedad())
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe la novedad seleccionada"));

        validateContractModalityType(novedad);

        /*
         * Fuente de verdad: si existe fotografía vigente en
         * NOVEDADCARGADOCENTE, se duplica desde ahí.
         * Si no, se construye desde CARGADOCENTE.
         */
        Optional<NovedadCargaDocenteEntity> previous =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                dto.idCargaDocente());

        validateNoOtherNoveltyInReview(
                previous,
                dto.idNovedad());

        NovedadCargaDocenteEntity entity =
                resolveEntityToPersist(previous, dto.idCargaDocente());
        boolean isNew = previous.isEmpty()
                && entity.getIdCargaDocente() == null;

        fillNovedad(entity, dto, previous.orElse(null), cargaDocente);
        persistNovedad(entity, isNew);
        replaceDetails(dto.idCargaDocente(), dto.detalles());

        log.info(
                "saveContractModalityProfessor ===> Novedad guardada. fuente={}, idCargaDocente={}",
                previous.isPresent() ? "NOVEDADCARGADOCENTE" : "CARGADOCENTE",
                dto.idCargaDocente());
    }

    @Override
    @Transactional
    public void changeProfessor(CambioDocenteDTO dto) {

        if (dto == null
                || dto.idCargaDocente() == null
                || dto.idNovedad() == null
                || dto.idPersonaGeneral() == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la novedad y el nuevo docente son obligatorios"
            );
        }

        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                .findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + dto.idCargaDocente()
                ));

        var novedad = novedadRepository
                .findById(dto.idNovedad())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la novedad seleccionada"
                ));

        if (!"change-professor".equalsIgnoreCase(novedad.getComponente())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Cambio de docente"
            );
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el docente seleccionado"
            );
        }

        if (novedadCargaDocenteRepository
                .countNoveltyInReview(dto.idCargaDocente()) > 0) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La carga docente ya tiene una novedad en revisión"
            );
        }

        Optional<NovedadCargaDocenteEntity> novedadOrigen =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                dto.idCargaDocente()
                        );

        Long idDocenteActual;

        if (novedadOrigen.isPresent()) {
            idDocenteActual =
                    novedadOrigen.get().getIdPersonaGeneral();
        } else {
            idDocenteActual =
                    cargaDocente.getIdPersonaGeneral();
        }

        if (idDocenteActual == null) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La carga seleccionada no tiene un docente asignado. Debe utilizar la novedad Asignar nombre a NN"
            );
        }

        if (Objects.equals(
                idDocenteActual,
                dto.idPersonaGeneral())) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente seleccionado ya es el docente actual de la carga"
            );
        }

        long asignaciones =
                novedadCargaDocenteRepository
                        .countProfessorAssignedToAnotherLoad(
                                cargaDocente.getIdCarga(),
                                dto.idCargaDocente(),
                                cargaDocente.getIdModalidadContratacion(),
                                dto.idPersonaGeneral()
                        );

        if (asignaciones > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente seleccionado ya se encuentra asignado en esta carga y modalidad"
            );
        }

        String registradoPor =
                RegistradoPorUtils.value(Accion.INSERT);

        int inserted;

        if (novedadOrigen.isPresent()) {

            inserted =
                    novedadCargaDocenteRepository
                            .insertChangeProfessorFromNovelty(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor
                            );

        } else {

            inserted =
                    novedadCargaDocenteRepository
                            .insertChangeProfessorFromCargaDocente(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor
                            );
        }

        if (inserted != 1) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible registrar la novedad de cambio de docente"
            );
        }
    }

    @Override
    @Transactional
        public void requestDeleteProfessor(EliminarDocenteDTO dto) {

        if (dto == null
                || dto.idCargaDocente() == null
                || dto.idNovedad() == null) {

                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "La carga docente y la novedad son obligatorias"
                );
        }

        cargaDocenteRepository
                .findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + dto.idCargaDocente()
                ));

        NovedadEntity novedad =
                novedadRepository
                        .findById(dto.idNovedad())
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe la novedad seleccionada"
                        ));

        validateDeleteProfessorType(novedad);

        if (novedadCargaDocenteRepository
                .countNoveltyInReview(dto.idCargaDocente()) > 0) {

                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "La carga docente ya tiene una novedad en revisión"
                );
        }

        Optional<NovedadCargaDocenteEntity> novedadOrigen =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                dto.idCargaDocente()
                        );

        String registradoPor =
                RegistradoPorUtils.value(Accion.INSERT);

        int inserted;

        if (novedadOrigen.isPresent()) {

                inserted = novedadCargaDocenteRepository
                        .insertDeleteProfessorFromNovelty(
                                dto.idCargaDocente(),
                                dto.idNovedad(),
                                registradoPor
                        );

        } else {

                inserted = novedadCargaDocenteRepository
                        .insertDeleteProfessorFromCargaDocente(
                                dto.idCargaDocente(),
                                dto.idNovedad(),
                                registradoPor
                        );
        }

        if (inserted != 1) {
                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "No fue posible registrar la novedad de eliminación del docente"
                );
        }
    }

    private void validateDeleteProfessorType(
                NovedadEntity novedad
        ) {

        String component = novedad.getComponente();

        if (component == null
                || !COMPONENT_DELETE_PROFESSOR.equalsIgnoreCase(
                        component.trim()
                )) {

                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "La novedad seleccionada no corresponde a Eliminar docente"
                );
        }
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

    private void validateNoveltyType(NovedadEntity novedad) {

        String component = novedad.getComponente();

        if (component == null ||!COMPONENT_ASSIGN_NAME_NN.equalsIgnoreCase(component.trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La novedad seleccionada no corresponde a Asignar nombre a NN");
        }
    }

    private void validateContractModalityRequest(CambioModalidadHoraCatedraticoDTO dto) {

        if (
            dto == null ||
            dto.idCargaDocente() == null ||
            dto.idCarga() == null ||
            dto.idNovedad() == null ||
            dto.idModalidadContratacion() == null
        ) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente, la carga, la novedad y la modalidad son obligatorias");
        }

        FechasConvocatoriaFormularioDTO fechas = dto.fechasConvocatoria();

        if (fechas == null || fechas.id() == null) {

            throw new ApiException(HttpStatus.BAD_REQUEST, "Las fechas de convocatoria son obligatorias");
        }

        if (dto.detalles() == null || dto.detalles().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Los detalles de la novedad son obligatorios");
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

    private void validateContractModalityType(NovedadEntity novedad) {

        String component =novedad.getComponente();

        if (component == null || !COMPONENT_CONTRACT_MODALITY.equalsIgnoreCase(component.trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La novedad seleccionada no corresponde a cambio de modalidad");
        }
    }

    private void validateNoOtherNoveltyInReview(Optional<NovedadCargaDocenteEntity> existing, Long idNovedad) {
        if (existing.isEmpty()) {
            return;
        }

        NovedadCargaDocenteEntity current = existing.get();

        if (ESTADO_NOVEDAD_REVISION.equals(current.getEstadoNovedad()) &&
            current.getIdNovedadCatalogo() != null &&
            !idNovedad.equals(current.getIdNovedadCatalogo())
        ) {
                throw new ApiException(HttpStatus.CONFLICT, "El docente tiene una novedad en revisión"
            );
        }
    }

    private void validateDetalleItem(DetalleCargaDocenteItemDTO detalle) {

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

    private NovedadCargaDocenteEntity resolveEntityToPersist(
            Optional<NovedadCargaDocenteEntity> previous,
            Long idCargaDocente
    ) {

        if (previous.isPresent()) {
            return previous.get();
        }

        return novedadCargaDocenteRepository
                .findByIdCargaDocente(idCargaDocente)
                .orElseGet(NovedadCargaDocenteEntity::new);
    }

    private void fillNovedad(
            NovedadCargaDocenteEntity entity,
            CambioModalidadHoraCatedraticoDTO dto,
            NovedadCargaDocenteEntity previous,
            CargaDocenteEntity cargaDocente
    ) {

        FechasConvocatoriaFormularioDTO fechas =
                dto.fechasConvocatoria();
        boolean isNew = entity.getIdCargaDocente() == null;
        Date now = new Date();

        entity.setIdCargaDocente(dto.idCargaDocente());
        entity.setIdCarga(dto.idCarga());
        entity.setIdPersonaGeneral(
                resolvePersonaGeneral(dto, previous, cargaDocente)
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
        copyContractValues(entity, dto);
        copyBaselineFromSource(entity, previous, cargaDocente);
        entity.setEstadoNovedad(ESTADO_NOVEDAD_REVISION);
        entity.setRegistradoPor(
                RegistradoPorUtils.value(
                        isNew ? Accion.INSERT : Accion.UPDATE
                )
        );
        entity.setFechaCambio(now);
    }

    private Long resolvePersonaGeneral(
            CambioModalidadHoraCatedraticoDTO dto,
            NovedadCargaDocenteEntity previous,
            CargaDocenteEntity cargaDocente
    ) {

        if (dto.idPersonaGeneral() != null) {
            return dto.idPersonaGeneral();
        }
        if (previous != null) {
            return previous.getIdPersonaGeneral();
        }
        return cargaDocente.getIdPersonaGeneral();
    }

    private void copyContractValues(
            NovedadCargaDocenteEntity entity,
            CambioModalidadHoraCatedraticoDTO dto
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
    }

    private void copyBaselineFromSource(
            NovedadCargaDocenteEntity entity,
            NovedadCargaDocenteEntity previous,
            CargaDocenteEntity cargaDocente
    ) {

        if (previous != null) {
            entity.setEstado(previous.getEstado());
            entity.setVigente(previous.getVigente());
            entity.setNivelFormacion(previous.getNivelFormacion());
            entity.setMomento(previous.getMomento());
            return;
        }

        entity.setEstado(cargaDocente.getEstado());
        entity.setVigente(cargaDocente.getVigente());
        entity.setNivelFormacion(cargaDocente.getNivelFormacion());
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
}
