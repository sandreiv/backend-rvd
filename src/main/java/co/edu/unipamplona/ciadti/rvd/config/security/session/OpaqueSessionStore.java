/**
 * Aplicación: rvd
 * Archivo: OpaqueSessionStore.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;

/**
 * Store en memoria (Caffeine) de sesiones BFF: id opaco → JWT SecurityAuth.
 *
 * <p>Cada entrada vence con el {@code exp} del JWT (o {@code defaultTtl}).
 * Una sola instancia de RVD: si se escala horizontalmente hay que mover
 * este store a un medio compartido.</p>
 */
@Component
public class OpaqueSessionStore {

    private static final int ID_BYTES = 32;
    private static final long MAX_SESSIONS = 10_000L;

    private final SecureRandom random = new SecureRandom();
    private final Duration defaultTtl;
    private final Cache<String, OpaqueSession> cache;

    public OpaqueSessionStore(SessionCookieProperties properties) {
        this.defaultTtl = properties.defaultTtl();
        this.cache = Caffeine.newBuilder()
                .maximumSize(MAX_SESSIONS)
                .expireAfter(new SessionExpiry())
                .build();
    }

    /**
     * Guarda la sesión y devuelve un id aleatorio (256 bits, base64url).
     */
    public String create(OpaqueSession session) {
        byte[] raw = new byte[ID_BYTES];
        random.nextBytes(raw);
        String id = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        cache.put(id, session);
        return id;
    }

    public Optional<OpaqueSession> find(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.getIfPresent(id))
                .filter(session -> !isExpired(session));
    }

    public void revoke(String id) {
        if (StringUtils.hasText(id)) {
            cache.invalidate(id);
        }
    }

    /** Instante de vencimiento efectivo: {@code exp} del JWT o ahora+TTL. */
    public Instant expiresAt(OpaqueSession session) {
        return session.expiresAt() != null
                ? session.expiresAt()
                : Instant.now().plus(defaultTtl);
    }

    private boolean isExpired(OpaqueSession session) {
        return !expiresAt(session).isAfter(Instant.now());
    }

    private long nanosUntilExpiry(OpaqueSession session) {
        Duration remaining = Duration.between(Instant.now(), expiresAt(session));
        return remaining.isNegative() ? 0L : remaining.toNanos();
    }

    private final class SessionExpiry implements Expiry<String, OpaqueSession> {

        @Override
        public long expireAfterCreate(
                String key, OpaqueSession value, long currentTime) {
            return nanosUntilExpiry(value);
        }

        @Override
        public long expireAfterUpdate(
                String key, OpaqueSession value,
                long currentTime, long currentDuration) {
            return nanosUntilExpiry(value);
        }

        @Override
        public long expireAfterRead(
                String key, OpaqueSession value,
                long currentTime, long currentDuration) {
            return currentDuration;
        }
    }
}
