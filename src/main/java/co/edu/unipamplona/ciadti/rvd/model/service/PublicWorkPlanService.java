package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.dto.PaisGeneralDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicLevelDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTitleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicBasicInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingSaveDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicActivitiesDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicDigitalWorkPlanDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;

public interface PublicWorkPlanService {

    PublicBasicInformationDTO getBasicInformation(
            Long idCargaDocente,
            Long idPersonaGeneral
    );

    List<PublicAcademicLevelDTO>
    getAcademicLevels();

    List<PublicAcademicTitleDTO>
    getAcademicTitles(
            Long idNivelAcademico,
            String query
    );

    List<PaisGeneralDTO>
    getCountries();

    PublicAcademicTrainingDTO saveAcademicTraining(
            Long idCargaDocente,
            Long idPersonaGeneral,
            PublicAcademicTrainingSaveDTO body
    );

    PublicAcademicTrainingDTO getLatestAcademicTraining(
            Long idCargaDocente,
            Long idPersonaGeneral
    );

    PublicActivitiesDTO getActivities(
            Long idCargaDocente,
            Long idPersonaGeneral
    );

    PublicDigitalWorkPlanDTO getDigitalWorkPlan(
            Long idCargaDocente,
            Long idPersonaGeneral
    );

    List<PtdiDocumentoCategoriaDTO> getSupportDocumentCategories();

    PtdiDocumentoContenidoDTO getSupportDocuments(
            Long idCargaDocente,
            Long idPersonaGeneral
    );

    PtdiDocumentoArchivoDescargaDTO getSupportDocumentFile(
            Long idCargaDocente,
            Long idPersonaGeneral,
            String idArchivo
    );

    PtdiDocumentoContenidoDTO addSupportDocuments(
            Long idCargaDocente,
            Long idPersonaGeneral,
            String codigoCategoria,
            String nombreCategoria,
            List<MultipartFile> archivos
    );

    PtdiDocumentoContenidoDTO deleteSupportDocumentFile(
            Long idCargaDocente,
            Long idPersonaGeneral,
            String idArchivo
    );

}