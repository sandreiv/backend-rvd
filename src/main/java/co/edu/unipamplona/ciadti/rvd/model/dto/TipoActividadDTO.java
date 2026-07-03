package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadDTO(
    Long id,
    Long idPadre,
    String nombre,
    String descripcion,
    String orden, 
    String codigo
) {}
