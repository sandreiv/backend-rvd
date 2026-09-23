/**
 * Aplicación: rvd
 * Archivo: OpaqueSessionAccessTokenResolverTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Cookie/store (o Bearer) → JWT
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class OpaqueSessionAccessTokenResolverTest {

    private static final String COOKIE = "RVD_SESSION";
    private static final String SESSION_ID = "abc123";
    private static final String JWT = "jwt-en-store";
    private static final String HEADER_JWT = "jwt-en-header";

    @Mock
    private OpaqueSessionStore sessionStore;

    @Test
    void cookieKnownReturnsStoredJwt() {
        when(sessionStore.find(SESSION_ID)).thenReturn(Optional.of(
                new OpaqueSession(JWT, null, Instant.now().plusSeconds(60))));

        Optional<String> token = resolver(true).resolveAccessToken(requestWithCookie());

        assertEquals(Optional.of(JWT), token);
    }

    @Test
    void cookieUnknownReturnsEmptyEvenIfBearerAllowed() {
        MockHttpServletRequest request = requestWithCookie();
        request.addHeader("Authorization", "Bearer " + HEADER_JWT);
        when(sessionStore.find(SESSION_ID)).thenReturn(Optional.empty());

        assertTrue(resolver(true).resolveAccessToken(request).isEmpty());
    }

    @Test
    void noCookieWithBearerAllowedReadsHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + HEADER_JWT);

        assertEquals(Optional.of(HEADER_JWT), resolver(true).resolveAccessToken(request));
        verifyNoInteractions(sessionStore);
    }

    @Test
    void noCookieWithBearerDisabledReturnsEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + HEADER_JWT);

        assertTrue(resolver(false).resolveAccessToken(request).isEmpty());
        verifyNoInteractions(sessionStore);
    }

    @Test
    void findSessionReturnsStoreRowForCookie() {
        OpaqueSession session = new OpaqueSession(
                JWT, null, Instant.now().plusSeconds(60));
        when(sessionStore.find(SESSION_ID)).thenReturn(Optional.of(session));

        assertEquals(Optional.of(session), resolver(false).findSession(requestWithCookie()));
    }

    private OpaqueSessionAccessTokenResolver resolver(boolean allowBearer) {
        SessionCookieProperties properties = new SessionCookieProperties(
                COOKIE, true, "Lax", allowBearer, Duration.ofHours(8));
        return new OpaqueSessionAccessTokenResolver(sessionStore, properties);
    }

    private static MockHttpServletRequest requestWithCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE, SESSION_ID));
        return request;
    }
}
