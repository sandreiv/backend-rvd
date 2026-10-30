/**
 * Aplicación: rvd
 * Archivo: HiringValuesInformationDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/10/2026
 * Modificaciones:
 * 29/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record HiringValuesInformationDTO(
    ValorContratacionDTO valorContratacion,
    List<CentroCostoResumenDTO> centrosCosto
) {}
