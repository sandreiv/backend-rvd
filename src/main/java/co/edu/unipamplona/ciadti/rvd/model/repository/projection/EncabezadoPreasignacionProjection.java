/**
 * Aplicación: rvd
 * Archivo: EncabezadoPreasignacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 04/09/2026 - PEUN_PERIODO para filtrar once meses en segundo periodo
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

    String getPeriodo();

    Long getAnio();

    Long getIdConvocatoria();

    String getConvocatoria();
}
