/**
 * Aplicación: rvd
 * Archivo: NovedadCargaDocenteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/09/2026
 * Modificaciones:
 * 17/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Andrés Hernández - Controlar los detalles de novedades
 * 18/09/2026 - Sebastian Jaimes - saveContractModalityProfessor inserta
 * fotografía nueva (mismo patrón que assign-name-nn)
 * 18/09/2026 - Sebastian Jaimes - montos de la fotografía con fórmula
 * de contratación, no con el tope de presupuesto
 * 18/09/2026 - Sebastian Jaimes - no actualiza CARG_VALOR al crear;
 * sí al aprobar
 * 22/09/2026 - Reasignación de actividades: FAD, CTEI e ISU
 * 22/09/2026 - Andrés Hernández - Ajuste de valores monetarios al realizar novedades
 * 23/09/2026 - Andrés Hernández - Novedad actualizar valores de contrato
 * 25/09/2026 - Andrés Hernández - Novedad agregar docente
 * 28/09/2026 - La vigencia pasa a la novedad aprobada
 * 29/09/2026 - Resumen de carga con novedad vigente
 * 29/09/2026 - Resumen de horas y centros con ResumenCargaAssembler
 * 29/09/2026 - Historial de novedades y fuente no rechazada
 * 30/09/2026 - Andrés Hernández - Traer detalles de la carga docente para las novedades
 * 05/10/2026 - Andrés Hernández - Consulta que trae los detalles para mostrar a desarrollo académico
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RelacionCargaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActualizarValorContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GuardarNovedadesDetallesProyectosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HistorialNovedadResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionDecanoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionResumenNovedadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenNovedadCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HistorialGeneralNovedadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsignarCentroCostoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsociacionCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleNovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ObservacionesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionCargaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleNovedadResumenProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HistorialNovedadResumenProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ObservacionResumenProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HistorialGeneralNovedadProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaBudgetService;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadCargaDocenteService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import co.edu.unipamplona.ciadti.rvd.util.ResumenCargaAssembler;
import co.edu.unipamplona.ciadti.rvd.util.ResumenCargaAssembler.DetalleResumen;
import co.edu.unipamplona.ciadti.rvd.util.ValorContratacionCalculator;

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
    private static final String COMPONENT_CHANGE_DIRECT_ACTIVITIES = "change-direct-activities";

    private static final String PREASIGNACION_SOLO_LECTURA = "La convocatoria tiene restricción activa y esta coordinación no está habilitada para edición en las fechas permitidas.";
    private static final Set<String> CODIGOS_CENTRO_COSTO_ESPECIAL = Set.of("CTEI", "ISU");
    private static final int ESCALA_MONETARIA = 2;
    private static final BigDecimal PUNTOS_DEFAULT = new BigDecimal("100");
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
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final NovedadRepository novedadRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final EscalafonRepository escalafonRepository;
    private final CoordinacionService coordinacionService;
    private final CargaBudgetService cargaBudgetService;
    private final ObjectMapper objectMapper;
    private final DetalleNovedadCargaDocenteMapper detalleNovedadCargaDocenteMapper;
    private final DetalleCargaDocenteMapper detalleCargaDocenteMapper;
    private final RelacionCargaProyectoMapper relacionCargaProyectoMapper;
    private final ProyectoMapper proyectoMapper;
    private final ObservacionesRepository observacionesRepository;
    private final ModalidadContratacionRepository modalidadContratacionRepository;


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
        
        validateProfessorNotDeleted(previous);

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

        validateProfessorNotDeleted(previous);

        /*
         * Asignar nombre a NN solamente se puede ejecutar
         * cuando el estado efectivo todavía no tiene persona.
         */
        Long currentPersonId =previous.map(NovedadCargaDocenteEntity::getIdPersonaGeneral).orElse(cargaDocente.getIdPersonaGeneral());

        if (currentPersonId != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga seleccionada ya tiene un docente asignado");
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el docente seleccionado"
            );
        }

        Long idModalidadContratacion =
        previous
                .map(NovedadCargaDocenteEntity::getIdModalidadContratacion)
                .orElse(cargaDocente.getIdModalidadContratacion());

        long asignaciones =
                novedadCargaDocenteRepository
                        .countProfessorAssignedToAnotherLoad(
                                cargaDocente.getIdCarga(),
                                dto.idCargaDocente(),
                                idModalidadContratacion,
                                dto.idPersonaGeneral()
                        );

        if (asignaciones > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente seleccionado ya se encuentra asignado en esta carga y modalidad"
            );
        }     
        
        EscalafonEntity escalafon =
        escalafonRepository
                .findById(dto.idEscalafon())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el escalafón seleccionado"
                ));

        if (!Objects.equals(
                escalafon.getIdPersonaGeneral(),
                dto.idPersonaGeneral())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no corresponde al docente seleccionado"
            );
        }

        if (!Objects.equals(
                escalafon.getIdModalidadContratacion(),
                idModalidadContratacion)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no corresponde a la modalidad de contratación actual"
            );
        }

        if (escalafon.getIdCategoriaCatedratico() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no tiene una categoría asociada"
            );
        }

        Long anio = resolveCargaYear(cargaDocente);

        ValorPuntosPrecargaDTO valoresPuntos =
                coordinacionService.getValuePointsPreload(
                        anio,
                        escalafon.getIdCategoriaCatedratico(),
                        dto.idPersonaGeneral(),
                        idModalidadContratacion
                );

        CargaBudgetOverlay overlay =
        buildChangeProfessorOverlay(
                cargaDocente,
                previous,
                valoresPuntos
        );     
        
        ValorContratacionDTO valoresContrato =
        cargaBudgetService.computeInclusive(
                overlay
        );

        cargaBudgetService.assertNotExceedsAuthorized(
                cargaDocente.getIdCarga(),
                overlay,
                "No se puede realizar el cambio de docente porque el valor proyectado supera el valor autorizado de la carga"
        );

        String registradoPor =RegistradoPorUtils.value(Accion.INSERT);

        int inserted;

        if (previous.isPresent()) {

            inserted =
                    novedadCargaDocenteRepository
                            .insertAssignNameNnFromNovelty(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    escalafon.getIdCategoriaCatedratico(),
                                    dto.idNovedad(),
                                    valoresContrato.valorContrato(),
                                    valoresContrato.totalPrestaciones(),
                                    valoresContrato.salario(),
                                    overlay.valorHora(),
                                    overlay.puntos(),
                                    overlay.valorPunto(),
                                    valoresContrato.totalContrato(),
                                    registradoPor
                            );

        } else {

            inserted =
                    novedadCargaDocenteRepository
                            .insertAssignNameNnFromCargaDocente(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    escalafon.getIdCategoriaCatedratico(),
                                    dto.idNovedad(),
                                    valoresContrato.valorContrato(),
                                    valoresContrato.totalPrestaciones(),
                                    valoresContrato.salario(),
                                    overlay.valorHora(),
                                    overlay.puntos(),
                                    overlay.valorPunto(),
                                    valoresContrato.totalContrato(),
                                    registradoPor
                            );
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

        validateProfessorNotDeleted(previous);     

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
        // Fase 2: el detalle se cuelga del NOCD_ID de la novedad (historial por novedad).
        Long nocdId = novedadCargaDocenteRepository.currentNovedadCargaDocenteId();
        insertDetails(nocdId, dto.detalles());

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
                || dto.idPersonaGeneral() == null
                || dto.idEscalafon() == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la novedad, el nuevo docente y el escalafón son obligatorios"
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
        
        validateProfessorNotDeleted(novedadOrigen);                

        Long idModalidadContratacion =
        novedadOrigen
                .map(NovedadCargaDocenteEntity::getIdModalidadContratacion)
                .orElse(cargaDocente.getIdModalidadContratacion());                
                        

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
                            idModalidadContratacion,
                            dto.idPersonaGeneral()
                    );

        if (asignaciones > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente seleccionado ya se encuentra asignado en esta carga y modalidad"
            );
        }

        EscalafonEntity escalafon =
        escalafonRepository
                .findById(dto.idEscalafon())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el escalafón seleccionado"
                )); 
        
        if (!Objects.equals(
                escalafon.getIdPersonaGeneral(),
                dto.idPersonaGeneral())) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no corresponde al docente seleccionado"
            );
        }

        if (!Objects.equals(
                escalafon.getIdModalidadContratacion(),
                idModalidadContratacion)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no corresponde a la modalidad de contratación actual"
            );
        }

        if (escalafon.getIdCategoriaCatedratico() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El escalafón seleccionado no tiene una categoría asociada"
            );
        }

        Long anio = resolveCargaYear(cargaDocente);

        ValorPuntosPrecargaDTO valoresPuntos =
                coordinacionService.getValuePointsPreload(
                        anio,
                        escalafon.getIdCategoriaCatedratico(),
                        dto.idPersonaGeneral(),
                        idModalidadContratacion
                );

        CargaBudgetOverlay overlay =
                buildChangeProfessorOverlay(
                        cargaDocente,
                        novedadOrigen,
                        valoresPuntos
                );

        ValorContratacionDTO valoresContrato =
                cargaBudgetService.computeInclusive(
                        overlay
                );

        cargaBudgetService.assertNotExceedsAuthorized(
                cargaDocente.getIdCarga(),
                overlay,
                "No se puede realizar el cambio de docente porque el valor proyectado supera el valor autorizado de la carga"
        );

        String registradoPor =
                RegistradoPorUtils.value(Accion.INSERT);

        int inserted;

        if (novedadOrigen.isPresent()) {

            inserted =
                    novedadCargaDocenteRepository
                            .insertChangeProfessorFromNovelty(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    escalafon.getIdCategoriaCatedratico(),
                                    dto.idNovedad(),
                                    valoresContrato.valorContrato(),
                                    valoresContrato.totalPrestaciones(),
                                    valoresContrato.salario(),
                                    overlay.valorHora(),
                                    overlay.puntos(),
                                    overlay.valorPunto(),
                                    valoresContrato.totalContrato(),
                                    registradoPor
                            );

        } else {

            inserted =
                    novedadCargaDocenteRepository
                            .insertChangeProfessorFromCargaDocente(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    escalafon.getIdCategoriaCatedratico(),
                                    dto.idNovedad(),
                                    valoresContrato.valorContrato(),
                                    valoresContrato.totalPrestaciones(),
                                    valoresContrato.salario(),
                                    overlay.valorHora(),
                                    overlay.puntos(),
                                    overlay.valorPunto(),
                                    valoresContrato.totalContrato(),
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

        validateProfessorNotDeleted(novedadOrigen);                

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
    public void saveNoveltyActivities(GuardarNovedadesDetallesProyectosDTO dto) {
        // Los campos de idCargaDocente o detallesCargaDocente ya vienen correctamente como novedad u original segun sea el caso
        log.info("saveNoveltyActivities ===> Guardando novedad detalle precarga docente. idCargaDocente={}", dto.idCargaDocente());

        NovedadEntity novedad = novedadRepository.findById(dto.idNovedad())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la novedad seleccionada"));
        validateChangeProjectActivitiesType(novedad);

        if (novedadCargaDocenteRepository.countNoveltyInReview(dto.idCargaDocente()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT,"El docente tiene una novedad en revisión");
        }

        Optional<NovedadCargaDocenteEntity> previous = novedadCargaDocenteRepository.findProfessorRecordToDuplicateNovelty(dto.idCargaDocente());
        
        validateProfessorNotDeleted(previous);
        
        CargaDocenteEntity cargaOriginal = cargaDocenteRepository.findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente seleccionada"));

        List<DetalleCargaDocenteDTO> detallesBase = detalleCargaDocenteMapper.toDtoList(
                detalleCargaDocenteRepository.findVigenteDetailsByIdCargaDocente(dto.idCargaDocente()),
                proyectoMapper);
        BigDecimal horasActividades = resolveDetallesHorasFromNoveltyActivityChanges(dto, detallesBase);

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
                horasActividades,
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
                horasActividades,
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
        // Fase 2: la novedad guarda su propia foto de detalle (historial). Se copia la
        // base efectiva, se aplican los cambios y se inserta todo bajo el NOCD_ID nuevo.
        Long idCoordinacion = resolveIdCoordinacionByNovedadCargaDocente(dto.idCargaDocente());
        validatePreassignmentWriteAllowedByNovedadCargaDocente(dto.idCargaDocente());

        Long nocdId = novedadCargaDocenteRepository.currentNovedadCargaDocenteId();
        guardarFotoDetalleNovedad(dto, nocdId, idCoordinacion);

        log.info("saveNoveltyActivities ===> Novedad detalle precarga docente guardado. idCargaDocente={}", dto.idCargaDocente());
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
                                        + idCargaDocente
                        ));

        NovedadCargaDocenteEntity novedadEnRevision =
                novedadCargaDocenteRepository
                        .findNoveltyInReview(idCargaDocente)
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.CONFLICT,
                                "El docente no tiene una novedad en revisión"
                        ));

        NovedadEntity novedad =
                novedadRepository
                        .findById(novedadEnRevision.getIdNovedadCatalogo())
                        .orElseThrow(() -> new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe la novedad seleccionada"
                        ));

        String estadoEliminado =
                COMPONENT_DELETE_PROFESSOR.equalsIgnoreCase(
                        novedad.getComponente() != null
                                ? novedad.getComponente().trim()
                                : ""
                )
                        ? "1"
                        : "0";

        Long idNovedadCargaDocente = novedadEnRevision.getIdNovedadCargaDocente();

        novedadCargaDocenteRepository
                .clearVigenteByIdCargaDocente(idCargaDocente);

        // Fase 3: se aprueba por NOCD_ID exacto (no por MAX(FECHACAMBIO)).
        int updated =
                novedadCargaDocenteRepository
                        .approveNoveltyById(
                                idNovedadCargaDocente,
                                estadoEliminado,
                                RegistradoPorUtils.value(Accion.UPDATE)
                        );

        if (updated < 1) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible aprobar la novedad"
            );
        }

        // Fase 3: activar la foto de detalle de la novedad aprobada. Solo si tiene
        // detalle, para no borrar el conjunto efectivo de una novedad sin actividades.
        if (detalleNovedadCargaDocenteRepository.countByIdNovedadCargaDocente(idNovedadCargaDocente) > 0) {
            detalleNovedadCargaDocenteRepository.clearDetalleVigenteByCargaDocente(idCargaDocente);
            detalleNovedadCargaDocenteRepository.setDetalleVigenteByNovedad(idNovedadCargaDocente);
        }

        cargaBudgetService.refreshCargValor(
                cargaDocente.getIdCarga()
        );

        log.info(
                "approveProfessorNovelty ===> Novedad aprobada. "
                        + "idCargaDocente={}, idNovedadCargaDocente={}, idCarga={}, estadoEliminado={}",
                idCargaDocente,
                idNovedadCargaDocente,
                cargaDocente.getIdCarga(),
                estadoEliminado
        );
    }

    @Override
    @Transactional
    public void rejectProfessorNovelty(Long idCargaDocente, ObservacionDecanoDTO dto) {
        log.info("rejectProfessorNovelty ===> Rechazando novedad. idCargaDocente={}", idCargaDocente);

        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                .findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));

        NovedadCargaDocenteEntity novedadEnRevision = novedadCargaDocenteRepository
                .findNoveltyInReview(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.CONFLICT,
                        "El docente no tiene una novedad en revisión"));

        NovedadEntity novedad = novedadRepository
                .findById(novedadEnRevision.getIdNovedadCatalogo())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la novedad seleccionada"));

        Long idNovedadCargaDocente = novedadEnRevision.getIdNovedadCargaDocente();

        // Fase 4: rechazo logico; la novedad queda en '2' y sin vigencia.
        int updated = novedadCargaDocenteRepository.rejectNoveltyById(
                idNovedadCargaDocente,
                RegistradoPorUtils.value(Accion.UPDATE));
        if (updated < 1) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible rechazar la novedad");
        }

        // Fase 4: el motivo del rechazo queda en RVD.OBSERVACIONES (fuente del resumen).
        saveObservacionRechazoNovedad(idCargaDocente, idNovedadCargaDocente, dto);

        // Fase 4: si la novedad agrego un docente, se elimina la carga creada y sus detalles.
        if (COMPONENT_ADD_NOVELTY_PROFESSOR.equalsIgnoreCase(
                novedad.getComponente() != null ? novedad.getComponente().trim() : "")) {
            eliminarDocenteCreadoPorNovedad(cargaDocente, idNovedadCargaDocente);
        }

        log.info(
                "rejectProfessorNovelty ===> Novedad rechazada. "
                        + "idCargaDocente={}, idNovedadCargaDocente={}",
                idCargaDocente,
                idNovedadCargaDocente);
    }

    // Fase 4: guarda el motivo del rechazo asociado a la novedad exacta
    // en RVD.OBSERVACIONES.
    private void saveObservacionRechazoNovedad(
            Long idCargaDocente,
            Long idNovedadCargaDocente,
            ObservacionDecanoDTO dto) {

        if (dto == null || !StringUtils.hasText(dto.observacion())) {
            return;
        }

        Date ahora = new Date();

        ObservacionesEntity observacion = new ObservacionesEntity();

        observacion.setIdCargaDocente(idCargaDocente);

        // Asociación lógica con la novedad exacta que fue rechazada.
        observacion.setIdNovedadCargaDocente(idNovedadCargaDocente);

        observacion.setIdPersonaGeneralRegistra(
                dto.idPersonaGeneral()
        );

        observacion.setTexto(
                dto.observacion().trim()
        );

        observacion.setFecha(ahora);

        observacion.setRegistradoPor(
                RegistradoPorUtils.value(Accion.INSERT)
        );

        observacion.setFechaCambio(ahora);

        observacionesRepository.save(observacion);
    }

    // Fase 4: al rechazar add-professor se elimina la carga creada y sus detalles.
    private void eliminarDocenteCreadoPorNovedad(
            CargaDocenteEntity cargaDocente,
            Long idNovedadCargaDocente) {

        String registradoPor = RegistradoPorUtils.value(Accion.DELETE);

        // Detalles de la novedad rechazada (si tiene).
        for (DetalleNovedadCargaDocenteEntity detalle :
                detalleNovedadCargaDocenteRepository.findByIdNovedadCargaDocente(idNovedadCargaDocente)) {
            relacionCargaProyectoRepository.deleteByIdDetalleNovedadCargaDocente(detalle.getId());
            detalleNovedadCargaDocenteRepository.deleteByProcedure(detalle.getId(), registradoPor);
        }

        // Detalles originales de la carga (si tiene).
        for (DetalleCargaDocenteEntity detalle :
                detalleCargaDocenteRepository.findAllByIdCargaDocente(cargaDocente.getId())) {
            relacionCargaProyectoRepository.deleteByIdDetalleCargaDocente(detalle.getId());
            detalleCargaDocenteRepository.deleteByProcedure(detalle.getId(), registradoPor);
        }

        Long idCarga = cargaDocente.getIdCarga();
        cargaDocenteRepository.deleteByProcedure(cargaDocente.getId(), registradoPor);

        if (idCarga != null) {
            cargaBudgetService.refreshPreassignmentTotals(idCarga);
        }

        log.info(
                "rejectProfessorNovelty ===> Docente creado por novedad eliminado. idCargaDocente={}",
                cargaDocente.getId());
    }


    private void validateRequest(
            AsignarNombreNnDTO dto
    ) {

        if (
            dto == null
            || dto.idCargaDocente() == null
            || dto.idNovedad() == null
            || dto.idPersonaGeneral() == null
            || dto.idEscalafon() == null
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la novedad, el nuevo docente y el escalafón son obligatorios"
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

        if (cargaDocente.getIdCarga() == null || !cargaDocente.getIdCarga().equals(dto.idCarga())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"La carga enviada no corresponde a la carga docente");
        }
    }

    private void validateContractModalityType(NovedadEntity novedad) {

        String component = novedad.getComponente();

        if (component == null || !COMPONENT_CONTRACT_MODALITY.equalsIgnoreCase(component.trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La novedad seleccionada no corresponde a cambio de modalidad");
        }
    }

    private void validateChangeProjectActivitiesType(
            NovedadEntity novedad
    ) {
        String component = novedad.getComponente();
        if (!isChangeActivitiesComponent(component)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a reasignación de actividades docente"
            );
        }
    }

    private boolean isChangeActivitiesComponent(String component) {
        if (component == null) {
            return false;
        }
        
        String value = component.trim();
        return COMPONENT_CHANGE_PROJECT_ACTIVITIES.equalsIgnoreCase(value) || COMPONENT_CHANGE_DIRECT_ACTIVITIES.equalsIgnoreCase(value);
    }

    private void validateDetalleItem(DetalleCargaDocenteItemDTO detalle) {

        if (detalle == null || detalle.horas() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }

        if (detalle.idCentroCosto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"El centro de costo del detalle es obligatorio");
        }

        Long tipoActividad = detalle.idTipoActividadHija() != null ? detalle.idTipoActividadHija() : detalle.idTipoActividad();

        if (tipoActividad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"El tipo de actividad del detalle es obligatorio");
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

    private BigDecimal resolveDetallesHorasFromNoveltyActivityChanges(GuardarNovedadesDetallesProyectosDTO dto, List<DetalleCargaDocenteDTO> detallesBase) {
        BigDecimal total = BigDecimal.ZERO;
        Optional<Integer> esDeNovedad = detallesBase.get(0).esDeNovedad();
        Boolean fromNovelty = esDeNovedad != null && esDeNovedad.orElse(0) == 1;

        // Si los detalles no vienen de novedades, su total es cero porque la primera vez el dto tiene todas las actividades
        // Si los detalles vienen de novedades, su total es la suma de actividades que se trae de la base de datos (que es la misma que se mostró en el front)
        if (fromNovelty) {
            for (DetalleCargaDocenteDTO detalle : detallesBase) {
                DetalleCargaDocenteActividadDTO actividad = detalle.detalles().get(0);
                if (actividad != null && actividad.horas() != null) {
                    total = total.add(parseDecimal(actividad.horas()));
                }
            }
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

                DetalleCargaDocenteDTO detallePersistido = detallesBase.stream()
                    .filter(base -> 
                        base != null &&
                        Objects.equals(base.idDetalleCargaDocente(), detalle.idDetalleCargaDocente())
                    )
                    .findFirst()
                    .orElse(null);

                String horasPersistidasRaw = detallePersistido.detalles().get(0).horas();
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
            DetalleCargaDocenteDTO detallePersistido = detallesBase.stream()
                    .filter(base -> 
                        base != null &&
                        Objects.equals(base.idDetalleCargaDocente(), idDetalle)
                    )
                    .findFirst()
                    .orElse(null);
            
            String horasPersistidasRaw = detallePersistido.detalles().get(0).horas();
            BigDecimal horasPersistidas = parseDecimal(horasPersistidasRaw);

            if (horasPersistidas != null) {
                total = total.subtract(horasPersistidas);
            }
        }

        return total.compareTo(BigDecimal.ZERO) > 0 ? total : BigDecimal.ZERO;
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

    private void insertDetails(Long idNovedadCargaDocente, List<DetalleCargaDocenteItemDTO> detalles) {

        for (DetalleCargaDocenteItemDTO detalle : detalles) {
            DetalleNovedadCargaDocenteEntity entity = new DetalleNovedadCargaDocenteEntity();

            fillDetalle(
                    entity,
                    idNovedadCargaDocente,
                    detalle
            );
            detalleNovedadCargaDocenteRepository.save(entity);
        }
    }

    private void fillDetalle(
            DetalleNovedadCargaDocenteEntity entity,
            Long idNovedadCargaDocente,
            DetalleCargaDocenteItemDTO detalle
    ) {

        Long tipoActividad =
                detalle.idTipoActividadHija() != null
                        ? detalle.idTipoActividadHija()
                        : detalle.idTipoActividad();

        entity.setIdNovedadCargaDocente(idNovedadCargaDocente);
        entity.setIdPrograma(detalle.idPrograma());
        entity.setIdGrupo(detalle.idGrupo());
        entity.setIdTipoActividad(tipoActividad);
        entity.setIdCentroCosto(detalle.idCentroCosto());
        entity.setHoras(detalle.horas().toString());
        entity.setVigente("0");
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

    // Fase 2: construye la foto de detalle de una novedad de actividades.
    // Parte de la base efectiva (novedad anterior con detalle) y aplica los cambios;
    // en la primera novedad el front envia el conjunto completo en detallesNuevos.
    private void guardarFotoDetalleNovedad(
            GuardarNovedadesDetallesProyectosDTO dto,
            Long idNovedadCargaDocente,
            Long idCoordinacion) {

        List<DetalleCargaDocenteListadoProjection> baseProyecciones =
                detalleCargaDocenteRepository.findVigenteDetailsByIdCargaDocente(dto.idCargaDocente());

        boolean baseEsNovedad = !baseProyecciones.isEmpty()
                && baseProyecciones.get(0).getEsDeNovedad() != null
                && baseProyecciones.get(0).getEsDeNovedad().orElse(0) == 1;

        Map<Long, DetalleCargaDocenteItemDTO> foto = new LinkedHashMap<>();
        if (baseEsNovedad) {
            Map<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>> agrupado =
                    detalleNovedadCargaDocenteMapper.agruparPorCargaYDetalle(baseProyecciones);
            for (Map.Entry<Long, List<DetalleCargaDocenteListadoProjection>> entry :
                    agrupado.getOrDefault(dto.idCargaDocente(), Map.of()).entrySet()) {
                foto.put(entry.getKey(), detalleNovedadCargaDocenteMapper.toItemDtoFromGroup(entry.getValue()));
            }
        }

        // Eliminados: se omiten de la foto; la fila historica no se toca.
        if (dto.detallesEliminados() != null) {
            for (Long idDetalle : dto.detallesEliminados()) {
                foto.remove(idDetalle);
            }
        }

        // Actualizados: se reemplaza la fila base por los nuevos valores.
        if (dto.detallesActualizados() != null) {
            for (DetalleCargaDocenteDTO detalle : dto.detallesActualizados()) {
                foto.put(detalle.idDetalleCargaDocente(), toItemDto(detalle.detalles().get(0)));
            }
        }

        List<DetalleCargaDocenteItemDTO> resultado = new ArrayList<>(foto.values());
        if (dto.detallesNuevos() != null) {
            resultado.addAll(dto.detallesNuevos());
        }

        if (!resultado.isEmpty()) {
            agregarDetalleNovedad(idNovedadCargaDocente, dto.idCargaDocente(), resultado, idCoordinacion);
        }
    }

    // Fase 2: convierte una actividad mostrada al item con el que se persiste el detalle.
    private DetalleCargaDocenteItemDTO toItemDto(DetalleCargaDocenteActividadDTO actividad) {
        Long idTipoActividad = actividad.tipoActividad() != null
                ? actividad.tipoActividad().id()
                : null;
        Long idTipoActividadHija = actividad.tipoActividadHija() != null
                && actividad.tipoActividadHija().size() == 1
                ? actividad.tipoActividadHija().get(0).id()
                : null;
        return new DetalleCargaDocenteItemDTO(
                idTipoActividad,
                idTipoActividadHija,
                actividad.tipoActividad() != null ? actividad.tipoActividad().codigo() : null,
                detalleNovedadCargaDocenteMapper.parseHoras(actividad.horas()),
                actividad.unidadRegional() != null ? actividad.unidadRegional().id() : null,
                actividad.programa() != null ? actividad.programa().id() : null,
                new MateriaFormularioDTO(
                        actividad.materia() != null ? actividad.materia().codigoMateria() : null,
                        actividad.centroCosto() != null ? actividad.centroCosto().id() : null),
                actividad.grupo() != null ? actividad.grupo().id() : null,
                actividad.centroCosto() != null ? actividad.centroCosto().id() : null,
                detalleNovedadCargaDocenteMapper.toRelacionesCargaProyecto(actividad.relacionCargaProyecto()));
    }

    // Similar a SaveDetailProfessorPreload pero enfocado en novedades
    private void agregarDetalleNovedad(Long idNovedadCargaDocente, Long idCargaDocente, List<DetalleCargaDocenteItemDTO> detalles, Long idCoordinacion) {
        // Fase 2: la validacion de horas por programa necesita el CADO_ID, no el NOCD_ID.
        validateProgramHourRestrictionOnSaveNovelty(detalles, idCargaDocente);

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

    private Long resolveIdCoordinacionByNovedadCargaDocente(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria");
        }
        // Fase 2: una carga tiene varias novedades; se resuelve la carga directamente
        // para no depender de una novedad unica.
        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));
        if (cargaDocente.getIdCarga() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene carga asociada");
        }
        CargaEntity carga = cargaRepository.findById(cargaDocente.getIdCarga())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga con id " + cargaDocente.getIdCarga()));
        return carga.getIdCoordinacion();
    }

    private void validatePreassignmentWriteAllowedByNovedadCargaDocente(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        // Fase 2: una carga tiene varias novedades; se valida desde la carga.
        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));

        validatePreassignmentWriteAllowedByCarga(cargaDocente.getIdCarga());
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

    // Fase 2: la modalidad efectiva es la de la ultima novedad no rechazada de la carga;
    // si no hay novedades, la de CARGADOCENTE. Evita findByIdCargaDocente (ambiguo con
    // varias novedades) y conserva la modalidad fotografiada por la novedad.
    private Long resolveModalidadEfectiva(Long idCargaDocente) {
        Long fromNovelty = novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(idCargaDocente)
                .map(NovedadCargaDocenteEntity::getIdModalidadContratacion)
                .orElse(null);
        if (fromNovelty != null) {
            return fromNovelty;
        }
        return cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente))
                .getIdModalidadContratacion();
    }

    private void validateProgramHourRestrictionOnSaveNovelty(List<DetalleCargaDocenteItemDTO> detalles, Long idCargaDocente) {
        Map<Long, String> maximos = resolveMaximosHorasPrograma(resolveModalidadEfectiva(idCargaDocente));
        if (maximos.isEmpty()) {
            return;
        }

        Map<Long, BigDecimal> horasPorPrograma = new LinkedHashMap<>();
        for (DetalleCargaDocenteItemDTO detalle : detalles) {
            if (detalle.idPrograma() == null || detalle.horas() == null) {
                continue;
            }
            if (!maximos.containsKey(detalle.idPrograma())) {
                continue;
            }
            horasPorPrograma.merge(
                    detalle.idPrograma(),
                    BigDecimal.valueOf(detalle.horas()),
                    BigDecimal::add);
        }

        if (horasPorPrograma.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, BigDecimal> entry : horasPorPrograma.entrySet()) {
            assertProgramHoursWithinLimit(
                    entry.getKey(),
                    maximos.get(entry.getKey()),
                    entry.getValue());
        }
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

    private void assertProgramHoursWithinLimit(
            Long idPrograma,
            String maximoHoras,
            BigDecimal horasAsignadas) {
        BigDecimal maximo = parseHorasDetalle(maximoHoras);
        if (horasAsignadas.compareTo(maximo) > 0) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas del programa " + idPrograma
                            + " exceden el máximo permitido de "
                            + maximoHoras
                            + " (asignadas: " + horasAsignadas + ")");
        }
    }

    private BigDecimal parseHorasDetalle(String horas) {
        return ResumenCargaAssembler.parseHoras(horas);
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

    private Long resolveCargaYear(
            CargaDocenteEntity cargaDocente) {

        CargaEntity carga = cargaRepository
                .findById(cargaDocente.getIdCarga())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga asociada al docente"
                ));

        if (carga.getConvocatoria() == null
                || carga.getConvocatoria().getPeriodoUniversidad() == null
                || carga.getConvocatoria()
                        .getPeriodoUniversidad()
                        .getAno() == null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible determinar la vigencia de la carga"
            );
        }

        return carga.getConvocatoria()
                .getPeriodoUniversidad()
                .getAno();
    }

    private CargaBudgetOverlay buildChangeProfessorOverlay(
            CargaDocenteEntity cargaDocente,
            Optional<NovedadCargaDocenteEntity> novedadOrigen,
            ValorPuntosPrecargaDTO valoresPuntos) {

        if (novedadOrigen.isPresent()) {

            NovedadCargaDocenteEntity origen =
                    novedadOrigen.get();

            return new CargaBudgetOverlay(
                    cargaDocente.getId(),
                    origen.getIdModalidadContratacion(),
                    origen.getFechaInicio(),
                    origen.getFechaFin(),
                    valoresPuntos.asignacionSalarial(),
                    valoresPuntos.valorHora(),
                    parseDecimal(origen.getSemanas()),
                    parseDecimal(origen.getHoras()),
                    decimalToString(
                            valoresPuntos.puntosDocente()),
                    valoresPuntos.valorPunto()
            );
        }

        return new CargaBudgetOverlay(
                cargaDocente.getId(),
                cargaDocente.getIdModalidadContratacion(),
                cargaDocente.getFechaInicio(),
                cargaDocente.getFechaFin(),
                valoresPuntos.asignacionSalarial(),
                valoresPuntos.valorHora(),
                parseDecimal(cargaDocente.getSemanas()),
                parseDecimal(cargaDocente.getHoras()),
                decimalToString(
                        valoresPuntos.puntosDocente()),
                valoresPuntos.valorPunto()
        );
    }

    private String decimalToString(BigDecimal value) {
        if (value == null) {
            return null;
        }

        return value
                .stripTrailingZeros()
                .toPlainString();
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

    @Override
    @Transactional(readOnly = true)
    public List<HistorialGeneralNovedadDTO> getGeneralNoveltyHistory(
            Long idCarga) {

        if (idCarga == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id de carga es obligatorio"
            );
        }

        if (!cargaRepository.existsById(idCarga)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la carga con id " + idCarga
            );
        }

        return novedadCargaDocenteRepository
                .findGeneralNoveltyHistory(idCarga)
                .stream()
                .map(this::toGeneralNoveltyHistory)
                .toList();
    }

    private HistorialGeneralNovedadDTO toGeneralNoveltyHistory(
            HistorialGeneralNovedadProjection projection) {

        return new HistorialGeneralNovedadDTO(
                projection.getIdNovedadCargaDocente(),
                projection.getIdCargaDocente(),
                projection.getIdPersonaGeneral(),
                projection.getNombreDocente(),
                projection.getIdNovedadCatalogo(),
                projection.getTipoNovedad(),
                projection.getFecha(),
                projection.getEstadoNovedad(),
                projection.getMotivoRechazo()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenNovedadCargaDocenteDTO getProfessorNoveltySummary(
            Long idCargaDocente) {
        log.debug(
                "getProfessorNoveltySummary ===> idCargaDocente={}",
                idCargaDocente);
        validateCargaDocenteForSummary(idCargaDocente);
        
        List<ObservacionResumenNovedadDTO> observaciones =
                listObservacionesResumen(idCargaDocente);
        List<HistorialNovedadResumenDTO> novedades =
                listHistorialNovedades(idCargaDocente);
        Optional<NovedadCargaDocenteEntity> actual =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                idCargaDocente);

        if (actual.isEmpty()) {
            return fromCargaDocenteSummary(
                    idCargaDocente, observaciones, novedades);
        }

        return fromNovedadSummary(
                actual.get(), observaciones, novedades);
    }

    private List<DetalleNovedadResumenProjection> loadActivityDetails(Long idCargaDocente, Long idNovedadCargaDocente) {
        
        List<DetalleNovedadResumenProjection> detallesNovedad = detalleNovedadCargaDocenteRepository.findResumenByIdNovedadCargaDocente(idNovedadCargaDocente, idCargaDocente);
        if (!detallesNovedad.isEmpty()) {
            return detallesNovedad;
        }
        return detalleCargaDocenteRepository.findResumenByIdCargaDocente(idCargaDocente);
    }

    private ResumenNovedadCargaDocenteDTO fromCargaDocenteSummary(
            Long idCargaDocente,
            List<ObservacionResumenNovedadDTO> observaciones,
            List<HistorialNovedadResumenDTO> novedades) {
        
        ResumenCargaDocenteDTO base = coordinacionService.getProfessorLoadSummary(idCargaDocente);

        log.info(
                "getProfessorNoveltySummary ===> sin novedad no rechazada. id={}",
                idCargaDocente);

        return new ResumenNovedadCargaDocenteDTO(
                base.idCargaDocente(),
                base.valorContratacion(),
                base.horasActividades(),
                base.centrosCosto(),
                observaciones,
                novedades);
    }

    private ResumenNovedadCargaDocenteDTO fromNovedadSummary(
            NovedadCargaDocenteEntity novedad,
            List<ObservacionResumenNovedadDTO> observaciones,
            List<HistorialNovedadResumenDTO> novedades) {
        
        List<DetalleResumen> detalles = ResumenCargaAssembler.fromNovedadList(
                loadActivityDetails(novedad.getIdCargaDocente(), novedad.getIdNovedadCargaDocente()));
        boolean planta = isPlantaModalidad(novedad.getIdModalidadContratacion());
        
        ValorContratacionDTO valor = planta
                ? null
                : calculateNoveltyContract(novedad, detalles);
        BigDecimal totalContrato = valor != null
                ? valor.totalContrato()
                : BigDecimal.ZERO;
        
        List<ActividadHorasResumenDTO> horas =
                ResumenCargaAssembler.buildActivityHours(detalles);
        List<CentroCostoResumenDTO> centros =
                ResumenCargaAssembler.buildCostCenters(
                        detalles, totalContrato);
        
        log.info(
                "getProfessorNoveltySummary ===> novedad no rechazada. "
                        + "id={}, planta={}, actividades={}, centros={}",
                novedad.getIdCargaDocente(),
                planta,
                horas.size(),
                centros.size());
        
        return new ResumenNovedadCargaDocenteDTO(
                novedad.getIdCargaDocente(),
                valor,
                horas,
                centros,
                observaciones,
                novedades);
    }

    private ValorContratacionDTO calculateNoveltyContract(NovedadCargaDocenteEntity novedad, List<DetalleResumen> detalles) {
        if (isCatedraModalidad(novedad.getIdModalidadContratacion())) {
            return calculateNoveltyCatedra(novedad, detalles);
        }

        return calculateNoveltyTco(novedad);
    }

    private ValorContratacionDTO calculateNoveltyCatedra(NovedadCargaDocenteEntity novedad, List<DetalleResumen> detalles) {
        
        BigDecimal horas = resolveHorasSemanalesNovedad(novedad, detalles);
        
        BigDecimal semanas = ResumenCargaAssembler.parseHoras(
                novedad.getSemanas());
        
        if (!canCalculateNoveltyCatedra(novedad, horas, semanas)) {
            return null;
        }
        return ValorContratacionCalculator.calculateCatedra(
                horas,
                semanas,
                novedad.getValorHora(),
                novedad.getFechaInicio(),
                novedad.getFechaFin());
    }

    private boolean canCalculateNoveltyCatedra(NovedadCargaDocenteEntity novedad, BigDecimal horas, BigDecimal semanas) {
        return novedad.getValorHora() != null
                && novedad.getFechaInicio() != null
                && novedad.getFechaFin() != null
                && semanas.compareTo(BigDecimal.ZERO) > 0
                && horas.compareTo(BigDecimal.ZERO) > 0;
    }

    private ValorContratacionDTO calculateNoveltyTco(NovedadCargaDocenteEntity novedad) {
        
        BigDecimal asignacion = resolveAsignacionNovedad(novedad);
        
        if (asignacion == null || novedad.getFechaInicio() == null || novedad.getFechaFin() == null) {
            return null;
        }
        return ValorContratacionCalculator.calculate(
                asignacion,
                novedad.getFechaInicio(),
                novedad.getFechaFin());
    }

    private BigDecimal resolveAsignacionNovedad(NovedadCargaDocenteEntity novedad) {
        if (novedad.getSalario() != null) {
            return novedad.getSalario();
        }

        if (novedad.getValorPunto() == null
                || !StringUtils.hasText(novedad.getPuntos())) {
            return null;
        }

        return novedad.getValorPunto()
                .multiply(parsePuntosNovedad(novedad.getPuntos()))
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    private BigDecimal resolveHorasSemanalesNovedad(NovedadCargaDocenteEntity novedad, List<DetalleResumen> detalles) {
        BigDecimal stored = ResumenCargaAssembler.parseHoras(novedad.getHoras());
        
        if (stored.compareTo(BigDecimal.ZERO) > 0) {
            return stored;
        }
        return ResumenCargaAssembler.sumHoras(detalles);
    }

    private List<ObservacionResumenNovedadDTO> listObservacionesResumen(Long idCargaDocente) {
        return observacionesRepository
                .findByIdCargaDocente(idCargaDocente)
                .stream()
                .map(this::toObservacionResumen)
                .toList();
    }

    private ObservacionResumenNovedadDTO toObservacionResumen(ObservacionResumenProjection projection) {
        return new ObservacionResumenNovedadDTO(
                projection.getIdObservacion(),
                projection.getIdPersonaGeneral(),
                projection.getNombrePersonaGeneral(),
                projection.getObservacion(),
                projection.getFecha());
    }

    private List<HistorialNovedadResumenDTO> listHistorialNovedades(
            Long idCargaDocente) {
        return novedadCargaDocenteRepository
                .findHistorialByIdCargaDocente(idCargaDocente)
                .stream()
                .map(this::toHistorialNovedad)
                .toList();
    }

    private HistorialNovedadResumenDTO toHistorialNovedad(
            HistorialNovedadResumenProjection projection) {
        return new HistorialNovedadResumenDTO(
                projection.getIdNovedad(),
                projection.getTipo(),
                projection.getDescripcion(),
                projection.getAccion(),
                projection.getComponente(),
                projection.getFecha(),
                projection.getEstadoNovedad(),
                projection.getVigente());
    }

    private void validateCargaDocenteForSummary(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de carga docente es obligatorio");
        }
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }
    }

    private boolean isPlantaModalidad(Long idModalidad) {
        if (idModalidad == null) {
            return false;
        }
        return modalidadContratacionRepository.findById(idModalidad).map(this::isPlantaNombre).orElse(false);
    }

    private boolean isPlantaNombre(ModalidadContratacionEntity modalidad) {
        return ValorContratacionCalculator.isPlanta(
                modalidad.getNombre(),
                modalidad.getSigla());
    }

    private boolean isCatedraModalidad(Long idModalidad) {
        if (idModalidad == null) {
            return false;
        }
        String formaPago = restriccionCargaRepository.findById(idModalidad)
                .map(RestriccionCargaEntity::getFormaPago)
                .orElse(null);
        return ValorContratacionCalculator.isCatedra(formaPago);
    }

    private BigDecimal parsePuntosNovedad(String puntos) {
        try {
            return new BigDecimal(puntos.trim());
        } catch (NumberFormatException ex) {
            return PUNTOS_DEFAULT;
        }
    }

    private void validateProfessorNotDeleted(
            Optional<NovedadCargaDocenteEntity> previous
    ) {

        if (
            previous.isPresent()
            && "1".equals(previous.get().getEstadoEliminado())
        ) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente ya fue eliminado de la carga y no admite nuevas novedades"
            );
        }
    }


















}
