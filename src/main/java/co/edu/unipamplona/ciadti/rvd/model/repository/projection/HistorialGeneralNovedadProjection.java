/**
 * Aplicación: rvd
 * Archivo: HistorialGeneralNovedadProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 07/10/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface HistorialGeneralNovedadProjection {

    Long getIdNovedadCargaDocente();

    Long getIdCargaDocente();

    Long getIdPersonaGeneral();

    String getNombreDocente();

    Long getIdNovedadCatalogo();

    String getTipoNovedad();

    LocalDateTime getFecha();

    String getEstadoNovedad();

    String getMotivoRechazo();
}