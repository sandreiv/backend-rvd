/**
 * Aplicación: rvd
 * Archivo: HistoriaGeneralNovedadDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDateTime;

public record HistorialGeneralNovedadDTO(
    Long idNovedadCargaDocente,
    Long idCargaDocente,
    Long idPersonaGeneral,
    String nombreDocente,
    Long idNovedadCatalogo,
    String tipoNovedad,
    LocalDateTime fecha,
    String estadoNovedad,
    String motivoRechazo
) {}