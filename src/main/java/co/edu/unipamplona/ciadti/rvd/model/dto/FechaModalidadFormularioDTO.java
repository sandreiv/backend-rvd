package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record FechaModalidadFormularioDTO(
    Long id,
    String vacaciones,
    Date fechaInicio,
    Date fechaFin,
    String semanas
) {}
