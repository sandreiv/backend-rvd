/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaEstadoService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

public interface ConvocatoriaEstadoService {

    void syncEstadoConvocatoria(Long idConvocatoria);

    void syncEstadoConvocatoriaByFecha(Long idFechasConvocatoria);

    void syncEstadosConvocatoriasConRestricciones();
}