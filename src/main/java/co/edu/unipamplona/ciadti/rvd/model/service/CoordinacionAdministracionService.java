package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoAdministracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionAdministracionListadoDTO;

public interface CoordinacionAdministracionService {

    CoordinacionAdministracionCatalogosDTO getCatalogs();

    List<CatalogoAdministracionDTO> searchUnits(String term);

    List<CoordinacionAdministracionListadoDTO> listParentCoordinations();

    List<CoordinacionAdministracionListadoDTO> listChildCoordinations(Long idPadre);

    void saveParentCoordination(CoordinacionAdministracionFormularioDTO dto);

    void saveChildCoordination(Long idPadre, CoordinacionAdministracionFormularioDTO dto);

    void updateCoordination(Long id, CoordinacionAdministracionFormularioDTO dto);

    void deleteCoordination(Long id);

    void deleteBulkCoordinations(List<Long> ids);
}