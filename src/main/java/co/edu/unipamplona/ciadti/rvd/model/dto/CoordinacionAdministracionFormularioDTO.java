package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionFormularioDTO(
        String nombre,
        String descripcion,
        Long idUnidadPadre,
        Long idUnidadRegional,
        Long idUnidad,
        Long idModalidad,
        Long idMetodologia,
        Long idCentroCosto,
        String codigo,
        String esAcademica
) {}