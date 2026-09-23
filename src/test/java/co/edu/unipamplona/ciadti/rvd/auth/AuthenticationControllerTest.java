/**
 * Aplicación: rvd
 * Archivo: AuthenticationControllerTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 * 23/09/2026 - Sebastian Jaimes - /menu usa store; /me rellena expiresAt
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.permissions.SecurityAuthMenuClient;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSession;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionAccessTokenResolver;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionStore;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieService;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String COOKIE = "RVD_SESSION";
    private static final String SESSION_ID = "opaque-id";
    private static final String CSRF_VALUE = "csrf-value";
    private static final String JWT = "jwt-real-security-auth";
    private static final Long APP_ID = 55100L;

    @Mock
    private SecurityAuthBootstrapService bootstrapService;

    @Mock
    private OpaqueSessionStore sessionStore;

    @Mock
    private SecurityAuthMenuClient menuClient;

    @Mock
    private SecurityAuthProperties securityAuthProperties;

    @Mock
    private OpaqueSessionAccessTokenResolver accessTokenResolver;

    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        SessionCookieProperties properties = new SessionCookieProperties(
                COOKIE, true, "Lax", false, Duration.ofHours(8));
        SessionCookieService cookieService =
                new SessionCookieService(properties, "/rvd");
        controller = new AuthenticationController(
                bootstrapService,
                sessionStore,
                cookieService,
                menuClient,
                securityAuthProperties,
                accessTokenResolver);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void bootstrapSetsHttpOnlyCookieAndReturnsBodyWithCsrf() {
        Instant exp = Instant.now().plusSeconds(3600);
        JwtAuthResponseDTO body = new JwtAuthResponseDTO();
        body.setUsername("pmduran");
        SecurityAuthBootstrapRequestDTO request = new SecurityAuthBootstrapRequestDTO();
        request.setAccessToken("jwt");
        when(bootstrapService.bootstrap(request))
                .thenReturn(new BootstrapResult(SESSION_ID, exp, body));
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<JwtAuthResponseDTO> result =
                controller.bootstrap(request, requestWithCsrf(), response);

        String setCookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.startsWith(COOKIE + "=" + SESSION_ID + ";"));
        assertTrue(setCookie.contains("HttpOnly"));
        assertTrue(setCookie.contains("Secure"));
        assertTrue(setCookie.contains("SameSite=Lax"));
        assertTrue(setCookie.contains("Path=/rvd"));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        JwtAuthResponseDTO dto = result.getBody();
        assertNotNull(dto);
        assertEquals("pmduran", dto.getUsername());
        assertEquals("X-XSRF-TOKEN", dto.getCsrfHeaderName());
        assertEquals(CSRF_VALUE, dto.getCsrfToken());
    }

    @Test
    void logoutRevokesSessionAndClearsCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE, SESSION_ID));
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<Void> result = controller.logout(request, response);

        verify(sessionStore).revoke(SESSION_ID);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        String setCookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.startsWith(COOKIE + "=;"));
        assertTrue(setCookie.contains("Max-Age=0"));
        assertTrue(setCookie.contains("HttpOnly"));
    }

    @Test
    void logoutWithoutCookieStillClearsCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        controller.logout(new MockHttpServletRequest(), response);

        verifyNoInteractions(sessionStore);
        String setCookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.contains("Max-Age=0"));
    }

    @Test
    void menuUsesJwtFromStoreNotCredentials() throws Exception {
        authenticateUser();
        MockHttpServletRequest request = new MockHttpServletRequest();
        JsonNode tree = new ObjectMapper().readTree("[{\"codigo\":\"01\"}]");
        when(accessTokenResolver.resolveAccessToken(request))
                .thenReturn(Optional.of(JWT));
        when(securityAuthProperties.applicationId()).thenReturn(APP_ID);
        when(menuClient.arbolRoles(List.of("Coordinador"), APP_ID, JWT))
                .thenReturn(tree);

        ResponseEntity<JsonNode> result = controller.menu(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("01", result.getBody().get(0).get("codigo").asText());
    }

    @Test
    void menuWithoutTokenThrowsUnauthorized() {
        authenticateUser();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(accessTokenResolver.resolveAccessToken(request))
                .thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class,
                () -> controller.menu(request));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        verifyNoInteractions(menuClient);
    }

    @Test
    void meFillsExpiresAtFromOpaqueSession() {
        authenticateUser();
        Instant exp = Instant.now().plusSeconds(1800);
        OpaqueSession session = new OpaqueSession(JWT, null, exp);
        MockHttpServletRequest request = requestWithCsrf();
        JwtAuthResponseDTO body = new JwtAuthResponseDTO();
        body.setUsername("pmduran");
        when(bootstrapService.buildResponse(org.mockito.ArgumentMatchers.any()))
                .thenReturn(body);
        when(accessTokenResolver.findSession(request))
                .thenReturn(Optional.of(session));
        when(sessionStore.expiresAt(session)).thenReturn(exp);

        ResponseEntity<JwtAuthResponseDTO> result = controller.me(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(exp, result.getBody().getExpiresAt());
        assertEquals("X-XSRF-TOKEN", result.getBody().getCsrfHeaderName());
    }

    private static void authenticateUser() {
        AuthUserDetails user = new AuthUserDetails(
                231326L, "pmduran", List.of("Coordinador"), List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    private static MockHttpServletRequest requestWithCsrf() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(
                CsrfToken.class.getName(),
                new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", CSRF_VALUE));
        return request;
    }
}
