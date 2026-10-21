package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PublicActivitiesDTO(
        List<ActividadHorasResumenDTO> actividades,
        String observacion
) {
}