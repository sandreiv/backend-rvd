/**
 * Aplicación: rvd
 * Archivo: OpaqueSessionStoreTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpaqueSessionStoreTest {

    private static final String JWT = "eyJhbGciOiJSUzI1NiJ9.payload.sig";

    private OpaqueSessionStore store;

    @BeforeEach
    void setUp() {
        store = new OpaqueSessionStore(new SessionCookieProperties(
                "RVD_SESSION", true, "Lax", false, Duration.ofHours(8)));
    }

    @Test
    void createReturnsDistinctUrlSafeIds() {
        OpaqueSession session = session(Instant.now().plusSeconds(600));

        String first = store.create(session);
        String second = store.create(session);

        assertNotEquals(first, second);
        assertEquals(43, first.length());
        assertTrue(first.matches("[A-Za-z0-9_-]+"));
    }

    @Test
    void findReturnsStoredSessionAndRevokeRemovesIt() {
        String id = store.create(session(Instant.now().plusSeconds(600)));

        Optional<OpaqueSession> found = store.find(id);
        assertTrue(found.isPresent());
        assertEquals(JWT, found.get().accessToken());

        store.revoke(id);
        assertTrue(store.find(id).isEmpty());
    }

    @Test
    void expiredSessionIsNotReturned() {
        String id = store.create(session(Instant.now().minusSeconds(1)));

        assertTrue(store.find(id).isEmpty());
    }

    @Test
    void unknownOrBlankIdReturnsEmpty() {
        assertTrue(store.find("no-existe").isEmpty());
        assertTrue(store.find(null).isEmpty());
        assertTrue(store.find("  ").isEmpty());
    }

    @Test
    void expiresAtFallsBackToDefaultTtlWhenJwtHasNoExp() {
        Instant before = Instant.now().plus(Duration.ofHours(8)).minusSeconds(5);

        Instant expiresAt = store.expiresAt(session(null));

        assertTrue(expiresAt.isAfter(before));
    }

    private static OpaqueSession session(Instant expiresAt) {
        return new OpaqueSession(JWT, "refresh", expiresAt);
    }
}
