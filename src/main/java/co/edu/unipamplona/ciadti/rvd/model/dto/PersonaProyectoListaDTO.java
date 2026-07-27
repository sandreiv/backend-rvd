/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoListaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaProyectoListaDTO(
    Long id,
    Long idProyecto,
    Long idPersonaGeneral,
    String nombreCompleto,
    Long idTipoActividad,
    IdNombreDTO tipoActividad,
    String tipo,
    String horas,
    String observacion
) {}
