package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;

public interface NovedadCargaDocenteService {

    void assignNameToNn(
            AsignarNombreNnDTO dto
    );

    void saveContractModalityProfessor(
            CambioModalidadHoraCatedraticoDTO dto
    );

    void changeProfessor(
            CambioDocenteDTO dto
    );

    void requestDeleteProfessor(
            EliminarDocenteDTO dto
    );

}