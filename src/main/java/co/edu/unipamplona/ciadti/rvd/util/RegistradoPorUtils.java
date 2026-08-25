/**
 * Aplicación: rvd
 * Archivo: RegistradoPorUtils.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/08/2026
 * Modificaciones:
 * 25/08/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - usuario, fecha y método llamador
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Arma {@code registradoPor} en {@code VARCHAR2(150)}.
 * Ejemplo:
 * {@code idPersona:231326|usuario:pmduran|accion:I|ip:10.20.30.41|fecha:2026-08-25 16:57:00|metodo:saveDetailProfessorPreload}
 */
public final class RegistradoPorUtils {

    public static final int MAX_LENGTH = 150;
    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum Accion {
        INSERT("I"),
        UPDATE("U"),
        DELETE("D");

        private final String codigo;

        Accion(String codigo) {
            this.codigo = codigo;
        }

        public String codigo() {
            return codigo;
        }
    }

    private RegistradoPorUtils() {
    }

    public static String value(Accion accion) {
        Accion resolved = accion == null ? Accion.UPDATE : accion;
        String raw = "idPersona:" + idPersona()
                + "-usuario:" + username()
                + "-accion:" + resolved.codigo()
                + "-ip:" + clientIp()
                + "-fecha:" + now()
                + "-metodo:" + callerMethod();
        return truncate(raw);
    }

    private static String idPersona() {
        return SecurityUtils.currentIdPersona()
                .map(String::valueOf)
                .orElse("0");
    }

    private static String username() {
        return SecurityUtils.currentUser()
                .map(AuthUserDetails::getUsername)
                .filter(StringUtils::hasText)
                .orElse("anon");
    }

    private static String now() {
        return LocalDateTime.now().format(FECHA);
    }

    private static String callerMethod() {
        return StackWalker.getInstance().walk(frames -> frames
                .filter(frame -> !isInternalFrame(frame))
                .map(StackWalker.StackFrame::getMethodName)
                .findFirst()
                .orElse("unknown"));
    }

    private static boolean isInternalFrame(StackWalker.StackFrame frame) {
        String cls = frame.getClassName();
        return cls.startsWith(RegistradoPorUtils.class.getName())
                || cls.startsWith("java.")
                || cls.startsWith("jdk.")
                || cls.startsWith("org.springframework");
    }

    private static String clientIp() {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return "local";
        }
        String forwarded = firstForwardedIp(
                request.getHeader("X-Forwarded-For"));
        if (StringUtils.hasText(forwarded)) {
            return normalizeIp(forwarded);
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return normalizeIp(realIp.trim());
        }
        String remote = request.getRemoteAddr();
        if (StringUtils.hasText(remote)) {
            return normalizeIp(remote);
        }
        return "local";
    }

    private static HttpServletRequest currentRequest() {
        RequestAttributes attrs =
                RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servletAttrs)) {
            return null;
        }
        return servletAttrs.getRequest();
    }

    private static String firstForwardedIp(String header) {
        if (!StringUtils.hasText(header)) {
            return null;
        }
        String first = header.split(",")[0].trim();
        if (!StringUtils.hasText(first)
                || "unknown".equalsIgnoreCase(first)) {
            return null;
        }
        return first;
    }

    private static String normalizeIp(String ip) {
        String value = ip.trim();
        if (value.startsWith("::ffff:")) {
            return value.substring("::ffff:".length());
        }
        return value;
    }

    private static String truncate(String value) {
        if (value.length() <= MAX_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_LENGTH);
    }
}
