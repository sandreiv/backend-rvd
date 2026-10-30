package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.util.ResumenCargaAssembler;

import co.edu.unipamplona.ciadti.rvd.util.OnceMesesReporteFilter;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicBasicInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PaisGeneralDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicLevelDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTitleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingSaveDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicActivitiesDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadPtdDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActivityWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdObservationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicDigitalWorkPlanDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicDigitalWorkPlanItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoDocumentoContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaNaturalGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionPtdEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PlanTrabajoDigitalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PaisGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TituloDocenteSniesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TituloPersonaNaturalGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ObservacionPtdRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PublicWorkPlanService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PlanTrabajoDigitalService;
import co.edu.unipamplona.ciadti.rvd.model.service.PtdiDocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicWorkPlanServiceImpl
                implements PublicWorkPlanService {

        private static final ZoneId ZONA = ZoneId.of("America/Bogota");
        private static final String ROL_COORDINADOR = "Coordinador";
        private static final String ROL_TALENTO = "Talento humano";

        private final PersonaGeneralRepository personaGeneralRepository;

        private final CargaDocenteRepository cargaDocenteRepository;

        private final TituloDocenteSniesRepository tituloDocenteSniesRepository;

        private final PaisGeneralRepository paisGeneralRepository;

        private final TituloPersonaNaturalGeneralRepository tituloPersonaNaturalGeneralRepository;

        private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;

        private final ObservacionPtdRepository observacionPtdRepository;

        private final PlanTrabajoDigitalRepository planTrabajoDigitalRepository;

        private final PtdiDocumentoService ptdiDocumentoService;

        private final CoordinacionService coordinacionService;

        private final PlanTrabajoDigitalService planTrabajoDigitalService;

        @Override
        @Transactional(readOnly = true)
        public PublicBasicInformationDTO getBasicInformation(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                log.debug(
                                "getBasicInformation ===> " +
                                                "Consultando información básica pública. " +
                                                "idCargaDocente={}, idPersonaGeneral={}",
                                idCargaDocente,
                                idPersonaGeneral);

                if (idCargaDocente == null) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                boolean contratacionPermitida = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .anyMatch(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente));

                if (!contratacionPermitida) {
                        throw new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "La contratación seleccionada no está disponible para el docente autenticado");
                }

                PersonaGeneralEntity persona = personaGeneralRepository
                                .findGeneralPersonById(
                                                idPersonaGeneral)
                                .orElseThrow(() -> new ApiException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró la información del docente"));

                PersonaNaturalGeneralEntity natural = persona.getPersonaNaturalGeneral();

                if (natural == null) {
                        throw new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No se encontró la información personal del docente");
                }

                PublicBasicInformationDTO result = new PublicBasicInformationDTO(

                                persona.getId(),

                                persona.getIdTipoDocumentoIdentidad(),

                                persona.getTipoDocumentoIdentidad() != null
                                                ? persona
                                                                .getTipoDocumentoIdentidad()
                                                                .getDescripcion()
                                                : null,

                                persona.getTipoDocumentoIdentidad() != null
                                                ? persona
                                                                .getTipoDocumentoIdentidad()
                                                                .getAbreviatura()
                                                : null,

                                persona.getDocumentoIdentidad(),

                                persona.getLugarExpedicion(),

                                toLocalDate(
                                                persona.getFechaExpedicion()),

                                buildFullName(natural),
                                natural.getSexo(),

                                toLocalDate(
                                                natural.getFechaNacimiento()),

                                natural.getIdEstadoCivil(),

                                natural.getEstadoCivil() != null
                                                ? natural
                                                                .getEstadoCivil()
                                                                .getDescripcion()
                                                : null,

                                natural.getIdLugarNacimiento(),

                                natural.getLugarNacimiento() != null
                                                ? natural
                                                                .getLugarNacimiento()
                                                                .getNombre()
                                                : null,

                                natural.getIdNacionalidad(),

                                natural.getNacionalidad() != null
                                                ? natural
                                                                .getNacionalidad()
                                                                .getNombre()
                                                : null,

                                natural.getIdNacimiento(),

                                natural.getNacimiento() != null
                                                ? natural
                                                                .getNacimiento()
                                                                .getNombre()
                                                : null,

                                natural.getRh(),

                                persona.getDireccion(),
                                persona.getTelefonoCelular(),
                                persona.getTelefono(),
                                persona.getMail(),
                                natural.getEmailInstitucional(),
                                natural.getFax(),

                                natural.getLibretaMilitar(),
                                natural.getClaseLibretaMilitar(),
                                natural.getDistritoMilitar(),

                                natural.getNumeroPasaporte(),

                                toLocalDate(
                                                natural.getFechaVigenciaPasaporte()),

                                natural.getOtraNacionalidad(),

                                natural.getVive(),
                                natural.getUbicacionFisicaHojavida());

                log.info(
                                "getBasicInformation ===> " +
                                                "Información básica consultada. " +
                                                "idCargaDocente={}, idPersonaGeneral={}",
                                idCargaDocente,
                                idPersonaGeneral);

                return result;
        }

        @Override
        @Transactional(readOnly = true)
        public List<PublicAcademicLevelDTO> getAcademicLevels() {

                return tituloDocenteSniesRepository
                                .findAcademicLevels()
                                .stream()
                                .map(item -> new PublicAcademicLevelDTO(
                                                item.getId(),
                                                item.getDescripcion(),
                                                item.getNivel()))
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PublicAcademicTitleDTO> getAcademicTitles(
                        Long idNivelAcademico,
                        String query) {

                if (idNivelAcademico == null
                                || idNivelAcademico < 0) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "El nivel académico es obligatorio");
                }

                String normalizedQuery = query == null
                                ? ""
                                : query.trim();

                if (normalizedQuery.length() < 2) {
                        return List.of();
                }

                return tituloDocenteSniesRepository
                                .findAcademicTitles(
                                                idNivelAcademico,
                                                normalizedQuery)
                                .stream()
                                .map(item -> new PublicAcademicTitleDTO(
                                                item.getId(),
                                                item.getIdNivelAcademico(),
                                                item.getNombre()))
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PaisGeneralDTO> getCountries() {

                return paisGeneralRepository
                                .findAvailableCountries()
                                .stream()
                                .map(item -> new PaisGeneralDTO(
                                                item.getId(),
                                                item.getNombre()))
                                .toList();
        }

        @Override
        @Transactional
        public PublicAcademicTrainingDTO saveAcademicTraining(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        PublicAcademicTrainingSaveDTO body) {

                if (idCargaDocente == null) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                boolean contratacionPermitida = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .anyMatch(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente));

                if (!contratacionPermitida) {
                        throw new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "La contratación seleccionada no está disponible para el docente autenticado");
                }

                requireEditableDigitalWorkPlan(
                        idCargaDocente,
                        idPersonaGeneral
                );

                if (!tituloDocenteSniesRepository
                                .existsById(
                                                body.idNivelAcademico())) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "El nivel académico seleccionado no existe");
                }

                Long titleCount = tituloPersonaNaturalGeneralRepository
                                .countTitleByIdAndAcademicLevel(
                                                body.idTitulo(),
                                                body.idNivelAcademico());

                if (titleCount == null
                                || titleCount == 0) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "El título seleccionado no corresponde al nivel académico indicado");
                }

                if (!paisGeneralRepository
                                .existsById(body.idPais())) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "El país seleccionado no existe");
                }

                int inserted = tituloPersonaNaturalGeneralRepository
                                .insertAcademicTraining(
                                                idPersonaGeneral,
                                                body.idTitulo(),
                                                body.institucion().trim(),

                                                /*
                                                 * La sesión pública actualmente
                                                 * identifica al docente por PEGE_ID.
                                                 */
                                                String.valueOf(
                                                                idPersonaGeneral),

                                                body.idPais(),

                                                java.sql.Date.valueOf(
                                                                body.fechaGrado()));

                if (inserted != 1) {
                        throw new ApiException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "No fue posible registrar la formación académica");
                }

                log.info(
                                "saveAcademicTraining ===> " +
                                                "Formación académica registrada. " +
                                                "idCargaDocente={}, " +
                                                "idPersonaGeneral={}, " +
                                                "idTitulo={}",
                                idCargaDocente,
                                idPersonaGeneral,
                                body.idTitulo());

                return findLatestAcademicTraining(
                                idPersonaGeneral);
        }

        @Override
        @Transactional(readOnly = true)
        public PublicAcademicTrainingDTO getLatestAcademicTraining(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                if (idCargaDocente == null) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                boolean contratacionPermitida = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .anyMatch(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente));

                if (!contratacionPermitida) {
                        throw new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "La contratación seleccionada no está disponible para el docente autenticado");
                }

                return findLatestAcademicTraining(
                                idPersonaGeneral);
        }

        @Override
        @Transactional(readOnly = true)
        public PublicActivitiesDTO getActivities(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                log.debug(
                                "getActivities ===> " +
                                                "Consultando actividades públicas efectivas. " +
                                                "idCargaDocente={}, idPersonaGeneral={}",
                                idCargaDocente,
                                idPersonaGeneral);

                if (idCargaDocente == null) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                boolean contratacionPermitida = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .anyMatch(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente));

                if (!contratacionPermitida) {
                        throw new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "La contratación seleccionada no está disponible para el docente autenticado");
                }

                var detalles = detalleCargaDocenteRepository
                                .findVigenteDetailsByIdCargaDocente(
                                                idCargaDocente);

                List<ActividadHorasResumenDTO> actividades = ResumenCargaAssembler
                                .buildActivityHours(
                                                ResumenCargaAssembler
                                                                .fromCargaList(
                                                                                detalles));

                ObservacionPtdEntity observacionEntity = observacionPtdRepository
                                .findByIdCargaDocenteAndPegeRole(
                                                idCargaDocente,
                                                ROL_COORDINADOR);

                String observacion = null;

                if (observacionEntity != null
                                && observacionEntity.getTexto() != null
                                && !observacionEntity
                                                .getTexto()
                                                .isBlank()) {

                        observacion = observacionEntity
                                        .getTexto()
                                        .strip();
                }

                log.info(
                                "getActivities ===> " +
                                                "Actividades efectivas consultadas. " +
                                                "idCargaDocente={}, totalTipos={}, tieneObservacion={}",
                                idCargaDocente,
                                actividades.size(),
                                observacion != null);

                return new PublicActivitiesDTO(
                                actividades,
                                observacion);
        }

        @Override
        @Transactional(readOnly = true)
        public PublicDigitalWorkPlanDTO getDigitalWorkPlan(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                log.debug(
                                "getDigitalWorkPlan ===> " +
                                                "Consultando Plan de Trabajo Digital público. " +
                                                "idCargaDocente={}, idPersonaGeneral={}",
                                idCargaDocente,
                                idPersonaGeneral);

                if (idCargaDocente == null) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                var contratacion = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .filter(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente))
                                .findFirst()
                                .orElseThrow(() -> new ApiException(
                                                HttpStatus.NOT_FOUND,
                                                "La contratación seleccionada no está disponible para el docente autenticado"));

                Long idPeriodoUniversitario = contratacion.getIdPeriodoUniversitario();

                if (idPeriodoUniversitario == null) {
                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "No se encontró el período universitario de la contratación");
                }

                List<ActividadPtdDTO> actividadesPtd = coordinacionService
                                .listPtdActivities(
                                                idCargaDocente);

                PtdInfoWithWeightsDTO informacionPtd = planTrabajoDigitalService
                                .getInfoPtdWithWeights(
                                                idPersonaGeneral,
                                                idCargaDocente,
                                                idPeriodoUniversitario);

                if (informacionPtd == null
                                || informacionPtd.actividades() == null
                                || informacionPtd.actividades().isEmpty()) {

                        log.info(
                                        "getDigitalWorkPlan ===> " +
                                                        "El docente no tiene PTD registrado. " +
                                                        "idCargaDocente={}, idPersonaGeneral={}",
                                        idCargaDocente,
                                        idPersonaGeneral);

                        return null;
                }

                List<PublicDigitalWorkPlanItemDTO> items = actividadesPtd
                                .stream()
                                .map(actividad -> {

                                        ActivityWeightsDTO ponderacion = informacionPtd
                                                        .actividades()
                                                        .stream()
                                                        .filter(item -> Objects.equals(
                                                                        item.idTipoActividad(),
                                                                        actividad.id()))
                                                        .findFirst()
                                                        .orElse(null);

                                        List<String> detalles = actividad.detalles() == null
                                                        ? List.of()
                                                        : actividad
                                                                        .detalles()
                                                                        .stream()
                                                                        .map(detalle -> detalle.descripcion())
                                                                        .filter(Objects::nonNull)
                                                                        .map(String::trim)
                                                                        .filter(value -> !value.isBlank())
                                                                        .toList();

                                        return new PublicDigitalWorkPlanItemDTO(
                                                        actividad.id(),
                                                        actividad.codigo(),
                                                        actividad.criterio(),
                                                        detalles,
                                                        actividad.totalHoras(),

                                                        ponderacion == null
                                                                        ? null
                                                                        : ponderacion.ponderado(),

                                                        ponderacion == null
                                                                        ? null
                                                                        : ponderacion.nivel());
                                })
                                .toList();

                long totalPonderacion = informacionPtd
                                .actividades()
                                .stream()
                                .map(ActivityWeightsDTO::ponderado)
                                .filter(Objects::nonNull)
                                .mapToLong(Long::longValue)
                                .sum();

                log.info(
                                "getDigitalWorkPlan ===> " +
                                                "PTD público consultado. " +
                                                "idCargaDocente={}, idPersonaGeneral={}, " +
                                                "totalItems={}, totalPonderacion={}",
                                idCargaDocente,
                                idPersonaGeneral,
                                items.size(),
                                totalPonderacion);

                return new PublicDigitalWorkPlanDTO(
                                items,
                                totalPonderacion);
        }

        @Override
        @Transactional
        public void decideDigitalWorkPlan(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        String nuevoEstado) {

                if (!"1".equals(nuevoEstado)
                                && !"2".equals(nuevoEstado)) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "El estado solicitado para el PTD no es válido");
                }

                if (idCargaDocente == null
                                || idCargaDocente <= 0) {

                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {

                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                var contratacion = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .filter(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente))
                                .findFirst()
                                .orElseThrow(() -> new ApiException(
                                                HttpStatus.NOT_FOUND,
                                                "La contratación seleccionada no está disponible para el docente autenticado"));

                Long idPeriodoUniversitario = contratacion.getIdPeriodoUniversitario();

                if (idPeriodoUniversitario == null) {

                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "No se encontró el período universitario de la contratación");
                }

                List<PlanTrabajoDigitalEntity> registros = planTrabajoDigitalRepository
                                .lockPtdForDecision(
                                                idPersonaGeneral,
                                                idPeriodoUniversitario);

                if (registros.isEmpty()) {

                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "El docente no tiene un Plan de Trabajo Digital registrado para este período");
                }

                Long idPtdReferencia = planTrabajoDigitalRepository
                                .findReferencePtdIdByHiringAndProfessor(
                                                idCargaDocente,
                                                idPersonaGeneral,
                                                idPeriodoUniversitario);

                boolean referenciaValida = idPtdReferencia != null
                                && registros.stream()
                                                .anyMatch(ptd -> Objects.equals(
                                                                ptd.getId(),
                                                                idPtdReferencia));

                if (!referenciaValida) {

                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "No fue posible identificar el PTD correspondiente a la contratación");
                }

                boolean todosEstipulados = registros.stream()
                                .allMatch(ptd -> "0".equals(
                                                ptd.getEstado()));

                if (!todosEstipulados) {

                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "El Plan de Trabajo Digital ya tiene una decisión registrada o presenta estados inconsistentes");
                }

                List<PtdiDocumentoCategoriaDTO> tiposObligatorios =
                        ptdiDocumentoService
                                .getAvailableCategories()
                                .stream()
                                .filter(tipo ->
                                        Boolean.TRUE.equals(
                                                tipo.obligatorio()
                                        )
                                )
                                .toList();

                if (!tiposObligatorios.isEmpty()) {

                List<String> faltantesPorContratacion =
                        new ArrayList<>();

                Set<Long> contratacionesRevisadas =
                        new HashSet<>();

                var contrataciones =
                        cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                        idPersonaGeneral
                                );

                for (var registroContratacion : contrataciones) {

                        if (!Objects.equals(
                                registroContratacion.getIdPeriodoUniversitario(),
                                idPeriodoUniversitario
                        )) {
                        continue;
                        }

                        Long idContratacion =
                                registroContratacion.getIdCargaDocente();

                        if (
                                idContratacion == null ||
                                !contratacionesRevisadas.add(idContratacion)
                        ) {
                        continue;
                        }

                        boolean esDocentePlanta =
                                "1".equals(
                                        registroContratacion.getEsDocentePlanta()
                                );

                        boolean onceMesesSegundoSemestre =
                                OnceMesesReporteFilter
                                        .excludeInheritedInSecondPeriod(
                                                registroContratacion.getOnceMeses(),
                                                registroContratacion.getPeriodoUniversidad()
                                        );

                        boolean exentoDocumentos =
                                esDocentePlanta
                                || onceMesesSegundoSemestre;

                        if (exentoDocumentos) {

                        log.debug(
                                "Contratación {} exenta de documentos. "
                                        + "Docente de planta: {}. "
                                        + "Once meses segundo semestre: {}",
                                idContratacion,
                                esDocentePlanta,
                                onceMesesSegundoSemestre
                        );

                        continue;
                        }

                        Long idPtdContratacion =
                                Objects.equals(
                                        idContratacion,
                                        idCargaDocente
                                )
                                        ? idPtdReferencia
                                        : planTrabajoDigitalRepository
                                                .findReferencePtdIdByHiringAndProfessor(
                                                        idContratacion,
                                                        idPersonaGeneral,
                                                        idPeriodoUniversitario
                                                );

                        if (idPtdContratacion == null) {
                        continue;
                        }

                        boolean ptdIncluido =
                                registros.stream()
                                        .anyMatch(ptd ->
                                                Objects.equals(
                                                        ptd.getId(),
                                                        idPtdContratacion
                                                )
                                        );

                        if (!ptdIncluido) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "Se encontró una inconsistencia "
                                        + "entre la contratación y "
                                        + "el Plan de Trabajo Digital"
                        );
                        }

                        PtdiDocumentoContenidoDTO contenido =
                                ptdiDocumentoService.getContent(
                                        idPtdContratacion
                                );

                        List<PtdiDocumentoCategoriaDTO> categoriasRegistradas =
                                contenido.categorias() == null
                                        ? List.of()
                                        : contenido.categorias();

                        List<String> documentosFaltantes =
                                tiposObligatorios
                                        .stream()
                                        .filter(tipoObligatorio ->

                                                categoriasRegistradas
                                                        .stream()
                                                        .noneMatch(categoria ->

                                                                tipoObligatorio.idTipoDocumento() != null

                                                                && Objects.equals(
                                                                        tipoObligatorio.idTipoDocumento(),
                                                                        categoria.idTipoDocumento()
                                                                )

                                                                && categoria.archivos() != null

                                                                && !categoria.archivos().isEmpty()
                                                        )
                                        )
                                        .map(PtdiDocumentoCategoriaDTO::nombre)
                                        .toList();

                        if (!documentosFaltantes.isEmpty()) {

                        faltantesPorContratacion.add(
                                "Contratación "
                                        + idContratacion
                                        + ": "
                                        + String.join(
                                                ", ",
                                                documentosFaltantes
                                        )
                        );
                        }
                }

                if (!faltantesPorContratacion.isEmpty()) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "No es posible aprobar ni rechazar "
                                        + "el Plan de Trabajo Digital. "
                                        + "Debe adjuntar al menos un archivo "
                                        + "en cada tipo documental obligatorio. "
                                        + "Documentos pendientes: "
                                        + String.join(
                                                " | ",
                                                faltantesPorContratacion
                                        )
                        );
                }
                }            

                Date fechaActual = new Date();

                String registradoPor = "idPersona:"
                                + idPersonaGeneral
                                + "-usuario:docente-accion:U";

                for (PlanTrabajoDigitalEntity ptd : registros) {

                        ptd.setEstado(
                                        nuevoEstado);

                        ptd.setFechaCambio(
                                        fechaActual);

                        ptd.setRegistradoPor(
                                        registradoPor);
                }

                planTrabajoDigitalRepository
                                .saveAllAndFlush(
                                                registros);

                log.info(
                                "decideDigitalWorkPlan ===> "
                                                + "Decisión registrada. "
                                                + "idCargaDocente={}, "
                                                + "idPersonaGeneral={}, "
                                                + "idPeriodoUniversitario={}, "
                                                + "nuevoEstado={}, "
                                                + "registrosActualizados={}",
                                idCargaDocente,
                                idPersonaGeneral,
                                idPeriodoUniversitario,
                                nuevoEstado,
                                registros.size());
        }

        @Override
        @Transactional(readOnly = true)
        public List<PtdiDocumentoCategoriaDTO> getSupportDocumentCategories() {

                return ptdiDocumentoService
                                .getAvailableCategories();
        }

        @Override
        @Transactional(readOnly = true)
        public List<TipoDocumentoContratacionDTO> getSupportDocumentTypes() {

        return ptdiDocumentoService
                .getDocumentTypes();
        }

        @Override
        @Transactional(readOnly = true)
        public PtdiDocumentoContenidoDTO getSupportDocuments(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                Long idPlanTrabajoDigital = resolveDocumentPtdId(
                                idCargaDocente,
                                idPersonaGeneral);

                return ptdiDocumentoService
                                .getContent(
                                                idPlanTrabajoDigital);
        }

        @Override
        @Transactional(readOnly = true)
        public PtdiDocumentoArchivoDescargaDTO getSupportDocumentFile(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        String idArchivo) {

                Long idPlanTrabajoDigital = resolveDocumentPtdId(
                                idCargaDocente,
                                idPersonaGeneral);

                return ptdiDocumentoService
                                .getFile(
                                                idCargaDocente,
                                                idPlanTrabajoDigital,
                                                idArchivo);
        }

        @Override
        @Transactional
        public PtdiDocumentoContenidoDTO deleteSupportDocumentFile(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        String idArchivo) {

                requireEditableDigitalWorkPlan(
                        idCargaDocente,
                        idPersonaGeneral
                );                

                Long idPlanTrabajoDigital = resolveDocumentPtdId(
                                idCargaDocente,
                                idPersonaGeneral);

                return ptdiDocumentoService
                                .deleteFile(
                                                idCargaDocente,
                                                idPlanTrabajoDigital,
                                                idArchivo);
        }

        @Override
        @Transactional
        public PtdiDocumentoContenidoDTO addSupportDocuments(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        String codigoCategoria,
                        String nombreCategoria,
                        List<MultipartFile> archivos) {

                requireEditableDigitalWorkPlan(
                        idCargaDocente,
                        idPersonaGeneral
                );                

                Long idPlanTrabajoDigital = resolveDocumentPtdId(
                                idCargaDocente,
                                idPersonaGeneral);

                return ptdiDocumentoService
                                .addFiles(
                                                idCargaDocente,
                                                idPlanTrabajoDigital,
                                                codigoCategoria,
                                                nombreCategoria,
                                                archivos);
        }

        @Override
        @Transactional
        public PtdiDocumentoContenidoDTO addSupportDocumentsByType(
                        Long idCargaDocente,
                        Long idPersonaGeneral,
                        Long idTipoDocumento,
                        List<MultipartFile> archivos) {

                requireEditableDigitalWorkPlan(
                        idCargaDocente,
                        idPersonaGeneral
                );

                Long idPlanTrabajoDigital = resolveDocumentPtdId(
                        idCargaDocente,
                        idPersonaGeneral
                );

                return ptdiDocumentoService.addFilesByType(
                        idCargaDocente,
                        idPlanTrabajoDigital,
                        idTipoDocumento,
                        archivos
                );
        }

        @Override
        @Transactional(readOnly = true)
        public PtdObservationDTO getObservationByTh(Long idCargaDocente) {
                log.info("getObservationByTh ===> Obteniendo observación de rechazo del PTD. idCargaDocente={}", idCargaDocente);

                ObservacionPtdEntity observacionEntity = observacionPtdRepository.findByIdCargaDocenteAndPegeRole(idCargaDocente, ROL_TALENTO);
                if (observacionEntity == null) {
                        return null;
                }

                log.info("getObservationByTh ===> Observación del PTD obtenida. idCargaDocente={}", idCargaDocente);
                return new PtdObservationDTO(observacionEntity.getTexto(), observacionEntity.getIdCargaDocente());
        }

        private LocalDate toLocalDate(Date value) {

                if (value == null) {
                        return null;
                }

                if (value instanceof java.sql.Date sqlDate) {
                        return sqlDate.toLocalDate();
                }

                return value
                                .toInstant()
                                .atZone(ZONA)
                                .toLocalDate();
        }

        private String buildFullName(
                        PersonaNaturalGeneralEntity natural) {

                return String.join(
                                " ",
                                valueOrEmpty(natural.getPrimerNombre()),
                                valueOrEmpty(natural.getSegundoNombre()),
                                valueOrEmpty(natural.getPrimerApellido()),
                                valueOrEmpty(natural.getSegundoApellido()))
                                .replaceAll("\\s+", " ")
                                .trim();
        }

        private String valueOrEmpty(String value) {
                return value == null
                                ? ""
                                : value.trim();
        }

        private PublicAcademicTrainingDTO findLatestAcademicTraining(
                        Long idPersonaGeneral) {

                return tituloPersonaNaturalGeneralRepository
                                .findLatestAcademicTraining(
                                                idPersonaGeneral)
                                .map(item -> new PublicAcademicTrainingDTO(
                                                item.getId(),
                                                item.getIdNivelAcademico(),
                                                item.getNivelAcademico(),
                                                item.getIdTitulo(),
                                                item.getTitulo(),
                                                item.getInstitucion(),
                                                toLocalDate(
                                                                item.getFechaGrado()),
                                                item.getIdPais(),
                                                item.getPais()))
                                .orElse(null);
        }


        private void requireEditableDigitalWorkPlan(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {


                if (idCargaDocente == null
                        || idCargaDocente <= 0) {

                        throw new ApiException(
                                HttpStatus.BAD_REQUEST,
                                "La contratación seleccionada es obligatoria"
                        );
                }

                if (idPersonaGeneral == null) {

                        throw new ApiException(
                                HttpStatus.UNAUTHORIZED,
                                "No autenticado"
                        );
                }

                var contratacion =
                        cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                        idPersonaGeneral
                                )
                                .stream()
                                .filter(record ->
                                        Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente
                                        )
                                )
                                .findFirst()
                                .orElseThrow(() ->
                                        new ApiException(
                                                HttpStatus.NOT_FOUND,
                                                "La contratación seleccionada no está disponible para el docente autenticado"
                                        )
                                );

                Long idPeriodoUniversitario =
                        contratacion.getIdPeriodoUniversitario();

                if (idPeriodoUniversitario == null) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "No se encontró el período universitario de la contratación"
                        );
                }

                List<PlanTrabajoDigitalEntity> registros =
                        planTrabajoDigitalRepository
                                .lockPtdForDecision(
                                        idPersonaGeneral,
                                        idPeriodoUniversitario
                                );

                if (registros.isEmpty()) {
                        return;
                }

                boolean editable =
                        registros.stream()
                                .allMatch(ptd ->
                                        "0".equals(
                                                ptd.getEstado()
                                        )
                                );

                if (!editable) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "El Plan de Trabajo Digital ya fue aceptado o rechazado y no permite modificaciones"
                        );
                }
        }

        private Long resolveDocumentPtdId(
                        Long idCargaDocente,
                        Long idPersonaGeneral) {

                if (idCargaDocente == null
                                || idCargaDocente <= 0) {
                        throw new ApiException(
                                        HttpStatus.BAD_REQUEST,
                                        "La contratación seleccionada es obligatoria");
                }

                if (idPersonaGeneral == null) {
                        throw new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado");
                }

                boolean contratacionPermitida = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .anyMatch(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente));

                var contratacion = cargaDocenteRepository
                                .findPublicHiringRecordsByProfessor(
                                                idPersonaGeneral)
                                .stream()
                                .filter(record -> Objects.equals(
                                                record.getIdCargaDocente(),
                                                idCargaDocente))
                                .findFirst()
                                .orElseThrow(() -> new ApiException(
                                                HttpStatus.NOT_FOUND,
                                                "La contratación seleccionada no está disponible para el docente autenticado"));

                Long idPeriodoUniversitario = contratacion.getIdPeriodoUniversitario();

                if (idPeriodoUniversitario == null) {

                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "No se encontró el período universitario de la contratación");
                }

                Long idPlanTrabajoDigital = planTrabajoDigitalRepository
                                .findReferencePtdIdByHiringAndProfessor(
                                                idCargaDocente,
                                                idPersonaGeneral,
                                                idPeriodoUniversitario);

                if (idPlanTrabajoDigital == null) {
                        throw new ApiException(
                                        HttpStatus.CONFLICT,
                                        "La contratación todavía no tiene un Plan de Trabajo Digital disponible para asociar documentos");
                }

                return idPlanTrabajoDigital;
        }

}