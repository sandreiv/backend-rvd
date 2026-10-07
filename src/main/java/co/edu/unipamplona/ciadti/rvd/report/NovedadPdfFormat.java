/**
 * Aplicación: rvd
 * Archivo: NovedadPdfFormat.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad)
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

/**
 * Formato de valores (moneda y números) para el PDF de novedad.
 */
public class NovedadPdfFormat {

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

    public String money(BigDecimal value) {
        return value != null ? MONEY.format(value) : "";
    }

    public String number(BigDecimal value) {
        return value != null ? NUMBER.format(value) : "";
    }

    public String horas(Map<String, BigDecimal> horasPorTipo, String codigo) {
        if (horasPorTipo == null) {
            return number(BigDecimal.ZERO);
        }
        BigDecimal valor = horasPorTipo.get(codigo);
        return number(valor != null ? valor : BigDecimal.ZERO);
    }
}
