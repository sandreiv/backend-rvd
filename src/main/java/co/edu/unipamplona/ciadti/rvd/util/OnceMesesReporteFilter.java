/**
 * Aplicación: rvd
 * Archivo: OnceMesesReporteFilter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/09/2026
 * Modificaciones:
 * 04/09/2026 - Exclusion de once meses heredados en segundo periodo
 */
package co.edu.unipamplona.ciadti.rvd.util;

public final class OnceMesesReporteFilter {

    private static final String INDICADOR_ONCE_MESES = "1";
    private static final String PERIODO_SEGUNDO = "2";

    private OnceMesesReporteFilter() {
    }

    /**
     * En el segundo periodo del año los docentes once meses ya se contaron
     * en los reportes del primer periodo (fueron heredados).
     */
    public static boolean excludeInheritedInSecondPeriod(
            String onceMeses,
            String periodoUniversidad) {
        return isOnceMeses(onceMeses) && isSegundoPeriodo(periodoUniversidad);
    }

    public static boolean isOnceMeses(String onceMeses) {
        return INDICADOR_ONCE_MESES.equals(trimToNull(onceMeses));
    }

    public static boolean isSegundoPeriodo(String periodoUniversidad) {
        return PERIODO_SEGUNDO.equals(trimToNull(periodoUniversidad));
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
