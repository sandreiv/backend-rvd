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
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PlanTrabajoDigitalMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActivityWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdObservationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSavePayloadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSaveRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionPtdEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ObservacionPtdRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PlanTrabajoDigitalRepository;
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
    
    private final PlanTrabajoDigitalRepository planTrabajoDigitalRepository;
    private final ObservacionPtdRepository observacionPtdRepository;

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
        observacion.setRegistradoPor(registradoPor);
        observacion.setFechaCambio(fechaActual);

        observacionPtdRepository.save(observacion);

        log.info("savePtd ===> Información del PTD guardada. idPersona={}", dto.registros().getFirst().idPersonaGeneral());
    }

    @Override
    @Transactional(readOnly = true)
    public PtdInfoWithWeightsDTO getInfoPtdWithWeights(Long idPersonaGeneral, Long idCargaDocente, Long idPeriodoUniversitario) {
        log.info("getInfoPtdWithWeights ===> Obteniendo información del PTD. idPersona={}, idCargaDocente={}", idPersonaGeneral, idCargaDocente);

        ObservacionPtdEntity observacionEntity = observacionPtdRepository.findByIdCargaDocente(idCargaDocente);
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


    private void validateCreation(PtdSavePayloadDTO primerRegistro) {
        if (planTrabajoDigitalRepository.existsByIdPersonaGeneralAndIdPeriodoUniversitario(primerRegistro.idPersonaGeneral(), primerRegistro.idPeriodoUniversitario())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El docente ya cuenta con actividades en el PTD para este periodo");
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
}
