package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaDatosInsertarMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.FechasConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ModalidadContratacionInsertarMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDatosInsertarDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionInsertarDTO;
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
    private final ModalidadContratacionInsertarMapper modalidadContratacionInsertarMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> findCallListWithDates() {
        return convocatoriaRepository.findCallListWithDates().stream()
                .map(convocatoria -> convocatoriaMapper.toListDto(
                        convocatoria,
                        convocatoriaRepository.findPeriodoEntityByConvocatoriaId(
                                convocatoria.getId()),
                        convocatoriaRepository.findNivelEntityByConvocatoriaId(
                                convocatoria.getId()),
                        convocatoriaRepository.findFechaCnvByConvocatoriaId(
                                convocatoria.getId()),
                        personaGeneralRepository.findByIdWithNatural(
                                        convocatoria.getIdPersonaGeneral())
                                .orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonaAutorizaConvocatoriaDTO> searchGeneralPerson(String nombre, String documento) {
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            return Collections.emptyList();
        }
        return personaAutorizaConvocatoriaMapper.toDtoList(
                personaGeneralRepository.searchGeneralPerson(
                        nombreParam,
                        documentoParam));
    }

    @Override
    @Transactional
    public void save(ConvocatoriaFormularioDTO dto) {
        
        ConvocatoriaEntity convocatoria = buildConvocatoria(dto.convocatoriaDatosInsertar());
        convocatoriaRepository.save(convocatoria);
        Long convId = convocatoria.getId();
        Map<Long, Long> convocatoriaTipoContratacion = saveModalidades(dto.modalidades(), convId);
        saveFechasGenerales(dto.fechas(), convId);
        saveFechasModalidad(dto.modalidades(), convId, convocatoriaTipoContratacion);
    }

    @Override
    @Transactional(readOnly = true)
    public ConvocatoriaFormularioDTO findCallDetail(Long id) {
        ConvocatoriaEntity convocatoria = convocatoriaRepository.findConvocatoriaByIdNative(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Convocatoria no encontrada"));
        return new ConvocatoriaFormularioDTO(
                convocatoriaDatosInsertarMapper.toDto(
                        convocatoria,
                        personaGeneralRepository.findByIdWithNatural(
                                        convocatoria.getIdPersonaGeneral())
                                .orElseThrow(() -> new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Persona autorizadora no encontrada")),
                        convocatoriaRepository.findPeriodoEntityByConvocatoriaId(id),
                        convocatoriaRepository.findNivelEntityByConvocatoriaId(id)),
                fechasConvocatoriaMapper.toFormularioDtoList(
                        convocatoriaRepository.findFechasGeneralesByConvocatoriaId(id)),
                modalidadContratacionInsertarMapper.toDtoList(
                        convocatoriaRepository.findModalidadesByConvocatoriaId(id)));
    }

    private ConvocatoriaEntity buildConvocatoria(ConvocatoriaDatosInsertarDTO datos) {
        ConvocatoriaEntity entity = new ConvocatoriaEntity();
        entity.setNombre(datos.nombre().trim());
        entity.setDescripcion(datos.descripcion().trim());
        entity.setIdPersonaGeneral(datos.autoriza().id());
        entity.setIdPeriodoUniversidad(datos.periodo().id());
        entity.setIdNivelEducativo(datos.nivelEducativo().id());
        entity.setEstado("1");
        entity.setFechaCambio(new Date());
        return entity;
    }

    private Map<Long, Long> saveModalidades(
            List<ModalidadContratacionInsertarDTO> modalidades,
            Long convId) {
        Map<Long, Long> cotcByModalidad = new HashMap<>();
        for (ModalidadContratacionInsertarDTO modalidad : modalidades) {
            Long modalidadId = modalidad.id();
            if (modalidadId == null || cotcByModalidad.containsKey(modalidadId)) {
                continue;
            }
            ConvocatoriaTipoContratacionEntity entity =
                    new ConvocatoriaTipoContratacionEntity();
            entity.setIdConvocatoria(convId);
            entity.setIdModalidadContratacion(modalidadId);
            entity.setFechaCambio(new Date());
            convocatoriaTipoContratacionRepository.save(entity);
            cotcByModalidad.put(modalidadId, entity.getId());
        }
        return cotcByModalidad;
    }

    private void saveFechasGenerales(List<FechasConvocatoriaFormularioDTO> fechas, Long convId) {
        for (FechasConvocatoriaFormularioDTO fecha : fechas) {
            FechasConvocatoriaEntity entity = buildFechaBase(
                    convId,
                    null,
                    fecha.fechaInicio(),
                    fecha.fechaFin(),
                    null);
            entity.setCodigo(fecha.codigo());
            fechasConvocatoriaRepository.save(entity);
        }
    }

    private void saveFechasModalidad(List<ModalidadContratacionInsertarDTO> modalidades, Long convId, Map<Long, Long> convocatoriaTipoContratacion) {
        for (ModalidadContratacionInsertarDTO modalidad : modalidades) {
            Long cotcId = convocatoriaTipoContratacion.get(modalidad.id());
            if (cotcId == null) {
                continue;
            }
            FechasConvocatoriaEntity entity = buildFechaBase(
                    convId,
                    cotcId,
                    modalidad.fechaInicio(),
                    modalidad.fechaFin(),
                    modalidad.semanas()
                );
            entity.setVacaciones(String.valueOf(modalidad.vacaciones()));
            fechasConvocatoriaRepository.save(entity);
        }
    }

    private FechasConvocatoriaEntity buildFechaBase(
            Long convId,
            Long cotcId,
            Date fechaInicio,
            Date fechaFin,
            String semanas
        ) {
        FechasConvocatoriaEntity entity = new FechasConvocatoriaEntity();
        entity.setIdConvocatoria(convId);
        entity.setIdConvocatoriaTipoContratacion(cotcId);
        entity.setFechaInicio(fechaInicio);
        entity.setFechaFin(fechaFin);
        entity.setSemanas(semanas);
        entity.setOnceMeses(FechasConvocatoriaCalculator.calcularOnceMeses(
                fechaInicio,
                fechaFin));
        entity.setFechaCambio(new Date());
        return entity;
    }

    private String normalizeParam(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
