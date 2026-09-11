package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.NovedadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadesService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadesServiceImpl
        implements NovedadesService {

    private static final Set<String> ACCIONES_PERMITIDAS =
            Set.of(
                    "ACTUALIZAR",
                    "ELIMINAR",
                    "GUARDAR"
            );

    private final NovedadRepository novedadRepository;

    private final NovedadCargaDocenteRepository
            novedadCargaDocenteRepository;

    private final NovedadMapper novedadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NovedadListadoDTO> listNovedades() {

        log.debug(
                "listNovedades ===> Listando novedades"
        );

        List<NovedadListadoDTO> result =
                novedadMapper.toNovedadListadoDTOList(
                        novedadRepository.findAllNovedades()
                );

        log.info(
                "listNovedades ===> Novedades listadas. total={}",
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void saveNovedad(
            NovedadFormularioDTO dto
    ) {

        log.info(
                "saveNovedad ===> Guardando novedad. tipo={}, accion={}",
                dto != null ? dto.tipo() : null,
                dto != null ? dto.accion() : null
        );

        validateNovedad(dto);

        NovedadEntity entity =
                new NovedadEntity();

        fillNovedad(
                entity,
                dto
        );

        novedadRepository.save(entity);

        log.info(
                "saveNovedad ===> Novedad guardada. tipo={}, accion={}",
                dto.tipo(),
                dto.accion()
        );
    }

    @Override
    @Transactional
    public void updateNovedad(
            Long id,
            NovedadFormularioDTO dto
    ) {

        log.info(
                "updateNovedad ===> Actualizando novedad. id={}, tipo={}, accion={}",
                id,
                dto != null ? dto.tipo() : null,
                dto != null ? dto.accion() : null
        );

        validateNovedad(dto);

        NovedadEntity entity =
                novedadRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "updateNovedad ===> Novedad no encontrada. id={}",
                                    id
                            );

                            return new ApiException(
                                    HttpStatus.NOT_FOUND,
                                    "No existe la novedad con id " + id
                            );
                        });

        fillNovedad(
                entity,
                dto
        );

        novedadRepository.save(entity);

        log.info(
                "updateNovedad ===> Novedad actualizada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteNovedad(Long id) {

        log.info(
                "deleteNovedad ===> Eliminando novedad. id={}",
                id
        );

        if (
            id == null ||
            !novedadRepository.existsById(id)
        ) {

            log.warn(
                    "deleteNovedad ===> Novedad no encontrada. id={}",
                    id
            );

            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la novedad con id " + id
            );
        }

        if (
            novedadCargaDocenteRepository
                    .existsByIdNovedadCatalogo(id)
        ) {

            log.warn(
                    "deleteNovedad ===> Eliminación bloqueada. Novedad asociada a carga docente. id={}",
                    id
            );

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar la novedad porque está asociada a una novedad de carga docente"
            );
        }

        BigDecimal result =
                novedadRepository.deleteByProcedure(
                        id,
                        RegistradoPorUtils.value(
                                Accion.DELETE
                        )
                );

        if (
            result == null ||
            BigDecimal.ONE.compareTo(result) != 0
        ) {

            log.warn(
                    "deleteNovedad ===> Procedimiento de eliminación falló. id={}, resultado={}",
                    id,
                    result
            );
        }

        validateProcedureResult(
                result,
                "No se pudo eliminar la novedad"
        );

        log.info(
                "deleteNovedad ===> Novedad eliminada. id={}",
                id
        );
    }

    @Override
    @Transactional
    public void deleteBulkNovedades(
            List<Long> ids
    ) {

        log.info(
                "deleteBulkNovedades ===> Eliminación masiva de novedades. total={}",
                ids != null ? ids.size() : 0
        );

        if (
            ids == null ||
            ids.isEmpty()
        ) {

            log.debug(
                    "deleteBulkNovedades ===> Lista vacía. No se realiza eliminación"
            );

            return;
        }

        for (Long id : ids) {
            deleteNovedad(id);
        }

        log.info(
                "deleteBulkNovedades ===> Eliminación masiva finalizada. total={}",
                ids.size()
        );
    }

    private void fillNovedad(
            NovedadEntity entity,
            NovedadFormularioDTO dto
    ) {

        entity.setTipo(
                dto.tipo().trim()
        );

        entity.setDescripcion(
                dto.descripcion().trim()
        );

        entity.setAccion(
                dto.accion()
                        .trim()
                        .toUpperCase()
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

    private void validateNovedad(
            NovedadFormularioDTO dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de la novedad es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.tipo())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de novedad es obligatorio"
            );
        }

        if (!StringUtils.hasText(dto.descripcion())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La descripción es obligatoria"
            );
        }

        if (!StringUtils.hasText(dto.accion())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La acción es obligatoria"
            );
        }

        if (dto.tipo().trim().length() > 150) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de novedad no puede superar 150 caracteres"
            );
        }

        if (dto.descripcion().trim().length() > 250) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La descripción no puede superar 250 caracteres"
            );
        }

        String accion =
                dto.accion()
                        .trim()
                        .toUpperCase();

        if (!ACCIONES_PERMITIDAS.contains(accion)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La acción de la novedad no es válida"
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