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

public final class FechasConvocatoriaCalculator {

    private static final int MESES_ANTES_REDONDEO = 10;
    private static final int DIAS_MEDIO_MES = 15;
    /** Semanas equivalentes a 11 meses (11 * 52 / 12 ≈ 47.67). */
    private static final int SEMANAS_ONCE_MESES = 48;
    private static final String ESTADO_ACTIVO = "1";
    private static final String ESTADO_INACTIVO = "0";
    private static final String INDICADOR_ONCE_MESES = "1";

    private FechasConvocatoriaCalculator() {
    }

    public static String calcularOnceMeses(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate inicio = fechaInicio;
        LocalDate fin = fechaFin;
        LocalDate limiteOnceMeses = inicio
                .plusMonths(MESES_ANTES_REDONDEO)
                .plusDays(DIAS_MEDIO_MES);
        return fin.isBefore(limiteOnceMeses) ? "0" : "1";
    }

    /**
     * Retorna "1" si las semanas alcanzan 11 meses; de lo contrario null.
     */
    public static String calcularOnceMesesPorSemanas(String semanas) {
        if (semanas == null || semanas.isBlank()) {
            return null;
        }
        try {
            int valor = Integer.parseInt(semanas.trim());
            return valor >= SEMANAS_ONCE_MESES ? INDICADOR_ONCE_MESES : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Activo ("1") si fechaFin es hoy o futura; inactivo ("0") si ya venció.
     */
    public static String resolveEstadoByFechaFin(LocalDate fechaFin) {
        if (fechaFin == null) {
            return ESTADO_ACTIVO;
        }
        LocalDate hoy = LocalDate.now();
        LocalDate fin = fechaFin;
        return fin.isBefore(hoy) ? ESTADO_INACTIVO : ESTADO_ACTIVO;
    }

    public static boolean isVencida(LocalDate fechaFin) {
        return ESTADO_INACTIVO.equals(resolveEstadoByFechaFin(fechaFin));
    }
}

/* 03/06/2026 @:Sebastian Jaimes */
