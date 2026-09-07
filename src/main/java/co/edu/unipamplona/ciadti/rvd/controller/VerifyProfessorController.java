/**
 * Aplicación: rvd
 * Archivo: VerifyProfessorController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/09/2026
 * Modificaciones:
 * 07/09/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteVerificacionPendienteListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/verify-professor")
public class VerifyProfessorController {

    private final CoordinacionService coordinacionService;
    private final PeriodoUniversidadService periodoUniversidadService;
    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;

    @Operation(
        summary = "Obtiene las convocatorias de precarga activas",
        description = "Obtiene las convocatorias activas del periodo universitario para filtrar coordinaciones"
    )
    @GetMapping("/list-active-preload-calls") 
    public ResponseEntity<?> listActivePreloadCalls(@RequestParam Long idPeriodoUniversidad) throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findActivePreloadCalls(idPeriodoUniversidad);
        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

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
        summary = "Lista coordinaciones académicas",
        description = """
            Lista coordinaciones hijas académicas con carga en el
            periodo y convocatoria, y al menos un docente con
            CADO_ESTADO = 1 listo para verificación.
            """
    )
    @GetMapping("/list-academic-coordinations")
    public ResponseEntity<List<CoordinacionBusquedaDTO>> listAcademicCoordinations(
            @RequestParam Long idPeriodoUniversidad,
            @RequestParam Long idConvocatoria) {
        List<CoordinacionBusquedaDTO> coordinations =
                coordinacionService.listAcademicCoordinations(
                        idPeriodoUniversidad,
                        idConvocatoria
                );
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista docentes pendientes de verificación",
        description = """
            Listado ligero para el header. El servidor resuelve el
            periodo vigente o las convocatorias activas.
            Incluye CADO_ESTADO = 1, vigentes y coordinaciones académicas con carga.
            """
    )
    @GetMapping("/pending")
    public ResponseEntity<DocenteVerificacionPendienteListadoDTO>
            listPendingProfessors() {
        return new ResponseEntity<>(
                coordinacionService.listPendingProfessors(),
                HttpStatus.OK
        );
    }

    @Operation(
        summary = "Lista docentes de una coordinación",
        description = """
            Lista docentes de la coordinación, convocatoria y periodo
            con CADO_ESTADO = 1, listos para verificación.
            Incluye planta, cátedra y tiempo completo ocasional.
            """
    )
    @GetMapping("/list-professors")
    public ResponseEntity<List<DocenteCoordinacionDTO>> listProfessors(
            @RequestParam Long idPeriodoUniversidad,
            @RequestParam Long idConvocatoria,
            @RequestParam Long idCoordinacion) {
        List<DocenteCoordinacionDTO> professors =
                coordinacionService.listProfessorsForVerification(
                        idPeriodoUniversidad,
                        idConvocatoria,
                        idCoordinacion
                );
        return new ResponseEntity<>(professors, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene el resumen completo de una carga docente",
        description = "Agrupa valor de contratación, horas de actividades y distribución por centros de costo."
    )
    @GetMapping("/professor-load-summary/{idCargaDocente}")
    public ResponseEntity<ResumenCargaDocenteDTO> getProfessorLoadSummary(@PathVariable Long idCargaDocente) {
        return new ResponseEntity<>(coordinacionService.getProfessorLoadSummary(idCargaDocente), HttpStatus.OK);
    }
    
}
