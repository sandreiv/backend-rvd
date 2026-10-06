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

    void approveCdpRequest(Long idSolicitud);
}