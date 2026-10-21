package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;

public interface PtdiDocumentoService {

    PtdiDocumentoContenidoDTO getContent(
            Long idPlanTrabajoDigital
    );

    List<PtdiDocumentoCategoriaDTO> getAvailableCategories();

    PtdiDocumentoContenidoDTO addFiles(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String codigoCategoria,
            String nombreCategoria,
            List<MultipartFile> archivos
    );

    PtdiDocumentoArchivoDescargaDTO getFile(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String idArchivo
    );

    PtdiDocumentoContenidoDTO deleteFile(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String idArchivo
    );

}