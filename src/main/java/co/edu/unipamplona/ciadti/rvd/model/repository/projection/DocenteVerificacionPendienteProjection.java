/**
 * Aplicación: rvd
 * Archivo: DocenteVerificacionPendienteProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/09/2026
 * Modificaciones:
 * 07/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface DocenteVerificacionPendienteProjection {

    Long getIdCargaDocente();

    String getNombreCompleto();

    Long getIdPeriodoUniversidad();

    Long getIdConvocatoria();

    Long getIdCoordinacion();

    String getNombreCoordinacion();
}
