package co.edu.unipamplona.ciadti.rvd.model.dto;

public record AsociacionCoordinacionFormularioDTO(
        Long id,
        Long idCoordinacion,
        Long idPrograma,
        String codigoMateria,
        Long idCentroCosto,
        String estado
) {}