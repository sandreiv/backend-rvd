package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaProyectoDTO(
    Long id, 
    Long idPersonaGeneral,
    TipoActividadDTO tipoActividad,
    String horas,
    String tipo,
    String observacion
) {}
