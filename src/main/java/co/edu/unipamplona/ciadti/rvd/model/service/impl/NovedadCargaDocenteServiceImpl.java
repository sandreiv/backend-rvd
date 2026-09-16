package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.AsignarNombreNnDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.NovedadCargaDocenteService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovedadCargaDocenteServiceImpl
        implements NovedadCargaDocenteService {

    private static final String COMPONENT_ASSIGN_NAME_NN =
            "asign-name-nn";

    private final CargaDocenteRepository cargaDocenteRepository;

    private final NovedadCargaDocenteRepository
            novedadCargaDocenteRepository;

    private final NovedadRepository novedadRepository;

    @Override
    @Transactional
    public void assignNameToNn(
            AsignarNombreNnDTO dto
    ) {

        validateRequest(dto);

        CargaDocenteEntity cargaDocente =
                cargaDocenteRepository
                        .findById(dto.idCargaDocente())
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la carga docente seleccionada"
                                )
                        );

        NovedadEntity novedad =
                novedadRepository
                        .findById(dto.idNovedad())
                        .orElseThrow(
                                () -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "No existe la novedad seleccionada"
                                )
                        );

        validateNoveltyType(
                novedad
        );

        /*
         * Una carga no debe recibir otra novedad
         * mientras tenga una pendiente de revisión.
         */
        if (
            novedadCargaDocenteRepository
                    .countNoveltyInReview(
                            dto.idCargaDocente()
                    ) > 0
        ) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El docente tiene una novedad en revisión"
            );
        }

        /*
         * El método agregado por el equipo determina
         * si existe una fotografía válida anterior.
         */
        Optional<NovedadCargaDocenteEntity> previous =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                dto.idCargaDocente()
                        );

        /*
         * Asignar nombre a NN solamente se puede ejecutar
         * cuando el estado efectivo todavía no tiene persona.
         */
        Long currentPersonId =
                previous
                        .map(
                            NovedadCargaDocenteEntity
                                    ::getIdPersonaGeneral
                        )
                        .orElse(
                            cargaDocente.getIdPersonaGeneral()
                        );

        if (currentPersonId != null) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga seleccionada ya tiene un docente asignado"
            );
        }

        String registradoPor =
                RegistradoPorUtils.value(
                        Accion.INSERT
                );

        int inserted;

        if (previous.isPresent()) {

            inserted =
                    novedadCargaDocenteRepository
                            .insertAssignNameNnFromNovelty(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor
                            );

        } else {

            inserted =
                    novedadCargaDocenteRepository
                            .insertAssignNameNnFromCargaDocente(
                                    dto.idCargaDocente(),
                                    dto.idPersonaGeneral(),
                                    dto.idNovedad(),
                                    registradoPor
                            );
        }

        if (inserted != 1) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No fue posible registrar la novedad"
            );
        }

        log.info(
                "assignNameToNn ===> Novedad registrada. cadoId={}, noveId={}, nuevoPegeId={}",
                dto.idCargaDocente(),
                dto.idNovedad(),
                dto.idPersonaGeneral()
        );
    }

    private void validateRequest(
            AsignarNombreNnDTO dto
    ) {

        if (
            dto == null ||
            dto.idCargaDocente() == null ||
            dto.idNovedad() == null ||
            dto.idPersonaGeneral() == null
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente, la novedad y el docente son obligatorios"
            );
        }
    }

    private void validateNoveltyType(
            NovedadEntity novedad
    ) {

        String component =
                novedad.getComponente();

        if (
            component == null ||
            !COMPONENT_ASSIGN_NAME_NN
                    .equalsIgnoreCase(
                            component.trim()
                    )
        ) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La novedad seleccionada no corresponde a Asignar nombre a NN"
            );
        }
    }
}