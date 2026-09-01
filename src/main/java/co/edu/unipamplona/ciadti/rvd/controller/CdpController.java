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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpContextDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/cdp")
public class CdpController {

    private final CoordinacionService coordinacionService;

    @Operation(
        summary = "Obtiene las solicitudes CPD",
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
        summary = "Obtiene el contexto del Decano para solicitudes CPD",
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
    
}

/* 31/08/2026 @:Daniel Arias */
