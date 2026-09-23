/**
 * Aplicación: rvd
 * Archivo: AuthenticationController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Bootstrap SSO Vortal → SecurityAuth → RVD
 * 23/09/2026 - Sebastian Jaimes - BFF: cookie opaca, logout, me, menu, csrf
 * 23/09/2026 - Sebastian Jaimes - /menu lee JWT del store, no de credentials
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.JsonNode;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.config.security.permissions.SecurityAuthMenuClient;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionAccessTokenResolver;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionStore;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieService;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CsrfTokenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final SecurityAuthBootstrapService securityAuthBootstrapService;
    private final OpaqueSessionStore sessionStore;
    private final SessionCookieService sessionCookieService;
    private final SecurityAuthMenuClient securityAuthMenuClient;
    private final SecurityAuthProperties securityAuthProperties;
    private final OpaqueSessionAccessTokenResolver accessTokenResolver;

    @Operation(
            summary = "Bootstrap SSO desde Vortal/SecurityAuth (BFF)",
            description = "Valida el accessToken RS256 de SecurityAuth, lo guarda "
                    + "en el servidor y entrega una cookie opaca HttpOnly. "
                    + "El JWT no vuelve al navegador.")
    @PostMapping("/bootstrap")
    public ResponseEntity<JwtAuthResponseDTO> bootstrap(
            @Valid @RequestBody SecurityAuthBootstrapRequestDTO body,
            HttpServletRequest request,
            HttpServletResponse response) {
        BootstrapResult result = securityAuthBootstrapService.bootstrap(body);
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                sessionCookieService
                        .create(result.sessionId(), result.expiresAt())
                        .toString());
        return ResponseEntity.ok(withCsrf(result.response(), request));
    }

    @Operation(
            summary = "Cerrar sesión BFF",
            description = "Revoca la sesión opaca en el servidor y borra la cookie.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, sessionCookieService.cookieName());
        if (cookie != null) {
            sessionStore.revoke(cookie.getValue());
        }
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                sessionCookieService.clear().toString());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Sesión actual",
            description = "Datos del usuario autenticado por la cookie opaca. "
                    + "Reemplaza la decodificación del JWT en el front.")
    @GetMapping("/me")
    public ResponseEntity<JwtAuthResponseDTO> me(HttpServletRequest request) {
        AuthUserDetails user = SecurityUtils.requireUser();
        JwtAuthResponseDTO dto = securityAuthBootstrapService.buildResponse(user);
        accessTokenResolver.findSession(request)
                .ifPresent(session -> dto.setExpiresAt(sessionStore.expiresAt(session)));
        return ResponseEntity.ok(withCsrf(dto, request));
    }

    @Operation(
            summary = "Token CSRF vigente",
            description = "Header y valor que el SPA debe enviar en POST/PUT/DELETE.")
    @GetMapping("/csrf")
    public ResponseEntity<CsrfTokenDTO> csrf(HttpServletRequest request) {
        return ResponseEntity.ok(currentCsrf(request));
    }

    @Operation(
            summary = "Árbol de funcionalidades (menú) vía BFF",
            description = "Consulta /funcionalidad/arbol-roles en SecurityAuth "
                    + "con el JWT guardado en la sesión opaca.")
    @GetMapping("/menu")
    public ResponseEntity<JsonNode> menu(HttpServletRequest request) {
        AuthUserDetails user = SecurityUtils.requireUser();
        String accessToken = accessTokenResolver.resolveAccessToken(request)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "Sesión sin token de SecurityAuth"));
        return ResponseEntity.ok(securityAuthMenuClient.arbolRoles(
                user.getRoles(),
                securityAuthProperties.applicationId(),
                accessToken));
    }

    private static JwtAuthResponseDTO withCsrf(
            JwtAuthResponseDTO dto,
            HttpServletRequest request) {
        CsrfTokenDTO csrf = currentCsrf(request);
        if (csrf != null) {
            dto.setCsrfHeaderName(csrf.headerName());
            dto.setCsrfToken(csrf.token());
        }
        return dto;
    }

    private static CsrfTokenDTO currentCsrf(HttpServletRequest request) {
        Object attribute = request.getAttribute(CsrfToken.class.getName());
        if (!(attribute instanceof CsrfToken token)) {
            return null;
        }
        return new CsrfTokenDTO(token.getHeaderName(), token.getToken());
    }
}
