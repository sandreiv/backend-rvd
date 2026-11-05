/**
 * Aplicación: rvd
 * Archivo: DatosAnexoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

/**
 * Datos que el usuario diligencia en el panel "Datos del anexo" y que se
 * guardan en CONTRATOS.CONTRATO junto con el anexo.
 *
 * @param numeroAnexo número de anexo (CONTRATO.CONT_NUMERO)
 * @param fechaIngreso fecha de ingreso (CONTRATO.CONT_FECHAFIRMA)
 * @param idFormaPago BIENESV3.FORMAPAGO (CONTRATO.FOPA_ID)
 * @param idTipoObjeto CONTRATOS.TIPOOBJETO (CONTRATO.TIOB_ID)
 */
public record DatosAnexoDTO(
    String numeroAnexo,
    LocalDate fechaIngreso,
    Long idFormaPago,
    Long idTipoObjeto
) {}
