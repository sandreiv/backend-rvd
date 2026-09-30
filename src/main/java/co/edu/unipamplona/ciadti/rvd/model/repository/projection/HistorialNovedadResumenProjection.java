/**
 * Aplicación: rvd
 * Archivo: HistorialNovedadResumenProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Historial de novedades del resumen
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface HistorialNovedadResumenProjection {

    Long getIdNovedad();

    String getTipo();

    String getDescripcion();

    String getAccion();

    String getComponente();

    LocalDateTime getFecha();

    String getEstadoNovedad();

    String getVigente();
}
