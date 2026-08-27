package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record FechaModalidadFormularioDTO(
    Long id,
    String vacaciones,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String semanas,
    String rangoHoras
) {}
