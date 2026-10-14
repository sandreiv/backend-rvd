/**
 * Aplicación: rvd
 * Archivo: HiringCallServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Información, parámetros y actividades PTD de contratación
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.InformacionDocenteContratacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ParametrosContratacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadPtdDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleActividadPtdDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ParametrosContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.HiringCallService;
import co.edu.unipamplona.ciadti.rvd.util.ResumenCargaAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HiringCallServiceImpl implements HiringCallService {

    private final CargaDocenteRepository cargaDocenteRepository;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final InformacionDocenteContratacionMapper informacionDocenteContratacionMapper;
    private final ParametrosContratacionMapper parametrosContratacionMapper;
    private final TipoActividadMapper tipoActividadMapper;

    @Override
    @Transactional(readOnly = true)
    public InformacionDocenteContratacionDTO findProfessorInformation(Long idCargaDocente) {
        log.debug(
                "findProfessorInformation ===> Consultando información del docente. idCargaDocente={}",
                idCargaDocente);

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        var projection = cargaDocenteRepository
                .findProfessorInformationByCargaDocente(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));

        List<ActividadHorasResumenDTO> horasActividades =
                listVigenteActivityHours(idCargaDocente);
        InformacionDocenteContratacionDTO result =
                informacionDocenteContratacionMapper.toDto(projection, horasActividades);

        log.info(
                "findProfessorInformation ===> Información del docente consultada. idCargaDocente={}, totalTipos={}",
                idCargaDocente,
                horasActividades.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ParametrosContratacionDTO findHiringParameters(Long idCargaDocente) {
        log.debug(
                "findHiringParameters ===> Consultando parámetros de contratación. idCargaDocente={}",
                idCargaDocente);

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        var projection = cargaDocenteRepository
                .findHiringParametersByCargaDocente(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));

        ParametrosContratacionDTO result = parametrosContratacionMapper.toDto(projection);
        log.info(
                "findHiringParameters ===> Parámetros de contratación consultados. idCargaDocente={}",
                idCargaDocente);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadPtdDTO> listPtdActivities(Long idCargaDocente) {
        log.debug("listPtdActivities ===> Listando actividades para el PTD");

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        List<TipoActividadDTO> actividades = tipoActividadMapper.toDtoList(
                tipoActividadesRepository.findPtdActivityTypes());

        Map<Long, DetalleCargaDocenteListadoProjection> unicos =
                uniqueDetalles(detalleCargaDocenteRepository.findVigenteDetailsByIdCargaDocente(idCargaDocente));

        List<ActividadPtdDTO> result = toPtdActivitiesDto(actividades, unicos);

        log.info("listPtdActivities ===> Tipos de actividades para el PTD listadas. total={}", actividades.size());
        return result;
    }

    private Map<Long, DetalleCargaDocenteListadoProjection> uniqueDetalles(
            List<DetalleCargaDocenteListadoProjection> detalles) {
        Map<Long, DetalleCargaDocenteListadoProjection> unicos = new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection detalle : detalles) {
            unicos.putIfAbsent(detalle.getIdDetalleCargaDocente(), detalle);
        }
        return unicos;
    }

    private List<ActividadHorasResumenDTO> listVigenteActivityHours(Long idCargaDocente) {
        Map<Long, DetalleCargaDocenteListadoProjection> unicos =
                uniqueDetalles(
                        detalleCargaDocenteRepository
                                .findVigenteDetailsByIdCargaDocente(idCargaDocente));
        return ResumenCargaAssembler.buildActivityHours(
                ResumenCargaAssembler.fromCargaList(unicos.values()));
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

    private boolean perteneceActividad(DetalleCargaDocenteListadoProjection detalle, Long idActividad) {
        Long idPadre = detalle.getIdTipoActividadPadre();
        if (idPadre != null) {
            return idPadre.equals(idActividad);
        }
        return Objects.equals(detalle.getIdTipoActividadResuelto(), idActividad);
    }

    private String resolveDescripcionActividadPtd(DetalleCargaDocenteListadoProjection detalle) {
        String codigo = detalle.getCodigoTipoActividadPadre();
        if ("FAD".equalsIgnoreCase(codigo)) {
            return detalle.getNombreMateria() + " - " + detalle.getNombreGrupo();
        }
        return detalle.getNombreProyecto();
    }

    private List<ActividadPtdDTO> toPtdActivitiesDto(
            List<TipoActividadDTO> actividades,
            Map<Long, DetalleCargaDocenteListadoProjection> unicos) {
        return actividades.stream()
            .map(actividad -> {
                if ("PTD".equalsIgnoreCase(actividad.codigo())) {
                    DetalleActividadPtdDTO detallePtd = new DetalleActividadPtdDTO(null, null, actividad.descripcion());
                    return new ActividadPtdDTO(
                        actividad.id(),
                        actividad.codigo(),
                        List.of(detallePtd),
                        actividad.nombre(),
                        actividad.nombre(),
                        null
                    );
                }

                List<DetalleCargaDocenteListadoProjection> detalles = unicos.values().stream()
                    .filter(detalle -> perteneceActividad(detalle, actividad.id()))
                    .toList();

                List<DetalleActividadPtdDTO> detallesDto = detalles.stream()
                    .map(detalle -> new DetalleActividadPtdDTO(
                        detalle.getIdDetalleCargaDocente(),
                        detalle.getEsDeNovedad().orElse(null),
                        resolveDescripcionActividadPtd(detalle)
                    ))
                    .toList();

                BigDecimal totalHoras = detalles.stream()
                    .map(DetalleCargaDocenteListadoProjection::getHoras)
                    .filter(Objects::nonNull)
                    .map(this::parseDecimal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                String criterio = "FAD".equalsIgnoreCase(actividad.codigo())
                    ? detalles.stream()
                        .map(DetalleCargaDocenteListadoProjection::getNombreTipoActividad)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(actividad.nombre())
                    : actividad.nombre();

                return new ActividadPtdDTO(
                    actividad.id(),
                    actividad.codigo(),
                    detallesDto,
                    criterio,
                    actividad.nombre(),
                    totalHoras
                );
            })
        .toList();
    }
}
