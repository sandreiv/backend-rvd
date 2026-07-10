package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadAdministracionFormularioDTO(
        String nombre,
        String descripcion,
        String codigo,
        Integer minimoHoras,
        Integer maximoHoras,
        String estado
) {}