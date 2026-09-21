package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;

public interface NovedadCargaDocenteService {

    void assignNameToNn(
            AsignarNombreNnDTO dto
    );

    void saveContractModalityProfessor(
            CambioModalidadHoraCatedraticoDTO dto
    );

    void approveProfessorNovelty(Long idCargaDocente);
}