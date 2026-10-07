package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActualizarValorContratoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GuardarNovedadesDetallesProyectosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminarDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionDecanoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenNovedadCargaDocenteDTO;

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

    void saveNoveltyActivities(
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

    void rejectProfessorNovelty(
            Long idCargaDocente,
            ObservacionDecanoDTO dto);

    ResumenNovedadCargaDocenteDTO getProfessorNoveltySummary(
            Long idCargaDocente);
}