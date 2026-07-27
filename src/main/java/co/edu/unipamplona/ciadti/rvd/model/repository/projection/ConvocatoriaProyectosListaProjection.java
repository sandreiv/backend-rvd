/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosListaProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface ConvocatoriaProyectosListaProjection {
    Long getId();
    String getNombre();
    String getDescripcion();
    String getCodigo();
    String getNombreConvocatoria();
}
