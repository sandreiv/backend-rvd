/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface PersonaProyectoListaProjection {
    Long getId();
    Long getIdProyecto();
    Long getIdPersonaGeneral();
    String getNombreCompleto();
    Long getIdTipoActividad();
    String getNombreTipoActividad();
    String getTipo();
    String getHoras();
    String getObservacion();
}
