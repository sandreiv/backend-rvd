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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteVerificacionPendienteListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/hiring-call")
public class HiringCallController {

    private final PeriodoUniversidadService periodoUniversidadService;
    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;

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
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService
                .findHiringCallListByPeriod(idPeriodoUniversidad);
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
    public ResponseEntity<List<CoordinacionDTO>> listCoordinations(
            @RequestParam(required = false) Long idConvocatoria,
            @RequestParam(required = false) Long idPeriodoUniversidad) {
        List<CoordinacionDTO> coordinations = coordinacionService
                .findHiringCoordinations(idConvocatoria, idPeriodoUniversidad);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los docentes aprobados de una carga según la modalidad de contratacion",
        description = """
            Lista docentes con CADO_ESTADO = 4 (aprobado).
            Si la modalidad es planta: docentes de DOCENTESPLANTACOORDINACION
            de la coordinación de la carga que tengan CARGADOCENTE aprobado.
            Para otras modalidades: solo docentes con CARGADOCENTE aprobado
            de esa carga.
            """
    )
    @GetMapping("/list-professors-modality")
    public ResponseEntity<List<DocenteCoordinacionDTO>> listProfessors(
            @RequestParam Long idCarga,
            @RequestParam Long idModalidadContratacion) {
        List<DocenteCoordinacionDTO> docentes = coordinacionService
                .listApprovedProfessorsForHiring(idCarga, idModalidadContratacion);
        return new ResponseEntity<>(docentes, HttpStatus.OK);
    }
    
}
