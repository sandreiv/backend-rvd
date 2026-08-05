/**
 * Aplicación: rvd
 * Archivo: ExternalJwtUserResolver.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial (adopción SecurityAuth)
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.config.security.permissions.SecurityAuthPermissionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resuelve el principal RVD desde el JWT de SecurityAuth.
 * No consulta BD local: RVD solo entra desde Vortal con claims del token.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalJwtUserResolver {

    private final SecurityAuthProperties securityAuthProperties;
    private final SecurityAuthPermissionsService permissionsService;

    public Optional<AuthUserDetails> resolve(Jwt jwt) {
        List<String> roles = rolesForApplication(jwt);
        if (roles.isEmpty()) {
            log.warn("JWT sin roles para application-id={}",
                    securityAuthProperties.applicationId());
            return Optional.empty();
        }

        String username = jwt.getSubject();
        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }

        Long idPersona = readIdPersona(jwt);
        Set<String> permissionAuthorities = permissionsService.permisosPorAplicacion(
                securityAuthProperties.applicationId(),
                roles,
                jwt.getTokenValue());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        for (String permission : permissionAuthorities) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }

        return Optional.of(new AuthUserDetails(
                idPersona,
                username.trim(),
                roles,
                authorities));
    }

    public List<String> rolesForApplication(Jwt jwt) {
        Set<String> roles = new LinkedHashSet<>();
        Object claim = jwt.getClaim("aplicaciones");
        if (!(claim instanceof Collection<?> apps)) {
            return List.of();
        }
        Long appId = securityAuthProperties.applicationId();
        for (Object appObj : apps) {
            if (!(appObj instanceof Map<?, ?> app)) {
                continue;
            }
            if (appId != null && !matchesAppId(app.get("id"), appId)) {
                continue;
            }
            if (app.get("roles") instanceof Collection<?> roleCol) {
                for (Object role : roleCol) {
                    if (role != null && StringUtils.hasText(role.toString())) {
                        roles.add(role.toString().trim());
                    }
                }
            }
        }
        return List.copyOf(roles);
    }

    private static Long readIdPersona(Jwt jwt) {
        Object claim = null;
        if (jwt.hasClaim("idPersona")) {
            claim = jwt.getClaim("idPersona");
        } else if (jwt.hasClaim("idPersonaGeneral")) {
            claim = jwt.getClaim("idPersonaGeneral");
        }
        if (claim == null) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(claim).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean matchesAppId(Object idObj, Long appId) {
        if (idObj instanceof Number number) {
            return number.longValue() == appId;
        }
        try {
            return appId.equals(Long.valueOf(String.valueOf(idObj).trim()));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
