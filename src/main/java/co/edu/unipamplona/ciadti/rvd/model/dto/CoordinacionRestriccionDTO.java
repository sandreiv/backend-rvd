package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record CoordinacionRestriccionDTO(
    Long id,
    CoordinacionBusquedaDTO coordinacion,
    Date fechaInicio,
    Date fechaFin,
    String estado
) {
    
}
