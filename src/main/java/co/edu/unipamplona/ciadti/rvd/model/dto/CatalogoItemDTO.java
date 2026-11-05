/**
 * Aplicación: rvd
 * Archivo: CatalogoItemDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

/**
 * Ítem de un catálogo simple (id + descripción) para los selects del anexo.
 */
public record CatalogoItemDTO(
    Long id,
    String descripcion
) {}
