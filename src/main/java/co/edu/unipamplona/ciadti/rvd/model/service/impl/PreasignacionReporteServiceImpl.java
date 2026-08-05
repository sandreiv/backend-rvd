/**
 * Aplicación: rvd
 * Archivo: PreasignacionReporteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 04/08/2026 - Sebastian Jaimes - Batch por carga y tablas por modalidad
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EncabezadoCargaReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadPreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PreasignacionExcelFileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ReportePreasignacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EncabezadoPreasignacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasCodigoActividadReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.PreasignacionReporteService;
import co.edu.unipamplona.ciadti.rvd.report.PreasignacionExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreasignacionReporteServiceImpl
        implements PreasignacionReporteService {

    private static final int ESCALA_MONETARIA = 2;
    private static final BigDecimal DIAS_MES = new BigDecimal("30");
    private static final BigDecimal DIAS_ANIO = new BigDecimal("360");
    private static final BigDecimal DIAS_VACACIONES = new BigDecimal("720");
    private static final BigDecimal TASA_INTERES = new BigDecimal("0.12");
    private static final BigDecimal PUNTOS_DOCENTE_DEFAULT =
            new BigDecimal("100");

    private final CargaRepository cargaRepository;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final PreasignacionExcelExporter excelExporter;

    @Override
    @Transactional(readOnly = true)
    public PreasignacionExcelFileDTO generatePreloadReport(Long idCarga) {
        log.debug("generatePreloadReport ===> idCarga={}", idCarga);
        if (idCarga == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id de la carga es obligatorio");
        }

        EncabezadoCargaReporteDTO encabezado = loadEncabezado(idCarga);
        List<DocentePreasignacionReporteProjection> docentes =
                cargaDocenteRepository.findReportProfessorsByCarga(idCarga);
        Map<Long, Map<String, BigDecimal>> horasByDocente =
                loadHorasPorDocente(idCarga);
        List<ModalidadPreasignacionReporteDTO> modalidades =
                buildModalidades(docentes, horasByDocente);

        ReportePreasignacionCargaDTO reporte =
                new ReportePreasignacionCargaDTO(encabezado, modalidades);
        byte[] content = excelExporter.export(reporte);
        String fileName = buildFileName(encabezado);
        log.info(
                "generatePreloadReport ===> idCarga={}, docentes={}, modalidades={}, bytes={}",
                idCarga,
                docentes.size(),
                modalidades.size(),
                content.length);
        return new PreasignacionExcelFileDTO(fileName, content);
    }

    private EncabezadoCargaReporteDTO loadEncabezado(Long idCarga) {
        EncabezadoPreasignacionProjection projection = cargaRepository
                .findEncabezadoReporteByIdCarga(idCarga)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga con id " + idCarga));
        return new EncabezadoCargaReporteDTO(
                projection.getIdCarga(),
                projection.getIdCoordinacion(),
                projection.getUnidad(),
                projection.getFacultad(),
                projection.getCoordinacion(),
                projection.getIdPeriodoUniversidad(),
                projection.getPeriodoAcademico(),
                projection.getIdConvocatoria(),
                projection.getConvocatoria());
    }

    private Map<Long, Map<String, BigDecimal>> loadHorasPorDocente(
            Long idCarga) {
        List<HorasCodigoActividadReporteProjection> rows =
                detalleCargaDocenteRepository
                        .findHorasPorCodigoPadreByCarga(idCarga);
        Map<Long, Map<String, BigDecimal>> result = new HashMap<>();
        for (HorasCodigoActividadReporteProjection row : rows) {
            if (row.getIdCargaDocente() == null
                    || !StringUtils.hasText(row.getCodigoPadre())) {
                continue;
            }
            result
                    .computeIfAbsent(
                            row.getIdCargaDocente(),
                            id -> new HashMap<>())
                    .put(
                            row.getCodigoPadre().trim().toUpperCase(),
                            row.getTotalHoras() != null
                                    ? row.getTotalHoras()
                                    : BigDecimal.ZERO);
        }
        return result;
    }

    private List<ModalidadPreasignacionReporteDTO> buildModalidades(
            List<DocentePreasignacionReporteProjection> docentes,
            Map<Long, Map<String, BigDecimal>> horasByDocente) {
        Map<Long, ModalidadBucket> buckets = new LinkedHashMap<>();
        for (DocentePreasignacionReporteProjection projection : docentes) {
            Long idModalidad = projection.getIdModalidadContratacion();
            String nombre = StringUtils.hasText(
                    projection.getModalidadContratacion())
                    ? projection.getModalidadContratacion()
                    : "Sin modalidad";
            Long key = idModalidad != null ? idModalidad : -1L;
            ModalidadBucket bucket = buckets.computeIfAbsent(
                    key,
                    k -> new ModalidadBucket(idModalidad, nombre));
            bucket.docentes.add(toDocenteReporte(
                    projection,
                    horasByDocente.getOrDefault(
                            projection.getIdCargaDocente(),
                            Map.of())));
        }
        List<ModalidadPreasignacionReporteDTO> result = new ArrayList<>();
        for (ModalidadBucket bucket : buckets.values()) {
            result.add(new ModalidadPreasignacionReporteDTO(
                    bucket.idModalidad,
                    bucket.nombre,
                    List.copyOf(bucket.docentes)));
        }
        return result;
    }

    private DocentePreasignacionReporteDTO toDocenteReporte(
            DocentePreasignacionReporteProjection projection,
            Map<String, BigDecimal> horasPorCodigo) {
        BigDecimal asignacion = null;
        ValorContratacionDTO valor = null;
        String error = null;
        try {
            asignacion = resolveAsignacionSalarial(projection);
            valor = calculateContractValue(projection, asignacion);
        } catch (ApiException ex) {
            error = ex.getMessage();
            log.warn(
                    "toDocenteReporte ===> contrato incompleto idCargaDocente={}, motivo={}",
                    projection.getIdCargaDocente(),
                    error);
        }
        return new DocentePreasignacionReporteDTO(
                projection.getIdCargaDocente(),
                projection.getNombreCompleto(),
                projection.getDocumento(),
                projection.getPuntos(),
                projection.getCategoria(),
                projection.getFechaInicio(),
                projection.getFechaFin(),
                projection.getSemanas(),
                asignacion,
                valor,
                Map.copyOf(horasPorCodigo),
                error);
    }

    private ValorContratacionDTO calculateContractValue(
            DocentePreasignacionReporteProjection projection,
            BigDecimal asignacionSalarial) {
        long cantidadDias = resolveCantidadDias(
                projection.getFechaInicio(),
                projection.getFechaFin());
        BigDecimal dias = BigDecimal.valueOf(cantidadDias);

        BigDecimal valorContrato = asignacionSalarial
                .divide(DIAS_MES, 8, RoundingMode.HALF_UP)
                .multiply(dias)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal valorCesantias = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_ANIO, ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal valorIntereses = valorCesantias
                .multiply(dias)
                .divide(DIAS_ANIO, 8, RoundingMode.HALF_UP)
                .multiply(TASA_INTERES)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal valorPrimaLegal = valorCesantias;
        BigDecimal valorVacaciones = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_VACACIONES, ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal totalPrestaciones = valorCesantias
                .add(valorIntereses)
                .add(valorPrimaLegal)
                .add(valorVacaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal totalContrato = valorContrato
                .add(totalPrestaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        return new ValorContratacionDTO(
                valorVacaciones,
                valorCesantias,
                valorIntereses,
                valorPrimaLegal,
                totalPrestaciones,
                valorContrato,
                totalContrato);
    }

    private BigDecimal resolveAsignacionSalarial(
            DocentePreasignacionReporteProjection projection) {
        if (projection.getSalario() != null) {
            return projection.getSalario();
        }
        BigDecimal valorPunto = projection.getValorPunto();
        if (valorPunto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene asignacion salarial ni valor del punto");
        }
        return resolvePuntos(projection.getPuntos())
                .multiply(valorPunto)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    private BigDecimal resolvePuntos(String puntos) {
        if (!StringUtils.hasText(puntos)) {
            return PUNTOS_DOCENTE_DEFAULT;
        }
        try {
            return new BigDecimal(puntos.trim());
        } catch (NumberFormatException ex) {
            return PUNTOS_DOCENTE_DEFAULT;
        }
    }

    private long resolveCantidadDias(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene fechas de inicio y fin");
        }
        LocalDate inicio = fechaInicio.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate fin = fechaFin.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if (fin.isBefore(inicio)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha fin no puede ser anterior a la fecha inicio");
        }
        return ChronoUnit.DAYS.between(inicio, fin) + 1;
    }

    private String buildFileName(EncabezadoCargaReporteDTO encabezado) {
        String coordinacion = sanitizeFilePart(encabezado.coordinacion());
        String periodo = sanitizeFilePart(encabezado.periodoAcademico());
        String base = "preasignacion-" + coordinacion + "-" + periodo;
        if (!StringUtils.hasText(coordinacion)
                && !StringUtils.hasText(periodo)) {
            base = "preasignacion-carga-" + encabezado.idCarga();
        }
        return base + ".xlsx";
    }

    private String sanitizeFilePart(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.trim()
                .replaceAll("[\\\\/:*?\"<>|]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    private static final class ModalidadBucket {
        private final Long idModalidad;
        private final String nombre;
        private final List<DocentePreasignacionReporteDTO> docentes =
                new ArrayList<>();

        private ModalidadBucket(Long idModalidad, String nombre) {
            this.idModalidad = idModalidad;
            this.nombre = nombre;
        }
    }
}
