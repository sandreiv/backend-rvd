/**
 * Aplicación: rvd
 * Archivo: HorasProgramaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/08/2026
 * Modificaciones:
 * 06/08/2026 - Sebastian Jaimes: Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface HorasProgramaProjection {

    Long getIdPrograma();

    BigDecimal getTotalHoras();
}
