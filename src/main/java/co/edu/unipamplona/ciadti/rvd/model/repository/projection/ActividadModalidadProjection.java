/**
 * Aplicación: rvd
 * Archivo: ActividadModalidadProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/07/2026
 * Modificaciones:
 * 29/07/2026 - Creación inicial para listado de actividades por modalidad.
 * 30/07/2026 - Se agrega campo componente.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface ActividadModalidadProjection {

    Long getIdModalidadContratacion();

    Long getIdTipoActividad();

    String getNombreTipoActividad();

    String getCodigoTipoActividad();

    String getEstadoTipoActividad();

    String getComponenteTipoActividad();
}

/* 29/07/2026 */
