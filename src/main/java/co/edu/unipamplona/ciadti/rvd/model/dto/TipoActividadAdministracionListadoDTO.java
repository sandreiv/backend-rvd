/**
 * Aplicación: rvd
 * Archivo: TipoActividadAdministracionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 30/07/2026 - Se agrega campo componente.
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record TipoActividadAdministracionListadoDTO(
        Long id,
        String nombre,
        String descripcion,
        String codigo,
        String componente,
        String minimoHoras,
        String maximoHoras,
        String orden,
        String estado
) {}

/* 17/07/2026 @:Daniel Arias */
