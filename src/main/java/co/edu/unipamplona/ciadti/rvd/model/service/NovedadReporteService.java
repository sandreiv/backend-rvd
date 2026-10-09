/**
 * Aplicación: rvd
 * Archivo: NovedadReporteService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial
 * 08/10/2026 - Daniel Arias - Modificación : PDFHISTORICO
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;

public interface NovedadReporteService {

    /**
     * Genera el reporte de la novedad aprobada vigente.
     * Entrada utilizada por la funcionalidad existente.
     *
     * @param idCargaDocente CADO_ID.
     */
    FileDTO generateNoveltyPdfReport(
            Long idCargaDocente
    );

    /**
     * Genera el reporte de una novedad aprobada histórica
     * seleccionada por su NOCD_ID.
     *
     * @param idNovedadCargaDocente NOCD_ID.
     */
    FileDTO generateHistoricalNoveltyPdfReport(
            Long idNovedadCargaDocente
    );

}
