/**
 * Aplicación: rvd
 * Archivo: PeriodoUniversidadService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/06/2026
 * Modificaciones:
 * 05/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;

public interface PeriodoUniversidadService {

    List<PeriodoUniversidadDTO> findUniversityPeriodList();
}
/* 05/06/2026 @:Sebastian Jaimes */
