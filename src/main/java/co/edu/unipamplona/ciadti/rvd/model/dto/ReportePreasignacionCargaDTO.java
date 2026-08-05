/**
 * Aplicación: rvd
 * Archivo: ReportePreasignacionCargaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 04/08/2026 - Sebastian Jaimes - Agrupación por modalidad
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ReportePreasignacionCargaDTO(
    EncabezadoCargaReporteDTO encabezado,
    List<ModalidadPreasignacionReporteDTO> modalidades
) {}
