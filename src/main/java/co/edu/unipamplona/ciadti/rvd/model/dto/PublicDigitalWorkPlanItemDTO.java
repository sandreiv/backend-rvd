package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record PublicDigitalWorkPlanItemDTO(
        Long idTipoActividad,
        String codigo,
        String criterio,
        List<String> actividades,
        BigDecimal totalHoras,
        Long ponderacion,
        String nivel
) {
}