/**
 * Aplicación: rvd
 * Archivo: PersonaGeneralDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/06/2026
 * Modificaciones:
 * 04/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PersonaAutorizaConvocatoriaDTO(
    Long id,
    String documentoIdentidad,
    String nombreCompleto,
    String telefonoCelular
) {}
