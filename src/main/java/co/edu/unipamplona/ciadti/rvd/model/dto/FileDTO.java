/**
 * Aplicación: rvd
 * Archivo: CdpExcelFileDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/09/2026
 * Modificaciones:
 * 01/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record FileDTO(
    String fileName,
    byte[] content
) {}
