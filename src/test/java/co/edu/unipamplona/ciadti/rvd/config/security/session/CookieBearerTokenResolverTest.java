/**
 * Aplicación: rvd
 * Archivo: CookieBearerTokenResolverTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 * 23/09/2026 - Sebastian Jaimes - Delega en OpaqueSessionAccessTokenResolver
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class CookieBearerTokenResolverTest {

    @Mock
    private OpaqueSessionAccessTokenResolver accessTokenResolver;

    @InjectMocks
    private CookieBearerTokenResolver resolver;

    @Test
    void returnsStoredJwtWhenHelperFindsToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(accessTokenResolver.resolveAccessToken(request))
                .thenReturn(Optional.of("jwt-en-store"));

        assertEquals("jwt-en-store", resolver.resolve(request));
    }

    @Test
    void returnsNullWhenHelperFindsNothing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(accessTokenResolver.resolveAccessToken(request))
                .thenReturn(Optional.empty());

        assertNull(resolver.resolve(request));
    }
}
