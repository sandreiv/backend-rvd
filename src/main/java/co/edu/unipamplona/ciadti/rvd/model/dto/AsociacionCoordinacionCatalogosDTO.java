package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record AsociacionCoordinacionCatalogosDTO(
        List<CatalogoAdministracionDTO> coordinaciones,
        List<CatalogoAdministracionDTO> programas,
        List<MateriaCatalogoAdministracionDTO> materias,
        List<CatalogoAdministracionDTO> centrosCosto,
        List<CatalogoAdministracionDTO> personas
) {}