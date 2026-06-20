package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ConvocatoriaListadoDTO(
    Long id,
    String nombre,
    String descripcion,
    String estado,
    NivelEducativoDTO nivelEducativo,
    PeriodoUniversidadDTO periodoUniversidad,
    List<ModalidadContratacionListadoDTO> modalidadesContratacion
) {}