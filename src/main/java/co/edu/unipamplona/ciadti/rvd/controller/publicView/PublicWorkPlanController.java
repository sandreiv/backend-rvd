package co.edu.unipamplona.ciadti.rvd.controller.publicView;

import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.Valid;

import co.edu.unipamplona.ciadti.rvd.config.security.docente.DocenteSecurity;
import co.edu.unipamplona.ciadti.rvd.model.dto.PaisGeneralDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicLevelDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTitleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicAcademicTrainingSaveDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicBasicInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicHiringRecordDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicActivitiesDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PublicDigitalWorkPlanDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PublicWorkPlanService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/work-plan")
public class PublicWorkPlanController {

        private final CoordinacionService coordinacionService;
        private final PublicWorkPlanService publicWorkPlanService;

        @Operation(summary = "Lista las contrataciones del docente autenticado", description = """
                        Lista únicamente los registros de contratación
                        correspondientes al docente autenticado mediante
                        la sesión pública.

                        El PEGE_ID se obtiene del JWT del docente y no se
                        recibe como parámetro.

                        Se consideran únicamente cargas aprobadas que
                        pertenecen a convocatorias de preasignación
                        relacionadas con una convocatoria activa de
                        contratación y cuyo estado de carga sea
                        AVAL DESARROLLO.

                        Las novedades rechazadas no modifican la fotografía
                        efectiva utilizada para el listado.
                        """)
        @GetMapping("/hiring-records")
        public ResponseEntity<List<PublicHiringRecordDTO>> listHiringRecords() {

                Long idPersonaGeneral = DocenteSecurity.requireIdPersonaGeneral();

                List<PublicHiringRecordDTO> records = coordinacionService
                                .findPublicHiringRecords(
                                                idPersonaGeneral);

                return ResponseEntity.ok(records);
        }

        @Operation(summary = "Consulta la información básica del docente", description = """
                        Consulta la información registrada del docente
                        autenticado en PERSONAGENERAL y
                        PERSONANATURALGENERAL.

                        La contratación seleccionada debe pertenecer
                        al docente autenticado y encontrarse disponible
                        dentro del flujo público de contratación.

                        El PEGE_ID se obtiene exclusivamente del JWT.
                        """)
        @GetMapping("/hiring-records/{idCargaDocente}/basic-information")
        public ResponseEntity<PublicBasicInformationDTO> getBasicInformation(
                        @PathVariable Long idCargaDocente) {

                Long idPersonaGeneral = DocenteSecurity.requireIdPersonaGeneral();

                return ResponseEntity.ok(
                                publicWorkPlanService
                                                .getBasicInformation(
                                                                idCargaDocente,
                                                                idPersonaGeneral));
        }

        @Operation(summary = "Lista los niveles académicos disponibles", description = """
                        Consulta los niveles académicos registrados
                        en ACADEMICO.TITULODOCENTESNIES.

                        Se conserva la clasificación original del
                        catálogo, incluyendo el nivel SIN DEFINIR
                        identificado con TITD_ID = 0.
                        """)
        @GetMapping("/academic-training/levels")
        public ResponseEntity<List<PublicAcademicLevelDTO>> getAcademicLevels() {

                return ResponseEntity.ok(
                                publicWorkPlanService
                                                .getAcademicLevels());
        }

        @Operation(summary = "Busca títulos por nivel académico", description = """
                        Consulta títulos registrados en
                        GENERAL.TITULOGENERAL asociados al
                        nivel académico seleccionado.

                        La búsqueda se realiza a partir
                        de dos caracteres.
                        """)
        @GetMapping("/academic-training/titles")
        public ResponseEntity<List<PublicAcademicTitleDTO>> getAcademicTitles(
                        @RequestParam Long academicLevelId,
                        @RequestParam(defaultValue = "") String query) {

                return ResponseEntity.ok(
                                publicWorkPlanService
                                                .getAcademicTitles(
                                                                academicLevelId,
                                                                query));
        }

        @Operation(summary = "Lista los países disponibles", description = """
                        Consulta los países registrados en
                        GENERAL.PAISGENERAL para el formulario
                        de formación académica.

                        No se incluye el registro Ninguno.
                        """)
        @GetMapping("/academic-training/countries")
        public ResponseEntity<List<PaisGeneralDTO>> getCountries() {

                return ResponseEntity.ok(
                                publicWorkPlanService
                                                .getCountries());
        }

        @Operation(summary = "Registra la formación académica del docente autenticado", description = """
                        Registra un título académico para el docente autenticado.

                        El PEGE_ID se obtiene exclusivamente del JWT.

                        La contratación seleccionada debe pertenecer
                        al docente autenticado.

                        El TPNG_ID es generado por el trigger
                        GENERAL.TR_TPNG_ID.
                        """)
        @PostMapping("/hiring-records/{idCargaDocente}/academic-training")
        public ResponseEntity<PublicAcademicTrainingDTO> saveAcademicTraining(
                        @PathVariable Long idCargaDocente,

                        @Valid @RequestBody PublicAcademicTrainingSaveDTO body) {

                Long idPersonaGeneral = DocenteSecurity.requireIdPersonaGeneral();

                PublicAcademicTrainingDTO result = publicWorkPlanService
                                .saveAcademicTraining(
                                                idCargaDocente,
                                                idPersonaGeneral,
                                                body);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(result);
        }

        @Operation(summary = "Consulta la última formación académica registrada", description = """
                        Consulta el último registro de formación académica
                        del docente autenticado.

                        La contratación seleccionada debe pertenecer
                        al docente autenticado y el PEGE_ID se obtiene
                        exclusivamente del JWT.

                        Si el docente aún no tiene formación académica
                        registrada, la respuesta no contiene contenido.
                        """)
        @GetMapping("/hiring-records/{idCargaDocente}/academic-training/latest")
        public ResponseEntity<PublicAcademicTrainingDTO> getLatestAcademicTraining(
                        @PathVariable Long idCargaDocente) {

                Long idPersonaGeneral = DocenteSecurity.requireIdPersonaGeneral();

                PublicAcademicTrainingDTO result = publicWorkPlanService
                                .getLatestAcademicTraining(
                                                idCargaDocente,
                                                idPersonaGeneral);

                if (result == null) {
                        return ResponseEntity
                                        .noContent()
                                        .build();
                }

                return ResponseEntity.ok(result);
        }

        @Operation(summary = "Consulta las actividades efectivas de la contratación", description = """
                        Consulta las actividades asociadas a la contratación
                        seleccionada del docente autenticado.

                        La contratación debe pertenecer al docente autenticado.

                        Para la fotografía de actividades se utilizan los detalles
                        de novedad vigentes cuando existen registros con
                        DNCD_VIGENTE = 1.

                        Si no existe una fotografía vigente de novedad,
                        se utilizan los registros originales de
                        DETALLECARGADOCENTE.

                        Las novedades rechazadas o en revisión no modifican
                        la fotografía efectiva de actividades.
                        """)
        @GetMapping("/hiring-records/{idCargaDocente}/activities")
        public ResponseEntity<PublicActivitiesDTO>getActivities(
                        @PathVariable Long idCargaDocente) {

                Long idPersonaGeneral = DocenteSecurity
                                .requireIdPersonaGeneral();

                return ResponseEntity.ok(
                                publicWorkPlanService
                                                .getActivities(
                                                                idCargaDocente,
                                                                idPersonaGeneral));
        }

        @Operation(
        summary = "Consulta el Plan de Trabajo Digital del docente",
        description = """
                Consulta el Plan de Trabajo Digital asociado
                al docente autenticado y a la contratación
                seleccionada.

                Las actividades se construyen utilizando
                la misma fotografía efectiva utilizada
                por el módulo administrativo, teniendo
                en cuenta los detalles vigentes de novedad
                cuando correspondan.

                Las ponderaciones y el nivel se obtienen
                de los registros almacenados en
                RVD.PLANTRABAJODIGITAL.

                La Vista Pública no recalcula ni modifica
                las ponderaciones del PTD.
                """
        )
        @GetMapping(
        "/hiring-records/{idCargaDocente}/digital-work-plan"
        )
        public ResponseEntity<PublicDigitalWorkPlanDTO>
        getDigitalWorkPlan(
                @PathVariable Long idCargaDocente) {

        Long idPersonaGeneral =
                DocenteSecurity
                        .requireIdPersonaGeneral();

        PublicDigitalWorkPlanDTO result =
                publicWorkPlanService
                        .getDigitalWorkPlan(
                                idCargaDocente,
                                idPersonaGeneral
                        );

        if (result == null) {
                return ResponseEntity
                        .noContent()
                        .build();
        }

        return ResponseEntity.ok(result);
        }

        @Operation(
                summary = "Consulta las categorías de documentos de soporte",
                description = """
                        Obtiene las categorías oficiales de documentos
                        de soporte definidas en el enum del backend.

                        La información permite construir dinámicamente
                        las filas de carga de documentos en la Vista Pública.

                        No consulta documentos almacenados ni modifica
                        información de Oracle.

                        Requiere autenticación del docente.
                        """
        )
        @GetMapping("/support-documents/categories")
        public ResponseEntity<List<PtdiDocumentoCategoriaDTO>>
                getSupportDocumentCategories() {

        DocenteSecurity.requireIdPersonaGeneral();

        return ResponseEntity.ok(
                publicWorkPlanService
                        .getSupportDocumentCategories()
        );
        }

        @Operation(
        summary = "Consulta los documentos de soporte de la contratación",
        description = """
                Consulta el paquete documental asociado
                al Plan de Trabajo Digital de la contratación
                seleccionada del docente autenticado.
                """
        )
        @GetMapping(
        "/hiring-records/{idCargaDocente}/support-documents"
        )
        public ResponseEntity<PtdiDocumentoContenidoDTO>
        getSupportDocuments(
                @PathVariable Long idCargaDocente) {

        Long idPersonaGeneral =
                DocenteSecurity
                        .requireIdPersonaGeneral();

        return ResponseEntity.ok(
                publicWorkPlanService
                        .getSupportDocuments(
                                idCargaDocente,
                                idPersonaGeneral
                        )
        );
        }

        @Operation(
                summary = "Visualiza o descarga un documento de soporte",
                description = """
                        Recupera un documento registrado en el
                        paquete documental de la contratación
                        seleccionada del docente autenticado.

                        El PEGE_ID se obtiene exclusivamente
                        de la sesión del docente.

                        El PTDI_ID se resuelve internamente.

                        El archivo se identifica mediante
                        su UUID registrado en PTDI_ADJUNTO.

                        No se reciben rutas físicas
                        desde el cliente.
                        """
        )
        @GetMapping(
                "/hiring-records/{idCargaDocente}/support-documents/{fileId}/file"
        )
        public ResponseEntity<Resource> getSupportDocumentFile(
                @PathVariable Long idCargaDocente,
                @PathVariable String fileId) {

        Long idPersonaGeneral =
                DocenteSecurity
                        .requireIdPersonaGeneral();

        PtdiDocumentoArchivoDescargaDTO documento =
                publicWorkPlanService
                        .getSupportDocumentFile(
                                idCargaDocente,
                                idPersonaGeneral,
                                fileId
                        );

        String nombreOriginal =
                documento.nombre();

        String nombreMinusculas =
                nombreOriginal.toLowerCase(
                        Locale.ROOT
                );

        MediaType contentType;

        if (nombreMinusculas.endsWith(".pdf")) {

                contentType =
                        MediaType.APPLICATION_PDF;

        } else if (nombreMinusculas.endsWith(".png")) {

                contentType =
                        MediaType.IMAGE_PNG;

        } else if (
                nombreMinusculas.endsWith(".jpg")
                || nombreMinusculas.endsWith(".jpeg")
        ) {

                contentType =
                        MediaType.IMAGE_JPEG;

        } else {

                contentType =
                        MediaType.APPLICATION_OCTET_STREAM;
        }

        boolean visualizar =
                !MediaType.APPLICATION_OCTET_STREAM
                        .equals(contentType);

        ContentDisposition disposition =
                (
                        visualizar
                                ? ContentDisposition.inline()
                                : ContentDisposition.attachment()
                )
                        .filename(
                                nombreOriginal,
                                StandardCharsets.UTF_8
                        )
                        .build();

        return ResponseEntity.ok()
                .cacheControl(
                        CacheControl.noStore()
                )
                .header(
                        "X-Content-Type-Options",
                        "nosniff"
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .contentType(
                        contentType
                )
                .body(
                        documento.resource()
                );
        }

        @Operation(
                summary = "Elimina un documento de soporte",
                description = """
                        Elimina individualmente un documento
                        registrado en el paquete documental
                        de la contratación del docente autenticado.

                        El PEGE_ID se obtiene desde la sesión.

                        El PTDI_ID se resuelve internamente.

                        El archivo se identifica por su UUID.

                        Los demás documentos de la categoría
                        permanecen intactos.
                        """
        )
        @DeleteMapping(
                "/hiring-records/{idCargaDocente}/support-documents/{fileId}"
        )
        public ResponseEntity<PtdiDocumentoContenidoDTO>
                deleteSupportDocumentFile(
                        @PathVariable Long idCargaDocente,
                        @PathVariable String fileId) {

        Long idPersonaGeneral =
                DocenteSecurity
                        .requireIdPersonaGeneral();

        PtdiDocumentoContenidoDTO resultado =
                publicWorkPlanService
                        .deleteSupportDocumentFile(
                                idCargaDocente,
                                idPersonaGeneral,
                                fileId
                        );

        return ResponseEntity.ok(
                resultado
        );
        }

        @Operation(
        summary = "Agrega documentos de soporte a una categoría",
        description = """
                Agrega uno o varios archivos a una categoría
                del paquete documental del docente.

                Los archivos previamente registrados no se
                sustituyen. La operación es acumulativa y no
                establece un límite funcional de cantidad de
                documentos por categoría.
                """
        )
        @PostMapping(
        value = "/hiring-records/{idCargaDocente}/support-documents",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public ResponseEntity<PtdiDocumentoContenidoDTO>
        addSupportDocuments(
                @PathVariable
                Long idCargaDocente,

                @RequestParam
                String codigoCategoria,

                @RequestParam(required = false)
                String nombreCategoria,

                @RequestParam("archivos")
                List<MultipartFile> archivos) {

        Long idPersonaGeneral =
                DocenteSecurity
                        .requireIdPersonaGeneral();

        return ResponseEntity.ok(
                publicWorkPlanService
                        .addSupportDocuments(
                                idCargaDocente,
                                idPersonaGeneral,
                                codigoCategoria,
                                nombreCategoria,
                                archivos
                        )
        );
        }





}