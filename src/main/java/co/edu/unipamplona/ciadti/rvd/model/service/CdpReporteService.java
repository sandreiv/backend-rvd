/**
 * Aplicación: rvd
 * Archivo: CdpReporteService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/09/2026
 * Modificaciones:
 * 01/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;

public interface CdpReporteService {

    FileDTO generateCdpReport(
            Long idConvocatoria,
            Long idPeriodoUniversidad);

    FileDTO generateCdpPdfReport(
            Long idConvocatoria,
            Long idPeriodoUniversidad);
}
