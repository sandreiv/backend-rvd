/**
 * Aplicación: rvd
 * Archivo: DocenteJwtService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.docente
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security.docente;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocenteJwtService {

    public static final String AUDIENCE = "rvd-docente";

    private final DocenteSessionProperties properties;

    public String issue(Long idPersonaGeneral) {
        Instant now = Instant.now();
        Instant exp = now.plus(properties.tokenMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(String.valueOf(idPersonaGeneral))
                .audience().add(AUDIENCE).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key())
                .compact();
    }

    public Long parseIdPersonaGeneral(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .requireAudience(AUDIENCE)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    public boolean isConfigured() {
        String secret = properties.hmacSecret();
        return secret != null && secret.getBytes(StandardCharsets.UTF_8).length >= 32;
    }

    private SecretKey key() {
        if (!isConfigured()) {
            throw new ApiException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Sesión de docente no configurada");
        }
        return Keys.hmacShaKeyFor(
                properties.hmacSecret().getBytes(StandardCharsets.UTF_8));
    }
}
