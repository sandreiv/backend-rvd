/**
 * Aplicación: rvd
 * Archivo: ContratoCatalogosDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

/**
 * Catálogos que el usuario selecciona en el panel "Datos del anexo".
 *
 * @param formasPago  BIENESV3.FORMAPAGO (FOPA_DESCRIPCION)
 * @param tiposObjeto CONTRATOS.TIPOOBJETO (TIOB_DESCRIPCION)
 */
public record ContratoCatalogosDTO(
    List<CatalogoItemDTO> formasPago,
    List<CatalogoItemDTO> tiposObjeto
) {}
