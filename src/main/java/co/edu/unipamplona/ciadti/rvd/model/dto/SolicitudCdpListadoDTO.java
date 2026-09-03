package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record SolicitudCdpListadoDTO(
    Long id,
    String estado,
    String observacion,
    List<AnexosSolicitudCdpDTO> anexos
) {}