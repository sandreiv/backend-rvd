package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record FechasConvocatoriaFormularioDTO(
    String codigo,
    Date fechaInicio,
    Date fechaFin
) {}
