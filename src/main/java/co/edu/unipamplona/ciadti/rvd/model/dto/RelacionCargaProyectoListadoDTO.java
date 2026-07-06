package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record RelacionCargaProyectoListadoDTO(
    Long idPersonaProyecto,
    Long idProyecto,
    List<ProyectoDTO> proyecto
) {}
