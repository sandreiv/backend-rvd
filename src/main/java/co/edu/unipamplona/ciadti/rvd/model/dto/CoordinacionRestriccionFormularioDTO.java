package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record CoordinacionRestriccionFormularioDTO(
    Long idCoordinacion,
    Long idFechasConvocatoria,
    Date fechaInicio,
    Date fechaFin,
    String estado
) {}
