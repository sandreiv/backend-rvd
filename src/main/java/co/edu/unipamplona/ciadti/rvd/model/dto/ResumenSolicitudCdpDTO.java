package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ResumenSolicitudCdpDTO(
    Long id,
    String nombre,
    String descripcion,
    String codigo,
    String esAcademica,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    MetodologiaDTO metodologia,
    ModalidadDTO modalidad,
    PeriodoUniversidadDTO periodoUniversidad,
    SolicitudCdpListadoDTO solicitud
) {}