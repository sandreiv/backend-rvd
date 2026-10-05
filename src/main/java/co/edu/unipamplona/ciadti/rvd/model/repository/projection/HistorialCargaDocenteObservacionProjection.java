/**
 * Aplicación: rvd
 * Archivo: HistorialCargaDocenteObservacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface HistorialCargaDocenteObservacionProjection {

    Long getIdHistorial();

    Long getIdPersonaGeneral();

    String getNombrePersonaGeneral();

    String getRolPersonaGeneral();

    String getObservacion();

    LocalDateTime getFecha();

    String getEstado();
}