/**
 * Aplicación: rvd
 * Archivo: ContratoRegistroService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial (registro del contrato al diligenciar el anexo)
 * 18/11/2026 - Juan Diego Florez Vera - Separación entre generar el contrato y vincular al docente
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;

public interface ContratoRegistroService {

    /**
     * Datos del anexo del contrato ya generado por el docente, para poder
     * mostrarlos al diligenciar la vinculación sin volver a capturarlos.
     *
     * @param idCargaDocente id de la carga docente del docente
     * @return los datos del anexo, o null si el docente todavía no tiene contrato generado
     */
    ContratoAnexoDTO findAnexoGenerado(Long idCargaDocente);

    /**
     * Datos del anexo de los contratos ya generados de varias cargas docentes.
     * Es la consulta que usa la vinculación masiva: devuelve una lista con un
     * elemento por carga docente que ya tiene contrato generado. Las cargas
     * docentes sin contrato generado no aparecen en la lista.
     *
     * @param idsCargaDocente ids de las cargas docentes consultadas
     * @return lista de datos del anexo, uno por carga docente con contrato generado
     */
    List<ContratoAnexoDTO> findAnexosGenerados(List<Long> idsCargaDocente);

    /**
     * Registra los datos del contrato del docente en CONTRATOS.CONTRATO cuando
     * el usuario diligencia los datos del anexo. Es el paso de generación del
     * contrato: aquí se guardan el número de anexo, la fecha de firma, la forma
     * de pago, el tipo de objeto, la forma de inicio del contrato, el objeto y
     * su descripción. No genera ninguna tabla de vinculación docente.
     *
     * <p>Si ya existe un contrato del mismo docente con el mismo número de
     * anexo y la misma fecha de inicio se actualiza en vez de duplicarlo.</p>
     *
     * @param informacion         datos del docente y de la carga (misma fuente del anexo)
     * @param idCargaDocente      id de la carga docente del docente
     * @param valorTotalContrato  total del contrato; si llega nulo se usa el de la carga
     * @param idCdp               TESORERIAV3.CDP.CDP_ID del CDP de la coordinación y periodo
     * @param datos               número de anexo, fecha de firma, objeto, descripción, forma de pago, tipo de objeto y forma de inicio de contrato
     * @return id del contrato registrado (CONT_ID)
     */
    Long registrarContrato(
            InformacionDocenteContratacionDTO informacion,
            Long idCargaDocente,
            ValorContratacionDTO valorTotalContrato,
            Long idCdp,
            DatosAnexoDTO datos);

    /**
     * Completa la vinculación del docente con el contrato ya generado: crea el
     * documento de vinculación (TALENTOV3.DOCUMENTOVINCULACION), las tablas de
     * labor (TRABAJADORLABOR, TRABAJADORLABORUNIDAD y DOCENTEUNIDAD) y marca
     * la carga docente como vinculada.
     *
     * <p>Si el docente todavía no tiene un contrato generado se registra el
     * contrato en este paso para no dejar la vinculación a medias.</p>
     *
     * @param informacion        datos del docente y de la carga
     * @param idCargaDocente     id de la carga docente del docente
     * @param valorTotalContrato total del contrato; si llega nulo se usa el de la carga
     * @param idCdp              TESORERIAV3.CDP.CDP_ID del CDP de la coordinación y periodo
     * @param datos              número de anexo, fecha de firma, labor docente y los datos del anexo
     * @return id del contrato vinculado (CONT_ID)
     */
    Long vincularDocente(
            InformacionDocenteContratacionDTO informacion,
            Long idCargaDocente,
            ValorContratacionDTO valorTotalContrato,
            Long idCdp,
            DatosAnexoDTO datos);


    int vincularDocentesMasivamente(
            List<Long> idsCargaDocente,
            Long idLabor);        
}
