/**
 * Aplicación: rvd
 * Archivo: CdpController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 31/08/2026
 * Modificaciones:
 * 31/08/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unipamplona.ciadti.rvd.model.service.SolicitudCdpService;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpContextDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenSolicitudCdpDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.CdpReporteService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/cdp")
public class CdpController {

    private final CoordinacionService coordinacionService;
    private final CdpReporteService cdpReporteService;
    private final SolicitudCdpService solicitudCdpService;

    @Operation(
        summary = "Obtiene las solicitudes CDP",
        description = """
            Lista para el Decano las coordinaciones cuya carga
            se encuentra en estado AVAL DESARROLLO.
            """
    )
    @GetMapping("/requests")
    public ResponseEntity<List<CoordinacionDTO>> listCdpRequests(
            @RequestParam(required = false) Long idConvocatoria,
            @RequestParam(required = false) Long idPeriodoUniversidad) {

        List<CoordinacionDTO> coordinations =
                coordinacionService.findCdpRequests(
                        idConvocatoria,
                        idPeriodoUniversidad
                );

        return new ResponseEntity<>(
                coordinations,
                HttpStatus.OK
        );
    }

    @Operation(
        summary = "Obtiene las solicitudes CDP para desarollo academico",
        description = """
            Lista para Desarrollo Academico las facultades que tienen
            solicitudes CDP en estado DESARROLLO ACADEMICO.
            """
    )
    @GetMapping("/requests-for-academic-development")
    public ResponseEntity<List<ResumenSolicitudCdpDTO>> listCdpRequestsForAcademicDevelopment(
            @RequestParam(required = false) Long idPeriodoUniversidad) {

        List<ResumenSolicitudCdpDTO> faculties = coordinacionService.findCdpRequestsForAcademicDevelopment(idPeriodoUniversidad);

        return new ResponseEntity<>(faculties, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene el contexto del Decano para solicitudes CDP",
        description = """
                Obtiene la Unidad Académica y Facultad asociadas
                al Decano autenticado.
                """
    )
    @GetMapping("/context")
    public ResponseEntity<CdpContextDTO> getCdpContext() {

        CdpContextDTO context =
                coordinacionService.getCdpContext();

        return ResponseEntity.ok(context);
    }
    

    @Operation(
        summary = "Genera el reporte Excel CDP de la facultad",
        description = """
            Exporta, para el Decano, un libro Excel con una hoja por cada
            coordinación de su facultad cuya carga está en Aval Desarrollo.
            Cada hoja conserva el resumen de docentes: encabezado, valor hora,
            valores de contratación y horas de actividades.
            """
    )
    @GetMapping("/cdp-report")
    public ResponseEntity<byte[]> generateCdpReport(
            @RequestParam(required = false) Long idConvocatoria,
            @RequestParam(required = false) Long idPeriodoUniversidad) {

        FileDTO file = cdpReporteService.generateCdpReport(
                idConvocatoria,
                idPeriodoUniversidad);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file.content());
    }

    @Operation(
        summary = "Genera el reporte PDF CDP de la facultad",
        description = """
            Exporta, para el Decano, el PDF institucional con encabezado de
            facultad, un bloque por cada coordinación en Aval Desarrollo
            (modalidades, docentes y total de la coordinación) y espacio
            de firma del decano al final.
            """
    )
    @GetMapping("/cdp-pdf-report")
    public ResponseEntity<byte[]> generateCdpPdfReport(
            @RequestParam(required = false) Long idConvocatoria,
            @RequestParam(required = false) Long idPeriodoUniversidad) {

        FileDTO file = cdpReporteService.generateCdpPdfReport(idConvocatoria, idPeriodoUniversidad);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file.content());
    }

    @Operation(
        summary = "Crea una solicitud CDP",
        description = """
                Registra una solicitud CDP para la facultad
                asociada al Decano autenticado, incluyendo
                observación y archivos adjuntos.
                """
    )
    @PostMapping(
        value = "/requests",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> createCdpRequest(
        @RequestPart(
            value = "observacion",
            required = false
        )
        String observacion,

        @RequestPart(
            value = "archivos",
            required = false
        )
        List<MultipartFile> archivos,
    
        @RequestPart(
            value = "idPeriodo",
            required = false
        )
        String idPeriodo) {

        solicitudCdpService.create(
            observacion,
            archivos,
            idPeriodo
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @Operation(
        summary = "Obtiene la solicitud CDP del Decano",
        description = """
                Consulta la solicitud CDP registrada para la
                facultad asociada al Decano autenticado.
                """
    )
    @GetMapping("/request")
        public ResponseEntity<CdpRequestDTO> getCurrentCdpRequest() {

        CdpRequestDTO request =
                solicitudCdpService.getCurrentRequest();

        return ResponseEntity.ok(request);
    }       


}

/* 31/08/2026 @:Daniel Arias */
