package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PublicDigitalWorkPlanDTO(
        List<PublicDigitalWorkPlanItemDTO> items,
        Long totalPonderacion
) {
}