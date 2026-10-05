/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/09/2026
 * Modificaciones:
 * 03/09/2026 - Andrés Hernández - Creación inicial
 * 04/09/2026 - Andrés Hernández - Adición de campos
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;
import java.util.List;

public record SolicitudCdpListadoDTO(
    Long id,
    Long idCoordinacion,
    String estado,
    String observacion,
    List<AnexosSolicitudCdpDTO> adjuntos,
    Date fechaCambio
) {}