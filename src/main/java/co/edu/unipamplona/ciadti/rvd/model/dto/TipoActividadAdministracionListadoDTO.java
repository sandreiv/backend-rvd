package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadAdministracionListadoDTO(
        Long id,
        String nombre,
        String descripcion,
        String codigo,
        String minimoHoras,
        String maximoHoras,
        String orden,
        String estado
) {}