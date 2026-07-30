/**
 * Aplicación: rvd
 * Archivo: TipoActividadBasicoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/07/2026
 * Modificaciones:
 * 29/07/2026 - Creación inicial para listado de actividades por modalidad.
 * 30/07/2026 - Se agrega campo componente.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadBasicoDTO(
        Long id,
        String nombre,
        String codigo,
        String estado,
        String componente
) {}

/* 29/07/2026 */
