/**
 * Aplicación: rvd
 * Archivo: HiringCallService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Información, parámetros y actividades PTD de contratación
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadPtdDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ParametrosContratacionDTO;

public interface HiringCallService {

    InformacionDocenteContratacionDTO findProfessorInformation(Long idCargaDocente);

    ParametrosContratacionDTO findHiringParameters(Long idCargaDocente);

    List<ActividadPtdDTO> listPtdActivities(Long idCargaDocente);
}
