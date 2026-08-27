/**
 * Aplicación: rvd
 * Archivo: CoordinacionServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - Listado coordinaciones por JWT (Coordinador/Decano)
 * 25/08/2026 - Sebastian Jaimes - registradoPor con idPersona, acción e IP
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaEstadoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.ActividadModalidadMapper;
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
import co.edu.unipamplona.ciadti.rvd.mapper.ObservacionesCargaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProgramaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.ProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RelacionCargaProyectoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RestriccionPorCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadCriterioMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TipoActividadMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.TotalPreasignacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.UnidadMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadDirectaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoResumenDTO;
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
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionDecanoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionProgramaHorasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalHorasPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AprobacionDetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaModalidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.HistorialCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionPorCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.AsignarCentroCostoRepository;
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
import co.edu.unipamplona.ciadti.rvd.model.repository.HistorialCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MateriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ObservacionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ProgramaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RelacionCargaProyectoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.UnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ActividadModalidadProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasProgramaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.FechaModalidadProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinacionServiceImpl implements CoordinacionService {

    private static final String REGISTRADO_POR = "REGISTRO_WEB";
    private static final String ESTADO_CARGA_INICIAL = "REGISTRADO"; // POR DEFINIR
    private static final String ESTADO_CARGA_INSCRITA = "INSCRITO";
    private static final String ROL_COORDINADOR = "Coordinador";
    private static final String ROL_DECANO = "Decano";
    private static final String ROL_DESARROLLO = "Desarrollo academico";

    private static final int ESCALA_MONETARIA = 2;
    private static final int ESCALA_PORCENTAJE = 2;
    
    private static final BigDecimal DIAS_MES = new BigDecimal("30");
    private static final BigDecimal DIAS_ANIO = new BigDecimal("360");
    private static final BigDecimal DIAS_VACACIONES = new BigDecimal("720");
    private static final BigDecimal TASA_INTERES = new BigDecimal("0.12");
    private static final BigDecimal PUNTOS_DOCENTE_DEFAULT = new BigDecimal("100");
    private static final BigDecimal CIEN = new BigDecimal("100");
    
    private static final String CODIGO_ACTIVIDAD_DIRECTA = "FAD";
    private static final String PREASIGNACION_SOLO_LECTURA = "La convocatoria tiene restricción activa y esta coordinación no está habilitada para edición en las fechas permitidas.";
    private static final Set<String> CODIGOS_CENTRO_COSTO_ESPECIAL = Set.of("CTEI", "ISU");
    private static final String MENSAJE_PLANTA_SIN_PROYECTO_CTEI_ISU = "Debe tener un proyecto CTEI o ISU asociado para aprobar";
   
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
    private final HistorialCargaDocenteRepository historialCargaDocenteRepository;
    private final ObservacionCargaRepository observacionCargaRepository;
    private final CargaDocenteMapper cargaDocenteMapper;
    private final DocenteCoordinacionMapper docenteCoordinacionMapper;
    private final UnidadRepository unidadRepository;
    private final ProgramaRepository programaRepository;
    private final UnidadMapper unidadMapper;
    private final ProgramaMapper programaMapper;
    private final TipoActividadesRepository tipoActividadesRepository;
    private final TipoActividadCriterioMapper tipoActividadCriterioMapper;
    private final TipoActividadMapper tipoActividadMapper;
    private final RestriccionCargaRepository restriccionCargaRepository;
    private final ObjectMapper objectMapper;
    private final ActividadModalidadMapper actividadModalidadMapper;
    private final MateriaRepository materiaRepository;
    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final MateriaMapper materiaMapper;
    private final AsociacionCoordinacionRepository asociacionCoordinacionRepository;
    private final AsignarCentroCostoRepository asignarCentroCostoRepository;
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
    private final ObservacionesCargaMapper observacionesCargaMapper;
    private final ConvocatoriaEstadoService convocatoriaEstadoService;

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByIdConvocatoria(
            Long idConvocatoria,
            Long idPeriodoUniversidad) {
        AuthUserDetails user = requireListadoUser();
        Long idPersona = user.getIdPersonaGeneral();
        boolean coordinador = hasRole(user, ROL_COORDINADOR);
        boolean decano = hasRole(user, ROL_DECANO);
        log.debug(
                "findCoordinationsByIdConvocatoria ===> Listando coordinaciones. idConvocatoria={}, idPeriodoUniversidad={}, idPersona={}, coordinador={}, decano={}",
                idConvocatoria, idPeriodoUniversidad, idPersona, coordinador, decano);

        validateListadoFiltros(idConvocatoria, idPeriodoUniversidad);
        List<CoordinacionListadoProjection> projections = new ArrayList<>();
        if (coordinador) {
            projections.addAll(listForCoordinador(
                    idConvocatoria, idPeriodoUniversidad, idPersona));
        }
        if (decano) {
            projections.addAll(listForDecano(
                    idConvocatoria, idPeriodoUniversidad, idPersona));
        }

        List<CoordinacionDTO> result = coordinacionMapper.toDtoList(projections);
        log.info(
                "findCoordinationsByIdConvocatoria ===> Coordinaciones listadas. idConvocatoria={}, idPeriodoUniversidad={}, total={}",
                idConvocatoria, idPeriodoUniversidad, result.size());
        return result;
    }

    private AuthUserDetails requireListadoUser() {
        AuthUserDetails user;
        try {
            user = SecurityUtils.requireUser();
            System.out.println("[DEBUG] USER: " + user);
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (user.getIdPersonaGeneral() == null) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El token no trae idPersona");
        }
        if (!hasRole(user, ROL_COORDINADOR) && !hasRole(user, ROL_DECANO) && !hasRole(user, ROL_DESARROLLO)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El listado de coordinaciones requiere rol Coordinador o Decano");
        }
        return user;
    }

    private void validateListadoFiltros(Long idConvocatoria, Long idPeriodoUniversidad) {
        if (idConvocatoria != null && idPeriodoUniversidad != null) {
            validateConvocatoriaBelongsToPeriod(idConvocatoria, idPeriodoUniversidad);
        }
        if (idConvocatoria == null && idPeriodoUniversidad == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El periodo universitario es obligatorio cuando no se envía convocatoria");
        }
    }

    private List<CoordinacionListadoProjection> listForCoordinador(
            Long idConvocatoria,
            Long idPeriodoUniversidad,
            Long idPersona) {
        if (idConvocatoria != null) {
            System.out.println("[DEBUG] LISTANDO COMO COORDINADOR");
            System.out.println("[DEBUG] idConvocatoria: " + idConvocatoria);
            System.out.println("[DEBUG] idPersona: " + idPersona);
            return coordinacionRepository.findByConvocatoriaWithCarga(idConvocatoria, idPersona);
        }
        System.out.println("[DEBUG] LISTANDO SIN CONVOCATORIA");
        System.out.println("[DEBUG] idPeriodoUniversidad: " + idPeriodoUniversidad);
        System.out.println("[DEBUG] idPersona: " + idPersona);
        return coordinacionRepository.findWithoutCarga(idPersona, idPeriodoUniversidad);
    }

    private List<CoordinacionListadoProjection> listForDecano(
            Long idConvocatoria,
            Long idPeriodoUniversidad,
            Long idPersona) {
        if (idConvocatoria != null) {
            System.out.println("[DEBUG] LISTANDO COMO DECANO");
            System.out.println("[DEBUG] idConvocatoria: " + idConvocatoria);
            System.out.println("[DEBUG] idPersona: " + idPersona);
            return coordinacionRepository.findByConvocatoriaForDean(idConvocatoria, idPersona);
        }
        System.out.println("[DEBUG] LISTANDO COMO DECANO SIN CONVOCATORIA");
        System.out.println("[DEBUG] idPeriodoUniversidad: " + idPeriodoUniversidad);
        System.out.println("[DEBUG] idPersona: " + idPersona);
        return coordinacionRepository.findByPeriodoForDean(idPeriodoUniversidad, idPersona);
    }

    private static boolean hasRole(AuthUserDetails user, String role) {
        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .anyMatch(item -> role.equalsIgnoreCase(item.trim()));
    }

    private void validateConvocatoriaBelongsToPeriod(
            Long idConvocatoria,
            Long idPeriodoUniversidad) {
        var periodo = convocatoriaRepository.findPeriodoEntityByConvocatoriaId(idConvocatoria);
        if (periodo == null || !Objects.equals(periodo.getId(), idPeriodoUniversidad)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria no pertenece al periodo universitario indicado");
        }
    }

    @Override
    @Transactional
    public void savePreload(RelacionConvocatoriaCoordinacionDTO dto) {
        log.info("savePreload ===> Guardando preasignación. idCoordinacion={}, idConvocatoria={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idConvocatoria() : null);
        validateSavePreload(dto);

        Optional<CargaEntity> cargaExistente = cargaRepository
                .findFirstByIdCoordinacionAndIdConvocatoria(
                        dto.idCoordinacion(),
                        dto.idConvocatoria());
        boolean isNewCarga = cargaExistente.isEmpty();

        CargaEntity carga = cargaExistente.orElseGet(CargaEntity::new);

        carga.setIdCoordinacion(dto.idCoordinacion());
        carga.setIdConvocatoria(dto.idConvocatoria());

        if (carga.getIdEstadoCarga() == null) {
            carga.setIdEstadoCarga(resolveEstadoCargaInicialId());
        }

        carga.setRegistradoPor(RegistradoPorUtils.value(isNewCarga ? Accion.INSERT : Accion.UPDATE));
        carga.setFechaCambio(new Date());

        cargaRepository.save(carga);
        log.info("savePreload ===> Preasignación guardada. idCarga={}, idCoordinacion={}, idConvocatoria={}",
                carga.getId(), dto.idCoordinacion(), dto.idConvocatoria());

        if (isNewCarga) {
            inheritOnceMesesTeachers(carga);
        }
    }

    private void inheritOnceMesesTeachers(CargaEntity cargaDestino) {
        if (cargaDestino == null
                || cargaDestino.getId() == null
                || cargaDestino.getIdConvocatoria() == null
                || cargaDestino.getIdCoordinacion() == null) {
            return;
        }

        ConvocatoriaEntity convocatoriaDestino = convocatoriaRepository
                .findById(cargaDestino.getIdConvocatoria())
                .orElse(null);
        if (convocatoriaDestino == null || convocatoriaDestino.getIdRelacion() == null) {
            log.debug(
                    "inheritOnceMesesTeachers ===> Sin relación de convocatoria. idCarga={}",
                    cargaDestino.getId());
            return;
        }

        Optional<CargaEntity> cargaOrigenOpt = cargaRepository
                .findFirstByIdCoordinacionAndIdConvocatoria(
                        cargaDestino.getIdCoordinacion(),
                        convocatoriaDestino.getIdRelacion());
        if (cargaOrigenOpt.isEmpty()) {
            log.debug(
                    "inheritOnceMesesTeachers ===> No hay carga origen. idCoordinacion={}, idConvocatoriaRelacion={}",
                    cargaDestino.getIdCoordinacion(),
                    convocatoriaDestino.getIdRelacion());
            return;
        }

        List<CargaDocenteEntity> docentesOnceMeses = cargaDocenteRepository
                .findByIdCargaAndOnceMeses(cargaOrigenOpt.get().getId(), "1");
        if (docentesOnceMeses.isEmpty()) {
            log.debug(
                    "inheritOnceMesesTeachers ===> Sin docentes once meses en origen. idCargaOrigen={}",
                    cargaOrigenOpt.get().getId());
            return;
        }

        int heredados = 0;
        for (CargaDocenteEntity origen : docentesOnceMeses) {
            if (origen.getIdPersonaGeneral() != null
                    && cargaDocenteRepository
                            .existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacion(
                                    origen.getIdPersonaGeneral(),
                                    cargaDestino.getId(),
                                    origen.getIdModalidadContratacion())) {
                continue;
            }

            FechasConvocatoriaEntity fechaDestino = fechasConvocatoriaRepository
                    .findByConvocatoriaAndModalidad(
                            cargaDestino.getIdConvocatoria(),
                            origen.getIdModalidadContratacion())
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "No existe fecha de convocatoria en la convocatoria destino"
                                    + " para la modalidad " + origen.getIdModalidadContratacion()));

            CargaDocenteEntity copia = cloneCargaDocenteForInheritance(origen, cargaDestino.getId(), fechaDestino);
            cargaDocenteRepository.save(copia);
            heredados++;
        }

        log.info(
                "inheritOnceMesesTeachers ===> Docentes once meses heredados. idCargaDestino={}, total={}",
                cargaDestino.getId(),
                heredados);
    }

    private CargaDocenteEntity cloneCargaDocenteForInheritance(CargaDocenteEntity origen, Long idCargaDestino, FechasConvocatoriaEntity fechaDestino) {
        CargaDocenteEntity copia = new CargaDocenteEntity();
        
        copia.setIdCarga(idCargaDestino);
        copia.setIdPersonaGeneral(origen.getIdPersonaGeneral());
        copia.setIdModalidadContratacion(origen.getIdModalidadContratacion());
        copia.setIdCategoriaCatedratico(origen.getIdCategoriaCatedratico());
        copia.setIdFechasConvocatoria(fechaDestino.getId());
        copia.setFechaInicio(origen.getFechaInicio());
        copia.setFechaFin(origen.getFechaFin());
        //copia.setValorContrato(origen.getValorContrato());
        //copia.setValorPrestaciones(origen.getValorPrestaciones());
        //copia.setSalario(origen.getSalario());
        copia.setEstado(origen.getEstado());
        copia.setVigente(origen.getVigente());
        copia.setHoras(origen.getHoras());
        copia.setValorHora(origen.getValorHora());
        copia.setPuntos(origen.getPuntos());
        copia.setValorPunto(origen.getValorPunto());
        //copia.setTotalContrato(origen.getTotalContrato());
        copia.setSemanas(origen.getSemanas());
        copia.setNivelFormacion(origen.getNivelFormacion());
        copia.setMomento(origen.getMomento());
        copia.setOnceMeses(origen.getOnceMeses());
        copia.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        copia.setFechaCambio(new Date());
        return copia;
    }

    private void validatePreassignmentWriteAllowedByCarga(Long idCarga) {
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }

        CargaEntity carga = cargaRepository.findById(idCarga)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga con id " + idCarga));

        validatePreassignmentWriteAllowed(carga);
    }

    private void validatePreassignmentWriteAllowedByCargaDocente(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));

        validatePreassignmentWriteAllowedByCarga(cargaDocente.getIdCarga());
    }

    private void validatePreassignmentWriteAllowedByDetalle(Long idDetalleCargaDocente) {
        if (idDetalleCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle de carga docente es obligatorio");
        }

        DetalleCargaDocenteEntity detalle = detalleCargaDocenteRepository.findById(idDetalleCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe el detalle de carga docente con id " + idDetalleCargaDocente));

        validatePreassignmentWriteAllowedByCargaDocente(detalle.getIdCargaDocente());
    }

    private void validatePreassignmentWriteAllowed(CargaEntity carga) {
        if (carga == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga de la preasignación");
        }

        if (isPreassignmentWriteAllowed(carga.getIdConvocatoria(), carga.getIdCoordinacion())) {
            return;
        }

        log.warn("validatePreassignmentWriteAllowed ===> Escritura bloqueada. idCarga={}, idConvocatoria={}, idCoordinacion={}",
                carga.getId(), carga.getIdConvocatoria(), carga.getIdCoordinacion());

        throw new ApiException(HttpStatus.FORBIDDEN, PREASIGNACION_SOLO_LECTURA);
    }

    private boolean isPreassignmentWriteAllowed(Long idConvocatoria, Long idCoordinacion) {
        if (idConvocatoria == null || idCoordinacion == null) {
            return false;
        }

        var convocatoria = convocatoriaRepository.findById(idConvocatoria)
                .orElse(null);

        if (convocatoria == null) {
            return false;
        }

        Long totalRestricciones = restriccionPorCoordinacionRepository
                .countActiveNonExpiredRestrictionsByConvocatoria(idConvocatoria);

        boolean tieneRestriccionesNoVencidas =
                totalRestricciones != null && totalRestricciones > 0;

        if (!tieneRestriccionesNoVencidas) {
            return "1".equals(convocatoria.getEstado());
        }

        Long totalRestriccionesEditables = restriccionPorCoordinacionRepository
                .countEditableRestrictionsByConvocatoriaAndCoordinacion(
                        idConvocatoria,
                        idCoordinacion);

        return totalRestriccionesEditables != null && totalRestriccionesEditables > 0;
    }

    private void validateAssignablePreloadCall(Long idConvocatoria) {
        if (idConvocatoria == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La convocatoria es obligatoria");
        }

        var convocatoria = convocatoriaRepository.findById(idConvocatoria)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada"));

        Long totalRestricciones = restriccionPorCoordinacionRepository
                .countActiveNonExpiredRestrictionsByConvocatoria(idConvocatoria);

        boolean tieneRestriccionesNoVencidas =
                totalRestricciones != null && totalRestricciones > 0;

        if (!"1".equals(convocatoria.getEstado()) || tieneRestriccionesNoVencidas) {
            log.warn("validateAssignablePreloadCall ===> Convocatoria no asignable libremente. idConvocatoria={}, estado={}, restricciones={}",
                    idConvocatoria,
                    convocatoria.getEstado(),
                    totalRestricciones);

            throw new ApiException(HttpStatus.CONFLICT, "La convocatoria no está disponible para asignación libre."
            );
        }
    }


    private Long resolveEstadoCargaInicialId() {
        return estadoCargaRepository.findByNombre(ESTADO_CARGA_INICIAL)
                .map(estado -> estado.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"No existe el estado de carga inicial: " + ESTADO_CARGA_INICIAL));
    }

    private Long resolveEstadoCargaInscritaId() {
        return estadoCargaRepository.findByNombre(ESTADO_CARGA_INSCRITA)
                .map(estado -> estado.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"No existe el estado de carga inscrita: " + ESTADO_CARGA_INICIAL));
    }

    private void validateSavePreload(RelacionConvocatoriaCoordinacionDTO dto) {
        if (dto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La información de la preasignación es obligatoria"
            );
        }

        if (dto.idCoordinacion() == null || dto.idConvocatoria() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinación y la convocatoria son obligatorias"
            );
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Coordinación no encontrada");
        }

        if (!convocatoriaRepository.existsById(dto.idConvocatoria())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Convocatoria no encontrada");
        }

        validateAssignablePreloadCall(dto.idConvocatoria());

    }

    @Override
    @Transactional(readOnly = true)
    public List<DocentePlantaCoordinacionDTO> listCareerProfessors(Long idCoordinacion) {
        log.debug("listCareerProfessors ===> Listando docentes de planta. idCoordinacion={}", idCoordinacion);
        
        List<DocentePlantaCoordinacionDTO> result = docentePlantaCoordinacionMapper.toDtoList(docentesPlantaCoordinacionRepository.findByIdCoordinacion(idCoordinacion));
        
        log.info("listCareerProfessors ===> Docentes de planta listados. idCoordinacion={}, total={}",idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocentePreasignacionDTO> searchProfessor(String nombre, String documento, Long idModalidadContratacion) {
        log.debug("searchProfessor ===> Buscando docente. nombre={}, documento={}, idModalidad={}", nombre, documento, idModalidadContratacion);
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            log.debug("searchProfessor ===> Búsqueda de docente sin criterios. Se retorna lista vacía");
            return Collections.emptyList();
        }
        List<DocentePreasignacionDTO> result = docentePreasignacionMapper.toDtoList(
                personaGeneralRepository.searchProfessorsForPreassignment(
                        nombreParam, documentoParam, idModalidadContratacion));
                        
        log.info("searchProfessor ===> Docentes encontrados. total={}", result.size());
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
    public List<FechaModalidadFormularioDTO> getWorkDate(Long idCarga, Long idModalidadContratacion) {
        log.debug("getWorkDate ===> Consultando fechas de trabajo. idCarga={}, idModalidad={}",
                idCarga, idModalidadContratacion);

        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        if (!cargaRepository.existsById(idCarga)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga);
        }

        List<FechaModalidadProjection> fechas =
                isModalidadPlanta(idModalidadContratacion)
                        ? fechasConvocatoriaRepository
                                .findPlantByCargaAndModalityAndRestrictionSemanal(
                                        idCarga,
                                        idModalidadContratacion
                                )
                        : fechasConvocatoriaRepository
                                .findByCargaAndModalityAndRestrictionSemanal(
                                        idCarga,
                                        idModalidadContratacion
                                );

        List<FechaModalidadFormularioDTO> result =
                fechasConvocatoriaMapper.toModalidadDtoList(fechas);

        log.info("getWorkDate ===> Fechas de trabajo consultadas. idCarga={}, total={}",
                idCarga, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ValorPuntosPrecargaDTO getValuePointsPreload(Long anio, Long idCategoriaCatedratico, Long idPersonaGeneral, Long idModalidadContratacion) {
        log.debug("getValuePointsPreload ===> Calculando valor puntos precarga. anio={}, idCategoria={}, idPersona={}", anio, idCategoriaCatedratico, idPersonaGeneral);

        PuntosVigenciaEntity vigencia = puntosVigenciaRepository.findByAnio(anio).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"No existe valor de puntos para la vigencia " + anio));

        PuntosCategoriaEntity puntosCategoria = puntosCategoriaRepository.findByIdCategoriaCatedratico(idCategoriaCatedratico);

        BigDecimal valorPunto = parseValor(vigencia.getValorPunto(), "valor del punto de la vigencia");
        BigDecimal puntosCategoriaValor = parseValor(puntosCategoria.getPuntos(), "puntos de la categoria");
        BigDecimal valorHora = valorPunto.multiply(puntosCategoriaValor).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        BigDecimal valorPuntoEscalado = valorPunto.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        String formaPago = resolveFormaPago(idModalidadContratacion);

        if (idPersonaGeneral == null) {
            log.info(
                    "getValuePointsPreload ===> Valor puntos precarga calculado sin persona. anio={}, valorHora={}, formaPago={}",
                    anio, valorHora, formaPago);
            return new ValorPuntosPrecargaDTO(
                    valorHora, valorPuntoEscalado, null, null, formaPago);
        }

        EscalafonEntity escalafon = escalafonRepository
                .findByIdCategoriaCatedratico(idCategoriaCatedratico, idPersonaGeneral);
        if (escalafon == null) {
            log.warn(
                    "getValuePointsPreload ===> Escalafón no encontrado. idPersona={}, idCategoria={}",
                    idPersonaGeneral, idCategoriaCatedratico);
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe escalafon para la persona " + idPersonaGeneral
                            + " y la categoria " + idCategoriaCatedratico);
        }

        BigDecimal puntosDocente = parseValor(escalafon.getPuntos(), "puntos del docente (escalafon)");
        BigDecimal asignacionSalarial = puntosDocente.multiply(valorPunto)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        log.info(
                "getValuePointsPreload ===> Valor puntos precarga calculado. anio={}, idPersona={}, valorHora={}, formaPago={}",
                anio, idPersonaGeneral, valorHora, formaPago);
        return new ValorPuntosPrecargaDTO(
                valorHora,
                valorPuntoEscalado,
                puntosDocente.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP),
                asignacionSalarial,
                formaPago);
    }

    private String resolveFormaPago(Long idModalidadContratacion) {
        if (idModalidadContratacion == null) {
            return null;
        }
        return restriccionCargaRepository.findById(idModalidadContratacion)
                .map(RestriccionCargaEntity::getFormaPago)
                .orElse(null);
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
        log.debug("listProfessorCategory ===> Listando categorias catedratico. idModalidad={}", idModalidadContratacion);
        
        List<CategoriaCatedraticoDTO> result = categoriaCatedraticoMapper.toDtoList(categoriaCatedraticoRepository.findAllCategories(idModalidadContratacion));
        
        log.info("listProfessorCategory ===> Categorias catedratico listadas. idModalidad={}, total={}", idModalidadContratacion, result.size());
        return result;
    }

    @Override
    @Transactional
    public void addProfessor(CargaDocenteFormularioDTO dto) {
        log.info("addProfessor ===> Agregando docente. idPersona={}, idCarga={}, idModalidad={}", dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion());
        
        validatePreassignmentWriteAllowedByCarga(dto.idCarga());

        if (dto.idPersonaGeneral() != null
                && cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacionAndIdFechasConvocatoria(
                        dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion(), dto.fechasConvocatoria().id())) {
            log.warn("addProfessor ===> Docente duplicado en modalidad. idPersona={}, idCarga={}", dto.idPersonaGeneral(), dto.idCarga());
            throw new ApiException(HttpStatus.CONFLICT, "El docente ya se encuentra registrado en esta modalidad de contratacion");
        }
        CargaDocenteEntity entity = cargaDocenteMapper.toEntity(dto);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());
        entity.setEstado("0");
        entity.setVigente("1");
        entity.setOnceMeses(FechasConvocatoriaCalculator.calcularOnceMesesPorSemanas(dto.semanas()));
        applyHorasDeExcepcion(entity);
        Long idNewCargaDocente = cargaDocenteRepository.save(entity).getId();

        // Registrar el estado dentro del historial
        registerProfessorPreloadHistory(idNewCargaDocente);

        log.info("addProfessor ===> Docente agregado. idCargaDocente={}", entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteCoordinacionDTO> listProfessors(Long idCarga, Long idModalidadContratacion) {
        log.debug("listProfessors ===> Listando docentes. idCarga={}, idModalidad={}",
                idCarga, idModalidadContratacion);

        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        if (!cargaRepository.existsById(idCarga)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga);
        }

        List<DocenteCargaCoordinacionProjection> projections;
        if (isModalidadPlanta(idModalidadContratacion)) {
            projections = cargaDocenteRepository.findPlantProfessorsByCargaAndModality(
                    idCarga, idModalidadContratacion);
        } else {
            projections = cargaDocenteRepository.findProfessorsByCargaAndModality(
                    idCarga, idModalidadContratacion);
        }
        List<DocenteCoordinacionDTO> result = docenteCoordinacionMapper.toDtoList(projections);
        log.info("listProfessors ===> Docentes listados. idCarga={}, total={}", idCarga, result.size());
        return result;
    }

    private void applyHorasDeExcepcion(CargaDocenteEntity entity) {
        if (entity == null) {
            return;
        }

        entity.setHorasDeExcepcion(
                resolveHorasDeExcepcion(
                        entity.getIdModalidadContratacion(),
                        entity.getIdPersonaGeneral()
                )
        );
    }

    private String resolveHorasDeExcepcion(Long idModalidadContratacion, Long idPersonaGeneral) {
        if (idModalidadContratacion == null || idPersonaGeneral == null) {
            return null;
        }

        return restriccionCargaRepository.findById(idModalidadContratacion)
                .map(RestriccionCargaEntity::getExcepcion)
                .flatMap(excepcion -> extractHorasDeExcepcion(excepcion, idPersonaGeneral))
                .orElse(null);
    }

    private Optional<String> extractHorasDeExcepcion(
            String excepcion,
            Long idPersonaGeneral) {
        if (!StringUtils.hasText(excepcion)) {
            return Optional.empty();
        }

        try {
            JsonNode root = objectMapper.readTree(excepcion);
            JsonNode personas = root.get("personas");

            if (personas == null || !personas.isArray()) {
                return Optional.empty();
            }

            for (JsonNode persona : personas) {
                if (persona == null || !persona.isObject()) {
                    continue;
                }

                Long idPersona = parseLongNode(persona.get("idPersona"));

                if (idPersona == null) {
                    idPersona = parseLongNode(persona.get("id"));
                }

                if (!idPersonaGeneral.equals(idPersona)) {
                    continue;
                }

                JsonNode maximoHorasNode = persona.get("maximoHoras");

                if (maximoHorasNode != null
                        && StringUtils.hasText(maximoHorasNode.asText())) {
                    return Optional.of(maximoHorasNode.asText().trim());
                }
            }

            return Optional.empty();
        } catch (JsonProcessingException ex) {
            log.warn("extractHorasDeExcepcion ===> No fue posible leer la excepción configurada. value={}",
                    excepcion);
            return Optional.empty();
        }
    }

    private Map<Long, String> resolveMaximosHorasPrograma(
            Long idModalidadContratacion) {
        Map<Long, String> result = new LinkedHashMap<>();

        restriccionCargaRepository.findById(idModalidadContratacion)
                .map(RestriccionCargaEntity::getExcepcion)
                .ifPresent(excepcion -> {
                    try {
                        JsonNode root = objectMapper.readTree(excepcion);
                        JsonNode programas = root.get("programas");
                        if (programas == null || !programas.isArray()) {
                            return;
                        }
                        for (JsonNode programa : programas) {
                            if (programa == null || programa.isNull()) {
                                continue;
                            }

                            Long idPrograma;
                            String maximoHoras = null;

                            if (programa.isObject()) {
                                idPrograma = parseLongNode(
                                        programa.get("idPrograma"));
                                if (idPrograma == null) {
                                    idPrograma = parseLongNode(
                                            programa.get("id"));
                                }
                                JsonNode maximoNode =
                                        programa.get("maximoHoras");
                                if (maximoNode != null
                                        && StringUtils.hasText(
                                                maximoNode.asText())) {
                                    maximoHoras = maximoNode.asText().trim();
                                }
                            } else {
                                idPrograma = parseLongNode(programa);
                            }

                            if (idPrograma != null
                                    && StringUtils.hasText(maximoHoras)) {
                                result.putIfAbsent(idPrograma, maximoHoras);
                            }
                        }
                    } catch (JsonProcessingException ex) {
                        log.warn(
                                "resolveMaximosHorasPrograma ===> No fue posible leer excepciones de programa. idModalidad={}",
                                idModalidadContratacion);
                    }
                });

        return result;
    }

    private Map<Long, BigDecimal> loadHorasAsignadasPorPrograma(
            Long idCargaDocente,
            Long idDetalleExcluido) {
        Map<Long, BigDecimal> result = new LinkedHashMap<>();
        List<HorasProgramaProjection> rows =
                detalleCargaDocenteRepository
                        .findHorasByProgramaAndCargaDocente(
                                idCargaDocente,
                                idDetalleExcluido);
        for (HorasProgramaProjection row : rows) {
            if (row.getIdPrograma() == null) {
                continue;
            }
            result.put(
                    row.getIdPrograma(),
                    row.getTotalHoras() != null
                            ? row.getTotalHoras()
                            : BigDecimal.ZERO);
        }
        return result;
    }

    private void validateProgramHourRestrictionOnSave(
            DetalleCargaDocenteFormularioDTO dto) {
        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                .findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + dto.idCargaDocente()));

        Map<Long, String> maximos = resolveMaximosHorasPrograma(
                cargaDocente.getIdModalidadContratacion());
        if (maximos.isEmpty()) {
            return;
        }

        Map<Long, BigDecimal> horasNuevas = new LinkedHashMap<>();
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            if (detalle.idPrograma() == null || detalle.horas() == null) {
                continue;
            }
            if (!maximos.containsKey(detalle.idPrograma())) {
                continue;
            }
            horasNuevas.merge(
                    detalle.idPrograma(),
                    BigDecimal.valueOf(detalle.horas()),
                    BigDecimal::add);
        }

        if (horasNuevas.isEmpty()) {
            return;
        }

        Map<Long, BigDecimal> horasAsignadas = loadHorasAsignadasPorPrograma(
                dto.idCargaDocente(),
                null);

        for (Map.Entry<Long, BigDecimal> entry : horasNuevas.entrySet()) {
            assertProgramHoursWithinLimit(
                    entry.getKey(),
                    maximos.get(entry.getKey()),
                    horasAsignadas.getOrDefault(
                            entry.getKey(),
                            BigDecimal.ZERO),
                    entry.getValue());
        }
    }

    private void validateProgramHourRestrictionOnUpdate(
            DetalleCargaDocenteDTO dto,
            DetalleCargaDocenteEntity detallePersistido) {
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        Long idPrograma = actividad.programa() != null
                ? actividad.programa().id()
                : null;
        if (idPrograma == null || !StringUtils.hasText(actividad.horas())) {
            return;
        }

        CargaDocenteEntity cargaDocente = cargaDocenteRepository
                .findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id "
                                + dto.idCargaDocente()));

        Map<Long, String> maximos = resolveMaximosHorasPrograma(
                cargaDocente.getIdModalidadContratacion());
        String maximoHoras = maximos.get(idPrograma);
        if (!StringUtils.hasText(maximoHoras)) {
            return;
        }

        Map<Long, BigDecimal> horasAsignadas = loadHorasAsignadasPorPrograma(
                dto.idCargaDocente(),
                detallePersistido.getId());

        assertProgramHoursWithinLimit(
                idPrograma,
                maximoHoras,
                horasAsignadas.getOrDefault(idPrograma, BigDecimal.ZERO),
                parseHorasDetalle(actividad.horas()));
    }

    private void assertProgramHoursWithinLimit(
            Long idPrograma,
            String maximoHoras,
            BigDecimal horasAsignadas,
            BigDecimal horasNuevas) {
        BigDecimal maximo = parseHorasDetalle(maximoHoras);
        BigDecimal total = horasAsignadas.add(horasNuevas);
        if (total.compareTo(maximo) > 0) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Las horas del programa " + idPrograma
                            + " exceden el máximo permitido de "
                            + maximoHoras
                            + " (asignadas: " + horasAsignadas
                            + ", nuevas: " + horasNuevas + ")");
        }
    }

    private Long parseLongNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isNumber()) {
            return node.longValue();
        }

        if (node.isTextual() && StringUtils.hasText(node.asText())) {
            try {
                return Long.valueOf(node.asText().trim());
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
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
        log.info("updateProfessor ===> Actualizando docente. idCargaDocente={}", idCargaDocente);
        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));
        
        validatePreassignmentWriteAllowedByCarga(entity.getIdCarga());

        cargaDocenteMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());
        entity.setOnceMeses(
                FechasConvocatoriaCalculator.calcularOnceMesesPorSemanas(dto.semanas()));
        applyHorasDeExcepcion(entity);
        cargaDocenteRepository.save(entity);
        log.info("updateProfessor ===> Docente actualizado. idCargaDocente={}", idCargaDocente);
    }

    @Override
    @Transactional
    public void deleteProfessor(Long idCargaDocente) {
        log.info("deleteProfessor ===> Eliminando docente. idCargaDocente={}", idCargaDocente);

        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> {
                    log.warn("deleteProfessor ===> Carga docente no encontrada al eliminar. id={}", idCargaDocente);
                    return new ApiException(HttpStatus.NOT_FOUND,
                            "No existe la carga docente con id " + idCargaDocente);
                });

        validatePreassignmentWriteAllowedByCarga(entity.getIdCarga());

        String registradoPor = RegistradoPorUtils.value(Accion.DELETE);
        cargaDocenteRepository.deleteByProcedure(idCargaDocente, registradoPor);

        // Eliminar su historial de estados
        List<HistorialCargaDocenteEntity> historialCargaDocente = historialCargaDocenteRepository.findByIdCargaDocente(idCargaDocente);
        for (HistorialCargaDocenteEntity registro : historialCargaDocente) {
            historialCargaDocenteRepository.deleteByProcedure(
                    registro.getId(), registradoPor);
        }

        log.info("deleteProfessor ===> Docente eliminado. idCargaDocente={}", idCargaDocente);
    }

    @Override
    public void registerProfessorPreloadHistory(Long idCargaDocente) {
        log.info("registerProfessorPreloadHistory ===> Registrando estado de carga docente. idCargaDocente={}", idCargaDocente);

        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> {
                    log.warn("registerProfessorPreloadHistory ===> Carga docente no encontrada. id={}", idCargaDocente);
                    return new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id" + idCargaDocente);
                });
        
        validatePreassignmentWriteAllowedByCargaDocente(idCargaDocente);

        HistorialCargaDocenteEntity historialCargaDocente = new HistorialCargaDocenteEntity();
        
        historialCargaDocente.setIdCargaDocente(idCargaDocente);
        historialCargaDocente.setEstado(cargaDocente.getEstado());
        historialCargaDocente.setRegistradoPor(
                RegistradoPorUtils.value(Accion.INSERT));
        historialCargaDocente.setFechaCambio(new Date());
        historialCargaDocenteRepository.save(historialCargaDocente);

        log.info("registerProfessorPreloadHistory ===> Estado de carga docente registrado. idCargaDocente={}", idCargaDocente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadDTO> listRegionalUnits(Long idCoordinacion) {
        log.debug("listRegionalUnits ===> Listando unidades regionales. idCoordinacion={}", idCoordinacion);
        List<UnidadDTO> result = unidadMapper.toDtoList(unidadRepository.findRegionalUnits(idCoordinacion));
        log.info("listRegionalUnits ===> Unidades regionales listadas. idCoordinacion={}, total={}", idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaDTO> listProgramsByRegionalUnit(Long idCoordinacion, Long idUnidadRegional, Long idNivelEducativo) {
        log.debug("listProgramsByRegionalUnit ===> Listando programas. idCoordinacion={}, idUnidad={}, idNivel={}", idCoordinacion, idUnidadRegional, idNivelEducativo);
        
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
        log.info("listProgramsByRegionalUnit ===> Programas listados. idCoordinacion={}, total={}", idCoordinacion, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadCriterioDTO> listCriteria(Long idTipoActividad) {
        log.debug("listCriteria ===> Listando criterios. idTipoActividad={}", idTipoActividad);
        
        List<TipoActividadCriterioDTO> result = tipoActividadCriterioMapper.toDtoList(
                tipoActividadesRepository.findCriteriaByParentId(idTipoActividad));
        
        log.info("listCriteria ===> Criterios listados. idTipoActividad={}, total={}", idTipoActividad, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoActividadDTO> listActivityTypes() {
        log.debug("listActivityTypes ===> Listando tipos de actividad");
        
        List<TipoActividadDTO> result = tipoActividadMapper.toDtoList(tipoActividadesRepository.findParentActivityTypes());
        
        log.info("listActivityTypes ===> Tipos de actividad listados. total={}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ActividadModalidadDTO listActivitiesModality(Long idModalidadContratacion) {
        log.debug("listActivitiesModality ===> Listando actividades por modalidad. idModalidad={}", idModalidadContratacion);

        List<ActividadModalidadProjection> projections = restriccionCargaRepository.findActivitiesByModality(idModalidadContratacion);
        ActividadModalidadDTO result = actividadModalidadMapper.toDto(idModalidadContratacion, projections);

        log.info("listActivitiesModality ===> Actividades por modalidad consultadas. idModalidad={}, total={}", idModalidadContratacion, result.tipoActividades().size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listSubjects(Long idPrograma, Long idCoordinacion) {
        log.debug("listSubjects ===> Listando materias. idPrograma={}, idCoordinacion={}", idPrograma, idCoordinacion);
        List<MateriaListadoProjection> materias;

        if (asociacionCoordinacionRepository.existsProgramasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findPensumExcluyendoTransversales(idPrograma);
        } else if (asociacionCoordinacionRepository.existsMateriasByCoordinacion(idCoordinacion)) {
            materias = materiaRepository.findTransversalesByCoordinacionAndPrograma(idCoordinacion, idPrograma);
        } else {
            materias = materiaRepository.findByPrograma(idPrograma);
        }

        List<MateriaDTO> result = mapMateriasConGrupo(materias);
        log.info("listSubjects ===> Materias listadas. idPrograma={}, total={}", idPrograma, result.size());
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
        log.debug("listSubjectGroup ===> Listando grupos. codigoMateria={}, idPeriodo={}", codigoMateria, idPeriodoUniversidad);
        
        List<GrupoDTO> result = grupoMapper.toDtoList(grupoRepository.findByCodigoMateriaAndIdPeriodoUniversidad(codigoMateria, idPeriodoUniversidad));
        
        log.info("listSubjectGroup ===> Grupos listados. codigoMateria={}, total={}",codigoMateria, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> listProjectsProfessor(
            Long idPersonaGeneral,
            Long idConvocatoria) {

        log.debug(
                "listProjectsProfessor ===> Listando proyectos del docente. idPersona={}, idConvocatoria={}",
                idPersonaGeneral,
                idConvocatoria
        );

        List<ProyectoDTO> result =
                proyectoMapper.toDtoList(
                        personaProyectoRepository
                                .findProyectosByIdPersonaGeneralAndConvocatoria(
                                        idPersonaGeneral,
                                        idConvocatoria
                                )
                );

        log.info(
                "listProjectsProfessor ===> Proyectos del docente listados. idPersona={}, idConvocatoria={}, total={}",
                idPersonaGeneral,
                idConvocatoria,
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void saveDetailProfessorPreload(DetalleCargaDocenteFormularioDTO dto) {
        log.info("saveDetailProfessorPreload ===> Guardando detalle precarga docente. idCargaDocente={}, detalles={}",
                dto.idCargaDocente(),
                dto.detalles() != null ? dto.detalles().size() : 0);

        Long idCoordinacion = resolveIdCoordinacionByCargaDocente(dto.idCargaDocente());
        validateSaveDetailProfessorPreload(dto, idCoordinacion);

        validatePreassignmentWriteAllowedByCargaDocente(dto.idCargaDocente());

        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            Long idCentroCosto = resolveIdCentroCosto(detalle, idCoordinacion);
            DetalleCargaDocenteEntity entity = detalleCargaDocenteMapper.toEntity(
                    dto.idCargaDocente(), detalle, idCentroCosto);
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            entity.setFechaCambio(new Date());
            DetalleCargaDocenteEntity saved = detalleCargaDocenteRepository.save(entity);
            saveRelacionesCargaProyecto(saved.getId(), detalle.relacionCargaProyecto());
        }
        log.info("saveDetailProfessorPreload ===> Detalle precarga docente guardado. idCargaDocente={}", dto.idCargaDocente());
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
                proyectoMapper);
        
        log.info("listDetailProfessorPreload ===> Detalle precarga docente listado. idCargaDocente={}, total={}", idCargaDocente, result.size());
        return result;
    }

    @Override
    @Transactional
    public void updateDetailProfessorPreload(DetalleCargaDocenteDTO dto) {
        log.info("updateDetailProfessorPreload ===> Actualizando detalle precarga. idDetalle={}, idCargaDocente={}",
                dto.idDetalleCargaDocente(), dto.idCargaDocente());

        Long idCoordinacion = resolveIdCoordinacionByCargaDocente(dto.idCargaDocente());
        validateUpdateDetailProfessorPreload(dto, idCoordinacion);

        validatePreassignmentWriteAllowedByCargaDocente(dto.idCargaDocente());

        Long idDetalleCargaDocente = dto.idDetalleCargaDocente();
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        DetalleCargaDocenteEntity detallePersistido = detalleCargaDocenteRepository.findById(idDetalleCargaDocente).orElseThrow();
        Long idCentroCosto = resolveIdCentroCostoFromActividad(actividad, idCoordinacion);
        DetalleCargaDocenteEntity entity = detalleCargaDocenteMapper.toEntityFromDto(dto, idCentroCosto);
        entity.setIdTipoActividad(detalleCargaDocenteMapper
                .resolveTipoActividadFromActividad(
                        actividad,
                        detallePersistido.getIdTipoActividad()));
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());
        detalleCargaDocenteRepository.save(entity);

        relacionCargaProyectoRepository.deleteByIdDetalleCargaDocente(
                idDetalleCargaDocente);
        saveRelacionesCargaProyecto(
                idDetalleCargaDocente,
                detalleCargaDocenteMapper.toRelacionesCargaProyecto(
                        actividad.relacionCargaProyecto()));
        log.info("updateDetailProfessorPreload ===> Detalle precarga actualizado. idDetalle={}", idDetalleCargaDocente);
    }

    private void validateSaveDetailProfessorPreload(
            DetalleCargaDocenteFormularioDTO dto,
            Long idCoordinacion) {
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"La carga docente es obligatoria");
        }
        if (!cargaDocenteRepository.existsById(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + dto.idCargaDocente());
        }
        for (DetalleCargaDocenteItemDTO detalle : dto.detalles()) {
            validateDetalleItem(detalle, idCoordinacion);
        }
        validateProgramHourRestrictionOnSave(dto);
    }

    private void validateUpdateDetailProfessorPreload(
            DetalleCargaDocenteDTO dto,
            Long idCoordinacion) {
        if (dto.idDetalleCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del detalle de carga docente es obligatorio");
        }
        if (dto.idCargaDocente() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,  "La carga docente es obligatoria");
        }
        if (dto.detalles() == null || dto.detalles().size() != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La actualizacion requiere exactamente un detalle");
        }
        if (!cargaDocenteRepository.existsById(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + dto.idCargaDocente());
        }

        DetalleCargaDocenteEntity detallePersistido = detalleCargaDocenteRepository
                .findById(dto.idDetalleCargaDocente())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe el detalle de carga docente con id " + dto.idDetalleCargaDocente()));

        if (!detallePersistido.getIdCargaDocente().equals(dto.idCargaDocente())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El detalle no pertenece a la carga docente enviada");
        }

        validateDetalleActividad(
                dto.detalles().get(0),
                detallePersistido.getIdTipoActividad(),
                idCoordinacion);
        validateProgramHourRestrictionOnUpdate(dto, detallePersistido);
    }


    private void validateDetalleActividad(
            DetalleCargaDocenteActividadDTO actividad,
            Long idTipoActividadPersistido,
            Long idCoordinacion) {
        if (actividad.horas() == null || actividad.horas().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        Long idCentroCostoResuelto = resolveIdCentroCostoFromActividad(
                actividad, idCoordinacion);
        if (idCentroCostoResuelto == null) {
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

    private void validateDetalleItem(
            DetalleCargaDocenteItemDTO detalle,
            Long idCoordinacion) {
        if (detalle.horas() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las horas del detalle son obligatorias");
        }
        Long idCentroCostoResuelto = resolveIdCentroCosto(detalle, idCoordinacion);
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

    private Long resolveIdCoordinacionByCargaDocente(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente es obligatoria");
        }
        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));
        if (cargaDocente.getIdCarga() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene carga asociada");
        }
        CargaEntity carga = cargaRepository.findById(cargaDocente.getIdCarga())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga con id " + cargaDocente.getIdCarga()));
        return carga.getIdCoordinacion();
    }

    private Long resolveIdCentroCosto(
            DetalleCargaDocenteItemDTO detalle,
            Long idCoordinacion) {
        Long idCentroEspecial = findIdCentroCostoByCodigoActividadEspecial(
                detalle.codigoTipoActividad());
        if (idCentroEspecial != null) {
            // Prioridad 1: CTEI/ISU desde coordinación con el mismo COOR_CODIGO.
            return idCentroEspecial;
        }
        Long idCentroPrograma = findIdCentroCostoProgramaAsociado(
                idCoordinacion, detalle.idPrograma());
        if (idCentroPrograma != null) {
            // Prioridad 2: centro de costo del programa en ASOCIACIONCOORDINACION.
            return idCentroPrograma;
        }
        if (detalle.materia() != null && detalle.materia().idCentroCosto() != null) {
            // Prioridad 3: centro de costo de la materia (transversales).
            return detalle.materia().idCentroCosto();
        }
        // Prioridad 4: centro de costo enviado en el formulario.
        return detalle.idCentroCosto();
    }

    private Long resolveIdCentroCostoFromActividad(DetalleCargaDocenteActividadDTO actividad, Long idCoordinacion) {
        Long idCentroEspecial = findIdCentroCostoByCodigoActividadEspecial(resolveCodigoTipoActividad(actividad));
        if (idCentroEspecial != null) {
            return idCentroEspecial;
        }
        Long idPrograma = actividad.programa() != null
                ? actividad.programa().id()
                : null;
        Long idCentroPrograma = findIdCentroCostoProgramaAsociado(
                idCoordinacion, idPrograma);
        if (idCentroPrograma != null) {
            return idCentroPrograma;
        }
        // En update no hay materia.idCentroCosto; se usa el del formulario.
        if (actividad.centroCosto() == null) {
            return null;
        }
        return actividad.centroCosto().id();
    }

    private String resolveCodigoTipoActividad(DetalleCargaDocenteActividadDTO actividad) {
        if (actividad.tipoActividadHija() != null) {
            for (TipoActividadDTO hija : actividad.tipoActividadHija()) {
                if (hija != null && StringUtils.hasText(hija.codigo())) {
                    return hija.codigo();
                }
            }
        }
        if (actividad.tipoActividad() != null
                && StringUtils.hasText(actividad.tipoActividad().codigo())) {
            return actividad.tipoActividad().codigo();
        }
        return null;
    }

    private Long findIdCentroCostoByCodigoActividadEspecial(String codigoTipoActividad) {
        if (!StringUtils.hasText(codigoTipoActividad)) {
            return null;
        }
        String codigo = codigoTipoActividad.trim().toUpperCase();
        if (!CODIGOS_CENTRO_COSTO_ESPECIAL.contains(codigo)) {
            return null;
        }
        return asignarCentroCostoRepository
                .findIdCentroCostoByCodigoCoordinacion(codigo)
                .orElse(null);
    }

    private Long findIdCentroCostoProgramaAsociado(Long idCoordinacion, Long idPrograma) {
        if (idCoordinacion == null || idPrograma == null) {
            return null;
        }
        return asociacionCoordinacionRepository
                .findIdCentroCostoByCoordinacionAndPrograma(
                        idCoordinacion, idPrograma)
                .orElse(null);
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
            entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
            entity.setFechaCambio(new Date());
            relacionCargaProyectoRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void saveCareerProfessorPreload(CargaDocentePlantaDTO dto) {
        log.info("saveCareerProfessorPreload ===> Guardando docente planta en precarga. idPersona={}, idCarga={}, idModalidad={}",
                dto != null ? dto.idPersonaGeneral() : null,
                dto != null ? dto.idCarga() : null,
                dto != null ? dto.idModalidadContratacion() : null);
        validateCareerProfessorPreload(dto);

        validatePreassignmentWriteAllowedByCarga(dto.idCarga());

        if (cargaDocenteRepository.existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacion(
                dto.idPersonaGeneral(), dto.idCarga(), dto.idModalidadContratacion())) {
            log.warn("saveCareerProfessorPreload ===> Docente planta duplicado. idPersona={}, idCarga={}",
                    dto.idPersonaGeneral(), dto.idCarga());
            throw new ApiException(HttpStatus.CONFLICT, "El docente ya se encuentra registrado en esta carga y modalidad");
        }

        FechasConvocatoriaEntity fechaConvocatoria =
                convocatoriaRepository.findFechaCnvByConvocatoriaId(
                        dto.idConvocatoria()
                );

        if (fechaConvocatoria == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la fecha general de la convocatoria seleccionada"
            );
        }

        CategoriaModalidadEntity categoriaModalidad = categoriaModalidadRepository
                .findByIdModalidadContratacion(dto.idModalidadContratacion())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe categoria asociada a la modalidad de contratacion"));

        CargaDocenteEntity entity = cargaDocenteMapper.toEntityFromPlanta(dto);
        entity.setIdFechasConvocatoria(fechaConvocatoria.getId());
        entity.setIdCategoriaCatedratico(categoriaModalidad.getIdCategoriaCatedratico());
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        entity.setFechaCambio(new Date());
        entity.setEstado("0");
        entity.setVigente("1");
        applyHorasDeExcepcion(entity);
        Long idNewCargaDocente = cargaDocenteRepository.save(entity).getId();

        // Registrar el estado dentro del historial
        registerProfessorPreloadHistory(idNewCargaDocente);
        
        log.info("saveCareerProfessorPreload ===> Docente planta guardado en precarga. idCargaDocente={}", entity.getId());
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
    @Transactional
    public void deleteProfessorActivity(Long idDetalleCargaDocente) {
        log.info("deleteProfessorActivity ===> Eliminando actividad docente. idDetalle={}",
                idDetalleCargaDocente);

        validatePreassignmentWriteAllowedByDetalle(idDetalleCargaDocente);

        detalleCargaDocenteRepository.deleteByProcedure(
                idDetalleCargaDocente,
                RegistradoPorUtils.value(Accion.DELETE));

        log.info("deleteProfessorActivity ===> Actividad docente eliminada. idDetalle={}", idDetalleCargaDocente);
    }

    @Override
    @Transactional
    public void approveProfessorActivityDistribution(AprobacionDetalleCargaDocenteDTO dto) {
        log.info(
                "approveProfessorActivityDistribution ===> Iniciando aprobación de distribución. idCargaDocente={}",
                dto != null ? dto.idCargaDocente() : null
        );

        validateApproveProfessorActivityDistribution(dto);

        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(dto.idCargaDocente())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + dto.idCargaDocente()
                ));

        validatePreassignmentWriteAllowedByCargaDocente(dto.idCargaDocente());

        if ("1".equals(cargaDocente.getEstado())) {
            log.info(
                    "approveProfessorActivityDistribution ===> La preasignación ya estaba aprobada. idCargaDocente={}",
                    dto.idCargaDocente()
            );
            return;
        }

        List<DetalleCargaDocenteDTO> detallesActualizados =
        dto.detallesActualizados() != null ? dto.detallesActualizados() : List.of();

        List<DetalleCargaDocenteItemDTO> detallesNuevos =
                dto.detallesNuevos() != null ? dto.detallesNuevos() : List.of();

        for (DetalleCargaDocenteDTO detalle : detallesActualizados) {
            updateDetailProfessorPreload(detalle);
        }

        if (!detallesNuevos.isEmpty()) {
            saveDetailProfessorPreload(
                    new DetalleCargaDocenteFormularioDTO(
                            dto.idCargaDocente(),
                            detallesNuevos
                    )
            );
        }

        /*
        * Validamos DESPUÉS de aplicar la distribución,
        * pero dentro de la misma transacción.
        * Si falla, el ApiException hace rollback de lo guardado.
        */
        detalleCargaDocenteRepository.flush();
        relacionCargaProyectoRepository.flush();

        validatePlantaHasCteiOrIsuProject(cargaDocente);

        int updated = cargaDocenteRepository.approvePreassignmentById(
                dto.idCargaDocente(),
                RegistradoPorUtils.value(Accion.UPDATE)
        );


        if (updated == 0) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible aprobar la preasignación del docente"
            );
        }

        // Como ya se aprobo, se actualiza el contexto para mantener la trazabilidad en el historial
        cargaDocente.setEstado("1");
        // Registrar el estado dentro del historial
        registerProfessorPreloadHistory(dto.idCargaDocente());

        log.info(
                "approveProfessorActivityDistribution ===> Distribución aprobada correctamente. idCargaDocente={}",
                dto.idCargaDocente()
        );
    }

    private void validateApproveProfessorActivityDistribution(AprobacionDetalleCargaDocenteDTO dto) {
        if (dto == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La información de aprobación es obligatoria"
            );
        }

        if (dto.idCargaDocente() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente es obligatoria para aprobar la preasignación"
            );
        }
    }

    private boolean isDocentePlanta(CargaDocenteEntity cargaDocente) {
        Long idModalidadContratacion = cargaDocente.getIdModalidadContratacion();

        if (idModalidadContratacion == null) {
            return false;
        }

        return modalidadContratacionRepository.findById(idModalidadContratacion)
                .map(modalidad -> {
                    String nombre = normalizeUpperText(modalidad.getNombre());
                    String sigla = normalizeUpperText(modalidad.getSigla());

                    return "PLANTA".equals(sigla)
                            || "CARRERA".equals(sigla)
                            || "DOCENTE_CARRERA".equals(sigla)
                            || "DOCENTE DE CARRERA".equals(sigla)
                            || nombre.contains("PLANTA")
                            || nombre.contains("CARRERA");
                })
                .orElse(false);
    }

    private String normalizeUpperText(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        return java.text.Normalizer
                .normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();
    }

    @Override
    @Transactional
    public void approveProfessorPreassignment(Long idCargaDocente) {
        log.info("approveProfessorPreassignment ===> Aprobando preasignación docente. idCargaDocente={}",
                idCargaDocente);

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La carga docente es obligatoria para aprobar la preasignación");
        }

        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));

        validatePreassignmentWriteAllowedByCargaDocente(idCargaDocente);

        if ("1".equals(entity.getEstado())) {
            log.info("approveProfessorPreassignment ===> La preasignación ya estaba aprobada. idCargaDocente={}",
                    idCargaDocente);
            return;
        }

        validatePlantaHasCteiOrIsuProject(entity);

        int updated = cargaDocenteRepository.approvePreassignmentById(
                idCargaDocente,
                RegistradoPorUtils.value(Accion.UPDATE)
        );

        if (updated == 0) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible aprobar la preasignación del docente");
        }

        log.info("approveProfessorPreassignment ===> Preasignación aprobada. idCargaDocente={}",
                idCargaDocente);
    }
    
    private void validatePlantaHasCteiOrIsuProject(
            CargaDocenteEntity cargaDocente) {

        if (!isDocentePlanta(cargaDocente)) {
            return;
        }

        int totalProjects = detalleCargaDocenteRepository
                .countCteiOrIsuProjectAssociationsByCargaDocente(
                        cargaDocente.getId()
                );

        if (totalProjects > 0) {
            return;
        }

        if (areCteiAndIsuExpired(cargaDocente)) {
            log.info(
                    "validatePlantaHasCteiOrIsuProject ===> " +
                    "Se permite aprobación sin proyecto porque " +
                    "CTEI e ISU están vencidos. idCargaDocente={}",
                    cargaDocente.getId()
            );
            return;
        }

        throw new ApiException(
                HttpStatus.BAD_REQUEST,
                MENSAJE_PLANTA_SIN_PROYECTO_CTEI_ISU
        );
    }

    private boolean areCteiAndIsuExpired(
                CargaDocenteEntity cargaDocente) {

            Long idCarga = cargaDocente.getIdCarga();

            if (idCarga == null) {
                return false;
            }

            CargaEntity carga = cargaRepository.findById(idCarga)
                    .orElse(null);

            if (carga == null || carga.getIdConvocatoria() == null) {
                return false;
            }

            List<FechasConvocatoriaEntity> fechas =
                    convocatoriaRepository
                            .findFechasGeneralesByConvocatoriaId(
                                    carga.getIdConvocatoria()
                            );

            boolean cteiExpired =
                    isGeneralDateExpired(fechas, "CTEI");

            boolean isuExpired =
                    isGeneralDateExpired(fechas, "ISU");

            return cteiExpired && isuExpired;
        }

        private boolean isGeneralDateExpired(
            List<FechasConvocatoriaEntity> fechas,
            String codigo) {

        if (fechas == null || fechas.isEmpty()) {
            return false;
        }

        return fechas.stream()
                .filter(fecha ->
                        codigo.equals(
                                normalizeUpperText(fecha.getCodigo())
                        )
                )
                .map(FechasConvocatoriaEntity::getFechaFin)
                .filter(Objects::nonNull)
                .anyMatch(
                        FechasConvocatoriaCalculator::isVencida
                );
    }

    @Override
    @Transactional(readOnly = true)
    public TotalPreasignacionDTO getTotalPreload(Long idCarga) {
        log.debug("getTotalPreload ===> Obteniendo total de preasignacion. idCarga={}", idCarga);
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        if (!cargaRepository.existsById(idCarga)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga);
        }

        List<Object[]> totalesRows = cargaDocenteRepository.findTotalPreasignacionByCargaId(idCarga);
        Object[] totales = extractTotalRow(totalesRows);

        List<TotalHorasPreasignacionDTO> horasPorTipo =totalPreasignacionMapper.toHorasDtoList(detalleCargaDocenteRepository.findTotalHorasPreasignacionByCargaId(idCarga));

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

        log.info("getTotalPreload ===> Total de preasignacion obtenido. idCarga={}, totalDocentes={}, totalPreasignacion={}", idCarga, result.totalDocentes(), result.totalPreasignacion());
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
            log.warn("toBigDecimalValue ===> Valor numérico inválido en total preasignación: {}", row[index]);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<CoordinacionBusquedaDTO> searchCoordination(String nombre) {
        log.debug("searchCoordination ===> Buscando coordinación. nombre={}", nombre);
        String param = normalizeParam(nombre);
        if(param == null || param.length() < 2) {
            log.debug("searchCoordination ===> Búsqueda de coordinación sin criterios válidos");
            return Collections.emptyList();
        }

        List<CoordinacionBusquedaDTO> result = coordinacionMapper.toBusquedaDtoList(coordinacionRepository.searchCoordination(param));
        log.info("searchCoordination ===> Coordinaciones encontradas. total={}", result.size());
        return result;
    }

    @Override
    public List<CoordinacionBusquedaDTO> searchCoordinationForRestriction(String nombre, Long idConvocatoria) {
        log.debug(
                "searchCoordinationForRestriction ===> Buscando coordinación disponible para restricción. nombre={}, idConvocatoria={}",
                nombre,
                idConvocatoria
        );

        String param = normalizeParam(nombre);

        if (param == null || param.length() < 2) {
            log.debug("searchCoordinationForRestriction ===> Búsqueda sin criterios válidos");
            return Collections.emptyList();
        }

        if (idConvocatoria == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La convocatoria es obligatoria para buscar coordinaciones disponibles"
            );
        }

        List<CoordinacionBusquedaDTO> result = coordinacionMapper.toBusquedaDtoList(
                coordinacionRepository.searchCoordinationAvailableForRestriction(
                        param,
                        idConvocatoria
                )
        );

        log.info(
                "searchCoordinationForRestriction ===> Coordinaciones disponibles encontradas. idConvocatoria={}, total={}",
                idConvocatoria,
                result.size()
        );

        return result;
    }

    @Override
    @Transactional
    public void saveCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto) {
        log.info(
                "saveCoordinationRestriction ===> Guardando restricción. idCoordinacion={}, idFechasConvocatoria={}",
                dto != null ? dto.idCoordinacion() : null,
                dto != null ? dto.idFechasConvocatoria() : null
        );

        validateCoordinationRestriction(dto);

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la coordinacion con id " + dto.idCoordinacion()
            );
        }

        if (!fechasConvocatoriaRepository.existsById(dto.idFechasConvocatoria())) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe la fecha de convocatoria con id " + dto.idFechasConvocatoria()
            );
        }

        Long idConvocatoria = resolveConvocatoriaIdFromFecha(dto.idFechasConvocatoria());

        validateCoordinationAvailableForRestriction(
                dto.idCoordinacion(),
                idConvocatoria,
                null
        );

        ensureCargaForRestriction(
                dto.idCoordinacion(),
                idConvocatoria
        );

        RestriccionPorCoordinacionEntity restriccion =
                restriccionPorCoordinacionMapper.toEntity(dto);

        restriccion.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        restriccion.setFechaCambio(new Date());

        restriccionPorCoordinacionRepository.save(restriccion);

        convocatoriaEstadoService.syncEstadoConvocatoriaByFecha(
                dto.idFechasConvocatoria()
        );

        log.info(
                "saveCoordinationRestriction ===> Restricción guardada. id={}, idCoordinacion={}, idConvocatoria={}",
                restriccion.getId(),
                dto.idCoordinacion(),
                idConvocatoria
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionRestriccionDTO> listCoordinationRestriction(Long idConvocatoria) {
        log.debug("listCoordinationRestriction ===> Listando restricciones. idConvocatoria={}", idConvocatoria);
        
        List<CoordinacionRestriccionDTO> result = restriccionPorCoordinacionMapper.toDtoList(restriccionPorCoordinacionRepository.findAllWithCoordinacion(idConvocatoria));
        log.info("listCoordinationRestriction ===> Restricciones listadas. idConvocatoria={}, total={}", idConvocatoria, result.size());
        return result;
    }

    private Long resolveConvocatoriaIdFromFecha(Long idFechasConvocatoria) {
        return restriccionPorCoordinacionRepository
                .findConvocatoriaIdByFechaId(idFechasConvocatoria)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "No fue posible identificar la convocatoria asociada a la fecha seleccionada"
                ));
    }

    private void validateCoordinationAvailableForRestriction(
            Long idCoordinacion,
            Long idConvocatoria,
            Long idRestriccionActual) {
        Long totalAsignadasOtraConvocatoria =
                cargaRepository.countAssignedToAnotherPreloadCall(
                        idCoordinacion,
                        idConvocatoria
                );

        if (totalAsignadasOtraConvocatoria != null
                && totalAsignadasOtraConvocatoria > 0) {
            log.warn(
                    "validateCoordinationAvailableForRestriction ===> Coordinación asociada a otra convocatoria. idCoordinacion={}, idConvocatoria={}",
                    idCoordinacion,
                    idConvocatoria
            );

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La coordinación ya se encuentra asociada a otra convocatoria del mismo periodo"
            );
        }

        Long totalRestricciones = idRestriccionActual == null
                ? restriccionPorCoordinacionRepository
                        .countByConvocatoriaAndCoordinacion(
                                idConvocatoria,
                                idCoordinacion
                        )
                : restriccionPorCoordinacionRepository
                        .countByConvocatoriaAndCoordinacionAndIdNot(
                                idConvocatoria,
                                idCoordinacion,
                                idRestriccionActual
                        );

        if (totalRestricciones != null && totalRestricciones > 0) {
            log.warn(
                    "validateCoordinationAvailableForRestriction ===> Restricción duplicada en convocatoria. idCoordinacion={}, idConvocatoria={}",
                    idCoordinacion,
                    idConvocatoria
            );

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La coordinación ya tiene una restricción registrada en esta convocatoria"
            );
        }
    }

    private void ensureCargaForRestriction(Long idCoordinacion, Long idConvocatoria) {
        CargaEntity carga = cargaRepository
                .findFirstByIdCoordinacionAndIdConvocatoria(idCoordinacion, idConvocatoria)
                .orElseGet(CargaEntity::new);

        Long totalAsignadasOtraConvocatoria =
                cargaRepository.countAssignedToAnotherPreloadCall(
                        idCoordinacion,
                        idConvocatoria
                );
        if (totalAsignadasOtraConvocatoria != null
                && totalAsignadasOtraConvocatoria > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "La coordinación ya se encuentra asociada a otra convocatoria del mismo periodo"
            );
        }

        carga.setIdCoordinacion(idCoordinacion);
        carga.setIdConvocatoria(idConvocatoria);

        if (carga.getIdEstadoCarga() == null) {
            carga.setIdEstadoCarga(resolveEstadoCargaInicialId());
        }

        carga.setRegistradoPor(RegistradoPorUtils.value(
                carga.getId() == null ? Accion.INSERT : Accion.UPDATE));
        carga.setFechaCambio(new Date());

        cargaRepository.save(carga);

        log.info(
                "ensureCargaForRestriction ===> Carga asociada para restricción. idCarga={}, idCoordinacion={}, idConvocatoria={}",
                carga.getId(),
                idCoordinacion,
                idConvocatoria
        );
    }

    @Override
    @Transactional
    public void updateCoordinationRestriction(Long id, CoordinacionRestriccionFormularioDTO dto) {
        log.info("updateCoordinationRestriction ===> Actualizando restricción id={}", id);
        validateCoordinationRestriction(dto);

        RestriccionPorCoordinacionEntity entity = restriccionPorCoordinacionRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la restriccion con id " + id));
        
        Long oldIdFechasConvocatoria = entity.getIdFechasConvocatoria();

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la coordinacion con id " + dto.idCoordinacion());
        }
        if (!fechasConvocatoriaRepository.existsById(dto.idFechasConvocatoria())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la fecha de convocatoria con id " + dto.idFechasConvocatoria());
        }

        Long idConvocatoria = resolveConvocatoriaIdFromFecha(dto.idFechasConvocatoria());

        validateCoordinationAvailableForRestriction(
                dto.idCoordinacion(),
                idConvocatoria,
                id
        );

        ensureCargaForRestriction(
                dto.idCoordinacion(),
                idConvocatoria
        );

        restriccionPorCoordinacionMapper.updateEntity(dto, entity);
        entity.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        entity.setFechaCambio(new Date());
        restriccionPorCoordinacionRepository.save(entity);

        convocatoriaEstadoService.syncEstadoConvocatoriaByFecha(oldIdFechasConvocatoria);
        convocatoriaEstadoService.syncEstadoConvocatoriaByFecha(dto.idFechasConvocatoria());

        log.info("updateCoordinationRestriction ===> Restricción actualizada. id={}", id);
    }

    @Override
    @Transactional
    public void deleteCoordinationRestriction(Long id, CoordinacionRestriccionDTO dto) {
        log.info("deleteCoordinationRestriction ===> Eliminando restricción id={}", id);
        if (dto == null || dto.id() == null || !dto.id().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la restriccion no coincide");
        }
        if (!restriccionPorCoordinacionRepository.existsById(id)) {
            log.warn("deleteCoordinationRestriction ===> Restricción no encontrada al eliminar. id={}", id);
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la restriccion con id " + id);
        }

        Long idConvocatoria = restriccionPorCoordinacionRepository
                .findConvocatoriaIdByRestrictionId(id)
                .orElse(null);

        restriccionPorCoordinacionRepository.deleteById(id);

        convocatoriaEstadoService.syncEstadoConvocatoria(idConvocatoria);

        log.info("deleteCoordinationRestriction ===> Restricción eliminada. id={}", id);
    }

    @Override
    @Transactional
    public void bulkDeleteCoordinationRestriction(List<CoordinacionRestriccionDTO> restricciones) {
        log.info("bulkDeleteCoordinationRestriction ===> Eliminación masiva de restricciones. total={}",
                restricciones != null ? restricciones.size() : 0);
        if (restricciones == null || restricciones.isEmpty()) {
            return;
        }
        for (CoordinacionRestriccionDTO restriccion : restricciones) {
            deleteCoordinationRestriction(restriccion.id(), restriccion);
        }
        log.info("bulkDeleteCoordinationRestriction ===> Eliminación masiva de restricciones finalizada");
    }

    private void validateCoordinationRestriction(CoordinacionRestriccionFormularioDTO dto) {
        if (dto == null
                || dto.idCoordinacion() == null
                || dto.idFechasConvocatoria() == null
                || dto.fechaInicio() == null
                || dto.fechaFin() == null
                || !StringUtils.hasText(dto.estado())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La coordinacion, fecha de convocatoria, fechas y estado son obligatorios");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ValorContratacionDTO getContractValue(Long idCargaDocente) {
        log.debug("getContractValue ===> idCargaDocente={}", idCargaDocente);
        
        CargaDocenteEntity cargaDocente = findCargaDocenteOrThrow(idCargaDocente);
        BigDecimal asignacionSalarial = resolveAsignacionSalarialContrato(cargaDocente);
        long cantidadDias = resolveCantidadDiasContrato(cargaDocente);
        BigDecimal dias = BigDecimal.valueOf(cantidadDias);

        BigDecimal valorContrato = asignacionSalarial
                .divide(DIAS_MES, 8, RoundingMode.HALF_UP)
                .multiply(dias)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorCesantias = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_ANIO, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorIntereses = valorCesantias
                .multiply(dias)
                .divide(DIAS_ANIO, 8, RoundingMode.HALF_UP)
                .multiply(TASA_INTERES)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorPrimaLegal = valorCesantias;
        BigDecimal valorVacaciones = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_VACACIONES, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalPrestaciones = valorCesantias
                .add(valorIntereses)
                .add(valorPrimaLegal)
                .add(valorVacaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalContrato = valorContrato
                .add(totalPrestaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        log.info("getContractValue ===> id={}, dias={}, totalContrato={}",
                idCargaDocente, cantidadDias, totalContrato);
        return new ValorContratacionDTO(
                valorVacaciones,
                valorCesantias,
                valorIntereses,
                valorPrimaLegal,
                totalPrestaciones,
                valorContrato,
                totalContrato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadHorasResumenDTO> listActivityHours(Long idCargaDocente) {
        log.debug("listActivityHours ===> idCargaDocente={}", idCargaDocente);
        validateCargaDocenteExists(idCargaDocente);

        Map<Long, DetalleCargaDocenteListadoProjection> unicos =
                loadDetallesUnicosByCargaDocente(idCargaDocente);

        Map<String, ActividadHorasAcumulado> acumulados = new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection detalle : unicos.values()) {
            String codigo = resolveCodigoActividad(detalle);
            String nombre = resolveNombreActividad(detalle);
            String tipo = resolveTipoActividad(detalle);
            String clave = codigo + "|" + nombre;
            ActividadHorasAcumulado actual = acumulados.computeIfAbsent(
                    clave,
                    k -> new ActividadHorasAcumulado(tipo, codigo, nombre));
            BigDecimal horas = parseHorasDetalle(detalle.getHoras());
            actual.totalHoras = actual.totalHoras.add(horas);
            if (isActividadDirecta(detalle)) {
                actual.detalles.add(toActividadDirectaDetalle(detalle, horas));
            }
        }

        List<ActividadHorasResumenDTO> resultado = new ArrayList<>();
        for (ActividadHorasAcumulado item : acumulados.values()) {
            resultado.add(new ActividadHorasResumenDTO(
                    item.tipo,
                    item.codigo,
                    item.nombre,
                    item.totalHoras,
                    item.detalles.isEmpty() ? null : List.copyOf(item.detalles)));
        }
        log.info("listActivityHours ===> id={}, totalTipos={}", idCargaDocente, resultado.size());
        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCostoResumenDTO> listCostCenters(Long idCargaDocente) {
        log.debug("listCostCenters ===> idCargaDocente={}", idCargaDocente);
        ValorContratacionDTO valor = getContractValue(idCargaDocente);
        return buildCostCenters(idCargaDocente, valor.totalContrato());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObservacionCargaDTO> listPreloadObservations(Long idCarga) {
        log.debug("listPreloadObservations ===> idCarga={}", idCarga);

        List<ObservacionCargaDTO> result = observacionesCargaMapper.toDtoList(observacionCargaRepository.findAllWithPreload(idCarga));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenCargaDocenteDTO getProfessorLoadSummary(Long idCargaDocente) {
        log.debug(
                "getProfessorLoadSummary ===> idCargaDocente={}",
                idCargaDocente
        );

        CargaDocenteEntity cargaDocente =
                findCargaDocenteOrThrow(idCargaDocente);

        boolean docentePlanta = isDocentePlanta(cargaDocente);

        ValorContratacionDTO valorContratacion =
                docentePlanta
                        ? null
                        : getContractValue(idCargaDocente);

        List<ActividadHorasResumenDTO> horasActividades =
                listActivityHours(idCargaDocente);

        BigDecimal totalContrato =
                valorContratacion != null
                        ? valorContratacion.totalContrato()
                        : BigDecimal.ZERO;

        List<CentroCostoResumenDTO> centrosCosto =
                buildCostCenters(
                        idCargaDocente,
                        totalContrato
                );

        log.info(
                "getProfessorLoadSummary ===> id={}, planta={}, actividades={}, centros={}",
                idCargaDocente,
                docentePlanta,
                horasActividades.size(),
                centrosCosto.size()
        );

        return new ResumenCargaDocenteDTO(
                idCargaDocente,
                valorContratacion,
                horasActividades,
                centrosCosto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestriccionProgramaHorasDTO> listProgramHourRestrictions(
            Long idModalidadContratacion,
            Long idCargaDocente) {
        log.debug(
                "listProgramHourRestrictions ===> idModalidad={}, idCargaDocente={}",
                idModalidadContratacion,
                idCargaDocente);

        if (idModalidadContratacion == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La modalidad de contratación es obligatoria");
        }
        if (!modalidadContratacionRepository.existsById(idModalidadContratacion)) {
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "No existe la modalidad de contratación");
        }

        Map<Long, String> maximosPorPrograma =
                resolveMaximosHorasPrograma(idModalidadContratacion);
        if (maximosPorPrograma.isEmpty()) {
            return List.of();
        }

        Map<Long, BigDecimal> horasAsignadasPorPrograma = Map.of();
        if (idCargaDocente != null) {
            if (!cargaDocenteRepository.existsById(idCargaDocente)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente);
            }
            horasAsignadasPorPrograma = loadHorasAsignadasPorPrograma(
                    idCargaDocente,
                    null);
        }

        List<RestriccionProgramaHorasDTO> result = new ArrayList<>();
        for (Map.Entry<Long, String> entry : maximosPorPrograma.entrySet()) {
            BigDecimal horasAsignadas = horasAsignadasPorPrograma.getOrDefault(
                    entry.getKey(),
                    BigDecimal.ZERO);
            BigDecimal maximo = parseHorasDetalle(entry.getValue());
            BigDecimal horasDisponibles = maximo.subtract(horasAsignadas);
            if (horasDisponibles.compareTo(BigDecimal.ZERO) < 0) {
                horasDisponibles = BigDecimal.ZERO;
            }
            result.add(new RestriccionProgramaHorasDTO(
                    entry.getKey(),
                    entry.getValue(),
                    horasAsignadas,
                    horasDisponibles));
        }

        log.info(
                "listProgramHourRestrictions ===> idModalidad={}, total={}",
                idModalidadContratacion,
                result.size());
        return result;
    }

    @Override
    @Transactional
    public void endorsePreloadDean(Long idCarga) {
        log.info("updateCarga ===> Actualizando estado carga. idCarga={}", idCarga);
        CargaEntity entity = cargaRepository.findById(idCarga).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga));
        
        validatePreassignmentWriteAllowed(entity);
        Long idEstadoInscrito = resolveEstadoCargaInscritaId();

        entity.setIdEstadoCarga(idEstadoInscrito);
        cargaRepository.save(entity);
        log.info("updateCarga ===> Estado carga actualizado. idCarga={}", idCarga);
    }

    @Override
    @Transactional
    public void declinePreloadDean(Long idCarga, ObservacionDecanoDTO dto) {
        log.info("updateCarga ===> Actualizando estado carga. idCarga={}", idCarga);
        CargaEntity carga = cargaRepository.findById(idCarga).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga));
        
        validatePreassignmentWriteAllowed(carga);
        Long idEstadoRegistrado = resolveEstadoCargaInicialId();

        carga.setIdEstadoCarga(idEstadoRegistrado);
        cargaRepository.save(carga);

        // Trazabilidad observaciones
        ObservacionCargaEntity observacionCarga = new ObservacionCargaEntity();

        observacionCarga.setIdCarga(idCarga);
        observacionCarga.setIdPersonaGeneralRegistra(dto.idPersonaGeneral());
        observacionCarga.setRolPersonaGeneralRegistra(ROL_DECANO);
        observacionCarga.setTexto(dto.observacion());
        observacionCarga.setFecha(new Date());
        observacionCarga.setRegistradoPor(REGISTRADO_POR);
        observacionCarga.setFechaCambio(new Date());
        observacionCargaRepository.save(observacionCarga);

        log.info("updateCarga ===> Estado carga actualizado. idCarga={}", idCarga);
    }

    private List<CentroCostoResumenDTO> buildCostCenters(Long idCargaDocente, BigDecimal totalContrato) {
        Map<Long, DetalleCargaDocenteListadoProjection> unicos = loadDetallesUnicosByCargaDocente(idCargaDocente);

        Map<Long, CentroCostoAcumulado> acumulados = new LinkedHashMap<>();
        BigDecimal totalHoras = BigDecimal.ZERO;
        for (DetalleCargaDocenteListadoProjection detalle : unicos.values()) {
            BigDecimal horas = parseHorasDetalle(detalle.getHoras());
            totalHoras = totalHoras.add(horas);
            Long idCentro = detalle.getIdCentroCosto();
            if (idCentro == null) {
                continue;
            }
            CentroCostoAcumulado actual = acumulados.computeIfAbsent(
                    idCentro,
                    k -> new CentroCostoAcumulado(
                            idCentro, resolveNombreCentroCosto(detalle)));
            actual.numeroActividades++;
            actual.totalHoras = actual.totalHoras.add(horas);
        }

        BigDecimal contrato = totalContrato != null ? totalContrato : BigDecimal.ZERO;
        List<CentroCostoResumenDTO> resultado = new ArrayList<>();
        for (CentroCostoAcumulado item : acumulados.values()) {
            BigDecimal porcentaje = calcularPorcentajeHoras(item.totalHoras, totalHoras);
            BigDecimal valorAsignado = contrato
                    .multiply(porcentaje)
                    .divide(CIEN, ESCALA_MONETARIA, RoundingMode.HALF_UP);
            resultado.add(new CentroCostoResumenDTO(
                    item.idCentroCosto,
                    item.nombre,
                    item.numeroActividades,
                    item.totalHoras,
                    porcentaje,
                    valorAsignado));
        }
        return resultado;
    }

    private Map<Long, DetalleCargaDocenteListadoProjection>loadDetallesUnicosByCargaDocente(Long idCargaDocente) {
        List<DetalleCargaDocenteListadoProjection> detalles = detalleCargaDocenteRepository.findByIdCargaDocente(idCargaDocente);
        Map<Long, DetalleCargaDocenteListadoProjection> unicos = new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection detalle : detalles) {
            unicos.putIfAbsent(detalle.getIdDetalleCargaDocente(), detalle);
        }
        return unicos;
    }

    private CargaDocenteEntity findCargaDocenteOrThrow(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de carga docente es obligatorio");
        }
        return cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));
    }

    private void validateCargaDocenteExists(Long idCargaDocente) {
        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de carga docente es obligatorio");
        }
        if (!cargaDocenteRepository.existsById(idCargaDocente)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente);
        }
    }

    private BigDecimal resolveAsignacionSalarialContrato(CargaDocenteEntity cargaDocente) {
        if (cargaDocente.getSalario() != null) {
            return cargaDocente.getSalario();
        }
        BigDecimal valorPunto = cargaDocente.getValorPunto();
        if (valorPunto == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente no tiene asignacion salarial ni valor del punto");
        }
        BigDecimal puntos = resolvePuntosDocenteContrato(cargaDocente);
        return puntos.multiply(valorPunto)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    private BigDecimal resolvePuntosDocenteContrato(
            CargaDocenteEntity cargaDocente) {
        if (!StringUtils.hasText(cargaDocente.getPuntos())) {
            return PUNTOS_DOCENTE_DEFAULT;
        }
        try {
            return new BigDecimal(cargaDocente.getPuntos().trim());
        } catch (NumberFormatException ex) {
            return PUNTOS_DOCENTE_DEFAULT;
        }
    }

    private long resolveCantidadDiasContrato(CargaDocenteEntity cargaDocente) {
        LocalDate fechaInicio = cargaDocente.getFechaInicio();
        LocalDate fechaFin = cargaDocente.getFechaFin();
        if (fechaInicio == null || fechaFin == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente no tiene fechas de inicio y fin");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La fecha fin no puede ser anterior a la fecha inicio");
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    private String resolveCodigoActividad(DetalleCargaDocenteListadoProjection detalle) {
        if (StringUtils.hasText(detalle.getCodigoTipoActividad())) {
            return detalle.getCodigoTipoActividad();
        }
        return detalle.getCodigoTipoActividadPadre();
    }

    private boolean isActividadDirecta(DetalleCargaDocenteListadoProjection detalle) {
        if (CODIGO_ACTIVIDAD_DIRECTA.equalsIgnoreCase(detalle.getCodigoTipoActividad())
                || CODIGO_ACTIVIDAD_DIRECTA.equalsIgnoreCase(
                        detalle.getCodigoTipoActividadPadre())) {
            return true;
        }
        String tipo = resolveTipoActividad(detalle);
        String nombre = resolveNombreActividad(detalle);
        return containsDirecta(tipo) || containsDirecta(nombre);
    }

    private boolean containsDirecta(String valor) {
        return StringUtils.hasText(valor)
                && valor.toLowerCase().contains("directa");
    }

    private ActividadDirectaDetalleDTO toActividadDirectaDetalle(
            DetalleCargaDocenteListadoProjection detalle,
            BigDecimal horas) {
        return new ActividadDirectaDetalleDTO(
                detalle.getNombreUnidadRegional(),
                detalle.getNombrePrograma(),
                detalle.getNombreMateria(),
                detalle.getNombreGrupo(),
                horas);
    }

    private String resolveNombreActividad(DetalleCargaDocenteListadoProjection detalle) {
        if (StringUtils.hasText(detalle.getNombreTipoActividad())) {
            return detalle.getNombreTipoActividad();
        }
        return detalle.getNombreTipoActividadPadre();
    }

    private String resolveTipoActividad(DetalleCargaDocenteListadoProjection detalle) {
        if (StringUtils.hasText(detalle.getNombreTipoActividadPadre())) {
            return detalle.getNombreTipoActividadPadre();
        }
        return detalle.getNombreTipoActividad();
    }

    private String resolveNombreCentroCosto(DetalleCargaDocenteListadoProjection detalle) {
        if (StringUtils.hasText(detalle.getDescripcionCentroCosto())) {
            return detalle.getDescripcionCentroCosto();
        }
        return "Sin nombre";
    }

    private BigDecimal calcularPorcentajeHoras(
            BigDecimal horasCentro,
            BigDecimal totalHoras) {
        if (totalHoras.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(ESCALA_PORCENTAJE);
        }
        return horasCentro
                .multiply(CIEN)
                .divide(totalHoras, ESCALA_PORCENTAJE, RoundingMode.HALF_UP);
    }

    private BigDecimal parseHorasDetalle(String horas) {
        if (!StringUtils.hasText(horas)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(horas.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private static final class ActividadHorasAcumulado {
        private final String tipo;
        private final String codigo;
        private final String nombre;
        private BigDecimal totalHoras = BigDecimal.ZERO;
        private final List<ActividadDirectaDetalleDTO> detalles =
                new ArrayList<>();

        private ActividadHorasAcumulado(
                String tipo, String codigo, String nombre) {
            this.tipo = tipo;
            this.codigo = codigo;
            this.nombre = nombre;
        }
    }

    private static final class CentroCostoAcumulado {
        private final Long idCentroCosto;
        private final String nombre;
        private long numeroActividades;
        private BigDecimal totalHoras = BigDecimal.ZERO;

        private CentroCostoAcumulado(Long idCentroCosto, String nombre) {
            this.idCentroCosto = idCentroCosto;
            this.nombre = nombre;
        }
    }

}
