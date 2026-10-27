package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoDocumentoContratacionDTO;

public interface PtdiDocumentoService {

    PtdiDocumentoContenidoDTO getContent(
            Long idPlanTrabajoDigital
    );

    List<PtdiDocumentoCategoriaDTO> getAvailableCategories();

    List<TipoDocumentoContratacionDTO> getDocumentTypes();

    PtdiDocumentoContenidoDTO addFiles(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String codigoCategoria,
            String nombreCategoria,
            List<MultipartFile> archivos
    );

    PtdiDocumentoContenidoDTO addFilesByType(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            Long idTipoDocumento,
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