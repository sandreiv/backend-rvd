/**
 * Aplicación: rvd
 * Archivo: PreasignacionReporteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 31/08/2026 - Sebastian Jaimes - Valor hora desde puntos vigencia
 * 31/08/2026 - Sebastian Jaimes - Reporte PDF con totales, grupo y cupos
 * 31/08/2026 - Sebastian Jaimes - Grupo como conteo numérico
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import co.edu.unipamplona.ciadti.rvd.model.dto.ReportePreasignacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalesPreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EncabezadoPreasignacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoCuposReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasCodigoActividadReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.PreasignacionReporteService;
import co.edu.unipamplona.ciadti.rvd.report.PreasignacionExcelExporter;
import co.edu.unipamplona.ciadti.rvd.report.PreasignacionPdfExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreasignacionReporteServiceImpl implements PreasignacionReporteService {

    private static final int ESCALA_MONETARIA = 2;
    private static final BigDecimal DIAS_MES = new BigDecimal("30");
    private static final BigDecimal DIAS_ANIO = new BigDecimal("360");
    private static final BigDecimal DIAS_VACACIONES = new BigDecimal("720");
    private static final BigDecimal TASA_INTERES = new BigDecimal("0.12");
    private static final BigDecimal PUNTOS_DOCENTE_DEFAULT = new BigDecimal("100");

    private final CargaRepository cargaRepository;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final PuntosVigenciaRepository puntosVigenciaRepository;
    private final PreasignacionExcelExporter excelExporter;
    private final PreasignacionPdfExporter pdfExporter;

    @Override
    @Transactional(readOnly = true)
    public FileDTO generatePreloadReport(Long idCarga) {
        log.debug("generatePreloadReport ===> idCarga={}", idCarga);
        ReportePreasignacionCargaDTO reporte = buildReport(idCarga);
        byte[] content = excelExporter.export(reporte);
        String fileName = buildFileName(reporte.encabezado(), "xlsx");
        log.info(
                "generatePreloadReport ===> idCarga={}, modalidades={}, bytes={}",
                idCarga,
                reporte.modalidades().size(),
                content.length);
        return new FileDTO(fileName, content);
    }

    @Override
    @Transactional(readOnly = true)
    public FileDTO generatePreloadPdfReport(Long idCarga) {
        log.debug("generatePreloadPdfReport ===> idCarga={}", idCarga);
        ReportePreasignacionCargaDTO reporte = buildReport(idCarga);
        byte[] content = pdfExporter.export(reporte);
        String fileName = buildFileName(reporte.encabezado(), "pdf");
        log.info(
                "generatePreloadPdfReport ===> idCarga={}, modalidades={}, bytes={}",
                idCarga,
                reporte.modalidades().size(),
                content.length);
        return new FileDTO(fileName, content);
    }

    private ReportePreasignacionCargaDTO buildReport(Long idCarga) {
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        EncabezadoCargaReporteDTO encabezado = loadEncabezado(idCarga);
        BigDecimal valorHoraVigencia = resolveValorHoraVigencia(encabezado.anio());
        List<DocentePreasignacionReporteProjection> docentes = cargaDocenteRepository.findReportProfessorsByCarga(idCarga);
        Map<Long, Map<String, BigDecimal>> horasByDocente = loadHorasPorDocente(idCarga);
        Map<Long, GrupoCupos> grupoCuposByDocente = loadGrupoCuposPorDocente(idCarga);
        List<ModalidadPreasignacionReporteDTO> modalidades = buildModalidades(docentes, horasByDocente, grupoCuposByDocente, valorHoraVigencia);
        TotalesPreasignacionReporteDTO resumen = sumModalidades(modalidades);

        return new ReportePreasignacionCargaDTO(encabezado, modalidades, resumen);
    }

    private EncabezadoCargaReporteDTO loadEncabezado(Long idCarga) {
        EncabezadoPreasignacionProjection projection = cargaRepository
                .findEncabezadoReporteByIdCarga(idCarga)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga));
        return new EncabezadoCargaReporteDTO(
                projection.getIdCarga(),
                projection.getIdCoordinacion(),
                projection.getUnidad(),
                projection.getFacultad(),
                projection.getCoordinacion(),
                projection.getIdPeriodoUniversidad(),
                projection.getPeriodoAcademico(),
                projection.getAnio(),
                projection.getIdConvocatoria(),
                projection.getConvocatoria());
    }

    private BigDecimal resolveValorHoraVigencia(Long anio) {
        if (anio == null) {
            log.warn("resolveValorHoraVigencia ===> La carga no tiene año de periodo");
            return null;
        }
        return puntosVigenciaRepository.findByAnio(anio)
                .map(this::parseValorPuntoVigencia)
                .orElseGet(() -> {
                    log.warn(
                            "resolveValorHoraVigencia ===> No hay puntos vigencia. anio={}",
                            anio);
                    return null;
                });
    }

    private BigDecimal parseValorPuntoVigencia(PuntosVigenciaEntity vigencia) {
        if (vigencia == null || !StringUtils.hasText(vigencia.getValorPunto())) {
            return null;
        }
        try {
            return new BigDecimal(vigencia.getValorPunto().trim())
                    .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        } catch (NumberFormatException ex) {
            log.warn(
                    "parseValorPuntoVigencia ===> Valor de punto no numerico. anio={}",
                    vigencia.getAnio());
            return null;
        }
    }

    private Map<Long, Map<String, BigDecimal>> loadHorasPorDocente(Long idCarga) {
        List<HorasCodigoActividadReporteProjection> rows = detalleCargaDocenteRepository.findHorasPorCodigoPadreByCarga(idCarga);
        Map<Long, Map<String, BigDecimal>> result = new HashMap<>();
        for (HorasCodigoActividadReporteProjection row : rows) {
            if (row.getIdCargaDocente() == null
                    || !StringUtils.hasText(row.getCodigoPadre())) {
                continue;
            }
            result.computeIfAbsent(row.getIdCargaDocente(), id -> new HashMap<>())
                    .put(
                            row.getCodigoPadre().trim().toUpperCase(),
                            row.getTotalHoras() != null
                                    ? row.getTotalHoras()
                                    : BigDecimal.ZERO);
        }
        return result;
    }

    private Map<Long, GrupoCupos> loadGrupoCuposPorDocente(Long idCarga) {
        List<GrupoCuposReporteProjection> rows = detalleCargaDocenteRepository.findGruposYCuposByCarga(idCarga);
        Map<Long, GrupoCupos> result = new HashMap<>();
        for (GrupoCuposReporteProjection row : rows) {
            if (row.getIdCargaDocente() == null) {
                continue;
            }
            result.put(
                    row.getIdCargaDocente(),
                    new GrupoCupos(
                            toEntero(row.getCantidadGrupos()),
                            row.getCupos()));
        }
        return result;
    }

    private List<ModalidadPreasignacionReporteDTO> buildModalidades(
            List<DocentePreasignacionReporteProjection> docentes,
            Map<Long, Map<String, BigDecimal>> horasByDocente,
            Map<Long, GrupoCupos> grupoCuposByDocente,
            BigDecimal valorHoraVigencia) {
        Map<Long, ModalidadBucket> buckets = new LinkedHashMap<>();
        for (DocentePreasignacionReporteProjection projection : docentes) {
            Long idModalidad = projection.getIdModalidadContratacion();
            String nombre = StringUtils.hasText(projection.getModalidadContratacion())
                    ? projection.getModalidadContratacion()
                    : "Sin modalidad";
            Long key = idModalidad != null ? idModalidad : -1L;
            ModalidadBucket bucket = buckets.computeIfAbsent(
                    key, k -> new ModalidadBucket(idModalidad, nombre));
            Long idCargaDocente = projection.getIdCargaDocente();
            bucket.docentes.add(toDocenteReporte(
                    projection,
                    horasByDocente.getOrDefault(idCargaDocente, Map.of()),
                    valorHoraVigencia,
                    grupoCuposByDocente.get(idCargaDocente)));
        }
        List<ModalidadPreasignacionReporteDTO> result = new ArrayList<>();
        for (ModalidadBucket bucket : buckets.values()) {
            List<DocentePreasignacionReporteDTO> docentesModalidad =
                    List.copyOf(bucket.docentes);
            result.add(new ModalidadPreasignacionReporteDTO(
                    bucket.idModalidad,
                    bucket.nombre,
                    docentesModalidad,
                    sumDocentes(docentesModalidad)));
        }
        return result;
    }

    private DocentePreasignacionReporteDTO toDocenteReporte(
            DocentePreasignacionReporteProjection projection,
            Map<String, BigDecimal> horasPorCodigo,
            BigDecimal valorHoraVigencia,
            GrupoCupos grupoCupos) {
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
        BigDecimal valorPunto = valorHoraVigencia != null
                ? valorHoraVigencia
                : projection.getValorPunto();
        BigDecimal horas = resolveHoras(projection.getHoras(), horasPorCodigo);
        Integer grupos = grupoCupos != null ? grupoCupos.cantidad() : 0;
        BigDecimal cupos = grupoCupos != null ? grupoCupos.cupos() : null;
        return new DocentePreasignacionReporteDTO(
                projection.getIdCargaDocente(),
                projection.getNombreCompleto(),
                projection.getDocumento(),
                projection.getPuntos(),
                valorPunto,
                projection.getCategoria(),
                projection.getFechaInicio(),
                projection.getFechaFin(),
                projection.getSemanas(),
                horas,
                grupos,
                cupos,
                asignacion,
                projection.getValorHora(),
                valor,
                Map.copyOf(horasPorCodigo),
                error);
    }

    private BigDecimal resolveHoras(
            String horasCarga,
            Map<String, BigDecimal> horasPorCodigo) {
        BigDecimal parsed = parseDecimal(horasCarga);
        if (parsed != null) {
            return parsed;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal valor : horasPorCodigo.values()) {
            if (valor != null) {
                total = total.add(valor);
            }
        }
        return total;
    }

    private TotalesPreasignacionReporteDTO sumDocentes(List<DocentePreasignacionReporteDTO> docentes) {
        int totalDocentes = docentes.size();
        BigDecimal totalHoras = BigDecimal.ZERO;
        BigDecimal totalPrestaciones = BigDecimal.ZERO;
        BigDecimal totalContratos = BigDecimal.ZERO;
        for (DocentePreasignacionReporteDTO docente : docentes) {
            totalHoras = totalHoras.add(nvl(docente.horas()));
            ValorContratacionDTO valor = docente.valorContratacion();
            if (valor != null) {
                totalPrestaciones = totalPrestaciones.add(
                        nvl(valor.totalPrestaciones()));
                totalContratos = totalContratos.add(nvl(valor.valorContrato()));
            }
        }
        return new TotalesPreasignacionReporteDTO(
                totalDocentes,
                totalHoras,
                totalPrestaciones,
                totalContratos,
                totalPrestaciones.add(totalContratos)
                        .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP));
    }

    private TotalesPreasignacionReporteDTO sumModalidades(List<ModalidadPreasignacionReporteDTO> modalidades) {
        int totalDocentes = 0;
        BigDecimal totalHoras = BigDecimal.ZERO;
        BigDecimal totalPrestaciones = BigDecimal.ZERO;
        BigDecimal totalContratos = BigDecimal.ZERO;
        for (ModalidadPreasignacionReporteDTO modalidad : modalidades) {
            TotalesPreasignacionReporteDTO t = modalidad.totales();
            if (t == null) {
                continue;
            }
            totalDocentes += t.totalDocentes();
            totalHoras = totalHoras.add(nvl(t.totalHoras()));
            totalPrestaciones = totalPrestaciones.add(nvl(t.totalPrestaciones()));
            totalContratos = totalContratos.add(nvl(t.totalContratos()));
        }
        return new TotalesPreasignacionReporteDTO(
                totalDocentes,
                totalHoras,
                totalPrestaciones,
                totalContratos,
                totalPrestaciones.add(totalContratos)
                        .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP));
    }

    private ValorContratacionDTO calculateContractValue(
            DocentePreasignacionReporteProjection projection,
            BigDecimal asignacionSalarial) {
        long cantidadDias = resolveCantidadDias(
                projection.getFechaInicio(), projection.getFechaFin());
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

    private BigDecimal resolveAsignacionSalarial(DocentePreasignacionReporteProjection projection) {
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
        BigDecimal parsed = parseDecimal(puntos);
        return parsed != null ? parsed : PUNTOS_DOCENTE_DEFAULT;
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

    private BigDecimal nvl(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private long resolveCantidadDias(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene fechas de inicio y fin");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha fin no puede ser anterior a la fecha inicio");
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    private String buildFileName(
            EncabezadoCargaReporteDTO encabezado,
            String extension) {
        String coordinacion = sanitizeFilePart(encabezado.coordinacion());
        String periodo = sanitizeFilePart(encabezado.periodoAcademico());
        String base = "preasignacion-" + coordinacion + "-" + periodo;
        if (!StringUtils.hasText(coordinacion) && !StringUtils.hasText(periodo)) {
            base = "preasignacion-carga-" + encabezado.idCarga();
        }
        return base + "." + extension;
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

    private Integer toEntero(BigDecimal value) {
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    private record GrupoCupos(Integer cantidad, BigDecimal cupos) {}
}
