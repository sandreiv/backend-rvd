package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConvocatoriaFormularioDTO(
    @JsonProperty("convocatoria")
    ConvocatoriaDatosInsertarDTO convocatoriaDatosInsertar,
    List<FechasConvocatoriaFormularioDTO> fechas,
    List<ModalidadContratacionInsertarDTO> modalidades
) {}
