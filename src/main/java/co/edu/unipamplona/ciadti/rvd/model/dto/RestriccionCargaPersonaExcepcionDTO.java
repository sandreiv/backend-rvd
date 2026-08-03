/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaPersonaExcepcionDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/07/2026
 * Modificaciones:
 * 30/07/2026 - Joel Daniel Arias Duarte - Creación inicial para manejar horas máximas por persona exceptuada.
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record RestriccionCargaPersonaExcepcionDTO(
        Long idPersona,
        String maximoHoras
) {}