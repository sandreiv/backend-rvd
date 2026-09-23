/**
 * Aplicación: rvd
 * Archivo: SecurityAuthMenuClientTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.permissions
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Token en blanco no llama a SecurityAuth
 */
package co.edu.unipamplona.ciadti.rvd.config.security.permissions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;

@ExtendWith(MockitoExtension.class)
class SecurityAuthMenuClientTest {

    @Mock
    private SecurityAuthProperties properties;

    @InjectMocks
    private SecurityAuthMenuClient client;

    @Test
    void blankAccessTokenFailsBeforeCallingSecurityAuth() {
        ApiException ex = assertThrows(ApiException.class,
                () -> client.arbolRoles(List.of("Coordinador"), 55100L, "  "));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }
}
