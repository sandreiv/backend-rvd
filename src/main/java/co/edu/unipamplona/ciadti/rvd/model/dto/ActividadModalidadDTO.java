/**
 * Aplicación: rvd
 * Archivo: ActividadModalidadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/07/2026
 * Modificaciones:
 * 29/07/2026 - Creación inicial para listado de actividades por modalidad.
 * 29/07/2026 - tipoActividades como lista (una modalidad puede tener varios tipos).
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ActividadModalidadDTO(
        Long idModalidadContratacion,
        List<TipoActividadBasicoDTO> tipoActividades
) {}

/* 29/07/2026 */
