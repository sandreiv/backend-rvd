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
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
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


    private final CargaBudgetService cargaBudgetService;

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
}
