package co.edu.unipamplona.ciadti.rvd.controller.basicTables;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsociacionCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoAsignadoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaPersonaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaCoordinacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.AdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/coordination-management")
public class CoordinationManagementController {

    private final AdministracionService administracionService;

    @Operation(
        summary = "Obtiene catálogos para asociación de coordinaciones",
        description = "Lista coordinaciones, programas, materias y centros de costo para el formulario de administración"
    )
    @GetMapping("/coordination-associations/catalogs")
    public ResponseEntity<AsociacionCoordinacionCatalogosDTO> getCoordinationAssociationCatalogs() {
        return ResponseEntity.ok(administracionService.getAssociationCatalogs());
    }

    @Operation(
        summary = "Lista asociaciones de coordinaciones",
        description = "Lista las asociaciones de coordinación con programa o materia y centro de costo"
    )
    @GetMapping("/coordination-associations/list")
    public ResponseEntity<List<AsociacionCoordinacionListadoDTO>> listCoordinationAssociations() {
        return ResponseEntity.ok(administracionService.listCoordinationAssociations());
    }

    @Operation(
        summary = "Guarda una asociación de coordinación",
        description = "Crea una asociación de coordinación con programa o materia y centro de costo"
    )
    @PostMapping("/coordination-associations/save")
    public ResponseEntity<Void> saveCoordinationAssociation(@RequestBody AsociacionCoordinacionFormularioDTO dto) {
        administracionService.saveCoordinationAssociation(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza una asociación de coordinación",
        description = "Actualiza la asociación de coordinación seleccionada"
    )
    @PutMapping("/coordination-associations/update/{id}")
    public ResponseEntity<Void> updateCoordinationAssociation(
            @PathVariable Long id,
            @RequestBody AsociacionCoordinacionFormularioDTO dto) {
        administracionService.updateCoordinationAssociation(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina una asociación de coordinación",
        description = "Elimina la asociación de coordinación seleccionada"
    )
    @DeleteMapping("/coordination-associations/delete/{id}")
    public ResponseEntity<Void> deleteCoordinationAssociation(@PathVariable Long id) {
        administracionService.deleteCoordinationAssociation(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina asociaciones de coordinación",
        description = "Elimina varias asociaciones de coordinación seleccionadas"
    )
    @PostMapping("/coordination-associations/delete-bulk")
    public ResponseEntity<Void> deleteBulkCoordinationAssociations(@RequestBody EliminacionMasivaDTO dto) {
        administracionService.deleteBulkCoordinationAssociations(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista centros de costo asignados",
        description = "Lista las asignaciones de centro de costo por coordinación"
    )
    @GetMapping("/cost-centers/list")
    public ResponseEntity<List<CentroCostoAsignadoListadoDTO>> listCostCenterAssignments() {
        return ResponseEntity.ok(administracionService.listCostCenterAssignments());
    }

    @Operation(
        summary = "Guarda centro de costo asignado",
        description = "Crea una asignación entre coordinación y centro de costo"
    )
    @PostMapping("/cost-centers/save")
    public ResponseEntity<Void> saveCostCenterAssignment(@RequestBody CentroCostoAsignadoFormularioDTO dto) {
        administracionService.saveCostCenterAssignment(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza centro de costo asignado",
        description = "Actualiza una asignación entre coordinación y centro de costo"
    )
    @PutMapping("/cost-centers/update/{id}")
    public ResponseEntity<Void> updateCostCenterAssignment(
            @PathVariable Long id,
            @RequestBody CentroCostoAsignadoFormularioDTO dto) {
        administracionService.updateCostCenterAssignment(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina centro de costo asignado",
        description = "Elimina una asignación entre coordinación y centro de costo"
    )
    @DeleteMapping("/cost-centers/delete/{id}")
    public ResponseEntity<Void> deleteCostCenterAssignment(@PathVariable Long id) {
        administracionService.deleteCostCenterAssignment(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina centros de costo asignados",
        description = "Elimina varias asignaciones entre coordinación y centro de costo"
    )
    @PostMapping("/cost-centers/delete-bulk")
    public ResponseEntity<Void> deleteBulkCostCenterAssignments(@RequestBody EliminacionMasivaDTO dto) {
        administracionService.deleteBulkCostCenterAssignments(dto.ids());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/people/list")
    public ResponseEntity<List<PersonaCoordinacionListadoDTO>> listPeopleCoordinations() {
        return ResponseEntity.ok(administracionService.listPeopleCoordinations());
    }

    @PostMapping("/people/save")
    public ResponseEntity<Void> savePeopleCoordination(@RequestBody PersonaCoordinacionFormularioDTO dto) {
        administracionService.savePeopleCoordination(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/people/update/{idPersonaGeneral}/{idCoordinacion}")
    public ResponseEntity<Void> updatePeopleCoordination(
            @PathVariable Long idPersonaGeneral,
            @PathVariable Long idCoordinacion,
            @RequestBody PersonaCoordinacionFormularioDTO dto) {
        administracionService.updatePeopleCoordination(idPersonaGeneral, idCoordinacion, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/people/delete/{idPersonaGeneral}/{idCoordinacion}")
    public ResponseEntity<Void> deletePeopleCoordination(
            @PathVariable Long idPersonaGeneral,
            @PathVariable Long idCoordinacion) {
        administracionService.deletePeopleCoordination(idPersonaGeneral, idCoordinacion);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/people/delete-bulk")
    public ResponseEntity<Void> deleteBulkPeopleCoordinations(@RequestBody EliminacionMasivaPersonaCoordinacionDTO dto) {
        administracionService.deleteBulkPeopleCoordinations(dto.registros());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plant-professors/list")
    public ResponseEntity<List<PersonaCoordinacionListadoDTO>> listPlantProfessorCoordinations() {
        return ResponseEntity.ok(administracionService.listPlantProfessorCoordinations());
    }

    @PostMapping("/plant-professors/save")
    public ResponseEntity<Void> savePlantProfessorCoordination(@RequestBody PersonaCoordinacionFormularioDTO dto) {
        administracionService.savePlantProfessorCoordination(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/plant-professors/update/{idPersonaGeneral}/{idCoordinacion}")
    public ResponseEntity<Void> updatePlantProfessorCoordination(
            @PathVariable Long idPersonaGeneral,
            @PathVariable Long idCoordinacion,
            @RequestBody PersonaCoordinacionFormularioDTO dto) {
        administracionService.updatePlantProfessorCoordination(idPersonaGeneral, idCoordinacion, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/plant-professors/delete/{idPersonaGeneral}/{idCoordinacion}")
    public ResponseEntity<Void> deletePlantProfessorCoordination(
            @PathVariable Long idPersonaGeneral,
            @PathVariable Long idCoordinacion) {
        administracionService.deletePlantProfessorCoordination(idPersonaGeneral, idCoordinacion);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/plant-professors/delete-bulk")
    public ResponseEntity<Void> deleteBulkPlantProfessorCoordinations(@RequestBody EliminacionMasivaPersonaCoordinacionDTO dto) {
        administracionService.deleteBulkPlantProfessorCoordinations(dto.registros());
        return ResponseEntity.ok().build();
    }    
}