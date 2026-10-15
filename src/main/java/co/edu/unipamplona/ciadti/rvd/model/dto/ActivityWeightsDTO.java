/**
 * Aplicación: rvd
 * Archivo: ActivityWeightsDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/10/2026
 * Modificaciones:
 * 14/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ActivityWeightsDTO(
    String nivel,
    Long ponderado,
    Long idTipoActividad
) {}
