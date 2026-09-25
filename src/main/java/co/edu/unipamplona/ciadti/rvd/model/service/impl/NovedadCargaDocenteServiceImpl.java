/**
 * Aplicación: rvd
 * Archivo: NovedadCargaDocenteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/09/2026
 * Modificaciones:
 * 17/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - saveContractModalityProfessor inserta
 * fotografía nueva (mismo patrón que assign-name-nn)
 * 18/09/2026 - Sebastian Jaimes - montos de la fotografía con fórmula
 * de contratación, no con el tope de presupuesto
 * 18/09/2026 - Sebastian Jaimes - no actualiza CARG_VALOR al crear;
 * sí al aprobar
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;

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
import co.edu.unipamplona.ciadti.rvd.model.dto.ActualizarValorContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GuardarNovedadesDetallesProyectosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
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
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionCargaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasProgramaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaBudgetService;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadCargaDocenteService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadCargaDocenteServiceImpl implements NovedadCargaDocenteService {

    private static final String COMPONENT_UPDATE_CONTRACT_VALUE = "update-contract-value";
    
    private static final String COMPONENT_ASSIGN_NAME_NN = "asign-name-nn";

    private static final String COMPONENT_CONTRACT_MODALITY = "change-contract-modality";

    private static final String COMPONENT_DELETE_PROFESSOR = "delete-professor";

    private static final String COMPONENT_ADD_NOVELTY_PROFESSOR = "add-professor";

    private static final String COMPONENT_CHANGE_PROJECT_ACTIVITIES = "change-project-activities";

    private static final String PREASIGNACION_SOLO_LECTURA = "La convocatoria tiene restricción activa y esta coordinación no está habilitada para edición en las fechas permitidas.";

    private static final Set<String> CODIGOS_CENTRO_COSTO_ESPECIAL = Set.of("CTEI", "ISU");

    private final CargaDocenteRepository cargaDocenteRepository;

    private final CargaRepository cargaRepository;

    private final ConvocatoriaRepository convocatoriaRepository;

    private final RestriccionPorCoordinacionRepository restriccionPorCoordinacionRepository;

    private final RestriccionCargaRepository restriccionCargaRepository;

    private final AsignarCentroCostoRepository asignarCentroCostoRepository;

    private final AsociacionCoordinacionRepository asociacionCoordinacionRepository;

    private final PersonaProyectoRepository personaProyectoRepository;

    private final RelacionCargaProyectoRepository relacionCargaProyectoRepository;

    private final NovedadCargaDocenteRepository novedadCargaDocenteRepository;

    private final DetalleNovedadCargaDocenteRepository detalleNovedadCargaDocenteRepository;

    private final NovedadRepository novedadRepository;

    private final PersonaGeneralRepository personaGeneralRepository;

    private final EscalafonRepository escalafonRepository;


    private final CargaBudgetService cargaBudgetService;

    private final ObjectMapper objectMapper;

    private final DetalleNovedadCargaDocenteMapper detalleNovedadCargaDocenteMapper;

    private final DetalleCargaDocenteMapper detalleCargaDocenteMapper;

    private final RelacionCargaProyectoMapper relacionCargaProyectoMapper;


    private final CoordinacionServiceImpl coordinacionServiceImpl;

    @Override
    @Transactional
    public void updateContractValue(ActualizarValorContratoDTO dto) {
        log.info("updateContractValue ===> Actualizando novedad carga docente. idCargaDocente={}", dto.idCargaDocente());

        NovedadEntity novedad = novedadRepository.findById(dto.idNovedad())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad seleccionada"));
        validateUpdateContractValueType(novedad);

        if (novedadCargaDocenteRepository.countNoveltyInReview(dto.idCargaDocente()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT,"El docente tiene una novedad en revisión");
        }

        Optional<NovedadCargaDocenteEntity> previous = novedadCargaDocenteRepository.findProfessorRecordToDuplicateNovelty(dto.idCargaDocente());
        CargaDocenteEntity cargaOriginal = cargaDocenteRepository.findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente seleccionada"));

        CargaBudgetOverlay overlay;
        ValorContratacionDTO valores;
        if (previous.isPresent()) {
            NovedadCargaDocenteEntity previousEntity = previous.get();
            FechasConvocatoriaEntity fechas = previousEntity.getFechaConvocatoria();
            EscalafonEntity escalafon = escalafonRepository.findByIdCategoriaCatedratico(previousEntity.getIdCategoriaCatedratico(), previousEntity.getIdPersonaGeneral());
            if (escalafon == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "No existe escalafon para la persona.");
            }
            if (Objects.equals(escalafon.getPuntos(), previousEntity.getPuntos())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La cantidad de puntos para este docente no ha sido cambiada en el escalafón.");
            }

            overlay = new CargaBudgetOverlay(
                previousEntity.getIdCargaDocente(),
                previousEntity.getIdModalidadContratacion(),
                fechas != null ? fechas.getFechaInicio() : null,
                fechas != null ? fechas.getFechaFin() : null,
                null,       // Su salario se va a calcular
                previousEntity.getValorHora(),
                parseDecimal(previousEntity.getSemanas()),
                parseDecimal(previousEntity.getHoras()),
                escalafon.getPuntos(),
                previousEntity.getValorPunto()
            );

            valores = cargaBudgetService.computeInclusive(overlay);
            cargaBudgetService.assertNotExceedsAuthorized(
                previousEntity.getIdCarga(),
                overlay);
        } else {
            FechasConvocatoriaEntity fechas = cargaOriginal.getFechaConvocatoria();
            EscalafonEntity escalafon = escalafonRepository.findByIdCategoriaCatedratico(cargaOriginal.getIdCategoriaCatedratico(), cargaOriginal.getIdPersonaGeneral());
            if (escalafon == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "No existe escalafon para la persona.");
            }
            if (Objects.equals(escalafon.getPuntos(), cargaOriginal.getPuntos())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La cantidad de puntos para este docente no ha sido cambiada en el escalafón.");
            }

            overlay = new CargaBudgetOverlay(
                cargaOriginal.getId(),
                cargaOriginal.getIdModalidadContratacion(),
                fechas != null ? fechas.getFechaInicio() : null,
                fechas != null ? fechas.getFechaFin() : null,
                null,       // Su salario se va a calcular
                cargaOriginal.getValorHora(),
                parseDecimal(cargaOriginal.getSemanas()),
                parseDecimal(cargaOriginal.getHoras()),
                escalafon.getPuntos(),
                cargaOriginal.getValorPunto()
            );

            valores = cargaBudgetService.computeInclusive(overlay);
            cargaBudgetService.assertNotExceedsAuthorized(
                cargaOriginal.getIdCarga(),
                overlay);
        }


        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        int inserted;

        if (previous.isPresent()) {
            inserted = novedadCargaDocenteRepository.insertUpdateContractValueFromNovelty(
                dto.idCargaDocente(),
                dto.idNovedad(),
                valores.valorContrato(),
                valores.totalPrestaciones(),
                valores.salario(),
                overlay.puntos(),
                valores.totalContrato(),
                registradoPor);
        } else {
            inserted = novedadCargaDocenteRepository.insertUpdateContractValueFromCargaDocente(
                dto.idCargaDocente(),
                dto.idNovedad(),
                valores.valorContrato(),
                valores.totalPrestaciones(),
                valores.salario(),
                overlay.puntos(),
                valores.totalContrato(),
                registradoPor);
        }
        if (inserted != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No fue posible registrar la novedad");
        }

        log.info("updateContractValue ===> Novedad carga docente actualizada. idCargaDocente={}", dto.idCargaDocente());
    }
    
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

        if (novedadCargaDocenteRepository
                .countNoveltyInReview(dto.idCargaDocente()) > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente tiene una novedad en revisión");
        }

        Optional<NovedadCargaDocenteEntity> previous =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                dto.idCargaDocente());

        CargaBudgetOverlay overlay = overlayFrom(dto);
        ValorContratacionDTO valor = cargaBudgetService.computeInclusive(overlay);
        cargaBudgetService.assertNotExceedsAuthorized(
                dto.idCarga(),
                overlay);

        int inserted = insertContractModalityPhotograph(
                dto,
                valor,
                previous.isPresent());
        if (inserted != 1) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible registrar la novedad");
        }
        insertDetails(dto.idCargaDocente(), dto.detalles());

        log.info(
                "saveContractModalityProfessor ===> Novedad insertada. fuente={}, idCargaDocente={}",
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

    @Override
    @Transactional
    public void addNoveltyProfessor(CargaDocenteFormularioDTO dto, Long idNovedad) {
        log.info("addNoveltyProfessor ===> Agregando docente mediante novedad. idCarga={}, idNovedad={}", dto.idCarga(), idNovedad);
        
        Long idNewCargaDocente = coordinacionServiceImpl.auxAddProfessorForNovelty(dto);

        NovedadEntity novedad = novedadRepository.findById(idNovedad)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad seleccionada"));
        validateAddNoveltyProfessorType(novedad);

        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        int inserted = novedadCargaDocenteRepository.insertAddNoveltyProfessor(
            idNewCargaDocente,
            idNovedad,
            registradoPor
        );

        if (inserted != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No fue posible registrar la novedad");
        }

        log.info("addNoveltyProfessor ===> Docente agregado mediante novedad. idCargaDocente={}, idNovedad={}", idNewCargaDocente, idNovedad);
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
        CargaDocenteEntity cargaOriginal = cargaDocenteRepository.findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente seleccionada"));

        CargaBudgetOverlay overlay;
        ValorContratacionDTO valores;
        if (previous.isPresent()) {
            NovedadCargaDocenteEntity previousEntity = previous.get();
            FechasConvocatoriaEntity fechas = previousEntity.getFechaConvocatoria();
            overlay = new CargaBudgetOverlay(
                previousEntity.getIdCargaDocente(),
                previousEntity.getIdModalidadContratacion(),
                fechas != null ? fechas.getFechaInicio() : null,
                fechas != null ? fechas.getFechaFin() : null,
                previousEntity.getSalario(),
                previousEntity.getValorHora(),
                parseDecimal(previousEntity.getSemanas()),
                resolveDetallesHorasFromNoveltyActivityChanges(dto, previousEntity.getHoras()),    // Envia las horas de la copia para determinar las totales despues del cambio
                previousEntity.getPuntos(),
                previousEntity.getValorPunto()
            );

            valores = cargaBudgetService.computeInclusive(overlay);
            cargaBudgetService.assertNotExceedsAuthorized(
                previousEntity.getIdCarga(),
                overlay);
        } else {
            FechasConvocatoriaEntity fechas = cargaOriginal.getFechaConvocatoria();
            overlay = new CargaBudgetOverlay(
                cargaOriginal.getId(),
                cargaOriginal.getIdModalidadContratacion(),
                fechas != null ? fechas.getFechaInicio() : null,
                fechas != null ? fechas.getFechaFin() : null,
                cargaOriginal.getSalario(),
                cargaOriginal.getValorHora(),
                parseDecimal(cargaOriginal.getSemanas()),
                resolveDetallesHorasFromNoveltyActivityChanges(dto, null),         // Calcula las horas del dto porque inicialmente se pasan todas las actividades originales
                cargaOriginal.getPuntos(),
                cargaOriginal.getValorPunto()
            );

            valores = cargaBudgetService.computeInclusive(overlay);
            cargaBudgetService.assertNotExceedsAuthorized(
                cargaOriginal.getIdCarga(),
                overlay);
        }


        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        int inserted;

        if (previous.isPresent()) {
            inserted = novedadCargaDocenteRepository.insertChangeProjectActivitiesFromNovelty(
                dto.idCargaDocente(),
                dto.idNovedad(),
                valores.valorContrato(),
                valores.totalPrestaciones(),
                valores.salario(),
                overlay.horasActividades().toString(),
                previous.get().getHorasDeExcepcion(),
                overlay.valorHora(),
                overlay.puntos(),
                overlay.valorPunto(),
                valores.totalContrato(),
                overlay.semanas().toString(),
                previous.get().getOnceMeses(),
                registradoPor);
        } else {
            inserted = novedadCargaDocenteRepository.insertChangeProjectActivitiesFromCargaDocente(
                dto.idCargaDocente(),
                dto.idNovedad(),
                valores.valorContrato(),
                valores.totalPrestaciones(),
                valores.salario(),
                overlay.horasActividades().toString(),
                cargaOriginal.getHorasDeExcepcion(),
                overlay.valorHora(),
                overlay.puntos(),
                overlay.valorPunto(),
                valores.totalContrato(),
                overlay.semanas().toString(),
                cargaOriginal.getOnceMeses(),
                registradoPor);
        }
        if (inserted != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No fue posible registrar la novedad");
        }


        // GUARDAR DETALLES
        Long idCoordinacion = resolveIdCoordinacionByNovedadCargaDocente(dto.idCargaDocente());
        validatePreassignmentWriteAllowedByNovedadCargaDocente(dto.idCargaDocente());

        if (dto.detallesNuevos() != null && !dto.detallesNuevos().isEmpty()) {
            agregarDetalleNovedad(dto.idCargaDocente(), dto.detallesNuevos(), idCoordinacion);
        }
        if (dto.detallesActualizados() != null && !dto.detallesActualizados().isEmpty()) {
            for (DetalleCargaDocenteDTO detalle : dto.detallesActualizados()) {
                actualizarDetalleNovedad(detalle, idCoordinacion);
            }
        }
        if (dto.detallesEliminados() != null && !dto.detallesEliminados().isEmpty()) {
            for (Long idDetalleNovedad : dto.detallesEliminados()) {
                deleteDetalleNovedad(idDetalleNovedad);
            }
        }

        log.info("saveNoveltyProjectActivities ===> Novedad detalle precarga docente guardado. idCargaDocente={}", dto.idCargaDocente());
    }


    @Override
    @Transactional
    public void approveProfessorNovelty(Long idCargaDocente) {
        CargaDocenteEntity cargaDocente =
                cargaDocenteRepository
                        .findById(idCargaDocente)
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe la carga docente con id "
                                        + idCargaDocente));
        if (novedadCargaDocenteRepository
                .countNoveltyInReview(idCargaDocente) <= 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente no tiene una novedad en revisión");
        }
        int updated = novedadCargaDocenteRepository
                .updateEstadoNovedadInReview(
                        idCargaDocente,
                        RegistradoPorUtils.value(Accion.UPDATE));
        if (updated < 1) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible aprobar la novedad");
        }
        cargaBudgetService.refreshCargValor(cargaDocente.getIdCarga());
        log.info(
                "approveProfessorNovelty ===> Novedad aprobada. idCargaDocente={}, idCarga={}",
                idCargaDocente,
                cargaDocente.getIdCarga());
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

    private void validateChangeProjectActivitiesType(
            NovedadEntity novedad
    ) {

        String component =novedad.getComponente();

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

        Long tipoActividad = detalle.idTipoActividadHija() != null ? detalle.idTipoActividadHija() : detalle.idTipoActividad();

        if (tipoActividad == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de actividad del detalle es obligatorio"
            );
        }
    }

    private int insertContractModalityPhotograph(
            CambioModalidadHoraCatedraticoDTO dto,
            ValorContratacionDTO valor,
            boolean fromNovelty
    ) {
        FechasConvocatoriaFormularioDTO fechas = dto.fechasConvocatoria();
        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        if (fromNovelty) {
            return novedadCargaDocenteRepository
                    .insertContractModalityFromNovelty(
                            dto.idCargaDocente(),
                            dto.idPersonaGeneral(),
                            dto.idModalidadContratacion(),
                            dto.idCategoriaCatedratico(),
                            fechas.id(),
                            dto.idNovedad(),
                            fechas.fechaInicio(),
                            fechas.fechaFin(),
                            valor.valorContrato(),
                            valor.totalPrestaciones(),
                            valor.salario(),
                            valor.totalContrato(),
                            dto.valorHora(),
                            trimToNull(dto.puntos()),
                            dto.valorPunto(),
                            trimToNull(dto.semanas()),
                            resolveHorasString(dto),
                            trimToNull(dto.horasDeExcepcion()),
                            resolveOnceMeses(dto),
                            registradoPor);
        }
        return novedadCargaDocenteRepository
                .insertContractModalityFromCargaDocente(
                        dto.idCargaDocente(),
                        dto.idPersonaGeneral(),
                        dto.idModalidadContratacion(),
                        dto.idCategoriaCatedratico(),
                        fechas.id(),
                        dto.idNovedad(),
                        fechas.fechaInicio(),
                        fechas.fechaFin(),
                        valor.valorContrato(),
                        valor.totalPrestaciones(),
                        valor.salario(),
                        valor.totalContrato(),
                        dto.valorHora(),
                        trimToNull(dto.puntos()),
                        dto.valorPunto(),
                        trimToNull(dto.semanas()),
                        resolveHorasString(dto),
                        trimToNull(dto.horasDeExcepcion()),
                        resolveOnceMeses(dto),
                        registradoPor);
    }

    private CargaBudgetOverlay overlayFrom(
            CambioModalidadHoraCatedraticoDTO dto) {
        FechasConvocatoriaFormularioDTO fechas = dto.fechasConvocatoria();
        return new CargaBudgetOverlay(
                dto.idCargaDocente(),
                dto.idModalidadContratacion(),
                fechas != null ? fechas.fechaInicio() : null,
                fechas != null ? fechas.fechaFin() : null,
                dto.asignacionSalarial(),
                dto.valorHora(),
                parseDecimal(dto.semanas()),
                resolveHorasCatedra(dto),
                dto.puntos(),
                dto.valorPunto());
    }

    private String resolveHorasString(CambioModalidadHoraCatedraticoDTO dto) {
        BigDecimal horas = resolveHorasCatedra(dto);
        if (horas == null) {
            return trimToNull(dto.horas());
        }
        return horas.stripTrailingZeros().toPlainString();
    }

    private BigDecimal resolveHorasCatedra(CambioModalidadHoraCatedraticoDTO dto) {
        BigDecimal fromDto = parseDecimal(dto.horas());
        if (fromDto != null && fromDto.compareTo(BigDecimal.ZERO) > 0) {
            return fromDto;
        }
        if (dto.detalles() == null) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            if (detalle != null && detalle.horas() != null) {
                total = total.add(BigDecimal.valueOf(detalle.horas()));
            }
        }
        return total.compareTo(BigDecimal.ZERO) > 0 ? total : null;
    }

    private BigDecimal resolveDetallesHorasFromNoveltyActivityChanges(GuardarNovedadesDetallesProyectosDTO dto, String horasOriginales) {
        BigDecimal total = BigDecimal.ZERO;
        if (horasOriginales != null) {
            total = parseDecimal(horasOriginales);
        }

        for (DetalleCargaDocenteItemDTO detalle : dto.detallesNuevos()) {
            if (detalle != null && detalle.horas() != null) {
                total = total.add(BigDecimal.valueOf(detalle.horas()));
            }
        }
        // Se entra cuando ya hay detalles en novedades
        for (DetalleCargaDocenteDTO detalle : dto.detallesActualizados()) {
            DetalleCargaDocenteActividadDTO actividad = detalle.detalles().get(0);
            if (actividad != null && actividad.horas() != null) {

                String horasPersistidasRaw = detalleNovedadCargaDocenteRepository.findById(detalle.idDetalleCargaDocente()).orElseThrow().getHoras();
                BigDecimal horasPersistidas = parseDecimal(horasPersistidasRaw);
                BigDecimal horasNuevas = parseDecimal(actividad.horas());

                if (horasPersistidas != null) {
                    total = total.subtract(horasPersistidas);
                }
                if (horasNuevas != null) {
                    total = total.add(horasNuevas);
                }
            }
        }
        // Se entra cuando ya hay detalles en novedades
        for (Long idDetalle : dto.detallesEliminados()) {
            String horasPersistidasRaw = detalleNovedadCargaDocenteRepository.findById(idDetalle).orElseThrow().getHoras();
            BigDecimal horasPersistidas = parseDecimal(horasPersistidasRaw);

            if (horasPersistidas != null) {
                total = total.subtract(horasPersistidas);
            }
        }

        return total.compareTo(BigDecimal.ZERO) > 0 ? total : null;
    }

    private BigDecimal parseDecimal(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
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

    private void insertDetails(
            Long idCargaDocente,
            List<DetalleCargaDocenteItemDTO> detalles
    ) {

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

    private void deleteDetalleNovedad(Long idDetalleNovedadCargaDocente) {
        validatePreassignmentWriteAllowedByNovedadDetalle(idDetalleNovedadCargaDocente);

        detalleNovedadCargaDocenteRepository.deleteByProcedure(
                idDetalleNovedadCargaDocente,
                RegistradoPorUtils.value(Accion.DELETE));
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

    private void validateUpdateContractValueType(
            NovedadEntity novedad
    ) {

        String component =novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_UPDATE_CONTRACT_VALUE
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Actualizar valor de contrato"
            );
        }
    }

    private void validateAddNoveltyProfessorType(
            NovedadEntity novedad
    ) {

        String component =novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_ADD_NOVELTY_PROFESSOR
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Agregar docente"
            );
        }
    }
}
