/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record DetalleCargaDocenteFormularioDTO(
    Long idCargaDocente,
    Long idPrograma,
    Long idGrupo,
    Long idTipoActividad,
    Long idCentroCosto,
    String horas,
    List<RelacionCargaProyectoDTO> relacionCargaProyecto
) {}
