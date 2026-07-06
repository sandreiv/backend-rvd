/**
 * Aplicación: rvd
 * Archivo: RelacionCargaProyectoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record RelacionCargaProyectoDTO(
    Long idPersonaProyecto,
    Long idProyecto
) {}
