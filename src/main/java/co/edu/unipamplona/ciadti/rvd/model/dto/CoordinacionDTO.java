package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionDTO(
    Long id,
    String nombre,
    String descripcion,
    String codigo,
    String esAcademica,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    MetodologiaDTO metodologia,
    ModalidadDTO modalidad,
    NivelEducativoDTO nivelEducativo,
    PeriodoUniversidadDTO periodoUniversidad,
    EstadoCargaDTO estadoCarga,
    Long idConvocatoria
) {}
