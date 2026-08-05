/**
 * Aplicación: rvd
 * Archivo: AuthenticationController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Bootstrap SSO Vortal → SecurityAuth → RVD
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final SecurityAuthBootstrapService securityAuthBootstrapService;

    @Operation(
            summary = "Bootstrap SSO desde Vortal/SecurityAuth",
            description = "Valida el accessToken RS256 de SecurityAuth "
                    + "y devuelve la sesión RVD. Sin login local.")
    @PostMapping("/bootstrap")
    public ResponseEntity<JwtAuthResponseDTO> bootstrap(
            @Valid @RequestBody SecurityAuthBootstrapRequestDTO body) {
        return ResponseEntity.ok(
                securityAuthBootstrapService.validateAndBuildResponse(body));
    }
}
