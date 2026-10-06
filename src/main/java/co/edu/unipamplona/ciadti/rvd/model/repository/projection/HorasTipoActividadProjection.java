/**
 * Aplicación: rvd
 * Archivo: HorasTipoActividadProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (horas por tipo de actividad)
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface HorasTipoActividadProjection {

    String getCodigoPadre();

    BigDecimal getTotalHoras();
}
