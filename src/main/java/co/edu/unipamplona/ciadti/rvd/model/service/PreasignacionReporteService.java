/**
 * Aplicación: rvd
 * Archivo: PreasignacionReporteService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 31/08/2026 - Sebastian Jaimes - Reporte PDF de preasignación
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;

public interface PreasignacionReporteService {

    FileDTO generatePreloadReport(Long idCarga);

    FileDTO generatePreloadPdfReport(Long idCarga);
}
