/**
 * Aplicación: rvd
 * Archivo: FechasConvocatoriaCalculator.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/06/2026
 * Modificaciones:
 * 03/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class FechasConvocatoriaCalculator {

    private static final int MESES_ANTES_REDONDEO = 10;
    private static final int DIAS_MEDIO_MES = 15;
    private static final String ESTADO_ACTIVO = "1";
    private static final String ESTADO_INACTIVO = "0";

    private FechasConvocatoriaCalculator() {
    }

    public static String calcularOnceMeses(Date fechaInicio, Date fechaFin) {
        LocalDate inicio = toLocalDate(fechaInicio);
        LocalDate fin = toLocalDate(fechaFin);
        LocalDate limiteOnceMeses = inicio
                .plusMonths(MESES_ANTES_REDONDEO)
                .plusDays(DIAS_MEDIO_MES);
        return fin.isBefore(limiteOnceMeses) ? "0" : "1";
    }

    /**
     * Activo ("1") si fechaFin es hoy o futura; inactivo ("0") si ya venció.
     */
    public static String resolveEstadoByFechaFin(Date fechaFin) {
        if (fechaFin == null) {
            return ESTADO_ACTIVO;
        }
        LocalDate hoy = LocalDate.now(ZoneId.systemDefault());
        LocalDate fin = toLocalDate(fechaFin);
        return fin.isBefore(hoy) ? ESTADO_INACTIVO : ESTADO_ACTIVO;
    }

    public static boolean isVencida(Date fechaFin) {
        return ESTADO_INACTIVO.equals(resolveEstadoByFechaFin(fechaFin));
    }

    private static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

/* 03/06/2026 @:Sebastian Jaimes */
