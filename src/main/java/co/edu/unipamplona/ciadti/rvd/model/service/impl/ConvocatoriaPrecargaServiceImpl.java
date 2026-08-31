/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaPrecargaServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/06/2026
 * Modificaciones:
 * 04/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaDatosInsertarMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaTipoContratacionFormularioMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.FechasConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDatosInsertarDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaTipoContratacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaTipoContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaTipoContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FechasConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaEstadoService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvocatoriaPrecargaServiceImpl implements ConvocatoriaPrecargaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final ConvocatoriaMapper convocatoriaMapper;
    private final ConvocatoriaTipoContratacionRepository convocatoriaTipoContratacionRepository;
    private final FechasConvocatoriaRepository fechasConvocatoriaRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;
    private final ConvocatoriaDatosInsertarMapper convocatoriaDatosInsertarMapper;
    private final FechasConvocatoriaMapper fechasConvocatoriaMapper;
    private final ConvocatoriaTipoContratacionFormularioMapper convocatoriaTipoContratacionFormularioMapper;
    private final ConvocatoriaEstadoService convocatoriaEstadoService;

    @Override
    @Transactional
    public List<ConvocatoriaDTO> findCallListWithDates(Long idPeriodoUniversidad) {
        log.debug("Listando convocatorias con fechas. idPeriodoUniversidad={}",
                idPeriodoUniversidad);

        convocatoriaEstadoService.syncEstadosConvocatoriasConRestricciones();

        List<ConvocatoriaDTO> result = convocatoriaRepository
                .findCallListWithDates(idPeriodoUniversidad)
                .stream()
                .map(this::toListDtoAndSyncEstado)
                .collect(Collectors.toList());

        log.info("Convocatorias listadas. periodo={}, total={}",
                idPeriodoUniversidad, result.size());
        return result;
    }

    @Override
    @Transactional
    public List<ConvocatoriaDTO> findCallListByFirstPeriodByYear(Long year) {
        log.debug("findCallListByFirstPeriodByYear ===> Listando convocatorias. year={}, periodo=1", year);

        convocatoriaEstadoService.syncEstadosConvocatoriasConRestricciones();

        List<ConvocatoriaDTO> result = convocatoriaRepository
                .findCallListByFirstPeriodByYear(year)
                .stream()
                .map(this::toListDtoAndSyncEstado)
                .collect(Collectors.toList());

        log.info("findCallListByFirstPeriodByYear ===> Convocatorias listadas. year={}, total={}", year, result.size());
        return result;
    }

    private ConvocatoriaDTO toListDtoAndSyncEstado(ConvocatoriaEntity convocatoria) {
        convocatoriaEstadoService.syncEstadoConvocatoria(convocatoria.getId());

        FechasConvocatoriaEntity fechaCnv = convocatoriaRepository.findFechaCnvByConvocatoriaId(convocatoria.getId());

        return convocatoriaMapper.toListDto(
                convocatoria,
                convocatoriaRepository.findPeriodoEntityByConvocatoriaId(
                        convocatoria.getId()),
                convocatoriaRepository.findNivelEntityByConvocatoriaId(
                        convocatoria.getId()),
                fechaCnv,
                personaGeneralRepository.findGeneralPersonById(
                                convocatoria.getIdPersonaGeneral())
                        .orElse(null));
    }

    @Override
    public List<PersonaAutorizaConvocatoriaDTO> searchGeneralPerson(String nombre, String documento) {
        log.debug("Buscando persona autoriza. nombre={}, documento={}", nombre, documento);
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            log.debug("Búsqueda de persona sin criterios. Se retorna lista vacía");
            return Collections.emptyList();
        }
        List<PersonaAutorizaConvocatoriaDTO> result = personaAutorizaConvocatoriaMapper.toDtoList(
                        personaGeneralRepository.searchGeneralPerson(
                                nombreParam, documentoParam));
        log.info("Personas autoriza encontradas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void save(ConvocatoriaFormularioDTO dto) {
        ConvocatoriaDatosInsertarDTO datos = dto.convocatoriaDatosInsertar();
        log.info("Creando convocatoria. nombre={}, periodoId={}",
                datos.nombre(), datos.periodo().id());
        if (datos.id() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no debe incluir id al crear");
        }

        ConvocatoriaEntity convocatoria = new ConvocatoriaEntity();
        convocatoria.setNombre(datos.nombre().trim());
        convocatoria.setDescripcion(datos.descripcion().trim());
        convocatoria.setIdPersonaGeneral(datos.autoriza().id());
        convocatoria.setIdPeriodoUniversidad(datos.periodo().id());
        convocatoria.setIdNivelEducativo(datos.nivelEducativo().id());
        convocatoria.setIdRelacion(resolveIdRelacion(datos.idRelacion(), null));
        convocatoria.setEstado("1");
        convocatoria.setFechaCambio(new Date());
        convocatoria.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        Long convId = convocatoriaRepository.save(convocatoria).getId();
        log.info("Convocatoria creada. id={}", convId);

        if (dto.fechas() != null) {
            for (FechasConvocatoriaFormularioDTO fecha : dto.fechas()) {
                FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
                entity.setIdConvocatoria(convId);
                entity.setCodigo(fecha.codigo());
                entity.setFechaInicio(fecha.fechaInicio());
                entity.setFechaFin(fecha.fechaFin());
                entity.setOnceMeses(FechasConvocatoriaCalculator.calcularOnceMeses(fecha.fechaInicio(), fecha.fechaFin()));
                entity.setFechaCambio(new Date());
                entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
                fechasConvocatoriaRepository.save(entity);
            }
        }

        if (dto.convocatoriaTipoContratacion() != null) {
            for (ConvocatoriaTipoContratacionFormularioDTO cotcDto: dto.convocatoriaTipoContratacion()) {
                ConvocatoriaTipoContratacionEntity cotc =new ConvocatoriaTipoContratacionEntity();
                cotc.setIdConvocatoria(convId);
                cotc.setIdModalidadContratacion(cotcDto.idModalidadContratacion());
                cotc.setFechaCambio(new Date());
                cotc.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
                Long cotcId = convocatoriaTipoContratacionRepository.save(cotc).getId();

                boolean esPlanta =
                        isModalidadPlanta(cotcDto.idModalidadContratacion());

                if (!esPlanta && cotcDto.fechas() != null) {
                    for (FechaModalidadFormularioDTO fecha : cotcDto.fechas()) {
                        FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
                        entity.setIdConvocatoria(convId);
                        entity.setIdConvocatoriaTipoContratacion(cotcId);
                        entity.setFechaInicio(fecha.fechaInicio());
                        entity.setFechaFin(fecha.fechaFin());
                        entity.setSemanas(fecha.semanas());
                        entity.setVacaciones(fecha.vacaciones());
                        entity.setOnceMeses(
                                resolveOnceMeses(
                                        fecha.fechaInicio(),
                                        fecha.fechaFin()));
                        entity.setFechaCambio(new Date());
                        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
                        fechasConvocatoriaRepository.save(entity);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void update(Long id, ConvocatoriaFormularioDTO dto) {
        log.info("Actualizando convocatoria id={}", id);
        ConvocatoriaDatosInsertarDTO datos = dto.convocatoriaDatosInsertar();
        if (datos.id() == null || !datos.id().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,  "El id de la convocatoria no coincide con la ruta");
        }

        int updated = convocatoriaRepository.update(
                datos.nombre().trim(),
                datos.descripcion().trim(),
                datos.autoriza().id(),
                datos.periodo().id(),
                datos.nivelEducativo().id(),
                resolveIdRelacion(datos.idRelacion(), id),
                new Date(),
                id);
        if (updated == 0) {
            log.warn("Convocatoria no encontrada al actualizar. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND,  "Convocatoria no encontrada");
        }

        Set<Long> fechaIds = new HashSet<>();
        if (dto.fechas() != null) {
            for (FechasConvocatoriaFormularioDTO fecha : dto.fechas()) {
                String onceMeses = FechasConvocatoriaCalculator.calcularOnceMeses(fecha.fechaInicio(), fecha.fechaFin());
                if (fecha.id() != null) {
                    fechasConvocatoriaRepository.updateGeneral(
                            fecha.codigo(),
                            fecha.fechaInicio(),
                            fecha.fechaFin(),
                            onceMeses,
                            new Date(),
                            fecha.id());
                    fechaIds.add(fecha.id());
                } else {
                    FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
                    entity.setIdConvocatoria(id);
                    entity.setCodigo(fecha.codigo());
                    entity.setFechaInicio(fecha.fechaInicio());
                    entity.setFechaFin(fecha.fechaFin());
                    entity.setOnceMeses(onceMeses);
                    entity.setFechaCambio(new Date());
                    fechaIds.add(fechasConvocatoriaRepository.save(entity).getId());
                }
            }
        }
        convocatoriaRepository.findFechasGeneralesByConvocatoriaId(id).stream()
                .map(FechasConvocatoriaEntity::getId)
                .filter(fechaId -> !fechaIds.contains(fechaId))
                .forEach(fechasConvocatoriaRepository::deleteById);

        Set<Long> cotcIds = new HashSet<>();
        if (dto.convocatoriaTipoContratacion() != null) {
            for (ConvocatoriaTipoContratacionFormularioDTO cotcDto: dto.convocatoriaTipoContratacion()) {
                Long cotcId;
                if (cotcDto.id() != null) {
                    convocatoriaTipoContratacionRepository.update(
                            cotcDto.idModalidadContratacion(),
                            new Date(),
                            cotcDto.id());
                    cotcId = cotcDto.id();
                } else {
                    ConvocatoriaTipoContratacionEntity cotc =
                            new ConvocatoriaTipoContratacionEntity();
                    cotc.setIdConvocatoria(id);
                    cotc.setIdModalidadContratacion(cotcDto.idModalidadContratacion());
                    cotc.setFechaCambio(new Date());
                    cotcId = convocatoriaTipoContratacionRepository.save(cotc).getId();
                }
                cotcIds.add(cotcId);

                Set<Long> fechaModalidadIds = new HashSet<>();

                boolean esPlanta =
                        isModalidadPlanta(cotcDto.idModalidadContratacion());

                if (!esPlanta && cotcDto.fechas() != null) {
                    for (FechaModalidadFormularioDTO fecha : cotcDto.fechas()) {
                        String onceMeses =
                                resolveOnceMeses(
                                        fecha.fechaInicio(),
                                        fecha.fechaFin());

                        if (fecha.id() != null) {
                            fechasConvocatoriaRepository.updateModalidad(
                                    fecha.fechaInicio(),
                                    fecha.fechaFin(),
                                    fecha.semanas(),
                                    fecha.vacaciones(),
                                    onceMeses,
                                    new Date(),
                                    fecha.id());

                            fechaModalidadIds.add(fecha.id());
                        } else {
                            FechasConvocatoriaEntity entity =
                                    new FechasConvocatoriaEntity();

                            entity.setIdConvocatoria(id);
                            entity.setIdConvocatoriaTipoContratacion(cotcId);
                            entity.setFechaInicio(fecha.fechaInicio());
                            entity.setFechaFin(fecha.fechaFin());
                            entity.setSemanas(fecha.semanas());
                            entity.setVacaciones(fecha.vacaciones());
                            entity.setOnceMeses(onceMeses);
                            entity.setFechaCambio(new Date());

                            fechaModalidadIds.add(
                                    fechasConvocatoriaRepository
                                            .save(entity)
                                            .getId());
                        }
                    }
                }
                fechasConvocatoriaRepository
                        .findByIdConvocatoriaTipoContratacion(cotcId).stream()
                        .map(FechasConvocatoriaEntity::getId)
                        .filter(fechaId -> !fechaModalidadIds.contains(fechaId))
                        .forEach(fechasConvocatoriaRepository::deleteById);
            }
        }
        convocatoriaTipoContratacionRepository.findByConvocatoriaId(id).stream()
                .filter(cotc -> !cotcIds.contains(cotc.getId()))
                .forEach(cotc -> {
                    fechasConvocatoriaRepository
                            .deleteByIdConvocatoriaTipoContratacion(cotc.getId());
                    convocatoriaTipoContratacionRepository.deleteById(cotc.getId());
                });
        log.info("Convocatoria actualizada. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ConvocatoriaFormularioDTO findCallDetail(Long id) {
        log.debug("Consultando detalle de convocatoria id={}", id);
        ConvocatoriaEntity convocatoria = convocatoriaRepository.findConvocatoriaByIdNative(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada"));
        
        return new ConvocatoriaFormularioDTO(
                convocatoriaDatosInsertarMapper.toDto(
                        convocatoria,
                        personaGeneralRepository.findGeneralPersonById(
                                        convocatoria.getIdPersonaGeneral())
                                .orElse(null),
                        convocatoriaRepository.findPeriodoEntityByConvocatoriaId(id),
                        convocatoriaRepository.findNivelEntityByConvocatoriaId(id)),
                fechasConvocatoriaMapper.toFormularioDtoList(convocatoriaRepository.findFechasGeneralesByConvocatoriaId(id)),
                convocatoriaTipoContratacionFormularioMapper.toFormularioDtoList(convocatoriaTipoContratacionRepository.findByConvocatoriaId(id), convocatoriaRepository.findModalidadesFechasByConvocatoriaId(id)));
    }

    private String normalizeParam(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    @Override
    public void delete(Long id, ConvocatoriaFormularioDTO dto) {
        log.info("Eliminando convocatoria id={}", id);
        ConvocatoriaDatosInsertarDTO datos = dto.convocatoriaDatosInsertar();
        if (datos.id() == null || !datos.id().equals(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND,  "El id de la convocatoria no existe");
        }
        convocatoriaRepository.deleteByProcedure(id, "REGISTRO_WEB");
        for(FechasConvocatoriaFormularioDTO fecha : dto.fechas()) {
            fechasConvocatoriaRepository.deleteByProcedure(fecha.id(), "REGISTRO_WEB");
        }
        for(ConvocatoriaTipoContratacionFormularioDTO cotc : dto.convocatoriaTipoContratacion()) {
            for(FechaModalidadFormularioDTO fecha : cotc.fechas()) {
                fechasConvocatoriaRepository.deleteByProcedure(fecha.id(), "REGISTRO_WEB");
            }
            convocatoriaTipoContratacionRepository.deleteByProcedure(cotc.id(), "REGISTRO_WEB");
        }
        log.info("Convocatoria eliminada id={}", id);
    }

    @Override
    @Transactional
    public void bulkDelete(List<ConvocatoriaFormularioDTO> listaConvocatorias) {
        log.info("Eliminación masiva de convocatorias. total={}",
                listaConvocatorias != null ? listaConvocatorias.size() : 0);
        for(ConvocatoriaFormularioDTO dto : listaConvocatorias) {
            delete(dto.convocatoriaDatosInsertar().id(), dto);
        }
    }

    @Override
    public List<ConvocatoriaDTO> findActivePreloadCalls(Long idPeriodoUniversidad) {
        log.debug("findActivePreloadCalls ===> Consultando convocatorias activas. idPeriodoUniversidad={}",
                idPeriodoUniversidad);

        if (idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }

        List<ConvocatoriaDTO> result = convocatoriaRepository
                .findActivePreloadCalls(idPeriodoUniversidad)
                .stream()
                .map(this::toPreloadCallListDto)
                .collect(Collectors.toList());

        log.info("findActivePreloadCalls ===> Convocatorias activas consultadas. periodo={}, total={}",
                idPeriodoUniversidad, result.size());

        return result;
    }

    @Override
    public List<ConvocatoriaDTO> findAssignableActivePreloadCalls(Long idPeriodoUniversidad) {
        log.debug(
                "findAssignableActivePreloadCalls ===> Consultando convocatorias asignables. idPeriodoUniversidad={}",
                idPeriodoUniversidad);

        if (idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio");
        }

        convocatoriaEstadoService.syncEstadosConvocatoriasConRestricciones();

        List<ConvocatoriaDTO> result = convocatoriaRepository
                .findAssignableActivePreloadCalls(idPeriodoUniversidad)
                .stream()
                .map(this::toPreloadCallListDto)
                .collect(Collectors.toList());

        log.info(
                "findAssignableActivePreloadCalls ===> Convocatorias asignables consultadas. periodo={}, total={}",
                idPeriodoUniversidad, result.size());

        return result;
    }

    private ConvocatoriaDTO toPreloadCallListDto(ConvocatoriaEntity convocatoria) {
        return convocatoriaMapper.toListDto(
                convocatoria,
                convocatoriaRepository.findPeriodoEntityByConvocatoriaId(
                        convocatoria.getId()),
                convocatoriaRepository.findNivelEntityByConvocatoriaId(
                        convocatoria.getId()),
                convocatoriaRepository.findFechaCnvByConvocatoriaId(
                        convocatoria.getId()),
                personaGeneralRepository.findGeneralPersonById(
                                convocatoria.getIdPersonaGeneral())
                        .orElse(null)
        );
    }

    private String resolveOnceMeses(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return "0";
        }

        return FechasConvocatoriaCalculator.calcularOnceMeses(fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public void updateRelation(Long idConvocatoria, Long idRelacion) {
        log.info("updateRelation ===> Relacionando convocatoria. idConvocatoria={}, idRelacion={}", idConvocatoria, idRelacion);

        if (!convocatoriaRepository.existsById(idConvocatoria)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada");
        }

        PeriodoUniversidadEntity periodoActual = convocatoriaRepository.findPeriodoEntityByConvocatoriaId(idConvocatoria);
        if (periodoActual == null || !"2".equals(periodoActual.getPeriodo())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Solo se puede relacionar una convocatoria de segundo periodo");
        }

        Long idRelacionResuelto = resolveIdRelacion(idRelacion, idConvocatoria);
        int updated = convocatoriaRepository.updateIdRelacion(
                idRelacionResuelto,
                new Date(),
                idConvocatoria);
        if (updated == 0) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada");
        }

        log.info("updateRelation ===> Relación actualizada. idConvocatoria={}, idRelacion={}", idConvocatoria, idRelacionResuelto);
    }

    private Long resolveIdRelacion(Long idRelacion, Long idConvocatoriaActual) {
        if (idRelacion == null) {
            return null;
        }
        if (idConvocatoriaActual != null && idRelacion.equals(idConvocatoriaActual)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Una convocatoria no puede relacionarse consigo misma");
        }
        if (!convocatoriaRepository.existsById(idRelacion)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la convocatoria relacionada con id " + idRelacion);
        }
        PeriodoUniversidadEntity periodoRelacionado =
                convocatoriaRepository.findPeriodoEntityByConvocatoriaId(idRelacion);
        if (periodoRelacionado == null
                || !"1".equals(periodoRelacionado.getPeriodo())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La convocatoria relacionada debe pertenecer al primer periodo");
        }
        return idRelacion;
    }

    private boolean isModalidadPlanta(Long idModalidadContratacion) {
        if (idModalidadContratacion == null) {
            return false;
        }

        return modalidadContratacionRepository
                .findById(idModalidadContratacion)
                .map(modalidad -> {
                    String sigla = modalidad.getSigla() == null
                            ? ""
                            : modalidad.getSigla().trim();

                    String nombre = modalidad.getNombre() == null
                            ? ""
                            : modalidad.getNombre().trim();

                    return "PLANTA".equalsIgnoreCase(sigla)
                            || "PLANTA".equalsIgnoreCase(nombre);
                })
                .orElse(false);
    }


}


 /* 04/06/2026 @:Sebastian Jaimes*/
