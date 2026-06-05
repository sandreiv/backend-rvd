package co.edu.unipamplona.ciadti.rvd.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class FechasConvocatoriaCalculator {

    private FechasConvocatoriaCalculator() {
    }

    public static String calcularOnceMeses(Date fechaInicio, Date fechaFin) {
        LocalDate inicio = toLocalDate(fechaInicio);
        LocalDate fin = toLocalDate(fechaFin);
        long meses = ChronoUnit.MONTHS.between(inicio, fin);
        return meses == 11 ? "1" : "0";
    }

    private static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
