/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteActividadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;
import java.util.Optional;

public record DetalleCargaDocenteActividadDTO(
    TipoActividadDTO tipoActividad,
    Optional<Integer> esDeNovedad,
    List<TipoActividadDTO> tipoActividadHija,
    UnidadDTO unidadRegional,
    ProgramaDTO programa,
    MateriaDetalleDTO materia,
    GrupoDTO grupo,
    CentroCostoDTO centroCosto,
    String horas,
    List<RelacionCargaProyectoListadoDTO> relacionCargaProyecto
) {}

/* 17/07/2026 @:Daniel Arias */
