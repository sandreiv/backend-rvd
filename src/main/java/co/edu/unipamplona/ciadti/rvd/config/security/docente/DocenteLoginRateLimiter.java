/**
 * Aplicación: rvd
 * Archivo: DocenteLoginRateLimiter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.docente
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security.docente;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocenteLoginRateLimiter {

    private final DocenteSessionProperties properties;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean isLocked(String remoteAddr, String documento) {
        Bucket bucket = buckets.get(key(remoteAddr, documento));
        return bucket != null && bucket.lockedUntilEpochMs > Instant.now().toEpochMilli();
    }

    public void onFailure(String remoteAddr, String documento) {
        String key = key(remoteAddr, documento);
        buckets.compute(key, (ignored, current) -> {
            int failures = current == null ? 1 : current.failures + 1;
            long lockedUntil = 0L;
            if (failures >= properties.maxAttempts()) {
                lockedUntil = Instant.now()
                        .plusSeconds(properties.lockMinutes() * 60L)
                        .toEpochMilli();
                failures = 0;
            }
            return new Bucket(failures, lockedUntil);
        });
    }

    public void onSuccess(String remoteAddr, String documento) {
        buckets.remove(key(remoteAddr, documento));
    }

    private String key(String remoteAddr, String documento) {
        String material = (remoteAddr == null ? "" : remoteAddr) + "|" + documento;
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(material.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private record Bucket(int failures, long lockedUntilEpochMs) {
    }
}
