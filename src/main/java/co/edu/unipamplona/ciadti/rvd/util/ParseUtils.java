/**
 * Aplicación: rvd
 * Archivo: ParseUtils.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/09/2026
 * Modificaciones:
 * 10/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.util;

public final class ParseUtils {

    private ParseUtils() {
    }

    public static Long parseNullableLong(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        return Long.valueOf(value.trim());
    }
}
/* 10/09/2026 @:Sebastian Jaimes */
