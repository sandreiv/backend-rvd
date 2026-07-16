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

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria, Long idUsuario) {
        List<CoordinacionListadoProjection> projections = idConvocatoria == null ? coordinacionRepository.findWithoutCarga(idUsuario) : coordinacionRepository.findByConvocatoriaWithCarga(idConvocatoria, idUsuario);
        return coordinacionMapper.toDtoList(projections);
    }

    @Override
    @Transactional
    public void savePreload(RelacionConvocatoriaCoordinacionDTO dto) {
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
        return docentePlantaCoordinacionMapper.toDtoList(docentesPlantaCoordinacionRepository.findByIdCoordinacion(idCoordinacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocentePreasignacionDTO> searchProfessor(String nombre, String documento, Long idModalidadContratacion) {
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            return Collections.emptyList();
        }
        return docentePreasignacionMapper.toDtoList(personaGeneralRepository.searchProfessorsForPreassignment(nombreParam, documentoParam, idModalidadContratacion));
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
        return fechasConvocatoriaMapper.toModalidadDtoList(fechasConvocatoriaRepository.findByCoordinationAndModality(coorId, mocoId));
    }

    @Override
    @Transactional(readOnly = true)
    public ValorPuntosPrecargaDTO getValuePointsPreload(Long anio, Long idCategoriaCatedratico, Long idPersonaGeneral) {

        PuntosVigenciaEntity vigencia = puntosVigenciaRepository.findByAnio(anio)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"No existe valor de puntos para la vigencia " + anio));

        PuntosCategoriaEntity puntosCategoria = puntosCategoriaRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico);

        BigDecimal valorPunto = parseValor(vigencia.getValorPunto(), "valor del punto de la vigencia");
        BigDecimal puntosCategoriaValor = parseValor(puntosCategoria.getPuntos(), "puntos de la categoria");
        BigDecimal valorHora = valorPunto.multiply(puntosCategoriaValor).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        String valorPuntoTexto = valorPunto.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP).toPlainString();
  
        
        if (idPersonaGeneral == null) {
            return new ValorPuntosPrecargaDTO(valorHora.toPlainString(), valorPuntoTexto, null, null);
        }
        
        EscalafonEntity escalafon = escalafonRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico, idPersonaGeneral);
        if (escalafon == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe escalafon para la persona " + idPersonaGeneral + " y la categoria " + idCategoriaCatedratico);
        }

        BigDecimal puntosDocente = parseValor(escalafon.getPuntos(), "puntos del docente (escalafon)");
        BigDecimal asignacionSalarial = puntosDocente.multiply(valorPunto).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        return new ValorPuntosPrecargaDTO(
                valorHora.toPlainString(),
                valorPuntoTexto,
                puntosDocente.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP)
                        .toPlainString(),
                asignacionSalarial.toPlainString());
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
        return categoriaCatedraticoMapper.toDtoList(categoriaCatedraticoRepository.findAllCategories(idModalidadContratacion));
    }

    @Override
    @Transactional
    public void addProfessor(CargaDocenteFormularioDTO dto) {
        if (dto.idPersonaGeneral() != null
                && cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacionAndIdFechasConvocatoria(
                        dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion(), dto.fechasConvocatoria().id())) {
            throw new ApiException(HttpStatus.CONFLICT, "El docente ya se encuentra registrado en esta modalidad de contratacion");
        }
        CargaDocenteEntity entity = cargaDocenteMapper.toEntity(dto);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        entity.setEstado("0");
        entity.setVigente("1");
        cargaDocenteRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteCoordinacionDTO> listProfessors(Long idCoordinacion, Long idModalidadContratacion) {
        List<DocenteCargaCoordinacionProjection> projections;
        if (isModalidadPlanta(idModalidadContratacion)) {
            projections = cargaDocenteRepository.findPlantProfessorsByCoordinationAndModality(idCoordinacion, idModalidadContratacion);
        } else {
            projections = cargaDocenteRepository.findProfessorsByCoordinationAndModality(idCoordinacion, idModalidadContratacion);
        }
        return docenteCoordinacionMapper.toDtoList(projections);
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
        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));
        
        cargaDocenteMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        cargaDocenteRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteProfessor(Long idCargaDocente) {
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }
        cargaDocenteRepository.deleteByProcedure(idCargaDocente, REGISTRADO_POR);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadDTO> listRegionalUnits(Long idCoordinacion) {
        return unidadMapper.toDtoList(unidadRepository.findRegionalUnits(idCoordinacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaDTO> listProgramsByRegionalUnit(Long idCoordinacion, Long idUnidadRegional, Long idNivelEducativo) {
        if (asociacionCoordinacionRepository.existsProgramasByCoordinacion(idCoordinacion)) {
            return programaMapper.toDtoList(
                    programaRepository.findByCoordinacionUnidadRegionalAndNivelEducativo(
                            idCoordinacion, idUnidadRegional, idNivelEducativo));
        }
        return programaMapper.toDtoList(
                programaRepository.findByUnidadRegionalAndNivelEducativo(
                        idUnidadRegional, idNivelEducativo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadCriterioDTO> listCriteria(Long idTipoActividad) {
        return tipoActividadCriterioMapper.toDtoList(tipoActividadesRepository.findCriteriaByParentId(idTipoActividad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDTO> listActivityTypes() {
        return tipoActividadMapper.toDtoList(tipoActividadesRepository.findParentActivityTypes());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listSubjects(Long idPrograma, Long idCoordinacion) {
        List<MateriaListadoProjection> materias;

        if (asociacionCoordinacionRepository.existsProgramasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findPensumExcluyendoTransversales(idPrograma);
        } else if (asociacionCoordinacionRepository.existsMateriasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findTransversalesByCoordinacionAndPrograma(
                    idCoordinacion, idPrograma);
        } else {
            materias = materiaRepository.findByPrograma(idPrograma);
        }

        return mapMateriasConGrupo(materias);
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
        return grupoMapper.toDtoList(grupoRepository.findByCodigoMateriaAndIdPeriodoUniversidad(codigoMateria, idPeriodoUniversidad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> listProjectsProfessor(Long idPersonaGeneral) {
        return proyectoMapper.toDtoList(personaProyectoRepository.findProyectosByIdPersonaGeneral(idPersonaGeneral));
    }

    @Override
    @Transactional
    public void saveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto) {
        validateSaveDetailProfessorPreload(dto);
        
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            DetalleCargaDocenteEntity entity = detalleCargaDocenteMapper.toEntity(dto.idCargaDocente(), detalle);
            entity.setRegistradoPor(REGISTRADO_POR);
            entity.setFechaCambio(new Date());
            DetalleCargaDocenteEntity saved = detalleCargaDocenteRepository.save(entity);
            saveRelacionesCargaProyecto(saved.getId(), detalle.relacionCargaProyecto());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleCargaDocenteDTO> listDetailProfessorPreload(Long idCargaDocente) {
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }

        return detalleCargaDocenteMapper.toDtoList(
                detalleCargaDocenteRepository.findByIdCargaDocente(idCargaDocente),
                proyectoMapper,
                tipoActividadMapper,
                tipoActividadesRepository);
    }

    @Override
    @Transactional
    public void updateDetailProfessorPreload(DetalleCargaDocenteDTO dto) {
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
        validateCareerProfessorPreload(dto);

        if (cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacion(
                dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion())) {
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
        if (idDetalleCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle de carga docente es obligatorio");
        }
        if (!detalleCargaDocenteRepository.existsById(idDetalleCargaDocente)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe el detalle de carga docente con id " + idDetalleCargaDocente);
        }
        detalleCargaDocenteRepository.deleteByProcedure(idDetalleCargaDocente, REGISTRADO_POR);
    }

    @Override
    public List<CoordinacionBusquedaDTO> searchCoordination(String nombre) {
        String param = normalizeParam(nombre);
        if(param == null || param.length() < 2) {
            return Collections.emptyList();
        }

        return coordinacionMapper.toBusquedaDtoList(coordinacionRepository.searchCoordination(param));
    }

    @Override
    @Transactional
    public void saveCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto) {

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinacion con id " + dto.idCoordinacion());
        }

        if (!fechasConvocatoriaRepository.existsById(dto.idFechasConvocatoria())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la fecha de convocatoria con id " + dto.idFechasConvocatoria());
        }

        if (restriccionPorCoordinacionRepository.existsByIdCoordinacionAndIdFechasConvocatoria(dto.idCoordinacion(), dto.idFechasConvocatoria())) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe una restriccion para la coordinacion y fecha indicadas");
        }

        RestriccionPorCoordinacionEntity restriccion = restriccionPorCoordinacionMapper.toEntity(dto);
        restriccion.setRegistradoPor(REGISTRADO_POR);
        restriccion.setFechaCambio(new Date());
        restriccionPorCoordinacionRepository.save(restriccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionRestriccionDTO> listCoordinationRestriction(Long idConvocatoria) {
        return restriccionPorCoordinacionMapper.toDtoList(
                restriccionPorCoordinacionRepository.findAllWithCoordinacion(idConvocatoria));
    }

    @Override
    @Transactional
    public void updateCoordinationRestriction(
            Long id,
            CoordinacionRestriccionFormularioDTO dto) {
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
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Ya existe una restriccion para la coordinacion y fecha indicadas");
        }

        restriccionPorCoordinacionMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(REGISTRADO_POR);
        entity.setFechaCambio(new Date());
        restriccionPorCoordinacionRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteCoordinationRestriction(Long id, CoordinacionRestriccionDTO dto) {
        if (dto == null || dto.id() == null || !dto.id().equals(id)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El id de la restriccion no coincide");
        }
        if (!restriccionPorCoordinacionRepository.existsById(id)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la restriccion con id " + id);
        }
        restriccionPorCoordinacionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void bulkDeleteCoordinationRestriction(
            List<CoordinacionRestriccionDTO> restricciones) {
        if (restricciones == null || restricciones.isEmpty()) {
            return;
        }
        for (CoordinacionRestriccionDTO restriccion : restricciones) {
            deleteCoordinationRestriction(restriccion.id(), restriccion);
        }
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
