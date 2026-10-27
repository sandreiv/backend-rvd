package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.nio.file.LinkOption;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.enums.PtdiDocumentoCategoriaEnum;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoDocumentoContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PtdiDocumentoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.PlanTrabajoDigitalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PtdiDocumentoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoDocumentoContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PtdiDocumentoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PtdiDocumentoServiceImpl
        implements PtdiDocumentoService {

    private static final int CONTENT_VERSION = 1;

    private static final String RELATIVE_ROOT =
            "archivos/ptd";

    @Value("${file.ptd.path}")
    private String ptdStoragePath;

    private final PtdiDocumentoRepository
            ptdiDocumentoRepository;

    private final PlanTrabajoDigitalRepository
            planTrabajoDigitalRepository;

    private final TipoDocumentoContratacionRepository
            tipoDocumentoContratacionRepository;        

    private final ObjectMapper objectMapper;


    
    @Override
    @Transactional(readOnly = true)
        public PtdiDocumentoContenidoDTO getContent(
                Long idPlanTrabajoDigital) {

        validatePtdId(
                idPlanTrabajoDigital
        );

        PtdiDocumentoContenidoDTO contenidoHistorico =
                ptdiDocumentoRepository
                        .findFirstByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNullOrderByFechaCambioDesc(
                                idPlanTrabajoDigital
                        )
                        .map(PtdiDocumentoEntity::getAdjunto)
                        .map(this::parseContent)
                        .orElseGet(this::emptyContent);

        List<PtdiDocumentoEntity> individuales =
                ptdiDocumentoRepository
                        .findByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNotNullOrderByFechaCambioDesc(
                                idPlanTrabajoDigital
                        );

        List<PtdiDocumentoCategoriaDTO> catalogo =
                getAvailableCategories();

        List<PtdiDocumentoCategoriaDTO> categorias =
                new ArrayList<>();

        for (
                PtdiDocumentoCategoriaDTO historica :
                contenidoHistorico.categorias()
        ) {

                if (
                        historica == null
                        || !StringUtils.hasText(
                                historica.codigo()
                        )
                ) {

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El contenido documental histórico no es válido"
                );
                }

                PtdiDocumentoCategoriaDTO tipo =
                        catalogo.stream()
                                .filter(categoria ->
                                        categoria.codigo().equals(
                                                historica.codigo()
                                        )
                                )
                                .findFirst()
                                .orElse(null);

                List<PtdiDocumentoArchivoDTO> archivos =
                        historica.archivos() == null
                                ? List.of()
                                : historica.archivos();

                categorias.add(
                        new PtdiDocumentoCategoriaDTO(
                                historica.codigo(),

                                tipo != null
                                        ? tipo.nombre()
                                        : historica.nombre(),

                                archivos,

                                tipo != null
                                        ? tipo.idTipoDocumento()
                                        : null,

                                tipo != null
                                        ? tipo.obligatorio()
                                        : null
                        )
                );
        }

        for (
                PtdiDocumentoEntity documento :
                individuales
        ) {

                Long idTipoDocumento =
                        documento
                                .getIdTipoDocumentoContratacion();

                PtdiDocumentoCategoriaDTO tipo =
                        catalogo.stream()
                                .filter(categoria ->
                                        idTipoDocumento.equals(
                                                categoria.idTipoDocumento()
                                        )
                                )
                                .findFirst()
                                .orElseThrow(() ->
                                        new ApiException(
                                                HttpStatus.INTERNAL_SERVER_ERROR,
                                                "El documento tiene un tipo documental no registrado"
                                        )
                                );

                PtdiDocumentoArchivoDTO archivo =
                        parseIndividualFile(
                                documento
                        );

                int indiceCategoria = -1;

                for (
                        int i = 0;
                        i < categorias.size();
                        i++
                ) {

                if (
                        tipo.codigo().equals(
                                categorias.get(i).codigo()
                        )
                ) {

                        indiceCategoria = i;
                        break;
                }
                }

                if (indiceCategoria < 0) {

                categorias.add(
                        new PtdiDocumentoCategoriaDTO(
                                tipo.codigo(),
                                tipo.nombre(),
                                List.of(archivo),
                                tipo.idTipoDocumento(),
                                tipo.obligatorio()
                        )
                );

                continue;
                }

                PtdiDocumentoCategoriaDTO categoriaActual =
                        categorias.get(
                                indiceCategoria
                        );

                List<PtdiDocumentoArchivoDTO> archivos =
                        new ArrayList<>(
                                categoriaActual.archivos() == null
                                        ? List.of()
                                        : categoriaActual.archivos()
                        );

                archivos.add(
                        archivo
                );

                categorias.set(
                        indiceCategoria,

                        new PtdiDocumentoCategoriaDTO(
                                tipo.codigo(),
                                tipo.nombre(),
                                List.copyOf(archivos),
                                tipo.idTipoDocumento(),
                                tipo.obligatorio()
                        )
                );
        }

        return new PtdiDocumentoContenidoDTO(
                CONTENT_VERSION,
                List.copyOf(categorias)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PtdiDocumentoCategoriaDTO> getAvailableCategories() {

        return tipoDocumentoContratacionRepository
                .findAllByOrderByIdAsc()
                .stream()
                .map(tipo -> {

                        String codigoCategoria =
                                Arrays.stream(
                                        PtdiDocumentoCategoriaEnum.values()
                                )
                                .filter(categoria ->
                                        categoria.getNombre()
                                                .trim()
                                                .equalsIgnoreCase(
                                                        tipo.getNombre().trim()
                                                )
                                )
                                .map(
                                        PtdiDocumentoCategoriaEnum::getCodigo
                                )
                                .findFirst()
                                .orElse(
                                        "TIDC_" + tipo.getId()
                                );

                        return new PtdiDocumentoCategoriaDTO(

                                codigoCategoria,

                                tipo.getNombre(),

                                List.of(),

                                tipo.getId(),

                                "1".equals(
                                        tipo.getObligatorio()
                                )
                        );
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<TipoDocumentoContratacionDTO> getDocumentTypes() {

        return tipoDocumentoContratacionRepository
                .findAllByOrderByIdAsc()
                .stream()
                .map(tipo ->

                        new TipoDocumentoContratacionDTO(

                                /*
                                * TIDC_ID
                                */
                                tipo.getId(),

                                /*
                                * TIDC_NOMBRE
                                */
                                tipo.getNombre(),

                                /*
                                * TIDC_DESCRIPCION
                                */
                                tipo.getDescripcion(),

                                /*
                                * TIDC_OBLIGATORIO
                                *
                                * 1 = Obligatorio
                                * 0 = Opcional
                                */
                                "1".equals(
                                        tipo.getObligatorio()
                                )
                        )
                )
                .toList();
        }
    
        @Override
        @Transactional
        public PtdiDocumentoContenidoDTO addFiles(
                Long idCargaDocente,
                Long idPlanTrabajoDigital,
                String codigoCategoria,
                String nombreCategoria,
                List<MultipartFile> archivos) {

        validateRequiredId(
                idCargaDocente,
                "La contratación es obligatoria"
        );

        validatePtdId(
                idPlanTrabajoDigital
        );

        PtdiDocumentoCategoriaEnum categoria =
                resolveCategory(
                        codigoCategoria
                );

        List<Long> tiposCoincidentes =
                tipoDocumentoContratacionRepository
                        .findAllByOrderByIdAsc()
                        .stream()
                        .filter(tipo ->
                                tipo.getNombre() != null
                                && categoria.getNombre()
                                        .trim()
                                        .equalsIgnoreCase(
                                                tipo.getNombre().trim()
                                        )
                        )
                        .map(tipo -> tipo.getId())
                        .toList();

        if (tiposCoincidentes.size() != 1) {

                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "No fue posible identificar de forma única "
                                + "el tipo documental en Oracle"
                );
        }

        Long idTipoDocumento =
                tiposCoincidentes.get(0);

        return addFilesPorTipo(
                idCargaDocente,
                idPlanTrabajoDigital,
                idTipoDocumento,
                archivos
        );
        }

        @Override 
        @Transactional
        public PtdiDocumentoContenidoDTO addFilesByType(
                Long idCargaDocente,
                Long idPlanTrabajoDigital,
                Long idTipoDocumento,
                List<MultipartFile> archivos) {

        return addFilesPorTipo(
                idCargaDocente,
                idPlanTrabajoDigital,
                idTipoDocumento,
                archivos
        );
        }

        private PtdiDocumentoContenidoDTO addFilesPorTipo(
                Long idCargaDocente,
                Long idPlanTrabajoDigital,
                Long idTipoDocumento,
                List<MultipartFile> archivos) {

        validateRequiredId(
                idCargaDocente,
                "La contratación es obligatoria"
        );

        validatePtdId(
                idPlanTrabajoDigital
        );

        validateRequiredId(
                idTipoDocumento,
                "El tipo documental es obligatorio"
        );

        tipoDocumentoContratacionRepository
                .findById(idTipoDocumento)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.BAD_REQUEST,
                                "El tipo documental seleccionado no existe en Oracle"
                        )
                );

        String codigoFisico =
                "TIDC_" + idTipoDocumento;        

        List<MultipartFile> archivosValidos =
                normalizeFiles(
                        archivos
                );

        if (archivosValidos.isEmpty()) {

                return getContent(
                        idPlanTrabajoDigital
                );
        }

        planTrabajoDigitalRepository
                .lockForDocumentUpdate(
                        idPlanTrabajoDigital
                )
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe el Plan de Trabajo Digital seleccionado"
                        )
                );

        List<Path> archivosCreados =
                new ArrayList<>();

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                                @Override
                                public void afterCompletion(int status) {

                                if (status !=
                                        TransactionSynchronization.STATUS_COMMITTED) {

                                        cleanupCreatedFiles(
                                                archivosCreados
                                        );
                                }
                                }
                        }
                );

        try {

                for (MultipartFile archivo : archivosValidos) {

                String idArchivo =
                        UUID.randomUUID().toString();

                Path storageDirectory =
                        buildStorageDirectory(
                                idCargaDocente,
                                idArchivo,
                                codigoFisico
                        );

                Files.createDirectories(
                        storageDirectory
                );

                PtdiDocumentoArchivoDTO archivoGuardado =
                        savePhysicalFile(
                                archivo,
                                idCargaDocente,
                                idArchivo,
                                codigoFisico,
                                storageDirectory,
                                archivosCreados,
                                idArchivo
                        );

                String jsonArchivo =
                        objectMapper.writeValueAsString(
                                archivoGuardado
                        );

                PtdiDocumentoEntity documento =
                        new PtdiDocumentoEntity();

                documento.setUniqueId(
                        idArchivo
                );

                documento.setIdPlanTrabajoDigital(
                        idPlanTrabajoDigital
                );

                documento.setIdTipoDocumentoContratacion(
                        idTipoDocumento
                );

                documento.setAdjunto(
                        jsonArchivo
                );

                documento.setFechaCambio(
                        new Date()
                );

                ptdiDocumentoRepository.save(
                        documento
                );

                log.info(
                        "addFiles ===> Documento individual preparado. "
                                + "idCargaDocente={}, "
                                + "idPlanTrabajoDigital={}, "
                                + "idTipoDocumento={}, "
                                + "idArchivo={}",
                        idCargaDocente,
                        idPlanTrabajoDigital,
                        idTipoDocumento,
                        idArchivo
                );
                }

                ptdiDocumentoRepository.flush();

                PtdiDocumentoContenidoDTO contenidoActualizado =
                        getContent(
                                idPlanTrabajoDigital
                        );

                log.info(
                        "addFiles ===> Carga documental individual completada. "
                                + "idCargaDocente={}, "
                                + "idPlanTrabajoDigital={}, "
                                + "idTipoDocumento={}, "
                                + "archivosAgregados={}",
                        idCargaDocente,
                        idPlanTrabajoDigital,
                        idTipoDocumento,
                        archivosValidos.size()
                );

                return contenidoActualizado;

        } catch (IOException ex) {

                cleanupCreatedFiles(
                        archivosCreados
                );

                log.error(
                        "addFiles ===> Error guardando documentos individuales. "
                                + "idCargaDocente={}, "
                                + "idPlanTrabajoDigital={}, "
                                + "idTipoDocumento={}",
                        idCargaDocente,
                        idPlanTrabajoDigital,
                        idTipoDocumento,
                        ex
                );

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "No fue posible guardar los documentos"
                );

        } catch (RuntimeException ex) {

                cleanupCreatedFiles(
                        archivosCreados
                );

                throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PtdiDocumentoArchivoDescargaDTO getFile(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String idArchivo) {

        validateRequiredId(
                idCargaDocente,
                "La contratación es obligatoria"
        );

        validatePtdId(
                idPlanTrabajoDigital
        );

        if (
                idArchivo == null
                || !idArchivo.matches(
                        "[0-9a-fA-F]{8}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{12}"
                )
        ) {

        throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "El identificador del archivo no es válido"
        );
        }

        PtdiDocumentoEntity documentoIndividual =
                ptdiDocumentoRepository
                        .findById(
                                idArchivo
                        )
                        .filter(documento ->
                                idPlanTrabajoDigital.equals(
                                        documento.getIdPlanTrabajoDigital()
                                )
                        )
                        .filter(documento ->
                                documento.getIdTipoDocumentoContratacion()
                                        != null
                        )
                        .orElse(null);

        PtdiDocumentoEntity documento;

        ArchivoLocalizado archivo;

        if (documentoIndividual != null) {

        documento =
                documentoIndividual;

        Long idTipoDocumento =
                documento.getIdTipoDocumentoContratacion();

        String codigoCategoria =
                "TIDC_" + idTipoDocumento;

        PtdiDocumentoArchivoDTO datosArchivo =
                parseIndividualFile(
                        documento
                );

        archivo =
                new ArchivoLocalizado(
                        codigoCategoria,
                        datosArchivo
                );

        } else {

        documento =
                ptdiDocumentoRepository
                        .findFirstByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNullOrderByFechaCambioDesc(
                                idPlanTrabajoDigital
                        )
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existen documentos para esta contratación"
                                )
                        );

        archivo =
                findFile(
                        documento,
                        idArchivo
                );
        }

        Path filePath =
                resolveDocumentFilePath(
                        idCargaDocente,
                        documento,
                        archivo
                );

        if (!Files.isRegularFile(
                filePath,
                LinkOption.NOFOLLOW_LINKS
        )) {

            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "El archivo solicitado no está disponible"
            );
        }

        try {

            Path root =
                    Paths.get(
                            ptdStoragePath
                    )
                    .toAbsolutePath()
                    .normalize();

            Path recorrido = root;

            for (Path segmento : root.relativize(filePath)) {

                recorrido = recorrido.resolve(segmento);

                if (Files.isSymbolicLink(recorrido)) {

                    throw new ApiException(
                            HttpStatus.FORBIDDEN,
                            "La ruta del documento no está autorizada"
                    );
                }
            }

            Path rootReal = root.toRealPath();

            Path fileReal = filePath.toRealPath();

            if (!fileReal.startsWith(rootReal)) {

                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "Acceso al documento no autorizado"
                );
            }

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.isReadable()) {

                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "El archivo solicitado no se puede leer"
                );
            }

            return new PtdiDocumentoArchivoDescargaDTO(
                    resource,
                    archivo.datos().nombre()
            );

        } catch (IOException ex) {

            log.error(
                    "getFile ===> Error recuperando documento PTD. "
                            + "idCargaDocente={}, "
                            + "idPlanTrabajoDigital={}, "
                            + "idArchivo={}",
                    idCargaDocente,
                    idPlanTrabajoDigital,
                    idArchivo,
                    ex
            );

            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "El archivo solicitado no está disponible"
            );
        }
    }

    @Override
    @Transactional
    public PtdiDocumentoContenidoDTO deleteFile(
            Long idCargaDocente,
            Long idPlanTrabajoDigital,
            String idArchivo) {

        validateRequiredId(
                idCargaDocente,
                "La contratación es obligatoria"
        );

        validatePtdId(
                idPlanTrabajoDigital
        );

        planTrabajoDigitalRepository
                .lockForDocumentUpdate(
                        idPlanTrabajoDigital
                )
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "No existe el Plan de Trabajo Digital seleccionado"
                        )
                );

        if (!StringUtils.hasText(idArchivo)) {

        throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "El identificador del archivo es obligatorio"
        );
        }

        PtdiDocumentoEntity documentoIndividual =
                ptdiDocumentoRepository
                        .findById(idArchivo)
                        .filter(documentoEncontrado ->
                                idPlanTrabajoDigital.equals(
                                        documentoEncontrado
                                                .getIdPlanTrabajoDigital()
                                )
                        )
                        .filter(documentoEncontrado ->
                                documentoEncontrado
                                        .getIdTipoDocumentoContratacion() != null
                        )
                        .orElse(null);

        if (documentoIndividual != null) {

        return deleteIndividualFile(
                idCargaDocente,
                idPlanTrabajoDigital,
                idArchivo,
                documentoIndividual
        );
        }               

        PtdiDocumentoEntity documento =
                ptdiDocumentoRepository
                        .findFirstByIdPlanTrabajoDigitalAndIdTipoDocumentoContratacionIsNullOrderByFechaCambioDesc(
                                idPlanTrabajoDigital
                        )
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existen documentos para esta contratación"
                                )
                        );

        ArchivoLocalizado archivoLocalizado =
                findFile(
                        documento,
                        idArchivo
                );

        Path archivoFisico =
                resolveDocumentFilePath(
                        idCargaDocente,
                        documento,
                        archivoLocalizado
                );

        validateDeletePath(
                archivoFisico
        );

        PtdiDocumentoContenidoDTO contenidoActual =
                parseContent(
                        documento.getAdjunto()
                );

        List<PtdiDocumentoCategoriaDTO>
                categoriasActualizadas =
                        new ArrayList<>();

        boolean archivoEliminado = false;

        for (
                PtdiDocumentoCategoriaDTO categoria :
                contenidoActual.categorias()
        ) {

            if (
                    categoria == null
                    || categoria.codigo() == null
            ) {

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El contenido documental almacenado no es válido"
                );
            }

            if (
                    !categoria.codigo()
                            .equalsIgnoreCase(
                                    archivoLocalizado.categoria()
                            )
            ) {

                categoriasActualizadas.add(
                        categoria
                );

                continue;
            }

            List<PtdiDocumentoArchivoDTO>
                    archivosRestantes =
                    new ArrayList<>(
                            categoria.archivos() == null
                                    ? List.of()
                                    : categoria.archivos()
                    );

            boolean removido =
                    archivosRestantes.removeIf(
                            archivo ->
                                    archivo != null
                                    && idArchivo.equals(
                                            archivo.id()
                                    )
                    );

            if (removido) {

                archivoEliminado = true;
            }

            categoriasActualizadas.add(
                    new PtdiDocumentoCategoriaDTO(
                            categoria.codigo(),
                            categoria.nombre(),
                            List.copyOf(
                                    archivosRestantes
                            )
                    )
            );
        }

        if (!archivoEliminado) {

            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "El documento solicitado no está registrado"
            );
        }

        PtdiDocumentoContenidoDTO
                contenidoActualizado =
                new PtdiDocumentoContenidoDTO(
                        contenidoActual.version(),
                        List.copyOf(
                                categoriasActualizadas
                        )
                );

        try {

            String json =
                    objectMapper.writeValueAsString(
                            contenidoActualizado
                    );

            documento.setAdjunto(
                    json
            );

        } catch (JsonProcessingException ex) {

            log.error(
                    "deleteFile ===> Error serializando JSON PTD. "
                            + "idArchivo={}",
                    idArchivo,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible actualizar los documentos"
            );
        }

        documento.setFechaCambio(
                new Date()
        );

        ptdiDocumentoRepository
                .saveAndFlush(
                        documento
                );

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCommit() {

                                try {

                                    validateDeletePath(
                                            archivoFisico
                                    );

                                    boolean eliminado =
                                            Files.deleteIfExists(
                                                    archivoFisico
                                            );

                                    if (!eliminado) {

                                        log.warn(
                                                "deleteFile ===> El archivo físico "
                                                        + "ya no existe. "
                                                        + "idArchivo={}, path={}",
                                                idArchivo,
                                                archivoFisico
                                        );
                                    }

                                } catch (
                                        IOException
                                        | RuntimeException ex
                                ) {
                                    log.error(
                                            "deleteFile ===> No fue posible "
                                                    + "eliminar el archivo físico "
                                                    + "después del commit. "
                                                    + "idArchivo={}, path={}",
                                            idArchivo,
                                            archivoFisico,
                                            ex
                                    );
                                }
                            }
                        }
                );

        log.info(
                "deleteFile ===> Eliminación documental registrada. "
                        + "idCargaDocente={}, "
                        + "idPlanTrabajoDigital={}, "
                        + "idArchivo={}",
                idCargaDocente,
                idPlanTrabajoDigital,
                idArchivo
        );

        return getContent(
                idPlanTrabajoDigital
        );
    }

       
    private PtdiDocumentoContenidoDTO deleteIndividualFile(
                Long idCargaDocente,
                Long idPlanTrabajoDigital,
                String idArchivo,
                PtdiDocumentoEntity documento) {

        if (StringUtils.hasText(documento.getEstado())) {

                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "El documento ya tiene un estado de revisión y no puede eliminarse"
                );
        }

        PtdiDocumentoArchivoDTO datosArchivo =
                parseIndividualFile(
                        documento
                );

        String codigoCategoria =
                "TIDC_"
                        + documento
                                .getIdTipoDocumentoContratacion();

        ArchivoLocalizado archivoLocalizado =
                new ArchivoLocalizado(
                        codigoCategoria,
                        datosArchivo
                );

        Path archivoFisico =
                resolveDocumentFilePath(
                        idCargaDocente,
                        documento,
                        archivoLocalizado
                );

        validateDeletePath(
                archivoFisico
        );

        ptdiDocumentoRepository.delete(
                documento
        );

        ptdiDocumentoRepository.flush();

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                                @Override
                                public void afterCommit() {

                                try {

                                        /*
                                        * Verificar nuevamente
                                        * la ruta física.
                                        */
                                        validateDeletePath(
                                                archivoFisico
                                        );

                                        /*
                                        * Eliminar el archivo.
                                        */
                                        boolean eliminado =
                                                Files.deleteIfExists(
                                                        archivoFisico
                                                );

                                        if (!eliminado) {

                                        log.warn(
                                                "deleteIndividualFile ===> "
                                                        + "El archivo físico ya no existe. "
                                                        + "idArchivo={}, path={}",
                                                idArchivo,
                                                archivoFisico
                                        );
                                        }

                                } catch (
                                        IOException
                                        | RuntimeException ex
                                ) {

                                        log.error(
                                                "deleteIndividualFile ===> "
                                                        + "Error eliminando archivo físico "
                                                        + "después del commit. "
                                                        + "idArchivo={}, path={}",
                                                idArchivo,
                                                archivoFisico,
                                                ex
                                        );
                                }
                                }
                        }
                );

        log.info(
                "deleteIndividualFile ===> Documento eliminado. "
                        + "idCargaDocente={}, "
                        + "idPlanTrabajoDigital={}, "
                        + "idArchivo={}, "
                        + "idTipoDocumento={}",
                idCargaDocente,
                idPlanTrabajoDigital,
                idArchivo,
                documento.getIdTipoDocumentoContratacion()
        );

        return getContent(
                idPlanTrabajoDigital
        );
    }    

    private void validateDeletePath(
            Path archivoFisico) {

        Path root =
                Paths.get(
                        ptdStoragePath
                )
                .toAbsolutePath()
                .normalize();

        Path file =
                archivoFisico
                        .toAbsolutePath()
                        .normalize();

        if (
                !file.startsWith(root)
                || file.equals(root)
        ) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La ruta del documento no está autorizada"
            );
        }

        if (Files.isSymbolicLink(root)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La ruta de almacenamiento no está autorizada"
            );
        }

        Path recorrido = root;

        for (
                Path segmento :
                root.relativize(file)
        ) {

            recorrido =
                    recorrido.resolve(
                            segmento
                    );

            if (
                    Files.isSymbolicLink(
                            recorrido
                    )
            ) {

                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "La ruta del documento contiene un enlace no autorizado"
                );
            }
        }
    }

    private record ArchivoLocalizado(
            String categoria,
            PtdiDocumentoArchivoDTO datos
    ) {
    }

    private ArchivoLocalizado findFile(
            PtdiDocumentoEntity documento,
            String idArchivo) {

        if (
                idArchivo == null
                || !idArchivo.matches(
                        "[0-9a-fA-F]{8}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{12}"
                )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador del archivo no es válido"
            );
        }

        PtdiDocumentoContenidoDTO contenido =
                parseContent(
                        documento.getAdjunto()
                );

        for (
                PtdiDocumentoCategoriaDTO categoria :
                contenido.categorias()
        ) {

            if (
                    categoria == null
                    || categoria.archivos() == null
            ) {
                continue;
            }

            for (
                    PtdiDocumentoArchivoDTO archivo :
                    categoria.archivos()
            ) {

                if (
                        archivo != null
                        && idArchivo.equals(
                                archivo.id()
                        )
                ) {

                    return new ArchivoLocalizado(
                            categoria.codigo(),
                            archivo
                    );
                }
            }
        }

        throw new ApiException(
                HttpStatus.NOT_FOUND,
                "El documento solicitado no está registrado"
        );
    }

    private Path resolveDocumentFilePath(
            Long idCargaDocente,
            PtdiDocumentoEntity documento,
            ArchivoLocalizado archivo) {

        String codigoCategoria =
                normalizeCategoryCode(
                        archivo.categoria()
                );

        if (!codigoCategoria.equals(archivo.categoria())) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La categoría almacenada no es válida"
            );
        }

        String uniqueId =
                documento.getUniqueId();

        if (
                uniqueId == null
                || !uniqueId.matches(
                        "[0-9a-fA-F]{8}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{4}-"
                        + "[0-9a-fA-F]{12}"
                )
        ) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "El paquete documental no es válido"
            );
        }

        String extension =
                archivo.datos().extension();

        if (
                extension != null
                && !extension.matches("[a-z0-9]+")
        ) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La extensión del archivo no es válida"
            );
        }

        String nombreFisico =
                archivo.datos().id()
                        + (
                                extension == null
                                        ? ""
                                        : "." + extension
                        );

       
        String rutaEsperada =
                RELATIVE_ROOT
                        + "/"
                        + idCargaDocente
                        + "/"
                        + uniqueId
                        + "/"
                        + codigoCategoria
                        + "/"
                        + nombreFisico;

        if (!rutaEsperada.equals(
                archivo.datos().path()
        )) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La ruta del documento no está autorizada"
            );
        }

        Path directory =
                buildStorageDirectory(
                        idCargaDocente,
                        uniqueId,
                        codigoCategoria
                );

        Path filePath =
                directory
                        .resolve(nombreFisico)
                        .normalize();

        if (!filePath.startsWith(directory)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La ruta del documento no es válida"
            );
        }

        return filePath;
    }


    private PtdiDocumentoEntity newDocument(
            Long idPlanTrabajoDigital) {

        PtdiDocumentoEntity documento =
                new PtdiDocumentoEntity();

        documento.setUniqueId(
                UUID.randomUUID()
                        .toString()
        );

        documento.setIdPlanTrabajoDigital(
                idPlanTrabajoDigital
        );

        documento.setAdjunto(
                null
        );

        documento.setFechaCambio(
                new Date()
        );

        return documento;
    }


    private PtdiDocumentoArchivoDTO savePhysicalFile(
                MultipartFile archivo,
                Long idCargaDocente,
                String uniqueIdDocumento,
                String codigoCategoria,
                Path storageDirectory,
                List<Path> archivosCreados,
                String idArchivo)
                throws IOException {

        String nombreOriginal =
                archivo.getOriginalFilename();

        if (!StringUtils.hasText(
                nombreOriginal
        )) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Uno de los archivos no tiene un nombre válido"
            );
        }

        /*
         * Evita que un nombre suministrado
         * por el cliente contenga rutas.
         */
        String nombreSeguro =
                Path.of(
                        nombreOriginal
                )
                .getFileName()
                .toString();

        String extension =
                getExtension(
                        nombreSeguro
                );

        String nombreFisico =
                extension == null
                        ? idArchivo
                        : idArchivo
                                + "."
                                + extension;

        Path destino =
                storageDirectory
                        .resolve(
                                nombreFisico
                        )
                        .normalize();

        if (!destino.startsWith(
                storageDirectory
        )) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La ruta del archivo no es válida"
            );
        }

        archivosCreados.add(destino);

        try (var inputStream = archivo.getInputStream()) {

            Files.copy(
                    inputStream,
                    destino
            );
        }

        String pathRelativo =
                RELATIVE_ROOT
                        + "/"
                        + idCargaDocente
                        + "/"
                        + uniqueIdDocumento
                        + "/"
                        + codigoCategoria
                        + "/"
                        + nombreFisico;

        return new PtdiDocumentoArchivoDTO(
                idArchivo,
                nombreSeguro,
                pathRelativo,
                extension,
                archivo.getSize()
        );
    }


    private Path buildStorageDirectory(
            Long idCargaDocente,
            String uniqueIdDocumento,
            String codigoCategoria) {

        Path root =
                Paths.get(
                        ptdStoragePath
                )
                .toAbsolutePath()
                .normalize();

        Path directory =
                root
                        .resolve(
                                idCargaDocente
                                        .toString()
                        )
                        .resolve(
                                uniqueIdDocumento
                        )
                        .resolve(
                                codigoCategoria
                        )
                        .normalize();

        if (!directory.startsWith(
                root
        )) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La ruta de almacenamiento no es válida"
            );
        }

        return directory;
    }


    private PtdiDocumentoContenidoDTO mergeCategory(
            PtdiDocumentoContenidoDTO contenidoActual,
            String codigoCategoria,
            String nombreCategoria,
            List<PtdiDocumentoArchivoDTO>
                    nuevosArchivos) {

        List<PtdiDocumentoCategoriaDTO>
                categorias =
                new ArrayList<>(
                        contenidoActual.categorias()
                                == null
                                ? List.of()
                                : contenidoActual
                                        .categorias()
                );

        int index = -1;

        for (
                int i = 0;
                i < categorias.size();
                i++
        ) {

            PtdiDocumentoCategoriaDTO
                    categoria =
                    categorias.get(i);

            if (
                    categoria.codigo() != null
                    && categoria.codigo()
                            .equalsIgnoreCase(
                                    codigoCategoria
                            )
            ) {
                index = i;
                break;
            }
        }

        if (index < 0) {

            categorias.add(
                    new PtdiDocumentoCategoriaDTO(
                            codigoCategoria,
                            nombreCategoria,
                            List.copyOf(
                                    nuevosArchivos
                            )
                    )
            );

        } else {

            PtdiDocumentoCategoriaDTO
                    categoriaActual =
                    categorias.get(
                            index
                    );

            List<PtdiDocumentoArchivoDTO>
                    archivos =
                    new ArrayList<>(
                            categoriaActual
                                    .archivos()
                                    == null
                                    ? List.of()
                                    : categoriaActual
                                            .archivos()
                    );

            /*
             * AQUÍ está la diferencia principal
             * frente a reemplazar el contenido.
             *
             * Siempre agregamos.
             */
            archivos.addAll(
                    nuevosArchivos
            );

            categorias.set(
                    index,
                    new PtdiDocumentoCategoriaDTO(
                            codigoCategoria,
                            nombreCategoria,
                            List.copyOf(
                                    archivos
                            )
                    )
            );
        }

        return new PtdiDocumentoContenidoDTO(
                CONTENT_VERSION,
                List.copyOf(
                        categorias
                )
        );
    }


    private PtdiDocumentoArchivoDTO parseIndividualFile(
                PtdiDocumentoEntity documento) {

        String json =
                documento.getAdjunto();

        if (!StringUtils.hasText(json)) {

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El documento individual no tiene información de archivo"
                );
        }

        try {

                PtdiDocumentoArchivoDTO archivo =
                        objectMapper.readValue(
                                json,
                                PtdiDocumentoArchivoDTO.class
                        );

                if (
                        archivo == null
                        || archivo.id() == null
                        || !archivo.id().equals(
                                documento.getUniqueId()
                        )
                        || !StringUtils.hasText(
                                archivo.nombre()
                        )
                        || !StringUtils.hasText(
                                archivo.path()
                        )
                ) {

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La información del documento individual no es válida"
                );
                }

                return new PtdiDocumentoArchivoDTO(
                        archivo.id(),
                        archivo.nombre(),
                        archivo.path(),
                        archivo.extension(),
                        archivo.tamano(),
                        documento.getEstado()
                );

        } catch (JsonProcessingException ex) {

                log.error(
                        "parseIndividualFile ===> "
                                + "Error leyendo documento individual. "
                                + "uniqueId={}",
                        documento.getUniqueId(),
                        ex
                );

                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "No fue posible leer el documento individual"
                );
        }
    }    


    private PtdiDocumentoContenidoDTO parseContent(
            String json) {

        if (!StringUtils.hasText(
                json
        )) {
            return emptyContent();
        }

        try {

            PtdiDocumentoContenidoDTO
                    contenido =
                    objectMapper
                            .readValue(
                                    json,
                                    PtdiDocumentoContenidoDTO.class
                            );

            if (contenido == null) {
                return emptyContent();
            }

            return new PtdiDocumentoContenidoDTO(
                    contenido.version() == null
                            ? CONTENT_VERSION
                            : contenido.version(),
                    contenido.categorias() == null
                            ? List.of()
                            : contenido.categorias()
            );

        } catch (
                JsonProcessingException ex
        ) {

            log.error(
                    "parseContent ===> Error leyendo JSON de documentos PTD",
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible leer la información de los documentos"
            );
        }
    }


    private PtdiDocumentoContenidoDTO emptyContent() {

        return new PtdiDocumentoContenidoDTO(
                CONTENT_VERSION,
                List.of()
        );
    }


    private List<MultipartFile> normalizeFiles(
            List<MultipartFile> archivos) {

        if (
                archivos == null
                || archivos.isEmpty()
        ) {
            return List.of();
        }

        return archivos
                .stream()
                .filter(
                        archivo ->
                                archivo != null
                                && !archivo.isEmpty()
                )
                .toList();
    }


    private String normalizeCategoryCode(
            String codigo) {

        if (!StringUtils.hasText(
                codigo
        )) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría del documento es obligatoria"
            );
        }

        String value =
                Normalizer
                        .normalize(
                                codigo.trim(),
                                Normalizer.Form.NFD
                        )
                        .replaceAll(
                                "\\p{M}",
                                ""
                        )
                        .toUpperCase(
                                Locale.ROOT
                        )
                        .replaceAll(
                                "[^A-Z0-9_-]+",
                                "_"
                        )
                        .replaceAll(
                                "_+",
                                "_"
                        )
                        .replaceAll(
                                "^_+|_+$",
                                ""
                        );

        if (value.isBlank()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría del documento no es válida"
            );
        }

        return value;
    }

    private PtdiDocumentoCategoriaEnum resolveCategory(
            String codigoCategoria) {

        String codigoSeguro =
                normalizeCategoryCode(
                        codigoCategoria
                );

        try {

            return PtdiDocumentoCategoriaEnum.valueOf(
                    codigoSeguro
            );

        } catch (IllegalArgumentException ex) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría de documentos no está habilitada"
            );
        }
    }

    private String getExtension(
            String filename) {

        int index =
                filename.lastIndexOf(
                        '.'
                );

        if (
                index <= 0
                || index
                        == filename.length() - 1
        ) {
            return null;
        }

        String extension =
                filename
                        .substring(
                                index + 1
                        )
                        .toLowerCase(
                                Locale.ROOT
                        )
                        .replaceAll(
                                "[^a-z0-9]",
                                ""
                        );

        return extension.isBlank()
                ? null
                : extension;
    }


    private void validatePtdId(
            Long idPlanTrabajoDigital) {

        validateRequiredId(
                idPlanTrabajoDigital,
                "El Plan de Trabajo Digital es obligatorio"
        );

        if (
                !planTrabajoDigitalRepository
                        .existsById(
                                idPlanTrabajoDigital
                        )
        ) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe el Plan de Trabajo Digital seleccionado"
            );
        }
    }


    private void validateRequiredId(
            Long value,
            String message) {

        if (
                value == null
                || value <= 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    message
            );
        }
    }


    private void cleanupCreatedFiles(
            List<Path> files) {

        for (Path file : files) {

            try {

                Files.deleteIfExists(
                        file
                );

            } catch (IOException cleanupException) {

                log.warn(
                        "cleanupCreatedFiles ===> "
                                + "No fue posible eliminar archivo temporal {}",
                        file,
                        cleanupException
                );
            }
        }
    }
}