/**
 * Aplicación: rvd
 * Archivo: CorsConfig.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.cors
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/03/2026
 * Modificaciones:
 * 26/03/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Primary
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Origin",
                        "Accept",
                        "Enctype",
                        "X-Requested-With",
                        "X-Tenant-Id",
                        "Content-Type",
                        "Content-Disposition",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "Authorization",
                        "sentry-trace", // Genera un identificador único para cada solicitud que se haga. Permite que Sentry pueda rastrear la solicitud por diferentes servicios.
                        "baggage"       // Permite agregar información adicional a la solicitud, por ejemplo, información del usuario o información de contexto, por defecto se accede a la configuración definida arriba.
                ));
        configuration.setExposedHeaders(
                Arrays.asList(
                        "Referer",
                        "Content-Type",
                        "Content-Disposition",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/* 26/03/2026 @:Sebastian Jaimes */