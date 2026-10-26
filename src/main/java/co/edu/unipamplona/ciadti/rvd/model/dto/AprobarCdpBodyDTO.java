/**
 * Aplicación: rvd
 * Archivo: AprobarCdpBodyDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/10/2026
 * Modificaciones:
 * 23/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record AprobarCdpBodyDTO(
    Long idSolicitud,
    Long idCdp,
    String numero
) {}
