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
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoTrabajoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.PrecargaDocenteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/professor-preload")
public class ProfessorPreloadController {

    private final PrecargaDocenteService precargaDocenteService;

    /**
     * TO-DO:
     */

    // Controlador para buscar las coordinaciones del usuario
    @Operation(
        summary = "Obtiene la lista de coordinaciones del usuario", 
        description = "Obtiene la lista de coordinaciones del usuario"
    )
    @GetMapping("/list")
    public ResponseEntity<?> listCoordinations() throws Exception {
        /*List<CoordinacionDTO> coordinations = precargaDocenteService.findCoordinations();
        return new ResponseEntity<>(coordinations, HttpStatus.OK);*/
        return null;
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
