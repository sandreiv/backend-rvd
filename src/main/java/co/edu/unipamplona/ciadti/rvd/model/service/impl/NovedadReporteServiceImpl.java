/**
 * Aplicación: rvd
 * Archivo: NovedadReporteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.EncabezadoCargaReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RegistroNovedadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ReporteNovedadCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadReporteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EncabezadoPreasignacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasTipoActividadProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.RegistroNovedadProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadReporteService;
import co.edu.unipamplona.ciadti.rvd.report.NovedadPdfExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadReporteServiceImpl implements NovedadReporteService {

    private static final ZoneId ZONA_BOGOTA = ZoneId.of("America/Bogota");
    private static final DateTimeFormatter FECHA_GENERACION =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final List<String> CODIGOS_ACTIVIDAD =
            List.of("FAD", "FAI", "CTEI", "ISU", "AC");

    private final CargaDocenteRepository cargaDocenteRepository;
    private final NovedadReporteRepository novedadReporteRepository;
    private final NovedadPdfExporter novedadPdfExporter;

    @Override
    @Transactional(readOnly = true)
    public FileDTO generateNoveltyPdfReport(Long idCargaDocente) {
        log.debug("generateNoveltyPdfReport ===> idCargaDocente={}", idCargaDocente);
        ReporteNovedadCargaDTO reporte = buildReport(idCargaDocente);
        byte[] content = novedadPdfExporter.export(reporte);
        String fileName = buildFileName(reporte);
        log.info(
                "generateNoveltyPdfReport ===> PDF generado. idCargaDocente={}, bytes={}",
                idCargaDocente,
                content.length);
        return new FileDTO(fileName, content);
    }

    private ReporteNovedadCargaDTO buildReport(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id de la carga docente es obligatorio");
        }
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la carga docente con id " + idCargaDocente);
        }

        NovedadCargaDocenteEntity actual = novedadReporteRepository
                .findCurrentApprovedNovelty(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe una novedad aprobada y vigente para la carga docente"));

        RegistroNovedadProjection actualSnap = novedadReporteRepository
                .findNovedadSnapshot(actual.getIdNovedadCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No fue posible leer la novedad vigente de la carga docente"));

        RegistroNovedadDTO actualDto = toRegistro(
                actualSnap,
                loadHorasNovedad(
                        actual.getIdNovedadCargaDocente(),
                        idCargaDocente));

        RegistroNovedadDTO anteriorDto = novedadReporteRepository
                .findPreviousApprovedNovelty(
                        idCargaDocente,
                        actual.getIdNovedadCargaDocente())
                .map(this::toRegistroFromNovelty)
                .orElseGet(() -> toRegistroFromCarga(idCargaDocente));

        EncabezadoCargaReporteDTO encabezado = novedadReporteRepository
                .findEncabezadoByIdCargaDocente(idCargaDocente)
                .map(this::toEncabezado)
                .orElse(null);

        return new ReporteNovedadCargaDTO(
                encabezado,
                actualSnap.getTipoNovedad(),
                anteriorDto,
                actualDto,
                resolveGeneradoPor(),
                ZonedDateTime.now(ZONA_BOGOTA).format(FECHA_GENERACION));
    }

    private RegistroNovedadDTO toRegistroFromNovelty(
            NovedadCargaDocenteEntity novedad) {
        RegistroNovedadProjection snap = novedadReporteRepository
                .findNovedadSnapshot(novedad.getIdNovedadCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No fue posible leer la novedad anterior de la carga docente"));
        return toRegistro(
                snap,
                loadHorasNovedad(
                        novedad.getIdNovedadCargaDocente(),
                        novedad.getIdCargaDocente()));
    }

    private RegistroNovedadDTO toRegistroFromCarga(Long idCargaDocente) {
        RegistroNovedadProjection snap = novedadReporteRepository
                .findCargaDocenteSnapshot(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el registro original de la carga docente"));
        return toRegistro(
                snap,
                toHorasMap(novedadReporteRepository.findHorasCargaDocente(idCargaDocente)));
    }

    private RegistroNovedadDTO toRegistro(
            RegistroNovedadProjection snap,
            Map<String, BigDecimal> horasPorTipo) {
        return new RegistroNovedadDTO(
                snap.getNombre(),
                snap.getDocumento(),
                snap.getModalidad(),
                snap.getPuntos(),
                snap.getHorasSemana(),
                snap.getValorContrato(),
                snap.getPrestaciones(),
                snap.getTotalContrato(),
                horasPorTipo);
    }

    private Map<String, BigDecimal> loadHorasNovedad(
            Long idNovedadCargaDocente,
            Long idCargaDocente) {
        List<HorasTipoActividadProjection> horas =
                novedadReporteRepository.findHorasNovedad(idNovedadCargaDocente);
        if (horas == null || horas.isEmpty()) {
            horas = novedadReporteRepository.findHorasCargaDocente(idCargaDocente);
        }
        return toHorasMap(horas);
    }

    private Map<String, BigDecimal> toHorasMap(
            List<HorasTipoActividadProjection> rows) {
        Map<String, BigDecimal> horas = new LinkedHashMap<>();
        for (String codigo : CODIGOS_ACTIVIDAD) {
            horas.put(codigo, BigDecimal.ZERO);
        }
        if (rows == null) {
            return horas;
        }
        for (HorasTipoActividadProjection row : rows) {
            if (!StringUtils.hasText(row.getCodigoPadre())) {
                continue;
            }
            horas.put(
                    row.getCodigoPadre().trim().toUpperCase(),
                    row.getTotalHoras() != null
                            ? row.getTotalHoras()
                            : BigDecimal.ZERO);
        }
        return horas;
    }

    private EncabezadoCargaReporteDTO toEncabezado(
            EncabezadoPreasignacionProjection projection) {
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

    private String resolveGeneradoPor() {
        return SecurityUtils.currentIdPersona()
                .map(novedadReporteRepository::findNombrePersona)
                .filter(StringUtils::hasText)
                .map(String::trim)
                .orElseGet(() -> SecurityUtils.currentUser()
                        .map(AuthUserDetails::getUsername)
                        .filter(StringUtils::hasText)
                        .orElse("Sistema"));
    }

    private String buildFileName(ReporteNovedadCargaDTO reporte) {
        String docente = reporte.actual() != null
                ? sanitizeFilePart(reporte.actual().nombre())
                : "";
        String base = StringUtils.hasText(docente)
                ? "novedad-" + docente
                : "novedad-carga-docente";
        return base + ".pdf";
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
}
