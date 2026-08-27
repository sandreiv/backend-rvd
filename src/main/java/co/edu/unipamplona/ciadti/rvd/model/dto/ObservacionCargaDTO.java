package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record ObservacionCargaDTO(
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String rolPersonaGeneral,
    String observacion,
    LocalDateTime fecha
) {}
