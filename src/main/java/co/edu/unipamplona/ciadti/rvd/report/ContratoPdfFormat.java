/**
 * Aplicación: rvd
 * Archivo: ContratoPdfFormat.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (formato del anexo de contratación)
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formato de los valores que imprime el anexo de contratación.
 * Se inyecta en la plantilla como ${fmt}.
 */
public class ContratoPdfFormat {

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FECHA_LARGA =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", LOCALE_ES);
    private static final DecimalFormat ENTERO;
    private static final DecimalFormat UN_DECIMAL;

    private static final String[] UNIDADES = {
        "", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE",
        "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE",
        "DIECIOCHO", "DIECINUEVE", "VEINTE", "VEINTIUNO", "VEINTIDÓS", "VEINTITRÉS",
        "VEINTICUATRO", "VEINTICINCO", "VEINTISÉIS", "VEINTISIETE", "VEINTIOCHO",
        "VEINTINUEVE"
    };
    private static final String[] DECENAS = {
        "", "", "", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA",
        "OCHENTA", "NOVENTA"
    };
    private static final String[] CENTENAS = {
        "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS",
        "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
    };

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE_ES);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        ENTERO = new DecimalFormat("#,##0", symbols);
        UN_DECIMAL = new DecimalFormat("#,##0.0", symbols);
        ENTERO.setParseBigDecimal(true);
        UN_DECIMAL.setParseBigDecimal(true);
    }

    public String text(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    public String fecha(LocalDate value) {
        return value == null ? "" : value.format(FECHA);
    }

    public String fechaLarga(LocalDate value) {
        return value == null ? "" : value.format(FECHA_LARGA);
    }

    public String numero(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number number) {
            return ENTERO.format(number);
        }
        return String.valueOf(value);
    }

    public String decimal(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number number) {
            return UN_DECIMAL.format(number);
        }
        return String.valueOf(value);
    }

    public String pesos(Object value) {
        String numero = numero(value);
        return numero.isBlank() ? "" : "$" + numero;
    }

    /**
     * Valor en letras con el formato del anexo:
     * "CUATRO MILLONES ... Pesos M/CTE ($4.426.884)".
     */
    public String letras(Object value) {
        if (value == null) {
            return "";
        }
        BigDecimal monto = value instanceof BigDecimal decimal
                ? decimal
                : new BigDecimal(String.valueOf(value));
        long entero = monto.setScale(0, java.math.RoundingMode.HALF_UP).longValue();
        return enLetras(entero) + " Pesos M/CTE (" + pesos(monto) + ")";
    }

    private String enLetras(long numero) {
        if (numero == 0) {
            return "CERO";
        }
        if (numero == 100) {
            return "CIEN";
        }
        StringBuilder texto = new StringBuilder();
        long resto = numero;
        if (resto >= 1_000_000L) {
            long millones = resto / 1_000_000L;
            texto.append(millones == 1
                    ? "UN MILLÓN"
                    : enLetras(millones) + " MILLONES");
            resto %= 1_000_000L;
            if (resto > 0) {
                texto.append(' ');
            }
        }
        if (resto >= 1_000L) {
            long miles = resto / 1_000L;
            texto.append(miles == 1 ? "MIL" : enLetras(miles) + " MIL");
            resto %= 1_000L;
            if (resto > 0) {
                texto.append(' ');
            }
        }
        if (resto > 0) {
            texto.append(enLetrasCentenas((int) resto));
        }
        String resultado = texto.toString().trim();
        if (resultado.endsWith("UNO")) {
            resultado = resultado.substring(0, resultado.length() - 3) + "UN";
        }
        return resultado;
    }

    private String enLetrasCentenas(int numero) {
        if (numero == 100) {
            return "CIEN";
        }
        StringBuilder texto = new StringBuilder();
        int centenas = numero / 100;
        int resto = numero % 100;
        if (centenas > 0) {
            texto.append(CENTENAS[centenas]);
        }
        if (resto > 0) {
            if (texto.length() > 0) {
                texto.append(' ');
            }
            if (resto < 30) {
                texto.append(UNIDADES[resto]);
            } else {
                int decenas = resto / 10;
                int unidades = resto % 10;
                texto.append(DECENAS[decenas]);
                if (unidades > 0) {
                    texto.append(" Y ").append(UNIDADES[unidades]);
                }
            }
        }
        return texto.toString();
    }
}
