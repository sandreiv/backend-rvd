package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record ObservacionCargaDTO(
    Long idObservacion,
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String rolPersonaGeneral,
    String observacion,
    LocalDateTime fecha,
    Integer visto
) {}
