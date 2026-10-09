/**
 * Aplicación: rvd
 * Archivo: DocenteSessionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionResponseDTO;

public interface DocenteSessionService {

    DocenteSessionResponseDTO openSession(
            DocenteSessionRequestDTO body,
            String remoteAddr);
}
