package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CdpContextDTO(
        Long idCoordinacionFacultad,
        Long idUnidadAcademica,
        String unidadAcademica,
        Long idFacultad,
        String facultad
) {
}