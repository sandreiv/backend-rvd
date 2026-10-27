/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalServiceImpl.java
 * Paquete: Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 * 14/10/2026 - Andrés Hernández - Almacenamiento del PTD y su observación
 * 14/10/2026 - Andrés Hernández - Visualización del PTD con su ponderación y observación
 * 20/10/2026 - Andrés Hernández - Busqueda de estados del PTD por persona y filtrado general por periodo universitario
 * 23/10/2026 - Andrés Hernández - Guardar el PTD y ver su info trae las observaciones hechas por el Coordinador
 * 26/10/2026 - Andrés Hernández - Gestión del PTD por Talento humano (Rechazo y Aprobación)
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PlanTrabajoDigitalMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActivityWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ApprovePtdRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdObservationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSavePayloadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSaveRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdStatesProfessorsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RejectPtdRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionPtdEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PtdiDocumentoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ObservacionPtdRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PlanTrabajoDigitalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PtdiDocumentoRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PlanTrabajoDigitalService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanTrabajoDigitalServiceImpl implements PlanTrabajoDigitalService{

    private static final String ROL_COORDINADOR = "Coordinador";
    private static final String ROL_TALENTO = "Talento humano";

    private static final String ESTADO_PTD_APROBADO_TH = "3";
    private static final String ESTADO_PTD_ESTIPULADO = "0";
    
    private final PlanTrabajoDigitalRepository planTrabajoDigitalRepository;
    private final ObservacionPtdRepository observacionPtdRepository;
    private final PtdiDocumentoRepository ptdiDocumentoRepository;

    private final PlanTrabajoDigitalMapper planTrabajoDigitalMapper;

    @Override
    @Transactional
    public void savePtd(PtdSaveRequestDTO dto) {
        log.info("savePtd ===> Guardando información del PTD. idPersona={}", dto.registros().getFirst().idPersonaGeneral());
        
        Date fechaActual = new Date();
        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        AuthUserDetails user;
        
        try {
            user = SecurityUtils.requireUser();
        } catch (IllegalStateException ex) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "El token no trae idPersona"
            );
        }
        if (!hasRole(user, ROL_COORDINADOR)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Para guardar el PTD se requiere rol Coordinador"
            );
        }

        validateCreation(dto.registros().getFirst());
        validatePtdSaveDto(dto.registros());
        validatePonderacion(dto.registros());

        List<PlanTrabajoDigitalEntity> ptds = dto.registros()
            .stream()
            .map(registro -> {
                PlanTrabajoDigitalEntity ptd = planTrabajoDigitalMapper.toEntity(registro);
                ptd.setEstado("0");
                ptd.setRegistradoPor(registradoPor);
                ptd.setFechaCambio(fechaActual);

                return ptd;
            })
            .toList();
        
        planTrabajoDigitalRepository.saveAll(ptds);

        ObservacionPtdEntity observacion = new ObservacionPtdEntity();
        observacion.setIdCargaDocente(dto.observacion().idCargaDocente());
        observacion.setIdPersonaGeneralRegistra(user.getIdPersonaGeneral());
        observacion.setTexto(dto.observacion().descripcion());
        observacion.setRetroalimentacion(null);
        observacion.setRolPersonaGeneralRegistra(ROL_COORDINADOR);
        observacion.setRegistradoPor(registradoPor);
        observacion.setFechaCambio(fechaActual);

        observacionPtdRepository.save(observacion);

        log.info("savePtd ===> Información del PTD guardada. idPersona={}", dto.registros().getFirst().idPersonaGeneral());
    }

    @Override
    @Transactional(readOnly = true)
    public PtdInfoWithWeightsDTO getInfoPtdWithWeights(Long idPersonaGeneral, Long idCargaDocente, Long idPeriodoUniversitario) {
        log.info("getInfoPtdWithWeights ===> Obteniendo información del PTD. idPersona={}, idCargaDocente={}", idPersonaGeneral, idCargaDocente);

        ObservacionPtdEntity observacionEntity = observacionPtdRepository.findByIdCargaDocenteAndPegeRole(idCargaDocente, ROL_COORDINADOR);
        List<ActivityWeightsDTO> actividades = planTrabajoDigitalRepository
            .findActivityWeightsByPersona(idPersonaGeneral, idPeriodoUniversitario)
            .stream()
            .map(planTrabajoDigitalMapper::toActivityWeightsDTO)
            .toList();

        if (actividades.isEmpty() || observacionEntity == null) {
            return null;
        }

        PtdObservationDTO observacion = new PtdObservationDTO(observacionEntity.getTexto(), observacionEntity.getIdCargaDocente());

        log.info("getInfoPtdWithWeights ===> Información del PTD obtenida. idPersona={}, idCargaDocente={}", idPersonaGeneral, idCargaDocente);
        return new PtdInfoWithWeightsDTO(actividades, observacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PtdStatesProfessorsDTO> getPtdStatesProfessors(Long idPeriodoUniversitario) {
        log.info("getPtdStatesProfessors ===> Obteniendo estados del PTD por persona. idPeriodo={}", idPeriodoUniversitario);

        List<PtdStatesProfessorsDTO> states = planTrabajoDigitalRepository
            .findPtdStatesByPeriod(idPeriodoUniversitario)
            .stream()
            .map(planTrabajoDigitalMapper::toPtdStatesProfessorsDTO)
            .toList();

        if (states.isEmpty()) {
            return List.of();
        }

        log.info("getPtdStatesProfessors ===> Estados por persona obtenidos. idPeriodo={}, cantidad={}", idPeriodoUniversitario, states.size());
        return states;
    }

    @Override
    @Transactional
    public void approvePtdByTh(ApprovePtdRequestDTO dto) {
        log.info("approvePtdByTh ===> Aprobando por TH el PTD. idPersona={}", dto.idPersonaGeneral());
        
        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        AuthUserDetails user;
        
        try {
            user = SecurityUtils.requireUser();
        } catch (IllegalStateException ex) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "El token no trae idPersona"
            );
        }
        if (!hasRole(user, ROL_TALENTO)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Para aprobar el PTD se requiere rol Talento humano"
            );
        }

        validatePtdApproveDto(dto);
        validateUpdate(dto.idPersonaGeneral(), dto.idPeriodoUniversitario());

        // Cambiar estado del PTD
        planTrabajoDigitalRepository.updateStateAllPtdActivities(dto.idPersonaGeneral(), dto.idPeriodoUniversitario(), ESTADO_PTD_APROBADO_TH, registradoPor);

        // Cambiar estado de los adjuntos del PTD
        updateStateDocumentsPtd(dto.idPersonaGeneral(), dto.idPeriodoUniversitario(), dto.idCargaDocente(), dto.idsDocumentosAdjuntos());

        log.info("approvePtdByTh ===> PTD aprobado por TH. idPersona={}", dto.idPersonaGeneral());
    }

    @Override
    @Transactional
    public void declinePtdByTh(RejectPtdRequestDTO dto) {
        log.info("declinePtdByTh ===> Rechazando por TH el PTD. idPersona={}", dto.idPersonaGeneral());
        
        Date fechaActual = new Date();
        String registradoPor = RegistradoPorUtils.value(Accion.INSERT);
        AuthUserDetails user;
        
        try {
            user = SecurityUtils.requireUser();
        } catch (IllegalStateException ex) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "El token no trae idPersona"
            );
        }
        if (!hasRole(user, ROL_TALENTO)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Para rechazar el PTD se requiere rol Talento humano"
            );
        }

        validatePtdRejectDto(dto);
        validateUpdate(dto.idPersonaGeneral(), dto.idPeriodoUniversitario());

        // Cambiar estado del PTD
        planTrabajoDigitalRepository.updateStateAllPtdActivities(dto.idPersonaGeneral(), dto.idPeriodoUniversitario(), ESTADO_PTD_ESTIPULADO, registradoPor);

        // Cambiar estado de los adjuntos validos del PTD
        updateStateDocumentsPtd(dto.idPersonaGeneral(), dto.idPeriodoUniversitario(), dto.idCargaDocente(), dto.idsDocumentosAdjuntos());

        // Crear la observación de rechazo
        ObservacionPtdEntity observacion = new ObservacionPtdEntity();
        observacion.setIdCargaDocente(dto.idCargaDocente());
        observacion.setIdPersonaGeneralRegistra(user.getIdPersonaGeneral());
        observacion.setTexto(dto.observacion());
        observacion.setRetroalimentacion(null);
        observacion.setRolPersonaGeneralRegistra(ROL_TALENTO);
        observacion.setRegistradoPor(registradoPor);
        observacion.setFechaCambio(fechaActual);

        observacionPtdRepository.save(observacion);

        log.info("declinePtdByTh ===> PTD rechazado por TH. idPersona={}", dto.idPersonaGeneral());
    }


    private void validateCreation(PtdSavePayloadDTO primerRegistro) {
        if (planTrabajoDigitalRepository.existsByIdPersonaGeneralAndIdPeriodoUniversitario(primerRegistro.idPersonaGeneral(), primerRegistro.idPeriodoUniversitario())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El docente ya cuenta con actividades en el PTD para este periodo");
        }
    }

    private void validateUpdate(Long idPersonaGeneral, Long idPeriodoUniversitario) {
        if (!planTrabajoDigitalRepository.existsByIdPersonaGeneralAndIdPeriodoUniversitario(idPersonaGeneral, idPeriodoUniversitario)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El docente no cuenta con actividades en el PTD para este periodo");
        }
    }
    
    private void validatePtdApproveDto(ApprovePtdRequestDTO dto) {
        if (dto == null) {
            throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "La información del PTD es obligatoria"
            );
        }

        if (dto.idPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
        }
        if (dto.idPeriodoUniversitario() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria");
        }
        if (dto.idsDocumentosAdjuntos() == null || dto.idsDocumentosAdjuntos().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Los adjuntos validos son obligatorios");
        }
    }

    private void validatePtdRejectDto(RejectPtdRequestDTO dto) {
        if (dto == null) {
            throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "La información del PTD es obligatoria"
            );
        }

        if (dto.idPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
        }
        if (dto.idPeriodoUniversitario() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria");
        }
        if (dto.observacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La observación de rechazo es obligatoria");
        }
    }

    private void validatePtdSaveDto(List<PtdSavePayloadDTO> dto) {
        if (dto == null || dto.isEmpty()) {
            throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "La información de las actividades PTD es obligatoria"
            );
        }

        for (PtdSavePayloadDTO item : dto) {
            if (item == null) {
                throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de la actividad PTD es obligatoria"
                );
            }

            boolean tieneDetalleCarga = item.idDetalleCargaDocente() != null;
            boolean tieneDetalleNovedad = item.idDetalleNovedadCargaDocente() != null;

            if (tieneDetalleCarga && tieneDetalleNovedad) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Debe indicar un detalle de carga docente o un detalle de novedad, pero no ambos");
            }

            if (item.idTipoActividad() == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad es obligatorio");
            }

            if (item.ponderado() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El ponderado debe ser positivo");
            }

            if (item.idPersonaGeneral() == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "La persona es obligatoria");
            }

            if (item.idPeriodoUniversitario() == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
            }
        }
    }

    private void validatePonderacion(List<PtdSavePayloadDTO> dto) {
        Map<Long, Long> ponderacionesPorActividad = new LinkedHashMap<>();

        for (PtdSavePayloadDTO item : dto) {
            ponderacionesPorActividad.putIfAbsent(
                item.idTipoActividad(),
                item.ponderado()
            );
        }

        long sumaPonderaciones = ponderacionesPorActividad.values()
            .stream()
            .mapToLong(Long::longValue)
            .sum();

        if (sumaPonderaciones != 100) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La suma de las ponderaciones por actividad debe ser igual a 100");
        }
    }

    // Llamar al metodo que busca los adjuntos de un PTD y a esos comparar con las ids que le paso para ponerle su estado en 1
    private void updateStateDocumentsPtd(Long idPersonaGeneral, Long idPeriodoUniversitario, Long idCargaDocente, List<String> idsDocumentosAdjuntos) {
        if (idsDocumentosAdjuntos == null || idsDocumentosAdjuntos.isEmpty()) {
            return;
        }

        Long idPtdReferencia = planTrabajoDigitalRepository.findReferencePtdIdByHiringAndProfessor(idCargaDocente, idPersonaGeneral, idPeriodoUniversitario);

        List<String> idsAdjuntosPersistidos = ptdiDocumentoRepository.findByIdPlanTrabajoDigital(idPtdReferencia)
            .stream()
            .map(PtdiDocumentoEntity::getUniqueId)
            .toList();
        
        Set<String> idsPersistidos = new HashSet<>(idsAdjuntosPersistidos);

        boolean todosExisten = idsDocumentosAdjuntos.stream().allMatch(idsPersistidos::contains);
        if (!todosExisten) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Alguno de los IDs de los adjuntos enviados no coinciden con los adjuntos persistidos");
        }

        ptdiDocumentoRepository.updateStatePtdAttachments(idsDocumentosAdjuntos);
    }

    private static boolean hasRole(AuthUserDetails user, String role) {
        return user.getRoles().stream()
            .filter(Objects::nonNull)
            .anyMatch(item -> role.equalsIgnoreCase(item.trim()));
    }
}
