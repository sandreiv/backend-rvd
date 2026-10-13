/**
 * Aplicación: rvd
 * Archivo: DocenteJwtAuthenticationFilter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.docente
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 * 13/10/2026 - Solo corre en /public y no limpia la sesión de Vortal
 */
package co.edu.unipamplona.ciadti.rvd.config.security.docente;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocenteJwtAuthenticationFilter extends OncePerRequestFilter {

    private final DocenteJwtService docenteJwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path == null || !path.startsWith("/public/")) {
            return true;
        }
        return HttpMethod.POST.matches(request.getMethod())
                && "/public/session".equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            try {
                Long id = docenteJwtService.parseIdPersonaGeneral(token);
                var auth = new UsernamePasswordAuthenticationToken(
                        new DocentePrincipal(id),
                        token,
                        List.of(new SimpleGrantedAuthority("DOCENTE")));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException ignored) {
                // El token de Vortal no es un JWT de docente. No se borra
                // el contexto: la cadena de SecurityAuth ya autenticó al personal.
            }
        }
        filterChain.doFilter(request, response);
    }
}
