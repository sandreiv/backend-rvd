package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaCoordinacionListadoDTO(
        Long idPersonaGeneral,
        String persona,
        String documentoIdentidad,
        Long idCoordinacion,
        String coordinacion,
        String estado
) {}