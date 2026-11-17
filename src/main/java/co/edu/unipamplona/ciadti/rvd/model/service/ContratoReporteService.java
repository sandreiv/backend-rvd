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

import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;

public interface ContratoReporteService {

    /**
     * Genera el anexo de contratación del docente.
     *
     * @param idCargaDocente carga docente (CADO_ID)
     * @param numeroPeriodo  número del periodo en que se encuentra (1 o 2)
     * @param operacion      opearción de generación de contrato y número de anexo, o
     *                       vinculación del docente en base de datos
     * @param tipoForzado    fuerza el tipo de anexo; si llega nulo se decide por
     *                       la regla de negocio (coordinador del programa)
     * @param body           número de anexo, fecha de ingreso, objeto, descripción,
     *                       forma de pago, tipo de objeto y forma de inicio de contrato
     */
    FileDTO generateContractPdf(
            Long idCargaDocente,
            String numeroPeriodo,
            String operacion,
            TipoContratoDocente tipoForzado,
            DatosAnexoDTO body);
}
