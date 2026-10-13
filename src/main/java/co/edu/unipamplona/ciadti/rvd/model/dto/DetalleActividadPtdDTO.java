/**
 * Aplicación: rvd
 * Archivo: DetalleActividadPtdDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record DetalleActividadPtdDTO(
    Long id,
    Integer esDeNovedad,
    String descripcion
) {}
