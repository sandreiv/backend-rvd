package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ConvocatoriaDatosInsertarDTO(
    Long id,
    String nombre,
    String descripcion,
    PersonaAutorizaConvocatoriaDTO autoriza,
    PeriodoUniversidadDTO periodo,
    NivelEducativoDTO nivelEducativo
) {}
