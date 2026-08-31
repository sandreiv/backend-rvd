/**
 * Aplicación: rvd
 * Archivo: PreasignacionPdfFileDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 31/08/2026
 * Modificaciones:
 * 31/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record PreasignacionPdfFileDTO(
    String fileName,
    byte[] content
) {}
