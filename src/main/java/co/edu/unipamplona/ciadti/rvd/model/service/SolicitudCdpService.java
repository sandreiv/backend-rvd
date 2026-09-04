package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.dto.CdpRequestDTO;

public interface SolicitudCdpService {

    void create(
            String observacion,
            List<MultipartFile> archivos,
            String idPeriodo
    );

    CdpRequestDTO getCurrentRequest();
}