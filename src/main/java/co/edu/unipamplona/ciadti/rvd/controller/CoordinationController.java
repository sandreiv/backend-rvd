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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/coordination")
public class CoordinationController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;
    
    @Operation(
        summary = "Obtiene las convocatorias de precarga activas",
        description = "Obtiene las convocatorias con el fin de filtrar las coordinaciones"
    )
    @GetMapping("/list-active-preload-calls") 
    public ResponseEntity<?> listActivePreloadCalls() throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findActivePreloadCalls();
        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

    // Controlador para buscar las coordinaciones del usuario
    /**
     * TO-DO: implementar obtencion de ID del usuario logueado.
     * Dos opciones: obtenerlo desde el contexto de la aplicacion aca en el backend.
     * Obtener el ID desde el frontend /{idPersonaGeneral}
     * 
     */
    @Operation(
        summary = "Obtiene la lista de coordinaciones",
        description = """
            Sin idConvocatoria: coordinaciones sin carga (convocatoria y carga null).
            Con idConvocatoria: coordinaciones con carga en esa convocatoria activa.
            """
    )
    @GetMapping("/list")
    public ResponseEntity<?> listCoordinations(@RequestParam(required = false) Long idConvocatoria) throws Exception {
        //String idUsuario = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        // Este Id es de prueba y solo sera temporal. ------------------ CAMBIAR --------------------
        Long idUsuario = 239419L;
        List<CoordinacionDTO> coordinations = coordinacionService.findCoordinationsByIdConvocatoria(idConvocatoria, idUsuario);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }


    @Operation(
        summary = "Guarda la relacion coordinacion-convocatoria en carga",
        description = "Crea un registro en RVD.CARGA con COOR_ID y CONV_ID"
    )
    @PostMapping("/save-preload")
    public ResponseEntity<Void> savePreload(@RequestBody RelacionConvocatoriaCoordinacionDTO dto) {
        System.out.println("[DEBUG] DTO: " + dto);
        coordinacionService.savePreload(dto);
        return ResponseEntity.ok().build();
    }

    
}
