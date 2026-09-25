package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActualizarValorContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GuardarNovedadesDetallesProyectosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;

public interface NovedadCargaDocenteService {

    void updateContractValue(
            ActualizarValorContratoDTO dto
    );
    
    void assignNameToNn(
            AsignarNombreNnDTO dto
    );

    void saveContractModalityProfessor(
            CambioModalidadHoraCatedraticoDTO dto
    );

    void saveNoveltyProjectActivities(
        GuardarNovedadesDetallesProyectosDTO dto
    );

    void changeProfessor(
            CambioDocenteDTO dto
    );

    void requestDeleteProfessor(
            EliminarDocenteDTO dto
    );

    void addNoveltyProfessor(
        CargaDocenteFormularioDTO dto,
        Long idNovedad
    );


    void approveProfessorNovelty(Long idCargaDocente);
}