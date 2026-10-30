package co.edu.unipamplona.ciadti.rvd.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
public record PtdiDocumentoArchivoDTO(

        String id,

        String nombre,

        String path,

        String extension,

        Long tamano,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String estado

) {
    public PtdiDocumentoArchivoDTO(
            String id,
            String nombre,
            String path,
            String extension,
            Long tamano) {

        this(
                id,
                nombre,
                path,
                extension,
                tamano,
                null
        );
    }
}