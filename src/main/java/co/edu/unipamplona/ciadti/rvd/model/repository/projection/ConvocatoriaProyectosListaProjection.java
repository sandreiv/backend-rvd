/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 29/10/2026 - Andrés Hernández - Rango de fechas para la convocatoria
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDate;

public interface ConvocatoriaProyectosListaProjection {
    Long getId();
    String getNombre();
    String getDescripcion();
    String getCodigo();
    Long getIdConvocatoria();
    String getNombreConvocatoria();
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
}
