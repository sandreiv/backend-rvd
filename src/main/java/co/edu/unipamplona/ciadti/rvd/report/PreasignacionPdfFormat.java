/**
 * Aplicación: rvd
 * Archivo: PreasignacionPdfFormat.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 31/08/2026
 * Modificaciones:
 * 31/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionReporteDTO;

public class PreasignacionPdfFormat {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat MONEY;
    private static final DecimalFormat NUMBER;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(
                Locale.forLanguageTag("es-CO"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        MONEY = new DecimalFormat("$#,##0.00", symbols);
        NUMBER = new DecimalFormat("#,##0.00", symbols);
        MONEY.setParseBigDecimal(true);
        NUMBER.setParseBigDecimal(true);
    }

    public String text(String value) {
        return value != null ? value : "";
    }

    public String date(LocalDate value) {
        return value != null ? value.format(DATE_FORMAT) : "";
    }

    public String money(BigDecimal value) {
        return value != null ? MONEY.format(value) : "";
    }

    public String number(BigDecimal value) {
        return value != null ? NUMBER.format(value) : "";
    }

    public String entero(Number value) {
        if (value == null) {
            return "0";
        }
        return String.valueOf(value.longValue());
    }

    public String horasCodigo(
            DocentePreasignacionReporteDTO docente,
            String codigo) {
        if (docente == null || docente.horasPorCodigo() == null) {
            return "";
        }
        Map<String, BigDecimal> horas = docente.horasPorCodigo();
        return number(horas.get(codigo));
    }
}
