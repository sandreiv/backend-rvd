package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record FechasConvocatoriaFormularioDTO(
    Long id,
    String codigo,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {}
