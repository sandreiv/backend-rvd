package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ConvocatoriaListadoDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado,
    NivelEducativoDTO nivelEducativo,
    PeriodoUniversidadDTO periodoUniversidad
) {}
