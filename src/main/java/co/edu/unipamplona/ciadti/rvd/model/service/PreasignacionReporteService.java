/**
 * Aplicación: rvd
 * Archivo: PreasignacionReporteService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.PreasignacionExcelFileDTO;

public interface PreasignacionReporteService {

    PreasignacionExcelFileDTO generatePreloadReport(Long idCarga);
}
