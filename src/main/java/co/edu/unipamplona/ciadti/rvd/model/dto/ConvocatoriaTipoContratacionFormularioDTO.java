package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ConvocatoriaTipoContratacionFormularioDTO(
    Long id,
    Long idModalidadContratacion,
    List<FechaModalidadFormularioDTO> fechas
) {}
