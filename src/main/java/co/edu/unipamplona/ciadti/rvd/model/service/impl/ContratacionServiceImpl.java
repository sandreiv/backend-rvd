package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.SolicitudCdpMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringCdpInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringValuesInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenNovedadCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.SolicitudCdpRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HiringCdpInformationProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadCargaDocenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratacionServiceImpl implements ContratacionService {

    private final SolicitudCdpRepository solicitudCdpRepository;
    private final SolicitudCdpMapper solicitudCdpMapper;
    private final NovedadCargaDocenteService novedadCargaDocenteService;
    
    @Override
    @Transactional(readOnly = true)
    public HiringCdpInformationDTO hiringCdpInformation(Long idPeriodoUniversitario, Long idCoordinacionHija) {
        log.info("hiringCdpInformation ===> Obteniendo información del CDP. idPeriodoUniversidad={}, idCoordinacion={}", idPeriodoUniversitario, idCoordinacionHija);

        if (idPeriodoUniversitario == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }
        if (idCoordinacionHija == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación es obligatoria");
        }

        HiringCdpInformationProjection projection = solicitudCdpRepository.getHiringCdpInformation(idCoordinacionHija, idPeriodoUniversitario);
        if (projection == null) {
            return  null;
        }

        HiringCdpInformationDTO result = solicitudCdpMapper.toHiringCdpInfoDto(projection);

        log.info("hiringCdpInformation ===> Información del CDP obtenida. idPeriodoUniversidad={}, idCoordinacion={}", idPeriodoUniversitario, idCoordinacionHija);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public HiringValuesInformationDTO hiringValuesInformation(Long idCargaDocente, String numeroPeriodo) {
        log.info("hiringValuesInformation ===> Obteniendo valores de contratación. idCargaDocente={}", idCargaDocente);

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria");
        }
        ResumenNovedadCargaDocenteDTO resumen = novedadCargaDocenteService.getProfessorNoveltySummary(idCargaDocente, numeroPeriodo);

        log.info("hiringValuesInformation ===> Valores de contratación obtenidos. idCargaDocente={}", idCargaDocente);
        return new HiringValuesInformationDTO(resumen.valorContratacion(), resumen.centrosCosto());
    }
}
