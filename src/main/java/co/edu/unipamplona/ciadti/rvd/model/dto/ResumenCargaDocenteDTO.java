/**
 * Aplicación: rvd
 * Archivo: ResumenCargaDocenteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/08/2026
 * Modificaciones:
 * 03/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record ResumenCargaDocenteDTO(
    Long idCargaDocente,
    ValorContratacionDTO valorContratacion,
    List<ActividadHorasResumenDTO> horasActividades,
    List<CentroCostoResumenDTO> centrosCosto
) {}
