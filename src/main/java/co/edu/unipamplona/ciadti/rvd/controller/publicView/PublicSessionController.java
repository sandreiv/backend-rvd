/**
 * Aplicación: rvd
 * Archivo: PublicSessionController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller.publicView
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller.publicView;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.DocenteSessionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/session")
public class PublicSessionController {

    private final DocenteSessionService docenteSessionService;

    @Operation(
            summary = "Abre la sesión pública del docente",
            description = "Valida documento y fecha de expedición contra el maestro "
                    + "de docentes y devuelve un JWT de corta duración.")
    @PostMapping
    public ResponseEntity<DocenteSessionResponseDTO> openSession(
            @Valid @RequestBody DocenteSessionRequestDTO body,
            HttpServletRequest request) {
        return ResponseEntity.ok(docenteSessionService.openSession(
                body,
                request.getRemoteAddr()));
    }
}
