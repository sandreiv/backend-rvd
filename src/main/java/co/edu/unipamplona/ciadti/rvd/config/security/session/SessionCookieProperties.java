/**
 * Aplicación: rvd
 * Archivo: SessionCookieProperties.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.session
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/09/2026
 * Modificaciones:
 * 23/09/2026 - Sebastian Jaimes - Creación inicial (BFF cookie opaca)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.session;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Propiedades de la cookie de sesión opaca del BFF.
 *
 * <p>{@code cookieName} nombre de la cookie HttpOnly; {@code secure} exige
 * HTTPS (apagar solo en local); {@code sameSite} Lax/Strict/None;
 * {@code allowBearer} mantiene {@code Authorization: Bearer} como fallback
 * (Swagger, Postman); {@code defaultTtl} vida de la sesión cuando el JWT no
 * trae {@code exp}.</p>
 */
@ConfigurationProperties(prefix = "rvd.security.session")
public record SessionCookieProperties(
        @DefaultValue("RVD_SESSION") String cookieName,
        @DefaultValue("true") boolean secure,
        @DefaultValue("Lax") String sameSite,
        @DefaultValue("false") boolean allowBearer,
        @DefaultValue("8h") Duration defaultTtl
) {
}
