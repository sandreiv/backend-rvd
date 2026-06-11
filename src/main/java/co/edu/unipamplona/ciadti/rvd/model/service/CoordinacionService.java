package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;

public interface CoordinacionService {

    List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria);
    
}
