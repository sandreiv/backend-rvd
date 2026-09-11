package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;

public interface NovedadesService {

    List<NovedadListadoDTO> listNovedades();

    void saveNovedad(
            NovedadFormularioDTO dto
    );

    void updateNovedad(
            Long id,
            NovedadFormularioDTO dto
    );

    void deleteNovedad(
            Long id
    );

    void deleteBulkNovedades(
            List<Long> ids
    );
}