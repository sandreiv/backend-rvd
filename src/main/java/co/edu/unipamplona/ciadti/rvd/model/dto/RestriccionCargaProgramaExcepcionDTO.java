/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaProgramaExcepcionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/08/2026
 * Modificaciones:
 * 06/08/2026 - Creación inicial para manejar horas máximas por programa exceptuado.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record RestriccionCargaProgramaExcepcionDTO(
        Long idPrograma,
        String maximoHoras
) {}
