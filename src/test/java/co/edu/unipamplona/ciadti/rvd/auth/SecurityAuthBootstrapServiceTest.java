/**
 * Aplicación: rvd
 * Archivo: SecurityAuthBootstrapServiceTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.auth
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.ExternalJwtUserResolver;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthTokenValidator;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSession;
import co.edu.unipamplona.ciadti.rvd.config.security.session.OpaqueSessionStore;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.SecurityAuthBootstrapRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;

@ExtendWith(MockitoExtension.class)
class SecurityAuthBootstrapServiceTest {

    private static final String JWT_VALUE = "header.payload.signature";
    private static final String REFRESH = "refresh-token";
    private static final String SESSION_ID = "opaque-session-id";
    private static final Long APP_ID = 55100L;

    @Mock
    private SecurityAuthTokenValidator tokenValidator;

    @Mock
    private ExternalJwtUserResolver userResolver;

    @Mock
    private SecurityAuthProperties properties;

    @Mock
    private PersonaGeneralRepository personaGeneralRepository;

    @Mock
    private OpaqueSessionStore sessionStore;

    @InjectMocks
    private SecurityAuthBootstrapService service;

    @Test
    void bootstrapStoresJwtServerSideAndReturnsSessionWithoutToken() {
        Instant exp = Instant.now().plus(1, ChronoUnit.HOURS)
                .truncatedTo(ChronoUnit.SECONDS);
        Jwt jwt = jwt(exp);
        when(tokenValidator.isActive()).thenReturn(true);
        when(tokenValidator.decode(JWT_VALUE)).thenReturn(jwt);
        when(userResolver.resolve(jwt)).thenReturn(Optional.of(user()));
        when(properties.applicationId()).thenReturn(APP_ID);
        when(personaGeneralRepository.findGeneralPersonById(231326L))
                .thenReturn(Optional.empty());
        when(sessionStore.create(any())).thenReturn(SESSION_ID);
        when(sessionStore.expiresAt(any())).thenReturn(exp);

        BootstrapResult result = service.bootstrap(request());

        ArgumentCaptor<OpaqueSession> captor =
                ArgumentCaptor.forClass(OpaqueSession.class);
        verify(sessionStore).create(captor.capture());
        assertEquals(JWT_VALUE, captor.getValue().accessToken());
        assertEquals(REFRESH, captor.getValue().refreshToken());
        assertEquals(exp, captor.getValue().expiresAt());

        assertEquals(SESSION_ID, result.sessionId());
        assertEquals(exp, result.expiresAt());
        assertEquals("pmduran", result.response().getUsername());
        assertEquals("231326", result.response().getIdPersona());
        assertEquals(List.of("Coordinador"), result.response().getRoles());
        assertEquals(exp, result.response().getExpiresAt());
        assertEquals(APP_ID, result.response().getUsuario().getIdAplicacion());
    }

    @Test
    void invalidJwtThrowsUnauthorizedAndDoesNotCreateSession() {
        when(tokenValidator.isActive()).thenReturn(true);
        when(tokenValidator.decode(JWT_VALUE))
                .thenThrow(new JwtException("expirado"));

        ApiException ex = assertThrows(ApiException.class,
                () -> service.bootstrap(request()));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        verify(sessionStore, never()).create(any());
    }

    @Test
    void userWithoutRolesThrowsForbidden() {
        Jwt jwt = jwt(Instant.now().plusSeconds(600));
        when(tokenValidator.isActive()).thenReturn(true);
        when(tokenValidator.decode(JWT_VALUE)).thenReturn(jwt);
        when(userResolver.resolve(jwt)).thenReturn(Optional.empty());
        when(properties.applicationId()).thenReturn(APP_ID);

        ApiException ex = assertThrows(ApiException.class,
                () -> service.bootstrap(request()));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
        verify(sessionStore, never()).create(any());
    }

    private static SecurityAuthBootstrapRequestDTO request() {
        SecurityAuthBootstrapRequestDTO body = new SecurityAuthBootstrapRequestDTO();
        body.setAccessToken(JWT_VALUE);
        body.setRefreshToken(REFRESH);
        return body;
    }

    private static Jwt jwt(Instant exp) {
        return Jwt.withTokenValue(JWT_VALUE)
                .header("alg", "RS256")
                .subject("pmduran")
                .issuedAt(Instant.now().minusSeconds(5))
                .expiresAt(exp)
                .build();
    }

    private static AuthUserDetails user() {
        return new AuthUserDetails(
                231326L, "pmduran", List.of("Coordinador"), List.of());
    }
}
