package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;

public interface CoordinacionService {

    List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria, Long idUsuario);

    void savePreload(RelacionConvocatoriaCoordinacionDTO dto);
}
