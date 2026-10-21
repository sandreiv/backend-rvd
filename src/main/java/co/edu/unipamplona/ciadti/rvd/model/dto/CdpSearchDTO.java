/**
 * Aplicación: rvd
 * Archivo: CdpSearchDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/10/2026
 * Modificaciones:
 * 20/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record CdpSearchDTO(
    Long idRubro,
    Long idCdp,
    String numero,
    LocalDate fecha,
    String vigencia,
    String articulo,
    String descripcion
) {
}
