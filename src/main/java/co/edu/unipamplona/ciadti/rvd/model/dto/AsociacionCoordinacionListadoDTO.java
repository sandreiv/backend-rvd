package co.edu.unipamplona.ciadti.rvd.model.dto;

public record AsociacionCoordinacionListadoDTO(
        Long id,
        Long idCoordinacion,
        String coordinacion,
        Long idPrograma,
        String programa,
        String codigoMateria,
        String materia,
        Long idCentroCosto,
        String centroCosto,
        String estado
) {}