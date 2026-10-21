package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PtdiDocumentoArchivoDTO(
        String id,
        String nombre,
        String path,
        String extension,
        Long tamano
) {
}