package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ConvocatoriaDatosInsertarDTO(
    String nombre,
    String descripcion,
    PersonaAutorizaConvocatoriaDTO autoriza,
    PeriodoUniversidadDTO periodo,  
    NivelEducativoDTO nivelEducativo
) {}
