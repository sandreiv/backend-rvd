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
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaTipoContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FechasConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConvocatoriaPrecargaServiceImpl implements ConvocatoriaPrecargaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final ConvocatoriaMapper convocatoriaMapper;
    private final ConvocatoriaTipoContratacionRepository convocatoriaTipoContratacionRepository;
    private final FechasConvocatoriaRepository fechasConvocatoriaRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;
    private final ConvocatoriaDatosInsertarMapper convocatoriaDatosInsertarMapper;
    private final FechasConvocatoriaMapper fechasConvocatoriaMapper;
    private final ConvocatoriaTipoContratacionFormularioMapper convocatoriaTipoContratacionFormularioMapper;

    @Override
    @Transactional
    public List<ConvocatoriaDTO> findCallListWithDates(Long idPeriodoUniversidad) {
        return convocatoriaRepository.findCallListWithDates(idPeriodoUniversidad)
                .stream()
                .map(this::toListDtoAndSyncEstado)
                .collect(Collectors.toList());
    }

    private ConvocatoriaDTO toListDtoAndSyncEstado(ConvocatoriaEntity convocatoria) {
        FechasConvocatoriaEntity fechaCnv = convocatoriaRepository
                .findFechaCnvByConvocatoriaId(convocatoria.getId());
        syncEstadoVencido(convocatoria, fechaCnv);
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

    private void syncEstadoVencido(
            ConvocatoriaEntity convocatoria,
            FechasConvocatoriaEntity fechaCnv) {
        if (fechaCnv == null
                || !FechasConvocatoriaCalculator.isVencida(fechaCnv.getFechaFin())) {
            return;
        }
        if ("0".equals(convocatoria.getEstado())) {
            return;
        }
        Date ahora = new Date();
        convocatoriaRepository.updateEstado("0", ahora, convocatoria.getId());
        convocatoria.setEstado("0");
        convocatoria.setFechaCambio(ahora);
    }

    @Override
    public List<PersonaAutorizaConvocatoriaDTO> searchGeneralPerson(String nombre, String documento) {
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            return Collections.emptyList();
        }
        return personaAutorizaConvocatoriaMapper.toDtoList(personaGeneralRepository.searchGeneralPerson(nombreParam, documentoParam));
    }

    @Override
    @Transactional
    public void save(ConvocatoriaFormularioDTO dto) {
        ConvocatoriaDatosInsertarDTO datos = dto.convocatoriaDatosInsertar();
        if (datos.id() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no debe incluir id al crear");
        }

        ConvocatoriaEntity convocatoria = new ConvocatoriaEntity();
        convocatoria.setNombre(datos.nombre().trim());
        convocatoria.setDescripcion(datos.descripcion().trim());
        convocatoria.setIdPersonaGeneral(datos.autoriza().id());
        convocatoria.setIdPeriodoUniversidad(datos.periodo().id());
        convocatoria.setIdNivelEducativo(datos.nivelEducativo().id());
        convocatoria.setEstado("1");
        convocatoria.setFechaCambio(new Date());
        convocatoria.setRegistradoPor("REGISTRO_WEB");
        Long convId = convocatoriaRepository.save(convocatoria).getId();

        if (dto.fechas() != null) {
            for (FechasConvocatoriaFormularioDTO fecha : dto.fechas()) {
                FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
                entity.setIdConvocatoria(convId);
                entity.setCodigo(fecha.codigo());
                entity.setFechaInicio(fecha.fechaInicio());
                entity.setFechaFin(fecha.fechaFin());
                entity.setOnceMeses(FechasConvocatoriaCalculator.calcularOnceMeses(fecha.fechaInicio(), fecha.fechaFin()));
                entity.setFechaCambio(new Date());
                entity.setRegistradoPor("REGISTRO_WEB");
                fechasConvocatoriaRepository.save(entity);
            }
        }

        if (dto.convocatoriaTipoContratacion() != null) {
            for (ConvocatoriaTipoContratacionFormularioDTO cotcDto: dto.convocatoriaTipoContratacion()) {
                ConvocatoriaTipoContratacionEntity cotc =new ConvocatoriaTipoContratacionEntity();
                cotc.setIdConvocatoria(convId);
                cotc.setIdModalidadContratacion(cotcDto.idModalidadContratacion());
                cotc.setFechaCambio(new Date());
                cotc.setRegistradoPor("REGISTRO_WEB");
                Long cotcId = convocatoriaTipoContratacionRepository.save(cotc).getId();

                if (cotcDto.fechas() != null) {
                    for (FechaModalidadFormularioDTO fecha : cotcDto.fechas()) {
                        FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
                        entity.setIdConvocatoria(convId);
                        entity.setIdConvocatoriaTipoContratacion(cotcId);
                        entity.setFechaInicio(fecha.fechaInicio());
                        entity.setFechaFin(fecha.fechaFin());
                        entity.setSemanas(fecha.semanas());
                        entity.setVacaciones(fecha.vacaciones());
                        entity.setOnceMeses(
                                FechasConvocatoriaCalculator.calcularOnceMeses(
                                        fecha.fechaInicio(),
                                        fecha.fechaFin()));
                        entity.setFechaCambio(new Date());
                        entity.setRegistradoPor("REGISTRO_WEB");
                        fechasConvocatoriaRepository.save(entity);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void update(Long id, ConvocatoriaFormularioDTO dto) {
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
                new Date(),
                id);
        if (updated == 0) {
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
                if (cotcDto.fechas() != null) {
                    for (FechaModalidadFormularioDTO fecha : cotcDto.fechas()) {
                        String onceMeses =
                                FechasConvocatoriaCalculator.calcularOnceMeses(
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
                                    fechasConvocatoriaRepository.save(entity).getId());
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
    }

    @Override
    @Transactional(readOnly = true)
    public ConvocatoriaFormularioDTO findCallDetail(Long id) {
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
    }

    @Override
    @Transactional
    public void bulkDelete(List<ConvocatoriaFormularioDTO> listaConvocatorias) {
        for(ConvocatoriaFormularioDTO dto : listaConvocatorias) {
            delete(dto.convocatoriaDatosInsertar().id(), dto);
        }
    }

    @Override
    public List<ConvocatoriaDTO> findActivePreloadCalls() {
        return convocatoriaRepository.findActivePreloadCalls().stream()
                .map(convocatoria -> convocatoriaMapper.toListDto(
                        convocatoria,
                        convocatoriaRepository.findPeriodoEntityByConvocatoriaId(
                                convocatoria.getId()),
                        convocatoriaRepository.findNivelEntityByConvocatoriaId(
                                convocatoria.getId()),
                        convocatoriaRepository.findFechaCnvByConvocatoriaId(
                                convocatoria.getId()),
                        personaGeneralRepository.findGeneralPersonById(
                                        convocatoria.getIdPersonaGeneral())
                                .orElse(null)))
                .collect(Collectors.toList());
    }
}

 /* 04/06/2026 @:Sebastian Jaimes*/
