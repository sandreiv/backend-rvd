/**
 * Aplicación: rvd
 * Archivo: ResumenCargaAssembler.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Armado compartido de horas y centros de costo
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadDirectaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleNovedadResumenProjection;

public final class ResumenCargaAssembler {

    private static final int ESCALA_MONETARIA = 2;
    private static final int ESCALA_PORCENTAJE = 2;
    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final String CODIGO_FAD = "FAD";
    private static final String CENTRO_SIN_NOMBRE = "Sin nombre";

    private ResumenCargaAssembler() {
    }

    public record DetalleResumen(
            Long idDetalle,
            String horas,
            String codigoTipoActividad,
            String codigoTipoActividadPadre,
            String nombreTipoActividad,
            String nombreTipoActividadPadre,
            String nombreUnidad,
            String nombrePrograma,
            String nombreMateria,
            String nombreGrupo,
            Long idCentroCosto,
            String descripcionCentroCosto) {
    }

    public static DetalleResumen fromCarga(
            DetalleCargaDocenteListadoProjection detalle) {
        return new DetalleResumen(
                detalle.getIdDetalleCargaDocente(),
                detalle.getHoras(),
                detalle.getCodigoTipoActividad(),
                detalle.getCodigoTipoActividadPadre(),
                detalle.getNombreTipoActividad(),
                detalle.getNombreTipoActividadPadre(),
                detalle.getNombreUnidadRegional(),
                detalle.getNombrePrograma(),
                detalle.getNombreMateria(),
                detalle.getNombreGrupo(),
                detalle.getIdCentroCosto(),
                detalle.getDescripcionCentroCosto());
    }

    public static DetalleResumen fromNovedad(
            DetalleNovedadResumenProjection detalle) {
        return new DetalleResumen(
                detalle.getIdDetalleNovedadCargaDocente(),
                detalle.getHoras(),
                detalle.getCodigoTipoActividad(),
                detalle.getCodigoTipoActividadPadre(),
                detalle.getNombreTipoActividad(),
                detalle.getNombreTipoActividadPadre(),
                detalle.getNombreUnidadRegional(),
                detalle.getNombrePrograma(),
                detalle.getNombreMateria(),
                detalle.getNombreGrupo(),
                detalle.getIdCentroCosto(),
                detalle.getDescripcionCentroCosto());
    }

    public static List<DetalleResumen> fromCargaList(
            Collection<DetalleCargaDocenteListadoProjection> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return List.of();
        }
        List<DetalleResumen> result = new ArrayList<>();
        for (DetalleCargaDocenteListadoProjection detalle : detalles) {
            result.add(fromCarga(detalle));
        }
        return unique(result);
    }

    public static List<DetalleResumen> fromNovedadList(
            List<DetalleNovedadResumenProjection> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return List.of();
        }
        List<DetalleResumen> result = new ArrayList<>();
        for (DetalleNovedadResumenProjection detalle : detalles) {
            result.add(fromNovedad(detalle));
        }
        return unique(result);
    }

    public static List<ActividadHorasResumenDTO> buildActivityHours(
            List<DetalleResumen> detalles) {
        Map<String, ActividadHorasAcumulado> acumulados =
                new LinkedHashMap<>();
        for (DetalleResumen detalle : safeList(detalles)) {
            accumulateActivity(acumulados, detalle);
        }
        return toActivityHours(acumulados);
    }

    public static List<CentroCostoResumenDTO> buildCostCenters(
            List<DetalleResumen> detalles,
            BigDecimal totalContrato) {
        Map<Long, CentroCostoAcumulado> acumulados =
                new LinkedHashMap<>();
        BigDecimal totalHoras = BigDecimal.ZERO;
        for (DetalleResumen detalle : safeList(detalles)) {
            totalHoras = totalHoras.add(
                    addCostCenter(acumulados, detalle));
        }
        return toCostCenters(acumulados, totalHoras, totalContrato);
    }

    public static BigDecimal sumHoras(List<DetalleResumen> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleResumen detalle : safeList(detalles)) {
            total = total.add(parseHoras(detalle.horas()));
        }
        return total;
    }

    public static BigDecimal parseHoras(String horas) {
        if (horas == null || horas.isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(horas.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private static List<DetalleResumen> unique(
            List<DetalleResumen> detalles) {
        Map<Long, DetalleResumen> unicos = new LinkedHashMap<>();
        List<DetalleResumen> sinId = new ArrayList<>();
        for (DetalleResumen detalle : detalles) {
            addUnique(unicos, sinId, detalle);
        }
        List<DetalleResumen> result = new ArrayList<>(unicos.values());
        result.addAll(sinId);
        return result;
    }

    private static void addUnique(
            Map<Long, DetalleResumen> unicos,
            List<DetalleResumen> sinId,
            DetalleResumen detalle) {
        if (detalle.idDetalle() == null) {
            sinId.add(detalle);
            return;
        }
        unicos.putIfAbsent(detalle.idDetalle(), detalle);
    }

    private static void accumulateActivity(
            Map<String, ActividadHorasAcumulado> acumulados,
            DetalleResumen detalle) {
        String codigo = resolveCodigo(detalle);
        String nombre = resolveNombre(detalle);
        ActividadHorasAcumulado actual = acumulados.computeIfAbsent(
                codigo + "|" + nombre,
                key -> new ActividadHorasAcumulado(
                        resolveTipo(detalle), codigo, nombre));
        BigDecimal horas = parseHoras(detalle.horas());
        actual.totalHoras = actual.totalHoras.add(horas);
        if (isActividadDirecta(detalle)) {
            actual.detalles.add(toDirectaDetalle(detalle, horas));
        }
    }

    private static List<ActividadHorasResumenDTO> toActivityHours(
            Map<String, ActividadHorasAcumulado> acumulados) {
        List<ActividadHorasResumenDTO> resultado = new ArrayList<>();
        for (ActividadHorasAcumulado item : acumulados.values()) {
            List<ActividadDirectaDetalleDTO> detalles =
                    item.detalles.isEmpty()
                            ? null
                            : List.copyOf(item.detalles);
            resultado.add(new ActividadHorasResumenDTO(
                    item.tipo,
                    item.codigo,
                    item.nombre,
                    item.totalHoras,
                    detalles));
        }
        return resultado;
    }

    private static BigDecimal addCostCenter(
            Map<Long, CentroCostoAcumulado> acumulados,
            DetalleResumen detalle) {
        BigDecimal horas = parseHoras(detalle.horas());
        if (detalle.idCentroCosto() == null) {
            return horas;
        }
        CentroCostoAcumulado actual = acumulados.computeIfAbsent(
                detalle.idCentroCosto(),
                key -> new CentroCostoAcumulado(
                        detalle.idCentroCosto(),
                        resolveNombreCentro(detalle)));
        actual.numeroActividades++;
        actual.totalHoras = actual.totalHoras.add(horas);
        return horas;
    }

    private static List<CentroCostoResumenDTO> toCostCenters(
            Map<Long, CentroCostoAcumulado> acumulados,
            BigDecimal totalHoras,
            BigDecimal totalContrato) {
        BigDecimal contrato = totalContrato != null
                ? totalContrato
                : BigDecimal.ZERO;
        List<CentroCostoResumenDTO> resultado = new ArrayList<>();
        for (CentroCostoAcumulado item : acumulados.values()) {
            resultado.add(toCostCenter(item, totalHoras, contrato));
        }
        return resultado;
    }

    private static CentroCostoResumenDTO toCostCenter(
            CentroCostoAcumulado item,
            BigDecimal totalHoras,
            BigDecimal contrato) {
        BigDecimal porcentaje = calcularPorcentaje(
                item.totalHoras, totalHoras);
        BigDecimal valorAsignado = contrato
                .multiply(porcentaje)
                .divide(CIEN, ESCALA_MONETARIA, RoundingMode.HALF_UP);
        return new CentroCostoResumenDTO(
                item.idCentroCosto,
                item.nombre,
                item.numeroActividades,
                item.totalHoras,
                porcentaje,
                valorAsignado);
    }

    private static String resolveCodigo(DetalleResumen detalle) {
        return firstNonBlank(
                detalle.codigoTipoActividad(),
                detalle.codigoTipoActividadPadre());
    }

    private static String resolveNombre(DetalleResumen detalle) {
        return firstNonBlank(
                detalle.nombreTipoActividad(),
                detalle.nombreTipoActividadPadre());
    }

    private static String resolveTipo(DetalleResumen detalle) {
        return firstNonBlank(
                detalle.nombreTipoActividadPadre(),
                detalle.nombreTipoActividad());
    }

    private static boolean isActividadDirecta(DetalleResumen detalle) {
        if (isCodigoDirecta(detalle.codigoTipoActividad())
                || isCodigoDirecta(detalle.codigoTipoActividadPadre())) {
            return true;
        }
        return containsDirecta(resolveTipo(detalle))
                || containsDirecta(resolveNombre(detalle));
    }

    private static boolean isCodigoDirecta(String codigo) {
        return codigo != null
                && CODIGO_FAD.equalsIgnoreCase(codigo);
    }

    private static boolean containsDirecta(String valor) {
        return hasText(valor)
                && valor.toLowerCase().contains("directa");
    }

    private static ActividadDirectaDetalleDTO toDirectaDetalle(
            DetalleResumen detalle,
            BigDecimal horas) {
        return new ActividadDirectaDetalleDTO(
                detalle.nombreUnidad(),
                detalle.nombrePrograma(),
                detalle.nombreMateria(),
                detalle.nombreGrupo(),
                horas);
    }

    private static String resolveNombreCentro(DetalleResumen detalle) {
        if (hasText(detalle.descripcionCentroCosto())) {
            return detalle.descripcionCentroCosto();
        }
        return CENTRO_SIN_NOMBRE;
    }

    private static BigDecimal calcularPorcentaje(
            BigDecimal horasCentro,
            BigDecimal totalHoras) {
        if (totalHoras.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(ESCALA_PORCENTAJE);
        }
        return horasCentro
                .multiply(CIEN)
                .divide(totalHoras, ESCALA_PORCENTAJE, RoundingMode.HALF_UP);
    }

    private static String firstNonBlank(String primary, String fallback) {
        if (hasText(primary)) {
            return primary;
        }
        return fallback;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static List<DetalleResumen> safeList(
            List<DetalleResumen> detalles) {
        return detalles == null ? List.of() : detalles;
    }

    private static final class ActividadHorasAcumulado {
        private final String tipo;
        private final String codigo;
        private final String nombre;
        private BigDecimal totalHoras = BigDecimal.ZERO;
        private final List<ActividadDirectaDetalleDTO> detalles =
                new ArrayList<>();

        private ActividadHorasAcumulado(
                String tipo, String codigo, String nombre) {
            this.tipo = tipo;
            this.codigo = codigo;
            this.nombre = nombre;
        }
    }

    private static final class CentroCostoAcumulado {
        private final Long idCentroCosto;
        private final String nombre;
        private long numeroActividades;
        private BigDecimal totalHoras = BigDecimal.ZERO;

        private CentroCostoAcumulado(Long idCentroCosto, String nombre) {
            this.idCentroCosto = idCentroCosto;
            this.nombre = nombre;
        }
    }
}
