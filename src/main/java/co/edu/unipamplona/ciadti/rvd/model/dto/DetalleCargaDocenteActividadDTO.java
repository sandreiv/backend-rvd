package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record DetalleCargaDocenteActividadDTO(
    TipoActividadDTO tipoActividad,
    List<TipoActividadDTO> tipoActividadHija,
    UnidadDTO unidadRegional,
    ProgramaDTO programa,
    MateriaDetalleDTO materia,
    GrupoDTO grupo,
    CentroCostoDTO centroCosto,
    String horas,
    List<RelacionCargaProyectoListadoDTO> relacionCargaProyecto
) {}
