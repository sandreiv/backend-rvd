/**
 * Aplicación: rvd
 * Archivo: CookieBearerTokenResolverTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class CookieBearerTokenResolverTest {

    private static final String COOKIE = "RVD_SESSION";
    private static final String SESSION_ID = "abc123";
    private static final String JWT = "jwt-en-store";
    private static final String HEADER_JWT = "jwt-en-header";

    @Mock
    private OpaqueSessionStore sessionStore;

    @Mock
    private BearerTokenResolver headerResolver;

    @Test
    void cookieKnownReturnsStoredJwt() {
        when(sessionStore.find(SESSION_ID)).thenReturn(Optional.of(
                new OpaqueSession(JWT, null, Instant.now().plusSeconds(60))));

        String token = resolver(true).resolve(requestWithCookie());

        assertEquals(JWT, token);
        verifyNoInteractions(headerResolver);
    }

    @Test
    void cookieUnknownReturnsNullEvenIfBearerAllowed() {
        when(sessionStore.find(SESSION_ID)).thenReturn(Optional.empty());

        assertNull(resolver(true).resolve(requestWithCookie()));
    }

    @Test
    void noCookieWithBearerAllowedDelegatesToHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + HEADER_JWT);
        when(headerResolver.resolve(request)).thenReturn(HEADER_JWT);

        assertEquals(HEADER_JWT, resolver(true).resolve(request));
    }

    @Test
    void noCookieWithBearerDisabledReturnsNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + HEADER_JWT);

        assertNull(resolver(false).resolve(request));
        verifyNoInteractions(headerResolver);
    }

    private CookieBearerTokenResolver resolver(boolean allowBearer) {
        SessionCookieProperties properties = new SessionCookieProperties(
                COOKIE, true, "Lax", allowBearer, Duration.ofHours(8));
        return new CookieBearerTokenResolver(
                sessionStore, properties, headerResolver);
    }

    private static MockHttpServletRequest requestWithCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE, SESSION_ID));
        return request;
    }
}
