/**
 * Aplicación: rvd
 * Archivo: ModalidadContratacionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/06/2026
 * Modificaciones:
 * 04/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ModalidadContratacionDTO(
    Long id,
    String nombre,
    String descripcion,
    String instructivo,
    String estado,
    String sigla
) {}
/* 04/06/2026 @:Sebastian Jaimes */
