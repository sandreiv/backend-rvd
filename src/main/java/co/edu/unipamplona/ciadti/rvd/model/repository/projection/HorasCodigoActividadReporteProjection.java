/**
 * Aplicación: rvd
 * Archivo: HorasCodigoActividadReporteProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface HorasCodigoActividadReporteProjection {

    Long getIdCargaDocente();

    String getCodigoPadre();

    BigDecimal getTotalHoras();
}
