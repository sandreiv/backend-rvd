/**
 * Aplicación: rvd
 * Archivo: ProjectsController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ProyectosService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/projects")
public class ProjectsController {

    private final ProyectosService proyectosService;

    @Operation(
        summary = "Lista proyectos padre",
        description = "Lista únicamente los proyectos con PROY_IDPROYECTO NULL"
    )
    @GetMapping("/list")
    public ResponseEntity<List<ProyectosListaDTO>> listProjects() {
        return ResponseEntity.ok(proyectosService.listProjects());
    }

    @Operation(
        summary = "Lista productos",
        description = "Lista productos (hijos) del proyecto padre indicado"
    )
    @GetMapping("/list-products")
    public ResponseEntity<List<ProyectosListaDTO>> listProducts(
            @RequestParam Long idProyecto) {
        return ResponseEntity.ok(proyectosService.listProducts(idProyecto));
    }

    @Operation(
        summary = "Guarda proyecto o producto",
        description = "Crea un proyecto (idProyectoPadre null) o producto (con padre)"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveProject(@RequestBody ProyectosFormularioDTO dto) {
        proyectosService.saveProject(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza proyecto o producto",
        description = "Actualiza un proyecto o producto existente"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @RequestBody ProyectosFormularioDTO dto) {
        proyectosService.updateProject(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina proyecto o producto",
        description = "Elimina mediante procedimiento si no tiene productos ni personas"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        proyectosService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina proyectos o productos",
        description = "Eliminación masiva atómica; falla si alguno no se puede eliminar"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkProjects(@RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjects(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista personas del proyecto",
        description = "Lista personas asociadas al proyecto indicado"
    )
    @GetMapping("/list-persons")
    public ResponseEntity<List<PersonaProyectoListaDTO>> listProjectPersons(@RequestParam Long idProyecto) {
        return ResponseEntity.ok(proyectosService.listProjectPersons(idProyecto));
    }

    @Operation(
        summary = "Guarda persona del proyecto",
        description = "Asocia una persona a un proyecto"
    )
    @PostMapping("/save-person")
    public ResponseEntity<Void> saveProjectPerson(@RequestBody PersonaProyectoFormularioDTO dto) {
        proyectosService.saveProjectPerson(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza persona del proyecto",
        description = "Actualiza la asociación persona-proyecto"
    )
    @PutMapping("/update-person/{id}")
    public ResponseEntity<Void> updateProjectPerson(@PathVariable Long id, @RequestBody PersonaProyectoFormularioDTO dto) {
        proyectosService.updateProjectPerson(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina persona del proyecto",
        description = "Elimina mediante procedimiento almacenado"
    )
    @DeleteMapping("/delete-person/{id}")
    public ResponseEntity<Void> deleteProjectPerson(@PathVariable Long id) {
        proyectosService.deleteProjectPerson(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina personas del proyecto",
        description = "Eliminación masiva atómica de personas"
    )
    @PostMapping("/delete-persons-bulk")
    public ResponseEntity<Void> deleteBulkProjectPersons(@RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjectPersons(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista tipos de proyecto",
        description = "Lista los tipos de proyecto disponibles"
    )
    @GetMapping("/project-types/list")
    public ResponseEntity<List<TipoProyectoListaDTO>> listProjectTypes() {
        return ResponseEntity.ok(proyectosService.listProjectTypes());
    }

    @Operation(
        summary = "Guarda tipo de proyecto",
        description = "Crea un nuevo tipo de proyecto"
    )
    @PostMapping("/project-types/save")
    public ResponseEntity<Void> saveProjectType(@RequestBody TipoProyectoFormularioDTO dto) {
        proyectosService.saveProjectType(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza tipo de proyecto",
        description = "Actualiza un tipo de proyecto existente"
    )
    @PutMapping("/project-types/update/{id}")
    public ResponseEntity<Void> updateProjectType(@PathVariable Long id, @RequestBody TipoProyectoFormularioDTO dto) {
        proyectosService.updateProjectType(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina tipo de proyecto",
        description = "Elimina un tipo de proyecto mediante procedimiento almacenado"
    )
    @DeleteMapping("/project-types/delete/{id}")
    public ResponseEntity<Void> deleteProjectType(@PathVariable Long id) {
        proyectosService.deleteProjectType(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina tipos de proyecto",
        description = "Elimina varios tipos de proyecto seleccionados"
    )
    @PostMapping("/project-types/delete-bulk")
    public ResponseEntity<Void> deleteBulkProjectTypes(@RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjectTypes(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista convocatorias de proyectos",
        description = "Lista las convocatorias de proyectos disponibles"
    )
    @GetMapping("/project-calls/list")
    public ResponseEntity<List<ConvocatoriaProyectosListaDTO>> listProjectCalls() {
        return ResponseEntity.ok(proyectosService.listProjectCalls());
    }

    @Operation(
        summary = "Guarda convocatoria de proyecto",
        description = "Crea una nueva convocatoria de proyecto"
    )
    @PostMapping("/project-calls/save")
    public ResponseEntity<Void> saveProjectCall(@RequestBody ConvocatoriaProyectosFormularioDTO dto) {
        proyectosService.saveProjectCall(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza convocatoria de proyecto",
        description = "Actualiza una convocatoria de proyecto existente"
    )
    @PutMapping("/project-calls/update/{id}")
    public ResponseEntity<Void> updateProjectCall(@PathVariable Long id, @RequestBody ConvocatoriaProyectosFormularioDTO dto) {
        System.out.println("updateProjectCall ===> " + dto);
        proyectosService.updateProjectCall(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina convocatoria de proyecto",
        description = "Elimina una convocatoria mediante procedimiento almacenado"
    )
    @DeleteMapping("/project-calls/delete/{id}")
    public ResponseEntity<Void> deleteProjectCall(@PathVariable Long id) {
        proyectosService.deleteProjectCall(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina convocatorias de proyecto",
        description = "Elimina varias convocatorias de proyecto seleccionadas"
    )
    @PostMapping("/project-calls/delete-bulk")
    public ResponseEntity<Void> deleteBulkProjectCalls(
            @RequestBody EliminacionMasivaDTO dto) {
        proyectosService.deleteBulkProjectCalls(dto.ids());
        return ResponseEntity.ok().build();
    }

}
