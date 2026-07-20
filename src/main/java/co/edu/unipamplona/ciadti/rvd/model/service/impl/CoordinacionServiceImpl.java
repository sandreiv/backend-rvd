package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.CargaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CategoriaCatedraticoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DetalleCargaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocenteCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocentePlantaCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocentePreasignacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.FechasConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.GrupoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.MateriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProgramaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RelacionCargaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RestriccionPorCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadCriterioMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TotalPreasignacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.UnidadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalHorasPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaModalidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionPorCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsociacionCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CategoriaCatedraticoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CategoriaModalidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocentesPlantaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadoCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FechasConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.GrupoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MateriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionCargaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.UnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinacionServiceImpl implements CoordinacionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";
    private static final String ESTADO_CARGA_INICIAL = "REGISTRADO"; // POR DEFINIR
    private static final int ESCALA_MONETARIA = 2;

    private final CoordinacionRepository coordinacionRepository;
    private final CargaRepository cargaRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final EstadoCargaRepository estadoCargaRepository;
    private final DocentesPlantaCoordinacionRepository docentesPlantaCoordinacionRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final FechasConvocatoriaRepository fechasConvocatoriaRepository;
    private final PuntosVigenciaRepository puntosVigenciaRepository;
    private final PuntosCategoriaRepository puntosCategoriaRepository;
    private final CategoriaCatedraticoRepository categoriaCatedraticoRepository;
    private final CategoriaModalidadRepository categoriaModalidadRepository;
    private final EscalafonRepository escalafonRepository;
    private final CoordinacionMapper coordinacionMapper;
    private final DocentePlantaCoordinacionMapper docentePlantaCoordinacionMapper;
    private final DocentePreasignacionMapper docentePreasignacionMapper;
    private final FechasConvocatoriaMapper fechasConvocatoriaMapper;
    private final CategoriaCatedraticoMapper categoriaCatedraticoMapper;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final CargaDocenteMapper cargaDocenteMapper;
    private final DocenteCoordinacionMapper docenteCoordinacionMapper;
    private final UnidadRepository unidadRepository;
    private final ProgramaRepository programaRepository;
    private final UnidadMapper unidadMapper;
    private final ProgramaMapper programaMapper;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final TipoActividadCriterioMapper tipoActividadCriterioMapper;
    private final TipoActividadMapper tipoActividadMapper;
    private final MateriaRepository materiaRepository;
    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final MateriaMapper materiaMapper;
    private final AsociacionCoordinacionRepository asociacionCoordinacionRepository;
    private final GrupoRepository grupoRepository;
    private final GrupoMapper grupoMapper;
    private final PersonaProyectoRepository personaProyectoRepository;
    private final ProyectoMapper proyectoMapper;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final RelacionCargaProyectoRepository relacionCargaProyectoRepository;
    private final DetalleCargaDocenteMapper detalleCargaDocenteMapper;
    private final RelacionCargaProyectoMapper relacionCargaProyectoMapper;
    private final RestriccionPorCoordinacionRepository restriccionPorCoordinacionRepository;
    private final RestriccionPorCoordinacionMapper restriccionPorCoordinacionMapper;
    private final TotalPreasignacionMapper totalPreasignacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria, Long idUsuario) {
        log.debug("Listando coordinaciones. idConvocatoria={}, idUsuario={}",
                idConvocatoria, idUsuario);
        List<CoordinacionListadoProjection> projections = idConvocatoria == null
                ? coordinacionRepository.findWithoutCarga(idUsuario)
                : coordinacionRepository.findByConvocatoriaWithCarga(
                        idConvocatoria, idUsuario);
        List<CoordinacionDTO> result = coordinacionMapper.toDtoList(projections);
        log.info("Coordinaciones listadas. idConvocatoria={}, total={}",
                idConvocatoria, result.size());
        return result;
    }

    @Override
    @Transactional
    public void savePreload(RelacionConvocatoriaCoordinacionDTO dto) {
        log.info("Guardando preasignación. idCoordinacion={}, idConvocatoria={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idConvocatoria() : null);
        validateSavePreload(dto);

        CargaEntity carga = cargaRepository
                .findFirstByIdCoordinacionOrderByIdDesc(dto.idCoordinacion())
                .orElseGet(CargaEntity::new);

        validatePreloadCallChangeAllowed(carga, dto.idConvocatoria());

        carga.setIdCoordinacion(dto.idCoordinacion());
        carga.setIdConvocatoria(dto.idConvocatoria());

        if (carga.getIdEstadoCarga() == null) {
            carga.setIdEstadoCarga(resolveEstadoCargaInicialId());
        }

        carga.setRegistradoPor(REGISTRADO_POR);
        carga.setFechaCambio(new Date());

        cargaRepository.save(carga);
        log.info("Preasignación guardada. idCarga={}, idCoordinacion={}, idConvocatoria={}",
                carga.getId(), dto.idCoordinacion(), dto.idConvocatoria());
    }

    private void validatePreloadCallChangeAllowed(
            CargaEntity carga,
            Long newPreloadCallId
    ) {
        if (carga.getId() == null) {
            return;
        }

        if (Objects.equals(carga.getIdConvocatoria(), newPreloadCallId)) {
            return;
        }

        if (cargaDocenteRepository.existsByIdCarga(carga.getId())) {
            log.warn("Cambio de convocatoria bloqueado. idCarga={}, nuevaConvocatoria={}",
                    carga.getId(), newPreloadCallId);
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No se puede cambiar la convocatoria porque ya existen docentes cargados en la preasignación"
            );
        }
    }


    private Long resolveEstadoCargaInicialId() {
        return estadoCargaRepository.findByNombre(ESTADO_CARGA_INICIAL)
                .map(estado -> estado.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"No existe el estado de carga inicial: " + ESTADO_CARGA_INICIAL));
    }

    private void validateSavePreload(RelacionConvocatoriaCoordinacionDTO dto) {
        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de la preasignación es obligatoria"
            );
        }

        if (dto.idCoordinacion() == null || dto.idConvocatoria() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La coordinación y la convocatoria son obligatorias"
            );
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Coordinación no encontrada");
        }

        if (!convocatoriaRepository.existsById(dto.idConvocatoria())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocentePlantaCoordinacionDTO> listCareerProfessors(Long idCoordinacion) {
        log.debug("Listando docentes de planta. idCoordinacion={}", idCoordinacion);
        List<DocentePlantaCoordinacionDTO> result = docentePlantaCoordinacionMapper
                .toDtoList(docentesPlantaCoordinacionRepository
                        .findByIdCoordinacion(idCoordinacion));
        log.info("Docentes de planta listados. idCoordinacion={}, total={}",
                idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocentePreasignacionDTO> searchProfessor(String nombre, String documento, Long idModalidadContratacion) {
        log.debug("Buscando docente. nombre={}, documento={}, idModalidad={}",
                nombre, documento, idModalidadContratacion);
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            log.debug("Búsqueda de docente sin criterios. Se retorna lista vacía");
            return Collections.emptyList();
        }
        List<DocentePreasignacionDTO> result = docentePreasignacionMapper.toDtoList(
                personaGeneralRepository.searchProfessorsForPreassignment(
                        nombreParam, documentoParam, idModalidadContratacion));
        log.info("Docentes encontrados. total={}", result.size());
        return result;
    }

    private String normalizeParam(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FechaModalidadFormularioDTO> getWorkDate(Long coorId, Long mocoId) {
        log.debug("Consultando fechas de trabajo. idCoordinacion={}, idModalidad={}",
                coorId, mocoId);
        List<FechaModalidadFormularioDTO> result = fechasConvocatoriaMapper
                .toModalidadDtoList(fechasConvocatoriaRepository
                        .findByCoordinationAndModality(coorId, mocoId));
        log.info("Fechas de trabajo consultadas. idCoordinacion={}, total={}",
                coorId, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ValorPuntosPrecargaDTO getValuePointsPreload(Long anio, Long idCategoriaCatedratico, Long idPersonaGeneral) {
        log.debug("Calculando valor puntos precarga. anio={}, idCategoria={}, idPersona={}",
                anio, idCategoriaCatedratico, idPersonaGeneral);

        PuntosVigenciaEntity vigencia = puntosVigenciaRepository.findByAnio(anio)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"No existe valor de puntos para la vigencia " + anio));

        PuntosCategoriaEntity puntosCategoria = puntosCategoriaRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico);

        BigDecimal valorPunto = parseValor(vigencia.getValorPunto(), "valor del punto de la vigencia");
        BigDecimal puntosCategoriaValor = parseValor(puntosCategoria.getPuntos(), "puntos de la categoria");
        BigDecimal valorHora = valorPunto.multiply(puntosCategoriaValor).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal valorPuntoEscalado = valorPunto.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        if (idPersonaGeneral == null) {
            log.info("Valor puntos precarga calculado sin persona. anio={}, valorHora={}",
                    anio, valorHora);
            return new ValorPuntosPrecargaDTO(valorHora, valorPuntoEscalado, null, null);
        }

        EscalafonEntity escalafon = escalafonRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico, idPersonaGeneral);
        if (escalafon == null) {
            log.warn("Escalafón no encontrado. idPersona={}, idCategoria={}",
                    idPersonaGeneral, idCategoriaCatedratico);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe escalafon para la persona " + idPersonaGeneral + " y la categoria " + idCategoriaCatedratico);
        }

        BigDecimal puntosDocente = parseValor(escalafon.getPuntos(), "puntos del docente (escalafon)");
        BigDecimal asignacionSalarial = puntosDocente.multiply(valorPunto).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        log.info("Valor puntos precarga calculado. anio={}, idPersona={}, valorHora={}",
                anio, idPersonaGeneral, valorHora);
        return new ValorPuntosPrecargaDTO(
                valorHora,
                valorPuntoEscalado,
                puntosDocente.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP),
                asignacionSalarial);
    }

    private BigDecimal parseValor(String valor, String campo) {
        if (!StringUtils.hasText(valor)) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "El " + campo + " no esta definido");
        }
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "El " + campo + " no es un valor numerico valido: " + valor);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaCatedraticoDTO> listProfessorCategory(Long idModalidadContratacion) {
        log.debug("Listando categorías catedrático. idModalidad={}",
                idModalidadContratacion);
        List<CategoriaCatedraticoDTO> result = categoriaCatedraticoMapper.toDtoList(
                categoriaCatedraticoRepository.findAllCategories(idModalidadContratacion));
        log.info("Categorías catedrático listadas. idModalidad={}, total={}",
                idModalidadContratacion, result.size());
        return result;
    }

    @Override
    @Transactional
    public void addProfessor(CargaDocenteFormularioDTO dto) {
        log.info("Agregando docente. idPersona={}, idCarga={}, idModalidad={}",
                dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion());
        if (dto.idPersonaGeneral() != null
                && cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacionAndIdFechasConvocatoria(
                        dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion(), dto.fechasConvocatoria().id())) {
            log.warn("Docente duplicado en modalidad. idPersona={}, idCarga={}",
                    dto.idPersonaGeneral(), dto.idCarga());
            throw new ApiException(HttpStatus.CONFLICT, "El docente ya se encuentra registrado en esta modalidad de contratacion");
        }
        CargaDocenteEntity entity = cargaDocenteMapper.toEntity(dto);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        entity.setEstado("0");
        entity.setVigente("1");
        cargaDocenteRepository.save(entity);
        log.info("Docente agregado. idCargaDocente={}", entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteCoordinacionDTO> listProfessors(Long idCoordinacion, Long idModalidadContratacion) {
        log.debug("Listando docentes. idCoordinacion={}, idModalidad={}",
                idCoordinacion, idModalidadContratacion);
        List<DocenteCargaCoordinacionProjection> projections;
        if (isModalidadPlanta(idModalidadContratacion)) {
            projections = cargaDocenteRepository.findPlantProfessorsByCoordinationAndModality(idCoordinacion, idModalidadContratacion);
        } else {
            projections = cargaDocenteRepository.findProfessorsByCoordinationAndModality(idCoordinacion, idModalidadContratacion);
        }
        List<DocenteCoordinacionDTO> result = docenteCoordinacionMapper.toDtoList(projections);
        log.info("Docentes listados. idCoordinacion={}, total={}",
                idCoordinacion, result.size());
        return result;
    }

    private boolean isModalidadPlanta(Long idModalidadContratacion) {
        return modalidadContratacionRepository.findById(idModalidadContratacion)
                .map(modalidad -> modalidad.getNombre() != null
                        && "planta".equalsIgnoreCase(modalidad.getNombre().trim()))
                .orElse(false);
    }

    @Override
    @Transactional
    public void updateProfessor(Long idCargaDocente, CargaDocenteFormularioDTO dto) {
        log.info("Actualizando docente. idCargaDocente={}", idCargaDocente);
        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));
        
        cargaDocenteMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        cargaDocenteRepository.save(entity);
        log.info("Docente actualizado. idCargaDocente={}", idCargaDocente);
    }

    @Override
    @Transactional
    public void deleteProfessor(Long idCargaDocente) {
        log.info("Eliminando docente. idCargaDocente={}", idCargaDocente);
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            log.warn("Carga docente no encontrada al eliminar. id={}", idCargaDocente);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }
        cargaDocenteRepository.deleteByProcedure(idCargaDocente, REGISTRADO_POR);
        log.info("Docente eliminado. idCargaDocente={}", idCargaDocente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadDTO> listRegionalUnits(Long idCoordinacion) {
        log.debug("Listando unidades regionales. idCoordinacion={}", idCoordinacion);
        List<UnidadDTO> result = unidadMapper.toDtoList(
                unidadRepository.findRegionalUnits(idCoordinacion));
        log.info("Unidades regionales listadas. idCoordinacion={}, total={}",
                idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaDTO> listProgramsByRegionalUnit(Long idCoordinacion, Long idUnidadRegional, Long idNivelEducativo) {
        log.debug("Listando programas. idCoordinacion={}, idUnidad={}, idNivel={}",
                idCoordinacion, idUnidadRegional, idNivelEducativo);
        List<ProgramaDTO> result;
        if (asociacionCoordinacionRepository.existsProgramasByCoordinacion(idCoordinacion)) {
            result = programaMapper.toDtoList(
                    programaRepository.findByCoordinacionUnidadRegionalAndNivelEducativo(
                            idCoordinacion, idUnidadRegional, idNivelEducativo));
        } else {
            result = programaMapper.toDtoList(
                    programaRepository.findByUnidadRegionalAndNivelEducativo(
                            idUnidadRegional, idNivelEducativo));
        }
        log.info("Programas listados. idCoordinacion={}, total={}",
                idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadCriterioDTO> listCriteria(Long idTipoActividad) {
        log.debug("Listando criterios. idTipoActividad={}", idTipoActividad);
        List<TipoActividadCriterioDTO> result = tipoActividadCriterioMapper.toDtoList(
                tipoActividadesRepository.findCriteriaByParentId(idTipoActividad));
        log.info("Criterios listados. idTipoActividad={}, total={}",
                idTipoActividad, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDTO> listActivityTypes() {
        log.debug("Listando tipos de actividad");
        List<TipoActividadDTO> result = tipoActividadMapper.toDtoList(
                tipoActividadesRepository.findParentActivityTypes());
        log.info("Tipos de actividad listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listSubjects(Long idPrograma, Long idCoordinacion) {
        log.debug("Listando materias. idPrograma={}, idCoordinacion={}",
                idPrograma, idCoordinacion);
        List<MateriaListadoProjection> materias;

        if (asociacionCoordinacionRepository.existsProgramasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findPensumExcluyendoTransversales(idPrograma);
        } else if (asociacionCoordinacionRepository.existsMateriasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findTransversalesByCoordinacionAndPrograma(
                    idCoordinacion, idPrograma);
        } else {
            materias = materiaRepository.findByPrograma(idPrograma);
        }

        List<MateriaDTO> result = mapMateriasConGrupo(materias);
        log.info("Materias listadas. idPrograma={}, total={}",
                idPrograma, result.size());
        return result;
    }

    private List<MateriaDTO> mapMateriasConGrupo(List<MateriaListadoProjection> projections) {
        if (projections.isEmpty()) {
            return List.of();
        }
        List<String> codigos = projections.stream()
                .map(MateriaListadoProjection::getCodigoMateria)
                .distinct()
                .toList();
        Set<String> codigosConGrupo = new HashSet<>(grupoRepository.findCodigosMateriaConGrupo(codigos));
        return projections.stream()
                .map(p -> materiaMapper.toDto(
                        p, codigosConGrupo.contains(p.getCodigoMateria())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoDTO> listSubjectGroup(String codigoMateria, Long idPeriodoUniversidad) {
        log.debug("Listando grupos. codigoMateria={}, idPeriodo={}",
                codigoMateria, idPeriodoUniversidad);
        List<GrupoDTO> result = grupoMapper.toDtoList(
                grupoRepository.findByCodigoMateriaAndIdPeriodoUniversidad(
                        codigoMateria, idPeriodoUniversidad));
        log.info("Grupos listados. codigoMateria={}, total={}",
                codigoMateria, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> listProjectsProfessor(Long idPersonaGeneral) {
        log.debug("Listando proyectos del docente. idPersona={}", idPersonaGeneral);
        List<ProyectoDTO> result = proyectoMapper.toDtoList(
                personaProyectoRepository.findProyectosByIdPersonaGeneral(idPersonaGeneral));
        log.info("Proyectos del docente listados. idPersona={}, total={}",
                idPersonaGeneral, result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto) {
        log.info("Guardando detalle precarga docente. idCargaDocente={}, detalles={}",
                dto.idCargaDocente(),
                dto.detalles() != null ? dto.detalles().size() : 0);
        validateSaveDetailProfessorPreload(dto);
        
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            DetalleCargaDocenteEntity entity = detalleCargaDocenteMapper.toEntity(dto.idCargaDocente(), detalle);
            entity.setRegistradoPor(REGISTRADO_POR);
            entity.setFechaCambio(new Date());
            DetalleCargaDocenteEntity saved = detalleCargaDocenteRepository.save(entity);
            saveRelacionesCargaProyecto(saved.getId(), detalle.relacionCargaProyecto());
        }
        log.info("Detalle precarga docente guardado. idCargaDocente={}",
                dto.idCargaDocente());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleCargaDocenteDTO> listDetailProfessorPreload(Long idCargaDocente) {
        log.debug("Listando detalle precarga docente. idCargaDocente={}", idCargaDocente);
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            log.warn("Carga docente no encontrada. id={}", idCargaDocente);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }

        List<DetalleCargaDocenteDTO> result = detalleCargaDocenteMapper.toDtoList(
                detalleCargaDocenteRepository.findByIdCargaDocente(idCargaDocente),
                proyectoMapper,
                tipoActividadMapper,
                tipoActividadesRepository);
        log.info("Detalle precarga docente listado. idCargaDocente={}, total={}",
                idCargaDocente, result.size());
        return result;
    }

    @Override
    @Transactional
    public void updateDetailProfessorPreload(DetalleCargaDocenteDTO dto) {
        log.info("Actualizando detalle precarga. idDetalle={}, idCargaDocente={}",
                dto.idDetalleCargaDocente(), dto.idCargaDocente());
        validateUpdateDetailProfessorPreload(dto);

        Long idDetalleCargaDocente = dto.idDetalleCargaDocente();
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        DetalleCargaDocenteEntity detallePersistido = detalleCargaDocenteRepository
                .findById(idDetalleCargaDocente)
                .orElseThrow();
        DetalleCargaDocenteEntity entity = detalleCargaDocenteMapper.toEntityFromDto(dto);
        entity.setIdTipoActividad(detalleCargaDocenteMapper
                .resolveTipoActividadFromActividad(
                        actividad,
                        detallePersistido.getIdTipoActividad()));
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        detalleCargaDocenteRepository.save(entity);

        relacionCargaProyectoRepository.deleteByIdDetalleCargaDocente(
                idDetalleCargaDocente);
        saveRelacionesCargaProyecto(
                idDetalleCargaDocente,
                detalleCargaDocenteMapper.toRelacionesCargaProyecto(
                        actividad.relacionCargaProyecto()));
        log.info("Detalle precarga actualizado. idDetalle={}", idDetalleCargaDocente);
    }

    private void validateSaveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto) {
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"La carga docente es obligatoria");
        }
        if (!cargaDocenteRepository.existsById(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + dto.idCargaDocente());
        }
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            validateDetalleItem(detalle);
        }
    }

    private void validateUpdateDetailProfessorPreload(DetalleCargaDocenteDTO dto) {
        if (dto.idDetalleCargaDocente() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id del detalle de carga docente es obligatorio");
        }
        if (dto.idCargaDocente() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente es obligatoria");
        }
        if (dto.detalles() == null || dto.detalles().size() != 1) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La actualizacion requiere exactamente un detalle");
        }
        if (!cargaDocenteRepository.existsById(dto.idCargaDocente())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la carga docente con id " + dto.idCargaDocente());
        }

        DetalleCargaDocenteEntity detallePersistido = detalleCargaDocenteRepository
                .findById(dto.idDetalleCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe el detalle de carga docente con id "
                                + dto.idDetalleCargaDocente()));

        if (!detallePersistido.getIdCargaDocente().equals(dto.idCargaDocente())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El detalle no pertenece a la carga docente enviada");
        }

        validateDetalleActividad(
                dto.detalles().get(0),
                detallePersistido.getIdTipoActividad());
    }


    private void validateDetalleActividad(
            DetalleCargaDocenteActividadDTO actividad,
            Long idTipoActividadPersistido) {
        if (actividad.horas() == null || actividad.horas().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        if (actividad.centroCosto() == null || actividad.centroCosto().id() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo del detalle es obligatorio");
        }
        Long tipoActividad = detalleCargaDocenteMapper
                .resolveTipoActividadFromActividad(
                        actividad,
                        idTipoActividadPersistido);
        if (tipoActividad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad del detalle es obligatorio");
        }
        if (actividad.relacionCargaProyecto() != null) {
            for (RelacionCargaProyectoListadoDTO relacion
                    : actividad.relacionCargaProyecto()) {
                validateRelacionCargaProyecto(new RelacionCargaProyectoDTO(
                        relacion.idPersonaProyecto(),
                        relacion.idProyecto()));
            }
        }
    }

    private void validateDetalleItem(DetalleCargaDocenteItemDTO detalle) {
        if (detalle.horas() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        Long idCentroCostoResuelto = resolveIdCentroCosto(detalle);
        if (idCentroCostoResuelto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El centro de costo del detalle es obligatorio");
        }
        Long tipoActividad = detalle.idTipoActividadHija() != null ? detalle.idTipoActividadHija() : detalle.idTipoActividad();
        if (tipoActividad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo de actividad del detalle es obligatorio");
        }
        if (detalle.relacionCargaProyecto() != null) {
            for (RelacionCargaProyectoDTO relacion : detalle.relacionCargaProyecto()) {
                validateRelacionCargaProyecto(relacion);
            }
        }
    }

    private Long resolveIdCentroCosto(DetalleCargaDocenteItemDTO detalle) {
        if (detalle.materia() != null && detalle.materia().idCentroCosto() != null) {
            // Prioridad: centro de costo principal de la materia.
            return detalle.materia().idCentroCosto();
        }
        return detalle.idCentroCosto();
    }

    private void validateRelacionCargaProyecto(RelacionCargaProyectoDTO relacion) {
        if (relacion.idPersonaProyecto() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona proyecto es obligatoria");
        }
        if (relacion.idProyecto() != null && !personaProyectoRepository.existsByIdAndIdProyecto(relacion.idPersonaProyecto(), relacion.idProyecto())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La persona proyecto no corresponde al proyecto indicado");
        }
        if (!personaProyectoRepository.existsById(relacion.idPersonaProyecto())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona proyecto con id " + relacion.idPersonaProyecto());
        }
    }

    private void saveRelacionesCargaProyecto(Long idDetalleCargaDocente, List<RelacionCargaProyectoDTO> relaciones) {
        if (relaciones == null || relaciones.isEmpty()) {
            return;
        }
        for (RelacionCargaProyectoDTO relacion : relaciones) {
            RelacionCargaProyectoEntity entity = relacionCargaProyectoMapper.toEntity(idDetalleCargaDocente, relacion);
            entity.setRegistradoPor(REGISTRADO_POR);
            entity.setFechaCambio(new Date());
            relacionCargaProyectoRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void saveCareerProfessorPreload(CargaDocentePlantaDTO dto) {
        log.info("Guardando docente planta en precarga. idPersona={}, idCarga={}, idModalidad={}",
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCarga() : null,
                dto != null ? dto.idModalidadContratacion() : null);
        validateCareerProfessorPreload(dto);

        if (cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacion(
                dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion())) {
            log.warn("Docente planta duplicado. idPersona={}, idCarga={}",
                    dto.idPersonaGeneral(), dto.idCarga());
            throw new ApiException(HttpStatus.CONFLICT, "El docente ya se encuentra registrado en esta carga y modalidad");
        }

        FechasConvocatoriaEntity fecha = fechasConvocatoriaRepository
                .findByConvocatoriaAndModalidad(
                        dto.idConvocatoria(), dto.idModalidadContratacion())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe fecha de convocatoria para la modalidad seleccionada"));

        CategoriaModalidadEntity categoriaModalidad = categoriaModalidadRepository
                .findByIdModalidadContratacion(dto.idModalidadContratacion())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe categoria asociada a la modalidad de contratacion"));

        CargaDocenteEntity entity = cargaDocenteMapper.toEntityFromPlanta(dto);
        entity.setIdFechasConvocatoria(fecha.getId());
        entity.setIdCategoriaCatedratico(categoriaModalidad.getIdCategoriaCatedratico());
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        entity.setEstado("0");
        entity.setVigente("1");
        cargaDocenteRepository.save(entity);
        log.info("Docente planta guardado en precarga. idCargaDocente={}", entity.getId());
    }

    private void validateCareerProfessorPreload(CargaDocentePlantaDTO dto) {
        if (dto == null
                || dto.idCarga() == null
                || dto.idConvocatoria() == null
                || dto.idPersonaGeneral() == null
                || dto.idModalidadContratacion() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga, convocatoria, persona y modalidad son obligatorias");
        }

        if (!cargaRepository.existsByIdAndIdConvocatoria(
                dto.idCarga(), dto.idConvocatoria())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga no pertenece a la convocatoria indicada");
        }

        if (!personaGeneralRepository.existsById(dto.idPersonaGeneral())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona general con id " + dto.idPersonaGeneral());
        }
    }

    @Override
    public void deleteProfessorActivity(Long idDetalleCargaDocente) {
        log.info("Eliminando actividad docente. idDetalle={}", idDetalleCargaDocente);
        if (idDetalleCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle de carga docente es obligatorio");
        }
        if (!detalleCargaDocenteRepository.existsById(idDetalleCargaDocente)) {
            log.warn("Detalle carga docente no encontrado. id={}", idDetalleCargaDocente);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el detalle de carga docente con id " + idDetalleCargaDocente);
        }
        detalleCargaDocenteRepository.deleteByProcedure(idDetalleCargaDocente, REGISTRADO_POR);
        log.info("Actividad docente eliminada. idDetalle={}", idDetalleCargaDocente);
    }

    @Override
    @Transactional(readOnly = true)
    public TotalPreasignacionDTO getTotalPreassignment(Long idCarga) {
        log.debug("Obteniendo total de preasignacion. idCarga={}", idCarga);
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        if (!cargaRepository.existsById(idCarga)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga);
        }

        List<Object[]> totalesRows =
                cargaDocenteRepository.findTotalPreasignacionByCargaId(idCarga);
        Object[] totales = extractTotalRow(totalesRows);

        List<TotalHorasPreasignacionDTO> horasPorTipo =
                totalPreasignacionMapper.toHorasDtoList(
                        detalleCargaDocenteRepository.findTotalHorasPreasignacionByCargaId(idCarga));

        BigDecimal sumaHoras = horasPorTipo.stream()
                .map(item -> item.horas() != null ? item.horas() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TotalPreasignacionDTO result = new TotalPreasignacionDTO(
                toLongValue(totales, 0),
                toBigDecimalValue(totales, 1),
                toBigDecimalValue(totales, 2),
                toBigDecimalValue(totales, 3),
                horasPorTipo,
                sumaHoras);

        log.info("Total de preasignacion obtenido. idCarga={}, totalDocentes={}, totalPreasignacion={}",
                idCarga, result.totalDocentes(), result.totalPreasignacion());
        return result;
    }

    private Object[] extractTotalRow(List<Object[]> totalesRows) {
        if (totalesRows == null || totalesRows.isEmpty()) {
            return null;
        }
        Object first = totalesRows.get(0);
        if (first instanceof Object[] row) {
            return row;
        }
        return totalesRows.toArray();
    }

    private Long toLongValue(Object[] row, int index) {
        BigDecimal value = toBigDecimalValue(row, index);
        return value.longValue();
    }

    private BigDecimal toBigDecimalValue(Object[] row, int index) {
        if (row == null || index >= row.length || row[index] == null) {
            return BigDecimal.ZERO;
        }
        if (row[index] instanceof BigDecimal value) {
            return value;
        }
        if (row[index] instanceof Number value) {
            return new BigDecimal(value.toString());
        }
        try {
            return new BigDecimal(row[index].toString().trim());
        } catch (NumberFormatException ex) {
            log.warn("Valor numérico inválido en total preasignación: {}", row[index]);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<CoordinacionBusquedaDTO> searchCoordination(String nombre) {
        log.debug("Buscando coordinación. nombre={}", nombre);
        String param = normalizeParam(nombre);
        if(param == null || param.length() < 2) {
            log.debug("Búsqueda de coordinación sin criterios válidos");
            return Collections.emptyList();
        }

        List<CoordinacionBusquedaDTO> result = coordinacionMapper.toBusquedaDtoList(
                coordinacionRepository.searchCoordination(param));
        log.info("Coordinaciones encontradas. total={}", result.size());
        return result;
    }

    @Override
    @Transactional
    public void saveCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto) {
        log.info("Guardando restricción. idCoordinacion={}, idFechasConvocatoria={}",
                dto.idCoordinacion(), dto.idFechasConvocatoria());

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinacion con id " + dto.idCoordinacion());
        }

        if (!fechasConvocatoriaRepository.existsById(dto.idFechasConvocatoria())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la fecha de convocatoria con id " + dto.idFechasConvocatoria());
        }

        if (restriccionPorCoordinacionRepository.existsByIdCoordinacionAndIdFechasConvocatoria(dto.idCoordinacion(), dto.idFechasConvocatoria())) {
            log.warn("Restricción duplicada. idCoordinacion={}, idFechas={}",
                    dto.idCoordinacion(), dto.idFechasConvocatoria());
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe una restriccion para la coordinacion y fecha indicadas");
        }

        RestriccionPorCoordinacionEntity restriccion = restriccionPorCoordinacionMapper.toEntity(dto);
        restriccion.setRegistradoPor(REGISTRADO_POR);
        restriccion.setFechaCambio(new Date());
        restriccionPorCoordinacionRepository.save(restriccion);
        log.info("Restricción guardada. id={}", restriccion.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionRestriccionDTO> listCoordinationRestriction(Long idConvocatoria) {
        log.debug("Listando restricciones. idConvocatoria={}", idConvocatoria);
        List<CoordinacionRestriccionDTO> result = restriccionPorCoordinacionMapper.toDtoList(
                restriccionPorCoordinacionRepository.findAllWithCoordinacion(idConvocatoria));
        log.info("Restricciones listadas. idConvocatoria={}, total={}",
                idConvocatoria, result.size());
        return result;
    }

    @Override
    @Transactional
    public void updateCoordinationRestriction(
            Long id,
            CoordinacionRestriccionFormularioDTO dto) {
        log.info("Actualizando restricción id={}", id);
        validateCoordinationRestriction(dto);

        RestriccionPorCoordinacionEntity entity = restriccionPorCoordinacionRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la restriccion con id " + id));

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la coordinacion con id " + dto.idCoordinacion());
        }
        if (!fechasConvocatoriaRepository.existsById(dto.idFechasConvocatoria())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la fecha de convocatoria con id "
                            + dto.idFechasConvocatoria());
        }
        if (restriccionPorCoordinacionRepository
                .existsByIdCoordinacionAndIdFechasConvocatoriaAndIdNot(
                        dto.idCoordinacion(),
                        dto.idFechasConvocatoria(),
                        id)) {
            log.warn("Conflicto al actualizar restricción. id={}, idCoordinacion={}",
                    id, dto.idCoordinacion());
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe una restriccion para la coordinacion y fecha indicadas");
        }

        restriccionPorCoordinacionMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        restriccionPorCoordinacionRepository.save(entity);
        log.info("Restricción actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCoordinationRestriction(Long id, CoordinacionRestriccionDTO dto) {
        log.info("Eliminando restricción id={}", id);
        if (dto == null || dto.id() == null || !dto.id().equals(id)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id de la restriccion no coincide");
        }
        if (!restriccionPorCoordinacionRepository.existsById(id)) {
            log.warn("Restricción no encontrada al eliminar. id={}", id);
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la restriccion con id " + id);
        }
        restriccionPorCoordinacionRepository.deleteById(id);
        log.info("Restricción eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void bulkDeleteCoordinationRestriction(
            List<CoordinacionRestriccionDTO> restricciones) {
        log.info("Eliminación masiva de restricciones. total={}",
                restricciones != null ? restricciones.size() : 0);
        if (restricciones == null || restricciones.isEmpty()) {
            return;
        }
        for (CoordinacionRestriccionDTO restriccion : restricciones) {
            deleteCoordinationRestriction(restriccion.id(), restriccion);
        }
        log.info("Eliminación masiva de restricciones finalizada");
    }

    private void validateCoordinationRestriction(
            CoordinacionRestriccionFormularioDTO dto) {
        if (dto == null
                || dto.idCoordinacion() == null
                || dto.idFechasConvocatoria() == null
                || dto.fechaInicio() == null
                || dto.fechaFin() == null
                || !StringUtils.hasText(dto.estado())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La coordinacion, fecha de convocatoria, fechas y estado son obligatorios");
        }
    }

}
