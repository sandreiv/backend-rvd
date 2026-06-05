package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModalidadContratacionInsertarDTO(
    @JsonProperty("idModalidadContratacion")
    Long id,
    Long vacaciones, 
    Date fechaInicio,
    Date fechaFin,
    String semanas
) {}
