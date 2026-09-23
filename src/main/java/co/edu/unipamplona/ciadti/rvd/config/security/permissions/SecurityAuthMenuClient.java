/**
 * Aplicación: rvd
 * Archivo: SecurityAuthMenuClient.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.permissions
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF: proxy arbol-roles)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.permissions;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.JsonNode;

import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Proxy BFF del árbol de funcionalidades de SecurityAuth
 * ({@code /funcionalidad/arbol-roles}). El SPA ya no tiene el JWT, así que
 * RVD hace la llamada con el token guardado en la sesión opaca y devuelve el
 * JSON tal cual.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthMenuClient {

    private static final String DEFAULT_BASE_URL = "http://127.0.0.1:8171";

    private final SecurityAuthProperties properties;
    private RestClient restClient;

    @PostConstruct
    void init() {
        String base = StringUtils.hasText(properties.baseUrl())
                ? properties.baseUrl().replaceAll("/$", "")
                : properties.issuer();
        this.restClient = RestClient.builder()
                .baseUrl(base != null ? base : DEFAULT_BASE_URL)
                .build();
    }

    public JsonNode arbolRoles(
            List<String> roles,
            Long idAplicacion,
            String accessToken) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/funcionalidad/arbol-roles")
                            .queryParam("roles", String.join(",", roles))
                            .queryParam("idAplicacion", idAplicacion)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientException ex) {
            log.warn("No se pudo obtener arbol-roles de SecurityAuth roles={} app={}: {}",
                    roles, idAplicacion, ex.getMessage());
            throw new ApiException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar el menú en SecurityAuth");
        }
    }
}
