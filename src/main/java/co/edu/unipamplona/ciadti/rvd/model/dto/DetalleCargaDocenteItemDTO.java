/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteItemDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

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

/* 17/07/2026 @:Daniel Arias */
