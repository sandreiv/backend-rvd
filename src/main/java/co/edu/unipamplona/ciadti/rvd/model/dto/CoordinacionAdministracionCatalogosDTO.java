package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record CoordinacionAdministracionCatalogosDTO(
        List<CatalogoAdministracionDTO> modalidades,
        List<CatalogoAdministracionDTO> metodologias,
        List<CatalogoAdministracionDTO> centrosCosto
) {}