package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PtdiDocumentoContenidoDTO(
        Integer version,
        List<PtdiDocumentoCategoriaDTO> categorias
) {
}