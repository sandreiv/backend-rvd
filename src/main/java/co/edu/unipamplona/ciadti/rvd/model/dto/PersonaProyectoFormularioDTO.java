/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 * 16/09/2026 - Andrés Hernández - Manejo del estado de actividad entre docente y proyecto
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaProyectoFormularioDTO(
    Long idProyecto,
    Long idPersonaGeneral,
    Long idTipoActividad,
    String tipo,
    String horas,
    String observacion,
    String esActivo
) {}
