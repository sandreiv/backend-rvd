/**
 * Aplicación: rvd
 * Archivo: DetallesCdpReporteProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;

public interface DetallesCdpReporteProjection {

    Long getIdCargaDocente();

    Long getIdDetalle();

    BigDecimal getHoras();

    Long getIdTipoActividad();

    Long getIdPrograma();

    Long getIdGrupo();

    Long getIdCentroCosto();

    String getCodigoPadre();

    BigDecimal getCupos();
}
