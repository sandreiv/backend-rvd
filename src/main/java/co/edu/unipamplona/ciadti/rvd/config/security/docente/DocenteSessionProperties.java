/**
 * Aplicación: rvd
 * Archivo: DocenteSessionProperties.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.docente
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security.docente;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rvd.security.docente")
public record DocenteSessionProperties(
        String hmacSecret,
        int tokenMinutes,
        int maxAttempts,
        int lockMinutes
) {
}
