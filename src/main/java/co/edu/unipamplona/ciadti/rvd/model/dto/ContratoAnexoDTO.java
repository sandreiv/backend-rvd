/**
 * Aplicación: rvd
 * Archivo: ContratoAnexoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 * Modificaciones:
 * 18/11/2026 - Juan Diego Florez Vera - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

/**
 * Datos del anexo de un contrato ya generado, para poder mostrarlos al
 * diligenciar la vinculación docente sin volver a capturarlos.
 *
 * @param idCargaDocente carga docente del docente (null en la consulta de un docente)
 * @param numeroAnexo número de anexo (CONTRATO.CONT_NUMERO)
 * @param fechaFirma fecha de firma del contrato (CONTRATO.CONT_FECHAFIRMA)
 * @param idFormaPago BIENESV3.FORMAPAGO (CONTRATO.FOPA_ID)
 * @param idTipoObjeto CONTRATOS.TIPOOBJETO (CONTRATO.TIOB_ID)
 * @param idFormaInicioContrato CONTRATOS.FORMAINICIOCONTRATO (CONTRATO.FOIC_ID)
 * @param objeto texto con el objeto del contrato (CONTRATO.CONT_OBJETO)
 * @param descripcion descripción del objeto del contrato (CONTRATO.CONT_DESCRIPCION)
 */
public record ContratoAnexoDTO(
    Long idCargaDocente,
    String numeroAnexo,
    LocalDate fechaFirma,
    Long idFormaPago,
    Long idTipoObjeto,
    Long idFormaInicioContrato,
    String objeto,
    String descripcion
) {}
