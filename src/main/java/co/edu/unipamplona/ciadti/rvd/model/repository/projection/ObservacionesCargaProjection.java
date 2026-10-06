/**
 * Aplicación: rvd
 * Archivo: ObservacionesCargaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface ObservacionesCargaProjection {
    
    Long getIdObservacion();
    Long getIdPersonaGeneral();
    String getNombrePersonaGeneral();
    String getRolPersonaGeneral();
    String getObservacion();
    LocalDateTime getFecha();
    Integer getVisto();
}
