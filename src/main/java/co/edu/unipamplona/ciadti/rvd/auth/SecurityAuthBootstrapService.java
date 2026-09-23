/**
 * Aplicación: rvd
 * Archivo: SecurityAuthBootstrapService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 23/09/2026 - Sebastian Jaimes - BFF: sesión opaca en servidor, body sin JWT
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.ExternalJwtUserResolver;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthTokenValidator;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSession;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionStore;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UsuarioSesionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityAuthBootstrapService {

    private final SecurityAuthTokenValidator securityAuthTokenValidator;
    private final ExternalJwtUserResolver externalJwtUserResolver;
    private final SecurityAuthProperties securityAuthProperties;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final OpaqueSessionStore sessionStore;

    /**
     * Valida el JWT de SecurityAuth, lo guarda en el store de sesiones y
     * arma la respuesta SPA. El JWT no se devuelve al navegador.
     */
    public BootstrapResult bootstrap(SecurityAuthBootstrapRequestDTO body) {
        if (!securityAuthTokenValidator.isActive()) {
            throw new ApiException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Validación JWT de SecurityAuth no configurada");
        }

        Jwt jwt = decode(body.getAccessToken());
        AuthUserDetails user = externalJwtUserResolver.resolve(jwt)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.FORBIDDEN,
                        "Usuario sin roles para RVD (application-id "
                                + securityAuthProperties.applicationId() + ")"));

        OpaqueSession session = new OpaqueSession(
                body.getAccessToken(),
                body.getRefreshToken(),
                jwt.getExpiresAt());
        String sessionId = sessionStore.create(session);
        Instant expiresAt = sessionStore.expiresAt(session);

        JwtAuthResponseDTO response = buildResponse(user);
        response.setExpiresAt(expiresAt);
        return new BootstrapResult(sessionId, expiresAt, response);
    }

    /** Arma el body de sesión para bootstrap y para {@code GET /me}. */
    public JwtAuthResponseDTO buildResponse(AuthUserDetails user) {
        String nombreCompleto = resolveNombreCompleto(
                user.getIdPersonaGeneral(),
                user.getUsername());

        JwtAuthResponseDTO response = new JwtAuthResponseDTO();
        response.setUsername(user.getUsername());
        response.setNombreCompleto(nombreCompleto);
        response.setIdPersona(user.getIdPersonaGeneral() != null
                ? String.valueOf(user.getIdPersonaGeneral())
                : null);
        response.setRoles(user.getRoles());
        response.setUsuario(UsuarioSesionDTO.builder()
                .username(user.getUsername())
                .nombreCompleto(nombreCompleto)
                .idPersona(user.getIdPersonaGeneral())
                .roles(user.getRoles())
                .idAplicacion(securityAuthProperties.applicationId())
                .build());
        return response;
    }

    private Jwt decode(String accessToken) {
        try {
            return securityAuthTokenValidator.decode(accessToken);
        } catch (JwtException e) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "Token inválido o expirado");
        }
    }

    private String resolveNombreCompleto(
            Long idPersonaGeneral,
            String username) {
        if (idPersonaGeneral == null) {
            return username;
        }
        return personaGeneralRepository.findGeneralPersonById(idPersonaGeneral)
                .map(persona -> persona.getPersonaNaturalGeneral())
                .map(natural -> Stream.of(
                                natural.getPrimerNombre(),
                                natural.getSegundoNombre(),
                                natural.getPrimerApellido(),
                                natural.getSegundoApellido())
                        .filter(part -> part != null && !part.isBlank())
                        .collect(Collectors.joining(" ")))
                .filter(nombre -> !nombre.isBlank())
                .orElse(username);
    }
}
