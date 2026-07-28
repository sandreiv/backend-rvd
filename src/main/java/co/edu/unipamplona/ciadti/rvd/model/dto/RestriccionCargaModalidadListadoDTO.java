/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaModalidadListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para listar modalidades en restricción de carga.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record RestriccionCargaModalidadListadoDTO(
        Long id,
        String nombre,
        String sigla,
        String estado
) {}