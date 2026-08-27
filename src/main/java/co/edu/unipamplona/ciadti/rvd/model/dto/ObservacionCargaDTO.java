package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record ObservacionCargaDTO(
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String rolPersonaGeneral,
    String observacion,
    Date fecha
) {}
