package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionDTO(
    Long id,
    String descripcion,
    String nombre,
    String unidad,
    String metodologia,
    String convocatoria,
    String periodoCarga
) {}
