/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaPrecargaController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelEducativoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.ModalidadContratacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.NivelEducativoService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/preload-call")
public class PreloadCallController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final ModalidadContratacionService modalidadContratacionService;
    private final PeriodoUniversidadService periodoUniversidadService;
    private final NivelEducativoService nivelEducativoService;
    private final CoordinacionService coordinacionService;
    @Operation(
        summary = "Obtiene la lista de convocatorias", 
        description = "Obtiene la lista de convocatorias"
    )
    @GetMapping("/list")
    public ResponseEntity<?> callList() throws Exception {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findCallListWithDates();
        return new ResponseEntity<>(callList, HttpStatus.OK);
    }

    @Operation(
        summary = "Busca persona general para autorizar una convocatoria",
        description = "Busca por documento y/o fragmento de nombre o apellido"
    )
    @GetMapping("/search-general-person")
    public ResponseEntity<List<PersonaAutorizaConvocatoriaDTO>> searchGeneralPerson(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String documento) {
        List<PersonaAutorizaConvocatoriaDTO> personas = convocatoriaPrecargaService.searchGeneralPerson(nombre, documento);
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de modalidades de contratación",
        description = "Obtiene la lista de modalidades de contratación"
    )
    @GetMapping("/list-modality")
    public ResponseEntity<List<ModalidadContratacionDTO>> listModality() {
        List<ModalidadContratacionDTO> modalities = modalidadContratacionService.findModalityList();
        return new ResponseEntity<>(modalities, HttpStatus.OK);
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
        summary = "Obtiene la lista de niveles educativos",
        description = "Obtiene la lista de niveles educativos"
    )
    @GetMapping("/list-educational-level")
    public ResponseEntity<List<NivelEducativoDTO>> listEducationalLevel() {
        List<NivelEducativoDTO> levels = nivelEducativoService.findEducationalLevelList();
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    
    @Operation(
        summary = "Guarda una nueva convocatoria", 
        description = "Guarda una nueva convocatoria"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveCall(@RequestBody ConvocatoriaFormularioDTO ConvocatoriaFormularioDTO) {
        System.out.println("[DEBUG] Guardando convocatoria: " + ConvocatoriaFormularioDTO);
        convocatoriaPrecargaService.save(ConvocatoriaFormularioDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Obtiene el detalle de una convocatoria", 
        description = "Obtiene el detalle de una convocatoria"
    )
    @GetMapping("/detail/{id}")
    public ResponseEntity<ConvocatoriaFormularioDTO> detailCall(@PathVariable Long id) {
        ConvocatoriaFormularioDTO detail = convocatoriaPrecargaService.findCallDetail(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(
        summary = "Actualiza una convocatoria", 
        description = "Actualiza una convocatoria"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCall(@PathVariable Long id, @RequestBody ConvocatoriaFormularioDTO dto) {
        convocatoriaPrecargaService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina una convocatoria", 
        description = "Elimina una convocatoria"
    )
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCall(@PathVariable Long id, @RequestBody ConvocatoriaFormularioDTO dto) {
        convocatoriaPrecargaService.delete(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina convocatorias masivas", description = "Elimina convocatorias masivas")
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> bulkDeleteCall(@RequestBody List<ConvocatoriaFormularioDTO> listaConvocatorias) {
        convocatoriaPrecargaService.bulkDelete(listaConvocatorias);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Busca coordinaciones",
        description = "Busca coordinaciones"
    )
    @GetMapping("/search-coordination")
    public ResponseEntity<List<CoordinacionBusquedaDTO>> searchCoordination(@RequestParam(required = false) String nombre) {
        List<CoordinacionBusquedaDTO> coordinations = coordinacionService.searchCoordination(nombre);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Guarda una restricción por coordinación",
        description = "Guarda una restricción por coordinación"
    )
    @PostMapping("/save-coordination-restriction")
    public ResponseEntity<Void> saveCoordinationRestriction(@RequestBody CoordinacionRestriccionFormularioDTO dto){
        coordinacionService.saveCoordinationRestriction(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista las restricciones por coordinación",
        description = "Lista las restricciones por coordinación"
    )
    @GetMapping("/list-coordination-restriction")
    public ResponseEntity<List<CoordinacionRestriccionDTO>> listCoordinationRestriction(@RequestParam Long idConvocatoria) {
        List<CoordinacionRestriccionDTO> restrictions = coordinacionService.listCoordinationRestriction(idConvocatoria);
        return new ResponseEntity<>(restrictions, HttpStatus.OK);
    }

    @Operation(
        summary = "Actualiza una restricción por coordinación",
        description = "Actualiza una restricción por coordinación"
    )
    @PutMapping("/update-coordination-restriction/{id}")
    public ResponseEntity<Void> updateCoordinationRestriction(@PathVariable Long id, @RequestBody CoordinacionRestriccionFormularioDTO dto) {
        coordinacionService.updateCoordinationRestriction(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina una restricción por coordinación",
        description = "Elimina una restricción por coordinación"
    )
    @PostMapping("/delete-coordination-restriction/{id}")
    public ResponseEntity<Void> deleteCoordinationRestriction(@PathVariable Long id, @RequestBody CoordinacionRestriccionDTO dto) {
        coordinacionService.deleteCoordinationRestriction(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina restricciones por coordinación masivas",
        description = "Elimina restricciones por coordinación masivas"
    )
    @PostMapping("/delete-bulk-coordination-restriction")
    public ResponseEntity<Void> bulkDeleteCoordinationRestriction(@RequestBody List<CoordinacionRestriccionDTO> restricciones) {
        coordinacionService.bulkDeleteCoordinationRestriction(restricciones);
        return ResponseEntity.ok().build();
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
