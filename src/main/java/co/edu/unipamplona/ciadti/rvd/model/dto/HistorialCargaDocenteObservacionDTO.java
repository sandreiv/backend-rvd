package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record HistorialCargaDocenteObservacionDTO(
    Long idHistorial,
    Long idPersonaGeneral,
    String nombrePersonaGeneral,
    String rolPersonaGeneral,
    String observacion,
    LocalDateTime fecha,
    String estado
) {}