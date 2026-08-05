/**
 * Aplicación: rvd
 * Archivo: EncabezadoPreasignacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface EncabezadoPreasignacionProjection {

    Long getIdCarga();

    Long getIdCoordinacion();

    String getUnidad();

    String getFacultad();

    String getCoordinacion();

    Long getIdPeriodoUniversidad();

    String getPeriodoAcademico();

    Long getIdConvocatoria();

    String getConvocatoria();
}
