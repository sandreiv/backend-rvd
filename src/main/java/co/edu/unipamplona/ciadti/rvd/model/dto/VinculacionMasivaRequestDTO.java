package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record VinculacionMasivaRequestDTO(
        List<Long> idsCargaDocente,
        Long idLabor
) {}