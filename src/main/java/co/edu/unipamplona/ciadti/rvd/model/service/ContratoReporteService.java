/**
 * Aplicación: rvd
 * Archivo: ContratoReporteService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (anexo de contratación en PDF)
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;

public interface ContratoReporteService {

    /**
     * Genera el anexo de contratación del docente.
     *
     * @param idCargaDocente carga docente (CADO_ID)
     * @param tipoForzado    fuerza el tipo de anexo; si llega nulo se decide por
     *                       la regla de negocio (coordinador del programa)
     */
    FileDTO generateContractPdf(Long idCargaDocente, TipoContratoDocente tipoForzado);
}
