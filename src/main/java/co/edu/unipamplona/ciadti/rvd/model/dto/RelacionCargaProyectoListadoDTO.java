/**
 * Aplicación: rvd
 * Archivo: RealcionCargaProyectoListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record RelacionCargaProyectoListadoDTO(
    Long idPersonaProyecto,
    Long idProyecto,
    List<ProyectoDTO> proyecto
) {}

/* 17/07/2026 @:Daniel Arias */
