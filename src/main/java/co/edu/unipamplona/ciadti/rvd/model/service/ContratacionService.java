/**
 * Aplicación: rvd
 * Archivo: ContratacionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/10/2026
 * Modificaciones:
 * 29/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.HiringCdpInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringValuesInformationDTO;

public interface ContratacionService {
    
    HiringCdpInformationDTO hiringCdpInformation(Long idPeriodoUniversitario, Long idCoordinacionHija);

    HiringValuesInformationDTO hiringValuesInformation(Long idCargaDocente);
}
