package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record AprobacionDetalleCargaDocenteDTO(
        Long idCargaDocente,
        List<DetalleCargaDocenteDTO> detallesActualizados,
        List<DetalleCargaDocenteItemDTO> detallesNuevos
) {}