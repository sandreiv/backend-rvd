/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoTrabajoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PrecargaDocenteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/professor-preload")
public class ProfessorPreloadController {

    private final PrecargaDocenteService precargaDocenteService;
    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;
    
    @Operation(
        summary = "Obtiene las convocatorias de precarga activas",
        description = "Obtiene las convocatorias con el fin de filtrar las coordinaciones"
    )
    @GetMapping("/list-active-preload-calls") 
    public ResponseEntity<?> listActivePreloadCalls() throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findActivePreloadCalls();
        System.out.println("[DEBUG] Active preload calls: " + activePreloadCalls);
        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

    // Controlador para buscar las coordinaciones del usuario
    @Operation(
        summary = "Obtiene la lista de coordinaciones",
        description = """
            Sin idConvocatoria: coordinaciones sin carga asignada.
            Con idConvocatoria: coordinaciones con carga en esa convocatoria.
            """
    )
    @GetMapping("/list")
    public ResponseEntity<?> listCoordinations(@RequestParam(required = false) Long idConvocatoria) throws Exception {
        List<CoordinacionDTO> coordinations = coordinacionService.findCoordinationsByIdConvocatoria(idConvocatoria);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de periodo de trabajo", 
        description = "Obtiene la lista de periodo de trabajo"
    )
    @GetMapping("/list-work-period")
    public ResponseEntity<?> workPeriodList() throws Exception {
        List<PeriodoTrabajoDTO> workPeriodList = precargaDocenteService.findWorkPeriodList();
        return new ResponseEntity<>(workPeriodList, HttpStatus.OK);
    }
    
}
