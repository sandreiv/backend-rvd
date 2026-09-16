package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PuntosVigenciaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.PuntosVigenciaAdministracionService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PuntosVigenciaAdministracionServiceImpl
        implements PuntosVigenciaAdministracionService {

    private final PuntosVigenciaRepository
            puntosVigenciaRepository;

    private final PuntosVigenciaMapper
            puntosVigenciaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PuntosVigenciaListadoDTO>
            listPointsValidity() {

        log.debug(
                "listPointsValidity ===> Listando puntos por vigencia"
        );

        List<PuntosVigenciaListadoDTO> result =
                puntosVigenciaMapper.toListadoDTOList(
                        puntosVigenciaRepository
                                .findAllPointsValidity()
                );

        log.info(
                "listPointsValidity ===> Vigencias listadas. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void savePointsValidity(
            PuntosVigenciaFormularioDTO dto
    ) {

        log.info(
                "savePointsValidity ===> Guardando vigencia. anio={}",
                dto != null ? dto.anio() : null
        );

        validate(dto);

        if (
            puntosVigenciaRepository
                    .existsByAnio(dto.anio())
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe un valor de punto registrado para el año "
                            + dto.anio()
            );
        }

        PuntosVigenciaEntity entity =
                new PuntosVigenciaEntity();

        fill(
                entity,
                dto
        );

        puntosVigenciaRepository.save(entity);

        log.info(
                "savePointsValidity ===> Vigencia guardada. anio={}",
                dto.anio()
        );
    }

    @Override
    @Transactional
    public void updatePointsValidity(
            Long id,
            PuntosVigenciaFormularioDTO dto
    ) {

        log.info(
                "updatePointsValidity ===> Actualizando vigencia. id={}, anio={}",
                id,
                dto != null ? dto.anio() : null
        );

        validate(dto);

        PuntosVigenciaEntity entity =
                puntosVigenciaRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la vigencia con id "
                                                + id
                                )
                        );

        if (
            puntosVigenciaRepository
                    .existsByAnioAndIdNot(
                            dto.anio(),
                            id
                    )
        ) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe un valor de punto registrado para el año "
                            + dto.anio()
            );
        }

        fill(
                entity,
                dto
        );

        puntosVigenciaRepository.save(entity);

        log.info(
                "updatePointsValidity ===> Vigencia actualizada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deletePointsValidity(
            Long id
    ) {

        log.info(
                "deletePointsValidity ===> Eliminando vigencia. id={}",
                id
        );

        if (
            id == null ||
            !puntosVigenciaRepository.existsById(id)
        ) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la vigencia con id " + id
            );
        }

        BigDecimal result =
                puntosVigenciaRepository
                        .deleteByProcedure(
                                id,
                                RegistradoPorUtils.value(
                                        Accion.DELETE
                                )
                        );

        validateProcedureResult(
                result,
                "No se pudo eliminar la vigencia"
        );

        log.info(
                "deletePointsValidity ===> Vigencia eliminada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteBulkPointsValidity(
            List<Long> ids
    ) {

        log.info(
                "deleteBulkPointsValidity ===> Eliminación masiva. total={}",
                ids != null ? ids.size() : 0
        );

        if (
            ids == null ||
            ids.isEmpty()
        ) {
            return;
        }

        for (Long id : ids) {
            deletePointsValidity(id);
        }

        log.info(
                "deleteBulkPointsValidity ===> Eliminación masiva finalizada. total={}",
                ids.size()
        );
    }

    private void fill(
            PuntosVigenciaEntity entity,
            PuntosVigenciaFormularioDTO dto
    ) {

        entity.setAnio(
                dto.anio()
        );

        entity.setValorPunto(
                normalizeValorPunto(
                        dto.valorPunto()
                )
        );

        entity.setRegistradoPor(
                RegistradoPorUtils.value(
                        entity.getId() == null
                                ? Accion.INSERT
                                : Accion.UPDATE
                )
        );

        entity.setFechaCambio(
                new Date()
        );
    }

    private void validate(
            PuntosVigenciaFormularioDTO dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de la vigencia es obligatoria"
            );
        }

        if (
            dto.anio() == null ||
            dto.anio() <= 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El año es obligatorio y debe ser mayor a cero"
            );
        }

        if (
            !StringUtils.hasText(
                    dto.valorPunto()
            )
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El valor del punto es obligatorio"
            );
        }

        /*
         * También valida que sea numérico,
         * positivo y compatible con VARCHAR2(30).
         */
        normalizeValorPunto(
                dto.valorPunto()
        );
    }

    private String normalizeValorPunto(
            String value
    ) {

        try {
            BigDecimal numeric =
                    new BigDecimal(
                            value.trim()
                    );

            if (
                numeric.compareTo(
                        BigDecimal.ZERO
                ) <= 0
            ) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "El valor del punto debe ser mayor a cero"
                );
            }

            /*
             * Guardamos el valor limpio:
             *
             * 78000
             * 78000.50
             *
             * Nunca:
             * $ 78.000,50
             *
             * porque los cálculos actuales usan
             * new BigDecimal(PUVI_VALORPUNTO).
             */
            String normalized =
                    numeric
                            .stripTrailingZeros()
                            .toPlainString();

            if (normalized.length() > 30) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "El valor del punto no puede superar 30 caracteres"
                );
            }

            return normalized;

        } catch (NumberFormatException ex) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El valor del punto debe ser numérico"
            );
        }
    }

    private void validateProcedureResult(
            BigDecimal result,
            String message
    ) {

        if (
            result == null ||
            BigDecimal.ONE.compareTo(result) != 0
        ) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    message
            );
        }
    }
}