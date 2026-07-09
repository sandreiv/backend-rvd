package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record DetalleCargaDocenteItemDTO(
    Long idTipoActividad,
    Long idTipoActividadHija,
    String codigoTipoActividad,
    Long horas,
    Long idUnidadRegional,
    Long idPrograma,
    MateriaFormularioDTO materia,
    Long idGrupo,
    Long idCentroCosto,
    List<RelacionCargaProyectoDTO> relacionCargaProyecto
) {}
