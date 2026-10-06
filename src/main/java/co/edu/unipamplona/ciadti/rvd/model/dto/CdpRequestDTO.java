/**
 * Aplicación: rvd
 * Archivo: CdpRequestDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;
import java.util.List;

public record CdpRequestDTO(
        Long id,
        Long idCoordinacion,
        String estado,
        String observacion,
        List<CdpAdjuntoDTO> adjuntos,
        Date fechaCambio
) {
}