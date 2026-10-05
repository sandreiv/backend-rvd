/**
 * Aplicación: rvd
 * Archivo: PersonaCoordinacionListadoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface PersonaCoordinacionListadoProjection {
    Long getIdPersonaGeneral();
    String getPersona();
    String getDocumentoIdentidad();
    Long getIdCoordinacion();
    String getCoordinacion();
    String getEstado();
}