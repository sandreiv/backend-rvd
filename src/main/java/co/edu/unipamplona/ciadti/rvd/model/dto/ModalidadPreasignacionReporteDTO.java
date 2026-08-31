/**
 * Aplicación: rvd
 * Archivo: ModalidadPreasignacionReporteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 31/08/2026 - Sebastian Jaimes - Totales por modalidad para reporte PDF
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ModalidadPreasignacionReporteDTO(
    Long idModalidadContratacion,
    String nombreModalidad,
    List<DocentePreasignacionReporteDTO> docentes,
    TotalesPreasignacionReporteDTO totales
) {}
