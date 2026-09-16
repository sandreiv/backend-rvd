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
import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.PuntosVigenciaAdministracionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(
    "/configuration/administration/points-validity"
)
public class PointsValidityController {

    private final PuntosVigenciaAdministracionService
            service;

    @Operation(
        summary = "Lista puntos por vigencia"
    )
    @GetMapping("/list")
    public ResponseEntity<List<PuntosVigenciaListadoDTO>>
            list() {

        return ResponseEntity.ok(
                service.listPointsValidity()
        );
    }

    @Operation(
        summary = "Guarda puntos por vigencia"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> save(
            @RequestBody
            PuntosVigenciaFormularioDTO dto
    ) {

        service.savePointsValidity(dto);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Actualiza puntos por vigencia"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody
            PuntosVigenciaFormularioDTO dto
    ) {

        service.updatePointsValidity(
                id,
                dto
        );

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Elimina puntos por vigencia"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.deletePointsValidity(id);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(
        summary = "Elimina varios puntos por vigencia"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulk(
            @RequestBody
            EliminacionMasivaDTO dto
    ) {

        service.deleteBulkPointsValidity(
                dto.ids()
        );

        return ResponseEntity
                .ok()
                .build();
    }
}