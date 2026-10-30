package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public record PtdiDocumentoCategoriaDTO(

        String codigo,

        String nombre,

        List<PtdiDocumentoArchivoDTO> archivos,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long idTipoDocumento,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean obligatorio

) {

    public PtdiDocumentoCategoriaDTO(
            String codigo,
            String nombre,
            List<PtdiDocumentoArchivoDTO> archivos) {

        this(
                codigo,
                nombre,
                archivos,
                null,
                null
        );
    }
}