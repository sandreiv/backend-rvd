/**
 * Aplicación: rvd
 * Archivo: ApprovePtdRequestDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/10/2026
 * Modificaciones:
 * 26/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ApprovePtdRequestDTO(
    Long idPersonaGeneral,
    Long idPeriodoUniversitario,
    Long idCargaDocente,
    List<String> idsDocumentosAdjuntos
) {}
