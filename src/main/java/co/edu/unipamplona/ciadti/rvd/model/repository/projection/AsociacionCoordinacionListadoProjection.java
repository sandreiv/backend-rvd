/**
 * Aplicación: rvd
 * Archivo: AsociacionCoordinacionListadoProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface AsociacionCoordinacionListadoProjection {
    Long getId();
    Long getIdCoordinacion();
    String getCoordinacion();
    Long getIdPrograma();
    String getPrograma();
    String getCodigoMateria();
    String getMateria();
    Long getIdCentroCosto();
    String getCentroCosto();
    String getEstado();
}