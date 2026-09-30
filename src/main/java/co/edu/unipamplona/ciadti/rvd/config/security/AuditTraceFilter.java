/**
 * Aplicación: btaa
 * Archivo: AuditTraceFilter.java
 * Paquete: co.edu.unipamplona.ciadti.btaa.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Leonel Antonio Pérez Ríos - Correlación de traza, MDC, normalización IP localhost y alerta de peticiones lentas
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import co.edu.unipamplona.ciadti.rvd.util.NanoIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad y auditoría HTTP ejecutado con la más alta precedencia.
 * <p>
 * Responsabilidades principales:
 * <ul>
 *     <li>Generación o propagación de identificador de traza único ({@code idTraza}) para correlación distribuida.</li>
 *     <li>Inyección en el contexto de diagnóstico {@link MDC} (idTraza e IP del cliente).</li>
 *     <li>Retorno de la cabecera HTTP {@code X-Trace-Id} en la respuesta al cliente.</li>
 *     <li>Registro detallado de tiempos de respuesta de cada petición API con alerta para peticiones lentas (> 2s).</li>
 *     <li>Limpieza garantizada del MDC al finalizar el ciclo de vida de la solicitud.</li>
 * </ul>
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuditTraceFilter extends OncePerRequestFilter {

    /** Nombre de la cabecera HTTP estándar para el ID de traza de correlación. */
    public static final String TRACE_HEADER = "X-Trace-Id";

    /** Nombre alternativo de la cabecera HTTP para ID de solicitud. */
    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    /** Clave utilizada en el {@link MDC} para almacenar el ID de traza. */
    public static final String MDC_TRACE_KEY = "idTraza";

    /** Clave utilizada en el {@link MDC} para almacenar la IP del cliente. */
    public static final String MDC_IP_KEY = "ip";

    /** Umbral en milisegundos para considerar una petición como lenta y emitir advertencia (2000 ms = 2s). */
    public static final long SLOW_REQUEST_THRESHOLD_MS = 2000L;

    /**
     * Intercepta cada solicitud HTTP entrante para inicializar la trazabilidad y medir el rendimiento.
     *
     * @param request     Solicitud HTTP entrante.
     * @param response    Respuesta HTTP saliente.
     * @param filterChain Cadena de filtros de Spring Security / Servlet.
     * @throws ServletException Si ocurre un error durante el procesamiento del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida durante el filtrado.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String idTraza = resolveTraceId(request);
        String clientIp = resolveClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();

        MDC.put(MDC_TRACE_KEY, idTraza);
        MDC.put(MDC_IP_KEY, clientIp);

        response.setHeader(TRACE_HEADER, idTraza);

        boolean isApiRequest = isApiRequest(uri);
        long startMs = System.currentTimeMillis();

        if (isApiRequest) {
            log.info("[REQ >>] INICIO: {} {}", method, uri);
        }

        try {
            filterChain.doFilter(request, response);
            if (isApiRequest) {
                long duration = System.currentTimeMillis() - startMs;
                log.info("[REQ <<] FIN: {} {} - Estado: {} ({} ms)", method, uri, response.getStatus(), duration);
                if (duration >= SLOW_REQUEST_THRESHOLD_MS) {
                    log.warn("[SLOW !!] ADVERTENCIA DE RENDIMIENTO: {} {} tardó {} ms (> 2s)", method, uri, duration);
                }
            }
        } catch (Exception ex) {
            if (isApiRequest) {
                long duration = System.currentTimeMillis() - startMs;
                log.error("[REQ !!] ERROR: {} {} tras {} ms: {}", method, uri, duration, ex.getMessage(), ex);
            }
            throw ex;
        } finally {
            MDC.remove(MDC_TRACE_KEY);
            MDC.remove(MDC_IP_KEY);
            MDC.remove("usuario");
        }
    }

    /**
     * Determina si la URI solicitada corresponde a un endpoint de la API de negocio que requiera logueo.
     *
     * @param uri Ruta solicitada en la petición HTTP.
     * @return {@code true} si la ruta contiene {@code /api/} y no es documentación Swagger ni recursos estáticos; {@code false} en caso contrario.
     */
    private boolean isApiRequest(String uri) {
        if (!StringUtils.hasText(uri)) return false;
        return uri.contains("/api/") && !uri.contains("/swagger-ui") && !uri.contains("/api-docs") && !uri.endsWith(".ico");
    }

    /**
     * Resuelve el identificador de traza a partir de las cabeceras HTTP entrantes o genera uno nuevo si no existe.
     *
     * @param request Solicitud HTTP de donde extraer cabeceras de traza.
     * @return Identificador de traza único alfanumérico (ej: {@code "k8X2a9Lq"}).
     */
    private String resolveTraceId(HttpServletRequest request) {
        String traceHeader = request.getHeader(TRACE_HEADER);
        if (StringUtils.hasText(traceHeader)) {
            return traceHeader.trim();
        }
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (StringUtils.hasText(requestId)) {
            return requestId.trim();
        }
        return NanoIdGenerator.random(8);
    }

    /**
     * Extrae y normaliza la dirección IP real del cliente considerando posibles proxies intermedios o balanceadores.
     *
     * @param request Solicitud HTTP entrante.
     * @return Cadena con la dirección IP normalizada en formato IPv4.
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded) && !"unknown".equalsIgnoreCase(forwarded)) {
            String first = forwarded.split(",")[0].trim();
            return cleanIp(first);
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp) && !"unknown".equalsIgnoreCase(realIp)) {
            return cleanIp(realIp.trim());
        }
        String remote = request.getRemoteAddr();
        if (StringUtils.hasText(remote)) {
            return cleanIp(remote.trim());
        }
        return "127.0.0.1";
    }

    /**
     * Limpia la cadena de dirección IP removiendo prefijos IPv6 y normalizando direcciones locales a {@code 127.0.0.1}.
     *
     * @param ip Dirección IP en bruto.
     * @return Dirección IP limpia en formato IPv4 estándar.
     */
    private String cleanIp(String ip) {
        if (ip == null) return "127.0.0.1";
        String clean = ip.trim();
        if (clean.startsWith("::ffff:")) {
            clean = clean.substring("::ffff:".length());
        }
        if ("0:0:0:0:0:0:0:1".equals(clean) || "::1".equals(clean)) {
            return "127.0.0.1";
        }
        return clean;
    }
}

/* 27/08/2026 @:Leonel Antonio Pérez Ríos */
