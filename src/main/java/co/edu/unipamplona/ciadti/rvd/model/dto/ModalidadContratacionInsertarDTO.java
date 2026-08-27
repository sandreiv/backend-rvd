package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModalidadContratacionInsertarDTO(
    @JsonProperty("idModalidadContratacion")
    Long id,
    String vacaciones, 
    LocalDate fechaInicio,
    LocalDate fechaFin, 
    String semanas
) {}
