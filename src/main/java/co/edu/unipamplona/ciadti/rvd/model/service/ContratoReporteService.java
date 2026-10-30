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

import java.time.LocalDate;

import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;

public interface ContratoReporteService {

    /**
     * Genera el anexo de contratación del docente.
     *
     * @param idCargaDocente carga docente (CADO_ID)
     * @param tipoForzado    fuerza el tipo de anexo; si llega nulo se decide por
     *                       la regla de negocio (coordinador del programa)
     * @param numeroAnexo    número de anexo diligenciado por el usuario; puede ser nulo
     * @param fechaIngreso   fecha de ingreso diligenciada por el usuario; puede ser nula
     */
    FileDTO generateContractPdf(
            Long idCargaDocente,
            TipoContratoDocente tipoForzado,
            String numeroAnexo,
            LocalDate fechaIngreso);
}
