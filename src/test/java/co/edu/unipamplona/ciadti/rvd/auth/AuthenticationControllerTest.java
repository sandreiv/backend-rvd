/**
 * Aplicación: rvd
 * Archivo: AuthenticationControllerTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;

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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.permissions.SecurityAuthMenuClient;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionStore;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.session.SessionCookieService;
import co.edu.unipamplona.ciadti.rvd.model.dto.JwtAuthResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String COOKIE = "RVD_SESSION";
    private static final String SESSION_ID = "opaque-id";
    private static final String CSRF_VALUE = "csrf-value";

    @Mock
    private SecurityAuthBootstrapService bootstrapService;

    @Mock
    private OpaqueSessionStore sessionStore;

    @Mock
    private SecurityAuthMenuClient menuClient;

    @Mock
    private SecurityAuthProperties securityAuthProperties;

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
                securityAuthProperties);
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

    private static MockHttpServletRequest requestWithCsrf() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(
                CsrfToken.class.getName(),
                new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", CSRF_VALUE));
        return request;
    }
}
