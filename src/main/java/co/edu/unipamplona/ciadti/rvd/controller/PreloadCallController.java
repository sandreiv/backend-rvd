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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.ModalidadContratacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/preload-call")
public class PreloadCallController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final ModalidadContratacionService modalidadContratacionService;

    @Operation(
        summary = "Obtiene la lista de convocatorias", 
        description = "Obtiene la lista de convocatorias"
    )
    @GetMapping("/list")
    public ResponseEntity<?> callList() throws Exception {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findCallListWithDates();
        System.out.println("[DEBUG] LISTA CONVOCATORIA:" + callList);
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
        List<ModalidadContratacionDTO> modalities =
                modalidadContratacionService.findModalityList();
        return new ResponseEntity<>(modalities, HttpStatus.OK);
    }

    //TODO
    /*@Operation(
        summary = "Guarda una nueva convocatoria", 
        description = "Guarda una nueva convocatoria"
    )
    @PostMapping("/save")
    public ResponseEntity<?> saveCall() throws Exception {
        List<ConvocatoriaDTO> callList = convocatoriaPrecargaService.findCallListWithDates();
        System.out.println("[DEBUG] LISTA CONVOCATORIA:" + callList);
        return new ResponseEntity<>(callList, HttpStatus.OK);
    }*/
    
}
/* 02/06/2026 @:Sebastian Jaimes */
