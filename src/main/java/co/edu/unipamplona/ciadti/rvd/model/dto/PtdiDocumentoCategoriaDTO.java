package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PtdiDocumentoCategoriaDTO(
        String codigo,
        String nombre,
        List<PtdiDocumentoArchivoDTO> archivos
) {
}