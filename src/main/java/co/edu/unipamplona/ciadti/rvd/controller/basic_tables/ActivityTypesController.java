package co.edu.unipamplona.ciadti.rvd.controller.basic_tables;

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

import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadAdministracionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.TipoActividadesAdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/activity-types")
public class ActivityTypesController {

    private final TipoActividadesAdministracionService tipoActividadesAdministracionService;

    @Operation(
        summary = "Lista tipos de actividades padre",
        description = "Lista únicamente los tipos de actividades con TIAC_IDPADRE NULL"
    )
    @GetMapping("/list")
    public ResponseEntity<List<TipoActividadAdministracionListadoDTO>> listActivityTypes() {
        return ResponseEntity.ok(tipoActividadesAdministracionService.listParentActivityTypes());
    }

    @Operation(
        summary = "Guarda tipo de actividad",
        description = "Crea un tipo de actividad padre con TIAC_IDPADRE NULL y TIAC_ORDEN calculado"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveActivityType(@RequestBody TipoActividadAdministracionFormularioDTO dto) {
        tipoActividadesAdministracionService.saveActivityType(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza tipo de actividad",
        description = "Actualiza un tipo de actividad padre sin recalcular el orden"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateActivityType(
            @PathVariable Long id,
            @RequestBody TipoActividadAdministracionFormularioDTO dto) {
        tipoActividadesAdministracionService.updateActivityType(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina tipo de actividad",
        description = "Elimina un tipo de actividad si no tiene actividades hijas asociadas"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivityType(@PathVariable Long id) {
        tipoActividadesAdministracionService.deleteActivityType(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina tipos de actividades",
        description = "Elimina varios tipos de actividades seleccionados"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkActivityTypes(@RequestBody EliminacionMasivaDTO dto) {
        tipoActividadesAdministracionService.deleteBulkActivityTypes(dto.ids());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista actividades hijas",
        description = "Lista las actividades hijas asociadas al tipo de actividad padre seleccionado"
    )
    @GetMapping("/{idPadre}/children/list")
    public ResponseEntity<List<TipoActividadAdministracionListadoDTO>> listChildActivityTypes(
            @PathVariable Long idPadre) {
        return ResponseEntity.ok(tipoActividadesAdministracionService.listChildActivityTypes(idPadre));
    }

    @Operation(
        summary = "Guarda actividad hija",
        description = "Crea una actividad hija asociada al tipo de actividad padre seleccionado"
    )
    @PostMapping("/{idPadre}/children/save")
    public ResponseEntity<Void> saveChildActivityType(
            @PathVariable Long idPadre,
            @RequestBody TipoActividadAdministracionFormularioDTO dto) {
        tipoActividadesAdministracionService.saveChildActivityType(idPadre, dto);
        return ResponseEntity.ok().build();
    }    

}