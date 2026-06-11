package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoordinacionServiceImpl implements CoordinacionService {

    private final CoordinacionRepository coordinacionRepository;
    private final CoordinacionMapper coordinacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria) {
        List<CoordinacionListadoProjection> projections = idConvocatoria == null ? coordinacionRepository.findWithoutCarga() : coordinacionRepository.findByConvocatoriaWithCarga(idConvocatoria);
        return coordinacionMapper.toDtoList(projections);
    }
}
