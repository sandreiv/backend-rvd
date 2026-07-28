/**
 * Aplicación: rvd
 * Archivo: LoadRestrictionController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller.basicTables
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para administración de restricción de carga.
 */
package co.edu.unipamplona.ciadti.rvd.controller.basicTables;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaModalidadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.RestriccionCargaAdministracionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionCargaFormularioDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/load-restriction")
public class LoadRestrictionController {

    private final RestriccionCargaAdministracionService restriccionCargaAdministracionService;

    @Operation(
        summary = "Lista modalidades de contratación",
        description = "Lista las modalidades de contratación disponibles para configurar restricción de carga"
    )
    @GetMapping("/modalities/list")
    public ResponseEntity<List<RestriccionCargaModalidadListadoDTO>> listModalities() {
        return ResponseEntity.ok(restriccionCargaAdministracionService.listModalities());
    }

    @Operation(
    summary = "Obtiene los catálogos de restricción de carga",
    description = "Obtiene categorías, tipos de actividad, programas y personas para el formulario"
    )
    
    @GetMapping("/restriction/catalogs")
    public ResponseEntity<RestriccionCargaCatalogosDTO> getCatalogs() {
        return ResponseEntity.ok(restriccionCargaAdministracionService.getCatalogs());
    }

    @Operation(
        summary = "Obtiene restricción de carga por modalidad",
        description = "Obtiene la restricción configurada para una modalidad de contratación"
    )
    @GetMapping("/restriction/{idModalidadContratacion}")
    public ResponseEntity<RestriccionCargaDetalleDTO> getRestriction(
            @PathVariable Long idModalidadContratacion) {
        return ResponseEntity.ok(
                restriccionCargaAdministracionService.getRestriction(idModalidadContratacion)
        );
    }

    @Operation(
        summary = "Guarda restricción de carga",
        description = "Registra o actualiza la restricción de carga de una modalidad de contratación"
    )
    @PostMapping("/restriction/save")
    public ResponseEntity<Void> saveRestriction(
            @RequestBody RestriccionCargaFormularioDTO dto) {
        restriccionCargaAdministracionService.saveRestriction(dto);
        return ResponseEntity.noContent().build();
    }

}