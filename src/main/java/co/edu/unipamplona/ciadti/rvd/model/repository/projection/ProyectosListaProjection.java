/**
 * Aplicación: rvd
 * Archivo: ProyectosListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface ProyectosListaProjection {
    Long getId();
    Long getIdConvocatoriaProyectos();
    Long getIdTipoProyecto();
    Long getIdCoordinacion();
    Long getIdProyectoPadre();
    String getNombre();
    String getDescripcion();
    String getMonto();
    Date getFechaInicio();
    Date getFechaFin();
    String getNombreConvocatoriaProyectos();
    String getNombreTipoProyecto();
    String getNombreCoordinacion();
}
