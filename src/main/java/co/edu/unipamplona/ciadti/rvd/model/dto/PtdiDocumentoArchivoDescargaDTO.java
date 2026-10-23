package co.edu.unipamplona.ciadti.rvd.model.dto;

import org.springframework.core.io.Resource;

public record PtdiDocumentoArchivoDescargaDTO(
        Resource resource,
        String nombre
) {
}