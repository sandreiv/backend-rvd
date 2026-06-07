package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ConvocatoriaTipoContratacionDTO(
    Long id,
    List<ModalidadContratacionDTO> modalidades,
    List<ConvocatoriaDatosInsertarDTO> convocatoria
) {}
