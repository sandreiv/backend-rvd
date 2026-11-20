/**
 * Aplicación: rvd
 * Archivo: VinculacionDocenteRequestDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Juan Diego Florez Vera - Creación inicial (vinculación individual del docente)
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

/**
 * Datos para vincular a un docente con su contrato ya generado.
 *
 * <p>Es la versión individual de la vinculación: a diferencia de la masiva,
 * el docente se vincula desde su propia carga y con los datos del anexo que
 * el usuario diligenció al generar el contrato.</p>
 *
 * @param idCargaDocente carga docente del docente a vincular (CADO_ID)
 * @param numeroPeriodo  periodo de la carga docente; se usa para resolver los
 *                        valores de contratación cuando el docente no tiene
 *                        contrato generado
 * @param anexo          datos del anexo del contrato (número de anexo, fecha de
 *                        firma, forma de pago, tipo de objeto, forma de inicio
 *                        de contrato, objeto, descripción y labor docente)
 */
public record VinculacionDocenteRequestDTO(
    Long idCargaDocente,
    String numeroPeriodo,
    DatosAnexoDTO anexo
) {}
