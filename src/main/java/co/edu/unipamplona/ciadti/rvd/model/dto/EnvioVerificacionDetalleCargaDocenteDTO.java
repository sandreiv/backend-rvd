package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record EnvioVerificacionDetalleCargaDocenteDTO(
        Long idCargaDocente,
        List<DetalleCargaDocenteDTO> detallesActualizados,
        List<DetalleCargaDocenteItemDTO> detallesNuevos
) {
}