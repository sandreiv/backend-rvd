package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoDocumentoContratacionDTO(

        Long id,

        String nombre,

        String descripcion,

        boolean obligatorio

) {
}