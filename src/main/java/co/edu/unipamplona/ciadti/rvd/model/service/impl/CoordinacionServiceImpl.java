package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.CargaDocenteMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CategoriaCatedraticoMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.CoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocenteCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocentePlantaCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.DocentePreasignacionMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.FechasConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.mapper.RelacionConvocatoriaCoordinacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CategoriaCatedraticoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EscalafonRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocentesPlantaCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadoCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FechasConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosCategoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PuntosVigenciaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
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
    private final EstadoCargaRepository estadoCargaRepository;
    private final DocentesPlantaCoordinacionRepository docentesPlantaCoordinacionRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final FechasConvocatoriaRepository fechasConvocatoriaRepository;
    private final PuntosVigenciaRepository puntosVigenciaRepository;
    private final PuntosCategoriaRepository puntosCategoriaRepository;
    private final CategoriaCatedraticoRepository categoriaCatedraticoRepository;
    private final EscalafonRepository escalafonRepository;
    private final CoordinacionMapper coordinacionMapper;
    private final RelacionConvocatoriaCoordinacionMapper relacionMapper;
    private final DocentePlantaCoordinacionMapper docentePlantaCoordinacionMapper;
    private final DocentePreasignacionMapper docentePreasignacionMapper;
    private final FechasConvocatoriaMapper fechasConvocatoriaMapper;
    private final CategoriaCatedraticoMapper categoriaCatedraticoMapper;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final CargaDocenteMapper cargaDocenteMapper;
    private final DocenteCoordinacionMapper docenteCoordinacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CoordinacionDTO> findCoordinationsByIdConvocatoria(Long idConvocatoria, Long idUsuario) {
        List<CoordinacionListadoProjection> projections = idConvocatoria == null ? coordinacionRepository.findWithoutCarga(idUsuario) : coordinacionRepository.findByConvocatoriaWithCarga(idConvocatoria, idUsuario);
        return coordinacionMapper.toDtoList(projections);
    }

    @Override
    @Transactional
    public void savePreload(RelacionConvocatoriaCoordinacionDTO dto) {

        CargaEntity carga = relacionMapper.toEntity(dto);
        carga.setIdEstadoCarga(resolveEstadoCargaInicialId());
        carga.setRegistradoPor(REGISTRADO_POR);
        carga.setFechaCambio(new Date());
        cargaRepository.save(carga);
    }

    private Long resolveEstadoCargaInicialId() {
        return estadoCargaRepository.findByNombre(ESTADO_CARGA_INICIAL)
                .map(estado -> estado.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"No existe el estado de carga inicial: " + ESTADO_CARGA_INICIAL));
    }

    private void validateSavePreload(RelacionConvocatoriaCoordinacionDTO dto) {
        if (dto.idCoordinacion() == null || dto.idConvocatoria() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"La coordinacion y la convocatoria son obligatorias");
        }

        if (!coordinacionRepository.existsById(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.NOT_FOUND,"Coordinacion no encontrada");
        }

        if (cargaRepository.existsByIdCoordinacion(dto.idCoordinacion())) {
            throw new ApiException(HttpStatus.CONFLICT,"La coordinacion ya tiene una carga asignada");
        }

        if (cargaRepository.existsByIdCoordinacionAndIdConvocatoria(dto.idCoordinacion(), dto.idConvocatoria())) {
            throw new ApiException(HttpStatus.CONFLICT,"La coordinacion ya tiene carga en esta convocatoria");
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
        return docenteCoordinacionMapper.toDtoList(cargaDocenteRepository.findProfessorsByCoordinationAndModality(idCoordinacion, idModalidadContratacion));
    }

    @Override
    @Transactional
    public void updateProfessor(Long idCargaDocente, CargaDocenteFormularioDTO dto) {
        CargaDocenteEntity entity = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga docente con id " + idCargaDocente));
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
}
