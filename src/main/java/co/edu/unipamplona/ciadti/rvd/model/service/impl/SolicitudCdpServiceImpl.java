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

    private static final String ESTADO_DESARROLLO_ACADEMICO =
            "DESARROLLO ACADEMICO";

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
    public CdpRequestDTO getCurrentRequest() {

        AuthUserDetails user = requireDecano();

        Long idPersonaGeneral =
                user.getIdPersonaGeneral();

        Long idCoordinacion =
                coordinacionRepository
                        .findCdpFacultyCoordinationIdByPersona(
                                idPersonaGeneral
                        );

        if (idCoordinacion == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "El Decano no tiene una coordinación de facultad asociada"
            );
        }

        return solicitudCdpRepository
                .findFirstByIdCoordinacionOrderByIdDesc(
                        idCoordinacion
                )
                .map(this::toDto)
                .orElse(null);
    }        

    @Override
    @Transactional
    public void create(
            String observacion,
            List<MultipartFile> archivos,
            String idPeriodo) {

        AuthUserDetails user = requireDecano();

        Long idPersonaGeneral =
                user.getIdPersonaGeneral();
        
        if (idPeriodo.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }

        Long idCoordinacion =
                coordinacionRepository
                        .findCdpFacultyCoordinationIdByPersona(
                                idPersonaGeneral
                        );

        if (idCoordinacion == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "El Decano no tiene una coordinación de facultad asociada"
            );
        }

        if (
            solicitudCdpRepository.existsByIdCoordinacion(
                idCoordinacion
            )
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe una solicitud CPD para la facultad asociada al Decano"
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

        solicitud.setIdPeriodoUniversitario(Long.valueOf(idPeriodo));

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
                    "No fue posible obtener el identificador de la solicitud CPD"
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
                    "create ===> Error generando JSON de adjuntos para solicitud CPD id={}",
                    idSolicitud,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible registrar la información de los archivos adjuntos"
            );
        }

        log.info(
                "create ===> Solicitud CPD creada. id={}, idCoordinacion={}, idPersonaGeneral={}, adjuntos={}",
                solicitud.getId(),
                idCoordinacion,
                idPersonaGeneral,
                adjuntos.size()
        );
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
                    "saveAttachments ===> Error guardando adjuntos de solicitud CPD id={}",
                    idSolicitud,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible guardar los archivos adjuntos de la solicitud CPD"
            );
        }

        return adjuntos;
    }

    private AuthUserDetails requireDecano() {

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

        boolean decano =
                user.getRoles() != null
                && user.getRoles()
                        .stream()
                        .anyMatch(
                                role ->
                                        ROL_DECANO.equalsIgnoreCase(
                                                role
                                        )
                        );

        if (!decano) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "La solicitud CPD requiere rol Decano"
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
                    "parseAttachments ===> Error leyendo JSON de adjuntos CPD",
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible leer los archivos adjuntos de la solicitud CPD"
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

}