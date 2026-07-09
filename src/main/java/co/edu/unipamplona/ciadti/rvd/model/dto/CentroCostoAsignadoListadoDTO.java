package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CentroCostoAsignadoListadoDTO(
        Long id,
        Long idCoordinacion,
        String coordinacion,
        Long idCentroCosto,
        String centroCosto,
        String estado
) {}