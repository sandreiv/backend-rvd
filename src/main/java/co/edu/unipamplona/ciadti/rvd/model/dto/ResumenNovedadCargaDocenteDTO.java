/**
 * Aplicación: rvd
 * Archivo: ResumenNovedadCargaDocenteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/09/2026
 * Modificaciones:
 * 29/09/2026 - Resumen de carga con novedad vigente
 * 29/09/2026 - Historial de novedades del docente
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ResumenNovedadCargaDocenteDTO(
    Long idCargaDocente,
    ValorContratacionDTO valorContratacion,
    List<ActividadHorasResumenDTO> horasActividades,
    List<CentroCostoResumenDTO> centrosCosto,
    List<ObservacionResumenNovedadDTO> observaciones,
    List<HistorialNovedadResumenDTO> novedades
) {}
