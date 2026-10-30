/**
 * Aplicación: rvd
 * Archivo: HiringCdpInformationDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/10/2026
 * Modificaciones:
 * 29/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record HiringCdpInformationDTO(
    Long idCdp,
    String numero,
    LocalDate fecha,
    String vigencia,
    String rubros,
    Long idSolicitudRvd
) {}
