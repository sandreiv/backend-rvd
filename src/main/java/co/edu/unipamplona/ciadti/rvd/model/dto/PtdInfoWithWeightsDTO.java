/**
 * Aplicación: rvd
 * Archivo: PtdInfoWithWeightsDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/10/2026
 * Modificaciones:
 * 14/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record PtdInfoWithWeightsDTO(
    List<ActivityWeightsDTO> actividades,
    PtdObservationDTO observacion
) {}
