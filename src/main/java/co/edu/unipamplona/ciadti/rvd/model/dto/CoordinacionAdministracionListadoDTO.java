package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionListadoDTO(
        Long id,
        Long idCoordinacionPadre,

        String nombre,
        String descripcion,

        Long idUnidadPadre,
        String unidadPadre,

        Long idUnidadRegional,
        String unidadRegional,

        Long idUnidad,
        String unidad,

        Long idModalidad,
        String modalidad,

        Long idMetodologia,
        String metodologia,

        Long idCentroCosto,
        String centroCosto,

        String codigo,
        String esAcademica
) {}