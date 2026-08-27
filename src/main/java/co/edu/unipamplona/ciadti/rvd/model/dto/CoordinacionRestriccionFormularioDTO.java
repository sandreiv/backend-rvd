package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record CoordinacionRestriccionFormularioDTO(
    Long idCoordinacion,
    Long idFechasConvocatoria,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String estado
) {}
