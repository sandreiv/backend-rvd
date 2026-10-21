/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.dto.CdpRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpSearchDTO;

public interface SolicitudCdpService {

    void create(
        String observacion,
        List<MultipartFile> archivos,
        String idPeriodo,
        String idCoordinacionFacultad,
        String idConvocatoria
    );

    CdpRequestDTO getCurrentRequest(
        Long idCoordinacionFacultad,
        Long idPeriodoUniversitario
    );

    void sendRequestToVice(Long idSolicitud);

    List<CdpSearchDTO> searchCdpsTesoreriaV3(String numeroCdp, Long idUnidad, Long anioVigencia);

    void approveCdpRequest(Long idSolicitud);
}