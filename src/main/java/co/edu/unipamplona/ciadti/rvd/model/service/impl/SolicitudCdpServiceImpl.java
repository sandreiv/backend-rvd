package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpRequestDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CdpAdjuntoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.SolicitudCdpEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.SolicitudCdpRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.SolicitudCdpService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudCdpServiceImpl
        implements SolicitudCdpService {

    @Value("${file.cdp.path}")
    private String cdpStoragePath;

    private static final String ROL_DECANO = "Decano";
    private static final String ROL_DESARROLLO_ACADEMICO = "Desarrollo academico";
    private static final String ROL_VICERRECTORIA_ACADEMICA = "Vicerrectoria academica";

    private static final String ESTADO_DESARROLLO_ACADEMICO = "DESARROLLO ACADEMICO";
    private static final String ESTADO_VICERRECTORIA_ACADEMICA = "VICERRECTORIA ACADEMICA";
    private static final String ESTADO_CDP_APROBADO = "CDP APROBADO";

    private static final int MAX_OBSERVACION = 250;

    private final SolicitudCdpRepository solicitudCdpRepository;
    private final CoordinacionRepository coordinacionRepository;

    private final ObjectMapper objectMapper;

    private static final long MAX_FILE_SIZE =
            10L * 1024L * 1024L;

    private static final long MAX_REQUEST_FILES_SIZE =
            100L * 1024L * 1024L;

    @Override
    @Transactional(readOnly = true)
        public CdpRequestDTO getCurrentRequest(
                Long idCoordinacionFacultad) {

        AuthUserDetails user = requireRol(ROL_DECANO);

        Long idPersonaGeneral =
                user.getIdPersonaGeneral();

        validateCdpFacultyAccess(
                idPersonaGeneral,
                idCoordinacionFacultad
        );

        return solicitudCdpRepository
                .findFirstByIdCoordinacionOrderByIdDesc(
                        idCoordinacionFacultad
                )
                .map(this::toDto)
                .orElse(null);
     }

    @Override
    @Transactional
    public void create(
        String observacion,
        List<MultipartFile> archivos,
        String idPeriodo,
        String idCoordinacionFacultad) {

        AuthUserDetails user = requireRol(ROL_DECANO);

        Long idPersonaGeneral =
                user.getIdPersonaGeneral();
        
        Long idPeriodoUniversitario =
                parseRequiredId(
                        idPeriodo,
                        "El periodo universitario es obligatorio"
                );

        Long idCoordinacion =
                parseRequiredId(
                        idCoordinacionFacultad,
                        "La facultad es obligatoria"
                );

        validateCdpFacultyAccess(
                idPersonaGeneral,
                idCoordinacion
        );        

        if (
        solicitudCdpRepository.existsByIdCoordinacion(
                idCoordinacion
        )
        ) {
        throw new ApiException(
                HttpStatus.CONFLICT,
                "Ya existe una solicitud CDP para la facultad seleccionada"
        );
        }

        String observacionNormalizada =
        normalizeObservation(observacion);

        validateAttachments(archivos);

        SolicitudCdpEntity solicitud =
                new SolicitudCdpEntity();

        solicitud.setIdCoordinacion(
                idCoordinacion
        );

        solicitud.setEstado(
                ESTADO_DESARROLLO_ACADEMICO
        );

        solicitud.setAdjunto(null);

        solicitud.setObservacion(
                observacionNormalizada
        );

        solicitud.setIdPeriodoUniversitario(
                idPeriodoUniversitario
        );

        solicitud.setNumero(null);

        solicitud.setRegistradoPor(
                RegistradoPorUtils.value(
                        Accion.INSERT
                )
        );

        solicitud.setFechaCambio(
                new Date()
        );

        /*
        * Primero se crea la solicitud para obtener
        * el SOCD_ID generado por Oracle.
        */
        solicitudCdpRepository.saveAndFlush(
                solicitud
        );

        Long idSolicitud = solicitud.getId();

        if (idSolicitud == null) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible obtener el identificador de la solicitud CDP"
            );
        }

        /*
        * Ahora que conocemos SOCD_ID,
        * guardamos los archivos en:
        *
        * archivos/cdp/{SOCD_ID}
        */
        List<CdpAdjuntoDTO> adjuntos =
                saveAttachments(
                        archivos,
                        idSolicitud
                );

        try {

            String adjuntosJson =
                    objectMapper.writeValueAsString(
                            adjuntos
                    );

            solicitud.setAdjunto(
                    adjuntosJson
            );

            solicitudCdpRepository.save(
                    solicitud
            );

        } catch (JsonProcessingException ex) {

            log.error(
                    "create ===> Error generando JSON de adjuntos para solicitud CDP id={}",
                    idSolicitud,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible registrar la información de los archivos adjuntos"
            );
        }

        log.info(
                "create ===> Solicitud CDP creada. id={}, idCoordinacion={}, idPersonaGeneral={}, adjuntos={}",
                solicitud.getId(),
                idCoordinacion,
                idPersonaGeneral,
                adjuntos.size()
        );
    }

    @Override
    @Transactional
    public void sendRequestToVice(Long idSolicitud) {
        requireRol(ROL_DESARROLLO_ACADEMICO);

        if (idSolicitud == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El ID de la solicitud es obligatorio");
        }

        SolicitudCdpEntity solicitud = solicitudCdpRepository.findById(idSolicitud).orElseThrow(() -> {
            return new ApiException(HttpStatus.NOT_FOUND, "No existe la solicitud CDP con id " + idSolicitud);
        });

        if (ESTADO_VICERRECTORIA_ACADEMICA.equals(solicitud.getEstado())) {
            log.info(
                "sendRequestToVice ===> La solicitud CDP ya se encuentra en vicerrectoria academica. idSolicitud={}", idSolicitud
            );
            return;
        }

        solicitud.setEstado(ESTADO_VICERRECTORIA_ACADEMICA);
        solicitud.setRegistradoPor(
            RegistradoPorUtils.value(
                Accion.UPDATE
            )
        );
        solicitud.setFechaCambio(new Date());

        solicitudCdpRepository.save(solicitud);

        log.info("sendRequestToVice ===> Solicitud CDP actualizada. id={}, estado={}", idSolicitud, ESTADO_VICERRECTORIA_ACADEMICA);
    }

    @Override
    @Transactional
    public void approveCdpRequest(Long idSolicitud) {
        requireRol(ROL_VICERRECTORIA_ACADEMICA);

        if (idSolicitud == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El ID de la solicitud es obligatorio");
        }

        SolicitudCdpEntity solicitud = solicitudCdpRepository.findById(idSolicitud).orElseThrow(() -> {
            return new ApiException(HttpStatus.NOT_FOUND, "No existe la solicitud CDP con id " + idSolicitud);
        });

        if (ESTADO_CDP_APROBADO.equals(solicitud.getEstado())) {
            log.info(
                "approveCdpRequest ===> La solicitud CDP ya se encuentra aprobada por vicerrectoria academica. idSolicitud={}", idSolicitud
            );
            return;
        }

        String numeroCdp = generateRandomCdpCode();

        solicitud.setEstado(ESTADO_CDP_APROBADO);
        solicitud.setNumero(numeroCdp);
        solicitud.setRegistradoPor(
            RegistradoPorUtils.value(
                Accion.UPDATE
            )
        );
        solicitud.setFechaCambio(new Date());

        solicitudCdpRepository.save(solicitud);

        log.info("approveCdpRequest ===> Solicitud CDP actualizada. id={}, estado={}, numero={}", idSolicitud, ESTADO_CDP_APROBADO, numeroCdp);
    }

    private List<CdpAdjuntoDTO> saveAttachments(
            List<MultipartFile> archivos,
            Long idSolicitud) {

        List<CdpAdjuntoDTO> adjuntos =
                new ArrayList<>();

        if (archivos == null || archivos.isEmpty()) {
            return adjuntos;
        }

        Path storageDirectory =
                Paths.get(
                        cdpStoragePath,
                        idSolicitud.toString()
                );

        try {

            Files.createDirectories(
                    storageDirectory
            );

            for (MultipartFile archivo : archivos) {

                if (archivo == null || archivo.isEmpty()) {
                    continue;
                }

                String nombreOriginal =
                        archivo.getOriginalFilename();

                if (!StringUtils.hasText(nombreOriginal)) {
                    continue;
                }

                String nombreSeguro =
                        Path.of(nombreOriginal)
                                .getFileName()
                                .toString();

                Path destino =
                        storageDirectory.resolve(
                                nombreSeguro
                        );

                Files.copy(
                        archivo.getInputStream(),
                        destino,
                        StandardCopyOption.REPLACE_EXISTING
                );

                String pathRelativo =
                        "archivos/cdp/"
                                + idSolicitud
                                + "/"
                                + nombreSeguro;

                adjuntos.add(
                        new CdpAdjuntoDTO(
                                nombreSeguro,
                                pathRelativo
                        )
                );
            }

        } catch (IOException ex) {

            log.error(
                    "saveAttachments ===> Error guardando adjuntos de solicitud CDP id={}",
                    idSolicitud,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible guardar los archivos adjuntos de la solicitud CDP"
            );
        }

        return adjuntos;
    }

    private AuthUserDetails requireRol(String rolRequerido) {

        AuthUserDetails user =
                SecurityUtils.currentUser()
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Usuario no autenticado"
                                )
                        );

        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "El usuario autenticado no tiene persona institucional asociada"
            );
        }

        boolean rolRequired =
                user.getRoles() != null
                && user.getRoles()
                        .stream()
                        .anyMatch(
                                role ->
                                        rolRequerido.equalsIgnoreCase(
                                                role
                                        )
                        );

        if (!rolRequired) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La solicitud CDP requiere rol " + rolRequerido
            );
        }

        return user;
    }

    private String normalizeObservation(
            String observacion) {

        if (!StringUtils.hasText(observacion)) {
            return null;
        }

        String value =
                observacion.trim();

        if (value.length() > MAX_OBSERVACION) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La observación no puede superar los 250 caracteres"
            );
        }

        return value;
    }

    private CdpRequestDTO toDto(
            SolicitudCdpEntity solicitud) {

        List<CdpAdjuntoDTO> adjuntos =
                parseAttachments(
                        solicitud.getAdjunto()
                );

        return new CdpRequestDTO(
                solicitud.getId(),
                solicitud.getIdCoordinacion(),
                solicitud.getEstado(),
                solicitud.getObservacion(),
                adjuntos,
                solicitud.getFechaCambio()
        );
    }

    private List<CdpAdjuntoDTO> parseAttachments(
            String adjuntosJson) {

        if (!StringUtils.hasText(adjuntosJson)) {
            return List.of();
        }

        try {

            return objectMapper.readValue(
                    adjuntosJson,
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    CdpAdjuntoDTO.class
                            )
            );

        } catch (JsonProcessingException ex) {

            log.error(
                    "parseAttachments ===> Error leyendo JSON de adjuntos CDP",
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible leer los archivos adjuntos de la solicitud CDP"
            );
        }
    }

    private void validateAttachments(
            List<MultipartFile> archivos) {

        if (archivos == null || archivos.isEmpty()) {
            return;
        }

        long totalSize = 0L;

        for (MultipartFile archivo : archivos) {

            if (archivo == null || archivo.isEmpty()) {
                continue;
            }

            if (archivo.getSize() > MAX_FILE_SIZE) {

                throw new ApiException(
                        HttpStatus.PAYLOAD_TOO_LARGE,
                        "El archivo "
                                + archivo.getOriginalFilename()
                                + " supera el tamaño máximo permitido de 10 MB"
                );
            }

            totalSize += archivo.getSize();
        }

        if (totalSize > MAX_REQUEST_FILES_SIZE) {

            throw new ApiException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "Los archivos adjuntos superan el tamaño máximo permitido de 100 MB por solicitud"
            );
        }
    }

    private void validateCdpFacultyAccess(
                Long idPersonaGeneral,
                Long idCoordinacionFacultad) {

        if (idCoordinacionFacultad == null) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "La facultad es obligatoria"
                );
        }

        Long idCoordinacionAsociada =
                coordinacionRepository
                        .findCdpFacultyCoordinationIdByPersonaAndId(
                                idPersonaGeneral,
                                idCoordinacionFacultad
                        );

        if (idCoordinacionAsociada == null) {
                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "La facultad seleccionada no está asociada al Decano autenticado"
                );
        }
    }

    private Long parseRequiredId(
        String value,
        String requiredMessage) {

        if (!StringUtils.hasText(value)) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        requiredMessage
                );
        }

        try {
                return Long.valueOf(value.trim());
        } catch (NumberFormatException ex) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "El identificador recibido no es válido"
                );
        }
    }

    private String generateRandomCdpCode() {
        int number = (int) (Math.random() * 1_000_000);
        return String.format("%06d", number);
    }
}