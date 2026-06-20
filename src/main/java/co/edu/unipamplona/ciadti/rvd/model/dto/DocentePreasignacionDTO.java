package co.edu.unipamplona.ciadti.rvd.model.dto;

public record DocentePreasignacionDTO(
    Long id,
    String documentoIdentidad,
    String nombreCompleto,
    //CategoriaCatedraticoDTO categoriaCatedratico,
    ConvocatoriaDTO convocatoria
) {}
