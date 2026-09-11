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

import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadesService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(
    "/configuration/administration/novelties"
)
public class NoveltiesController {

    private final NovedadesService novedadesService;

    @Operation(
        summary = "Lista novedades",
        description = "Lista las novedades configuradas en la tabla básica"
    )
    @GetMapping("/list")
    public ResponseEntity<List<NovedadListadoDTO>>
            listNovedades() {

        return ResponseEntity.ok(
                novedadesService.listNovedades()
        );
    }

    @Operation(
        summary = "Guarda novedad",
        description = "Crea una nueva novedad"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> saveNovedad(
            @RequestBody NovedadFormularioDTO dto
    ) {

        novedadesService.saveNovedad(dto);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Actualiza novedad",
        description = "Actualiza una novedad existente"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateNovedad(
            @PathVariable Long id,
            @RequestBody NovedadFormularioDTO dto
    ) {

        novedadesService.updateNovedad(
                id,
                dto
        );

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Elimina novedad",
        description = "Elimina una novedad mediante procedimiento almacenado"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNovedad(
            @PathVariable Long id
    ) {

        novedadesService.deleteNovedad(id);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Elimina novedades",
        description = "Elimina varias novedades seleccionadas"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulkNovedades(
            @RequestBody EliminacionMasivaDTO dto
    ) {

        novedadesService.deleteBulkNovedades(
                dto.ids()
        );

        return ResponseEntity
                .ok()
                .build();
    }
}