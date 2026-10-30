/**
 * Aplicación: rvd
 * Archivo: HiringCallController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/09/2026
 * Modificaciones:
 * 07/09/2026 - Sebastian Jaimes - Creación inicial
 * 10/09/2026 - Sebastian Jaimes - Coordinaciones vía CONV_IDRELACION de contratación
 * 10/09/2026 - Sebastian Jaimes - Docentes aprobados (CADO_ESTADO = 4)
 * 07/10/2026 - Andrés Hernández - Los docentes para contratación vienen de los efectivos
 * 07/10/2026 - Andrés Hernández - Información del docente para contratación
 * 08/10/2026 - Sebastian Jaimes - Información del docente con actividades PTD
 * 14/10/2026 - Andrés Hernández - Almacenamiento del PTD
 * 26/10/2026 - Andrés Hernández - Gestión del PTD por Talento humano
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadPtdDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;
import co.edu.unipamplona.ciadti.rvd.model.dto.ApprovePtdRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteEfectivoCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringCdpInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringValuesInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSaveRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdStatesProfessorsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoArchivoDescargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoCategoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdiDocumentoContenidoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RejectPtdRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoReporteService;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import co.edu.unipamplona.ciadti.rvd.model.service.PlanTrabajoDigitalService;
import co.edu.unipamplona.ciadti.rvd.model.service.PtdiDocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/hiring-call")
public class HiringCallController {

    private final PeriodoUniversidadService periodoUniversidadService;
    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;
    private final PlanTrabajoDigitalService planTrabajoDigitalService;
    private final PtdiDocumentoService ptdiDocumentoService;
    private final ContratacionService contratacionService;
    private final ContratoReporteService contratoReporteService;

    @Operation(
        summary = "Obtiene la lista de periodos de universidad",
        description = "Obtiene la lista de periodos de universidad"
    )
    @GetMapping("/list-university-period")
    public ResponseEntity<List<PeriodoUniversidadDTO>> listUniversityPeriod() {
        List<PeriodoUniversidadDTO> periods = periodoUniversidadService.findUniversityPeriodList();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de convocatorias de contratación",
        description = "Obtiene las convocatorias de contratación (CONV_CONTRATACION = 1) del periodo"
    )
    @GetMapping("/list-active-calls")
    public ResponseEntity<List<ConvocatoriaDTO>> callList(@RequestParam Long idPeriodoUniversidad) {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findHiringCallListByPeriod(idPeriodoUniversidad);
        return new ResponseEntity<>(callList, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de coordinaciones",
        description = """
            Coordinador autenticado (JWT). El idConvocatoria es de contratación
            (CONV_CONTRATACION = 1). Se resuelve la preasignación vía CONV_IDRELACION
            y se listan las coordinaciones de esa preasignación en PERSONACOORDINACION
            cuya carga está en AVAL DESARROLLO.
            Sin idConvocatoria: requiere idPeriodoUniversidad.
            """
    )
    @GetMapping("/list-coordinations")
    public ResponseEntity<List<CoordinacionDTO>> listCoordinations(@RequestParam(required = false) Long idConvocatoria, @RequestParam(required = false) Long idPeriodoUniversidad) {
        List<CoordinacionDTO> coordinations = coordinacionService.findHiringCoordinations(idConvocatoria, idPeriodoUniversidad);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los docentes aprobados de una carga según la modalidad de contratacion",
        description = """
            Lista docentes con CADO_ESTADO = 4 (aprobado)
            y con CARGADOCENTE de esa carga y modalidad.
            """
    )
    @GetMapping("/list-professors-modality")
    public ResponseEntity<List<DocenteEfectivoCoordinacionDTO>> listProfessors(@RequestParam Long idCarga, @RequestParam Long idModalidadContratacion) {
        List<DocenteEfectivoCoordinacionDTO> docentes = coordinacionService.listApprovedProfessorsForHiring(idCarga, idModalidadContratacion);
        return new ResponseEntity<>(docentes, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los docentes de planta aprobados de una carga",
        description = """
            Docentes de DOCENTESPLANTACOORDINACION de la coordinación de la carga
            con CARGADOCENTE aprobado y sin modalidad de contratación.
            """
    )
    @GetMapping("/list-career-professors")
    public ResponseEntity<List<DocenteEfectivoCoordinacionDTO>> listCareerProfessors(@RequestParam Long idCarga) {
        return new ResponseEntity<>(
                coordinacionService.listCareerApprovedProfessorsForHiring(idCarga),
                HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la información de contratación de un docente",
        description = """
            Datos de la persona (nombre, documento, dirección y correos),
            modalidad con fecha de inicio y fin, categoría del docente,
            puntos y horas de actividades PTD.
            Si la carga tiene una novedad vigente, los puntos, la modalidad,
            las fechas y la categoría salen de esa novedad; si no, de la carga.
            Las actividades salen de DETALLENOVEDADCARGADOCENTE si DNCD_VIGENTE = 1;
            si no hay vigentes (rechazada o solo carga), salen de DETALLECARGADOCENTE.
            """
    )
    @GetMapping("/professor-information")
    public ResponseEntity<InformacionDocenteContratacionDTO> professorInformation(@RequestParam Long idCargaDocente) {
        InformacionDocenteContratacionDTO information = coordinacionService.findProfessorInformation(idCargaDocente);
        return new ResponseEntity<>(information, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los tipos de actividad para el PTD",
        description = "Lista los tipos de actividad que son para gestionar el plan de trabajo digital"
    )
    @GetMapping("/list-ptd-activities")
    public ResponseEntity<List<ActividadPtdDTO>> listPtdActivities(@RequestParam Long idCargaDocente) {
        List<ActividadPtdDTO> actividades = coordinacionService.listPtdActivities(idCargaDocente);
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la información del CDP para generar el contrato",
        description = """
            Obtiene la información del CDP de TESORERIAV3 para la contratación de un docente. Contiene un resumen
            de la información almacenada allí dependiendo del idCdp guardado en RVD.SOLICITUDCDP para la facultad
            a la que pertenece la coordinación
            """
    )
    @GetMapping("/hiring-cdp-information")
    public ResponseEntity<HiringCdpInformationDTO> hiringCdpInformation(@RequestParam Long idPeriodoUniversitario, @RequestParam Long idCoordinacionHija) {
        HiringCdpInformationDTO cdp = contratacionService.hiringCdpInformation(idPeriodoUniversitario, idCoordinacionHija);
        return new ResponseEntity<>(cdp, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene los valores de contratación para generar el contrato",
        description = """
            Obtiene la información de los centros de costos y calculos salariales.
            Esta consulta funciona bajo la misma logica del Resumen de novedades, trayendo la
            información relevante de la novedad o de la carga original y entregando solamente
            los valores de contratación
            """
    )
    @GetMapping("/hiring-values-information")
    public ResponseEntity<HiringValuesInformationDTO> hiringValuesInformation(@RequestParam Long idCargaDocente) {
        HiringValuesInformationDTO valores = contratacionService.hiringValuesInformation(idCargaDocente);
        return new ResponseEntity<>(valores, HttpStatus.OK);
    }

    @Operation(
        summary = "Genera el anexo de contratación de un docente",
        description = """
            Genera el PDF del anexo de contratación a partir de la carga docente.
            Si el docente es el coordinador del programa de la carga se usa el anexo
            con funciones administrativas; en caso contrario el anexo de 4 meses.
            El parámetro opcional tipo (CUATRO_MESES | CON_FUNCIONES_ADMINISTRATIVAS)
            permite forzar cualquiera de los dos.
            """
    )
    @GetMapping("/contract-pdf-report/{idCargaDocente}")
    public ResponseEntity<byte[]> generateContractPdf(
            @PathVariable Long idCargaDocente,
            @RequestParam(required = false) String tipo) {
        FileDTO file = contratoReporteService.generateContractPdf(idCargaDocente, parseTipoContrato(tipo));
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file.content());
    }

    private TipoContratoDocente parseTipoContrato(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return null;
        }
        try {
            return TipoContratoDocente.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de contrato no válido: " + tipo
                            + ". Use CUATRO_MESES o CON_FUNCIONES_ADMINISTRATIVAS");
        }
    }

    @Operation(
        summary = "Guarda los detalles de las actividades del PTD",
        description = "Guarda los detalles de las actividades relacionadas al PTD de una persona"
    )
    @PostMapping("/save-ptd")
    public ResponseEntity<Void> savePtd(@RequestBody PtdSaveRequestDTO dto) {
        planTrabajoDigitalService.savePtd(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Objeto con la información del ptd y la especificación de la ponderación por actividades",
        description = "Objeto con la información de la novedad registrada por el coordinador y las ponderaciones de cada actividad"
    )
    @GetMapping("/get-ptd-with-weights")
    public ResponseEntity<PtdInfoWithWeightsDTO> getInfoPtdWithWeights(@RequestParam Long idPersonaGeneral, @RequestParam Long idCargaDocente, @RequestParam Long idPeriodoUniversitario) {
        PtdInfoWithWeightsDTO ptd = planTrabajoDigitalService.getInfoPtdWithWeights(idPersonaGeneral, idCargaDocente, idPeriodoUniversitario);
        return new ResponseEntity<>(ptd, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista con los estados del ptd por persona",
        description = "Lista que entrega el estado del ptd para las personas de cierto periodo. Util para bloquear o mostrar información según su estado"
    )
    @GetMapping("/get-ptd-states-professors")
    public ResponseEntity<List<PtdStatesProfessorsDTO>> getPtdStatesProfessors(@RequestParam Long idPeriodoUniversitario) {
        List<PtdStatesProfessorsDTO> states = planTrabajoDigitalService.getPtdStatesProfessors(idPeriodoUniversitario);
        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    @Operation(
        summary = "Objeto con los adjuntos pertenecientes al PTD",
        description = "Objeto con los adjuntos cargados por el docente para el PTD"
    )
    @GetMapping("/get-ptd-attachments")
    public ResponseEntity<PtdiDocumentoContenidoDTO> getPtdAttachments(@RequestParam Long idPersonaGeneral, @RequestParam Long idCargaDocente, @RequestParam Long idPeriodoUniversitario) {
        PtdiDocumentoContenidoDTO adjuntos = planTrabajoDigitalService.getPtdAttachments(idPersonaGeneral, idCargaDocente, idPeriodoUniversitario);
        return new ResponseEntity<>(adjuntos, HttpStatus.OK);
    }

    @Operation(
        summary = "Objeto con los adjuntos pertenecientes al PTD",
        description = "Objeto con los adjuntos cargados por el docente para el PTD"
    )
    @GetMapping("/get-ptd-attachment-categories")
    public ResponseEntity<List<PtdiDocumentoCategoriaDTO>> getPtdAttachmentCategories() {
        List<PtdiDocumentoCategoriaDTO> categorias = ptdiDocumentoService.getAvailableCategories();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @Operation(
        summary = "Visualiza o descarga un documento de soporte fuera de la vista pública",
        description = """
            Recupera un documento registrado en el paquete documental de la contratación.
            El PTDI_ID se resuelve internamente.
            El archivo se identifica mediante su UUID registrado en PTDI_ADJUNTO.
            No se reciben rutas físicas desde el cliente.
            """
        )
        @GetMapping("/get-ptd-document-file/{fileId}/file")
        public ResponseEntity<Resource> getPtdDocumentFile(@PathVariable String fileId, @RequestParam Long idPersonaGeneral, @RequestParam Long idCargaDocente, @RequestParam Long idPeriodoUniversitario) {
        
        PtdiDocumentoArchivoDescargaDTO documento =
            planTrabajoDigitalService.getPtdDocumentFile(idPersonaGeneral, idCargaDocente, idPeriodoUniversitario, fileId);

        String nombreOriginal = documento.nombre();
        String nombreMinusculas = nombreOriginal.toLowerCase(Locale.ROOT);
        MediaType contentType;

        if (nombreMinusculas.endsWith(".pdf")) {
            contentType = MediaType.APPLICATION_PDF;
        } else if (nombreMinusculas.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG;
        } else if (nombreMinusculas.endsWith(".jpg") || nombreMinusculas.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG;
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        boolean visualizar = !MediaType.APPLICATION_OCTET_STREAM.equals(contentType);
        ContentDisposition disposition =(visualizar ? ContentDisposition.inline() : ContentDisposition.attachment())
            .filename(nombreOriginal, StandardCharsets.UTF_8)
            .build();

        return ResponseEntity.ok()
            .cacheControl(CacheControl.noStore())
            .header(
                "X-Content-Type-Options",
                "nosniff"
            )
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                disposition.toString()
            )
            .contentType(contentType)
            .body(documento.resource());
        }

    @Operation(
        summary = "Actualiza el estado del PTD y sus adjuntos",
        description = """
                Actualiza el estado del PTD a 'Aprobado por Talento' y marca los documentos adjuntos enviados como validos
                """
    )
    @PutMapping("/approve-ptd-th")
    public ResponseEntity<Void> approvePtdByTh(@RequestBody ApprovePtdRequestDTO dto) {
        planTrabajoDigitalService.approvePtdByTh(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza el estado del PTD y sus adjuntos validados por TH. Ademas de una observación que indica las correcciones",
        description = """
                Actualiza el estado del PTD a 'Estipulado', marca los documentos adjuntos enviados como validos y registra
                la observación para corregir los adjuntos invalidos
                """
    )
    @PutMapping("/decline-ptd-th")
    public ResponseEntity<Void> declinePtdByTh(@RequestBody RejectPtdRequestDTO dto) {
        planTrabajoDigitalService.declinePtdByTh(dto);
        return ResponseEntity.ok().build();
    }
}
