package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;
import java.util.List;

public record CdpRequestDTO(
        Long id,
        Long idCoordinacion,
        String estado,
        String observacion,
        List<CdpAdjuntoDTO> adjuntos,
        Date fechaCambio
) {
}