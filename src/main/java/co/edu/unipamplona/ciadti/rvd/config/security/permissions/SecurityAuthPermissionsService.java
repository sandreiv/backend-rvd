/**
 * Aplicación: rvd
 * Archivo: SecurityAuthPermissionsService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.permissions
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security.permissions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resuelve permisos METHOD:URL desde SecurityAuth (misma lógica que
 * PermisoRolCacheService del AS) y los cachea en memoria.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuthPermissionsService {

    private static final Map<String, String> METODOS_HTTP = Map.of(
            "LISTAR", "GET",
            "ACTUALIZAR", "PUT",
            "ELIMINAR", "DELETE",
            "GUARDAR", "POST");

    private static final Set<String> HTTP_VERBS = Set.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "*");

    private final SecurityAuthProperties properties;
    private RestClient restClient;
    private Cache<String, Set<String>> cache;

    @PostConstruct
    void init() {
        String base = StringUtils.hasText(properties.baseUrl())
                ? properties.baseUrl().replaceAll("/$", "")
                : properties.issuer();
        this.restClient = RestClient.builder()
                .baseUrl(base != null ? base : "http://127.0.0.1:8171")
                .build();
        this.cache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(java.time.Duration.ofHours(1))
                .build();
    }

    public Set<String> permisosPorAplicacion(
            Long idAplicacion,
            List<String> roles,
            String accessToken) {
        if (roles == null || roles.isEmpty() || idAplicacion == null) {
            return Collections.emptySet();
        }
        String key = buildCacheKey(idAplicacion, roles);
        Set<String> cached = cache.getIfPresent(key);
        if (cached != null) {
            return cached;
        }
        Set<String> resolved = new HashSet<>();
        for (String role : roles) {
            resolved.addAll(fetchAndTransform(role, idAplicacion, accessToken));
        }
        Set<String> immutable = Set.copyOf(resolved);
        cache.put(key, immutable);
        log.debug("Permisos SecurityAuth app={} roles={} size={}",
                idAplicacion, roles, immutable.size());
        return immutable;
    }

    private Set<String> fetchAndTransform(
            String rol,
            Long idAplicacion,
            String accessToken) {
        try {
            List<FuncionalidadPermisoDTO> list = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/funcionalidad/rol-aplicacion")
                            .queryParam("rol", rol)
                            .queryParam("idAplicacion", idAplicacion)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<FuncionalidadPermisoDTO>>() {
                    });
            if (list == null || list.isEmpty()) {
                return Collections.emptySet();
            }
            return transformarPermisos(list);
        } catch (Exception ex) {
            log.warn("No se pudieron obtener permisos de SecurityAuth rol={} app={}: {}",
                    rol, idAplicacion, ex.getMessage());
            return Collections.emptySet();
        }
    }

    /**
     * Convierte funcionalidades a authorities {@code HTTP:URL}, alineado a
     * SecurityAuth (LISTAR→GET, GUARDAR→POST, ACTUALIZAR→PATCH, ELIMINAR→DELETE).
     */
    Set<String> transformarPermisos(List<FuncionalidadPermisoDTO> funcionalidades) {
        return funcionalidades.stream()
                .filter(f -> f.getUrlRecurso() != null
                        && StringUtils.hasText(f.getUrlRecurso()))
                .map(this::toAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private String toAuthority(FuncionalidadPermisoDTO f) {
        String http = resolveHttpMethod(f);
        if (http == null) {
            return null;
        }
        return http + ":" + f.getUrlRecurso().trim();
    }

    private String resolveHttpMethod(FuncionalidadPermisoDTO f) {
        String raw = firstNonBlank(f.getMetodo(), f.getNombre());
        if (!StringUtils.hasText(raw)) {
            return "*";
        }
        String upper = raw.trim().toUpperCase();
        if (HTTP_VERBS.contains(upper)) {
            return upper;
        }
        return METODOS_HTTP.getOrDefault(upper, "*");
    }

    private static String firstNonBlank(String a, String b) {
        if (StringUtils.hasText(a)) {
            return a;
        }
        return b;
    }

    public static String buildCacheKey(Long idAplicacion, List<String> roles) {
        String normalized = roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .sorted()
                .collect(Collectors.joining(","));
        return idAplicacion + ":" + normalized;
    }
}
