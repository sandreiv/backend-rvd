package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ProyectoDTO(
    Long id,
    Long idProyecto,
    String nombre,
    String descripcion,
    List<TipoProyectoDTO> tipoProyecto,
    List<PersonaProyectoDTO> personaProyecto,
    List<ProyectoDTO> productos
) {}
