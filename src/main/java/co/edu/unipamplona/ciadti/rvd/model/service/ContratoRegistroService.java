/**
 * Aplicación: rvd
 * Archivo: ContratoRegistroService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial (registro del contrato al diligenciar el anexo)
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.math.BigDecimal;

import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;

public interface ContratoRegistroService {

    /**
     * Registra el contrato del docente en CONTRATOS.CONTRATO cuando el usuario
     * diligencian el anexo. Si ya existe un contrato con el mismo número de
     * anexo se actualiza en vez de duplicarlo.
     *
     * @param informacion        datos del docente y de la carga (misma fuente del anexo)
     * @param valorTotalContrato total del contrato; si llega nulo se usa el de la carga
     * @param idCdp              TESORERIAV3.CDP.CDP_ID del CDP de la coordinación y periodo
     * @param datos              número de anexo, fecha de ingreso, forma de pago y tipo de objeto
     * @return id del contrato registrado (CONT_ID)
     */
    Long registrarContrato(
            InformacionDocenteContratacionDTO informacion,
            BigDecimal valorTotalContrato,
            Long idCdp,
            DatosAnexoDTO datos);
}
