package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record DetalleCargaDocenteItemDTO(
    Long idTipoActividad,
    Long idTipoActividadHija,
    String codigoTipoActividad,
    Long horas,
    Long idUnidadRegional,
    Long idPrograma,
    String codigoMateria,
    Long idGrupo,
    Long idCentroCosto,
    List<RelacionCargaProyectoDTO> relacionCargaProyecto
) {}
