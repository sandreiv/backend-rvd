/**
 * Aplicación: rvd
 * Archivo: DocenteSessionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/10/2026
 * Modificaciones:
 * 09/10/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.config.security.docente.DocenteJwtService;
import co.edu.unipamplona.ciadti.rvd.config.security.docente.DocenteLoginRateLimiter;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSesionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteSessionResponseDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaNaturalGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.DocenteSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocenteSessionServiceImpl implements DocenteSessionService {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final String FECHA_NULA = "0000-00-00";
    private static final String CREDENCIALES_INVALIDAS = "No fue posible iniciar sesión";

    private final PersonaGeneralRepository personaGeneralRepository;
    private final DocenteJwtService docenteJwtService;
    private final DocenteLoginRateLimiter rateLimiter;

    @Override
    @Transactional(readOnly = true)
    public DocenteSessionResponseDTO openSession(
            DocenteSessionRequestDTO body,
            String remoteAddr) {
        if (!docenteJwtService.isConfigured()) {
            throw new ApiException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Sesión de docente no configurada");
        }

        String documento = body.numeroDocumento().trim();
        if (rateLimiter.isLocked(remoteAddr, documento)) {
            log.warn("openSession ===> Intentos de sesión docente bloqueados");
            throw new ApiException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiados intentos. Intente más tarde");
        }

        List<PersonaGeneralEntity> candidatos =
                personaGeneralRepository.findDocentesByDocumentoExacto(documento);

        List<PersonaGeneralEntity> coincidencias = candidatos.stream()
                .filter(persona -> fechasCoinciden(
                        persona.getFechaExpedicion(),
                        body.fechaExpedicion()))
                .toList();

        if (candidatos.isEmpty()) {
            fechasCoinciden(null, body.fechaExpedicion());
        }

        if (coincidencias.size() != 1) {
            rateLimiter.onFailure(remoteAddr, documento);
            if (coincidencias.size() > 1) {
                log.warn("openSession ===> Más de un docente coincide. total={}",
                        coincidencias.size());
            }
            throw new ApiException(HttpStatus.UNAUTHORIZED, CREDENCIALES_INVALIDAS);
        }

        rateLimiter.onSuccess(remoteAddr, documento);
        PersonaGeneralEntity docente = coincidencias.get(0);
        String token = docenteJwtService.issue(docente.getId());
        return new DocenteSessionResponseDTO(
                token,
                new DocenteSesionDTO(
                        docente.getDocumentoIdentidad(),
                        nombreCompleto(docente.getPersonaNaturalGeneral())));
    }

    private boolean fechasCoinciden(Date almacenada, LocalDate enviada) {
        String izquierda = aLocalDate(almacenada);
        String derecha = enviada == null ? FECHA_NULA : enviada.toString();
        return MessageDigest.isEqual(
                izquierda.getBytes(StandardCharsets.UTF_8),
                derecha.getBytes(StandardCharsets.UTF_8));
    }

    private String aLocalDate(Date almacenada) {
        if (almacenada == null) {
            return FECHA_NULA;
        }
        if (almacenada instanceof java.sql.Date sql) {
            return sql.toLocalDate().toString();
        }
        return almacenada.toInstant().atZone(ZONA).toLocalDate().toString();
    }

    private String nombreCompleto(PersonaNaturalGeneralEntity natural) {
        if (natural == null) {
            return "";
        }
        return Stream.of(
                        natural.getPrimerNombre(),
                        natural.getSegundoNombre(),
                        natural.getPrimerApellido(),
                        natural.getSegundoApellido())
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
    }
}
