/**
 * Aplicación: rvd
 * Archivo: TotalHorasPreasignacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/07/2026
 * Modificaciones:
 * 20/07/2026 - Sebastian Jaimes - Creación inicial
 * 20/07/2026 - Sebastian Jaimes - Horas a BigDecimal
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface TotalHorasPreasignacionProjection {

    Long getIdTipoActividad();

    String getCodigo();

    String getNombre();

    BigDecimal getHoras();
}
