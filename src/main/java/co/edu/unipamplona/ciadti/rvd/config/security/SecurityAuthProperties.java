/**
 * Aplicación: rvd
 * Archivo: SecurityAuthProperties.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial (adopción SecurityAuth)
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades para validar JWT emitidos por SecurityAuth y resolver permisos.
 *
 * <p>{@code jwkSetUri} (recomendado) o {@code publicKeyPem} para verificar la
 * firma; {@code issuer} valida {@code iss}; {@code applicationId} filtra el
 * claim {@code aplicaciones}; {@code baseUrl} consulta funcionalidades;
 * {@code enforceFuncionalidad} activa el 403 estricto por METHOD:URL.</p>
 */
@ConfigurationProperties(prefix = "rvd.security.security-auth")
public record SecurityAuthProperties(
        String publicKeyPem,
        String jwkSetUri,
        String issuer,
        String baseUrl,
        Long applicationId,
        boolean enforceFuncionalidad
) {
}
