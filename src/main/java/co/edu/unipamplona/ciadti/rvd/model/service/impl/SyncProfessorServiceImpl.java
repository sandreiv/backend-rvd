/**
 * Aplicación: rvd
 * Archivo: SyncProfessorServiceImpl.java
 * Paquete: Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.mapper.PlanTrabajoDigitalMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdStatesProfessorsDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ClaseSemanalEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteClaseSemanalEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteGrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteUnidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.MateriaDocenteUnidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorLaborEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ClaseSemanalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocenteClaseSemanalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocenteGrupoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocenteUnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.MateriaDocenteUnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PlanTrabajoDigitalRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorLaborRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleDirectoEfectivoSincronizacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteEfectivoSincronizacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.SyncProfessorService;
import co.edu.unipamplona.ciadti.rvd.util.OnceMesesReporteFilter;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncProfessorServiceImpl implements SyncProfessorService {

    private static final String INDICADOR_ESTADO_VERDADERO = "1";
    private static final String INDICADOR_ESTADO_FALSO = "0";
    private static final String ESTADO_PTD_ACEPTADO = "1";
    private static final String ESTADO_APROBADO_DESARROLLO = "4";
    private static final String RESPONSABLE_TITULAR = "TITULAR";
    private static final String RESPONSABLE_AUXILIAR = "AUXILIAR";
    private static final String SI = "1";

    private final CargaDocenteRepository cargaDocenteRepository;
    private final CargaRepository cargaRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PlanTrabajoDigitalRepository planTrabajoDigitalRepository;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final TrabajadorLaborRepository trabajadorLaborRepository;
    private final DocenteUnidadRepository docenteUnidadRepository;
    private final DocenteGrupoRepository docenteGrupoRepository;
    private final MateriaDocenteUnidadRepository materiaDocenteUnidadRepository;
    private final ClaseSemanalRepository claseSemanalRepository;
    private final DocenteClaseSemanalRepository docenteClaseSemanalRepository;

    private final PlanTrabajoDigitalMapper planTrabajoDigitalMapper;
    
    @Override
    @Transactional
    public int syncProfessors(Long idCarga, List<Long> idsCargaDocente) {
        log.info("syncProfessors ===> Sincronizando docentes. idCarga={}", idCarga);

        validarDatosBasicos(idCarga, idsCargaDocente);

        CargaEntity carga = cargaRepository.findById(idCarga)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe la carga con id " + idCarga));
        
        List<DocenteEfectivoSincronizacionProjection> cargaDocenteProjections = cargaDocenteRepository.findSyncEffectiveProfessorsByCargaDocenteIds(idsCargaDocente);
        Map<Long, List<DetalleDirectoEfectivoSincronizacionProjection>> detalleProjectionsPorDocente = obtenerDetallesPorDocente(
            cargaDocenteProjections
                .stream()
                .map(DocenteEfectivoSincronizacionProjection::getIdCargaDocente)
                .toList()
        );

        validarProfesoresCumplanFiltros(carga, cargaDocenteProjections, detalleProjectionsPorDocente);

        int sincronizados = registrarProcesoSincronizacion(cargaDocenteProjections, detalleProjectionsPorDocente);

        log.info("syncProfessors ===> Docentes sincronizados correctamente. idCarga={}", idCarga);
        return sincronizados;
    }


    
    private int registrarProcesoSincronizacion(List<DocenteEfectivoSincronizacionProjection> docentes, Map<Long, List<DetalleDirectoEfectivoSincronizacionProjection>> detallesPorDocente) {
        Date fechaActual = new Date();
        AuthUserDetails usuario = requireUsuario();
        String registradoPor = formatRegistradoPor(usuario.getIdPersonaGeneral());
        int cantidadSincronizada = 0;

        Map<Long, Long> titularPorGrupo = new LinkedHashMap<>();

        for (DocenteEfectivoSincronizacionProjection docente : docentes) {
            List<DetalleDirectoEfectivoSincronizacionProjection> detallesActuales = detallesPorDocente.getOrDefault(docente.getIdCargaDocente(), List.of());

            TrabajadorLaborEntity trabajadorLabor = trabajadorLaborRepository.findTrlaVigenteByIdPersonaGeneral(docente.getIdPersonaGeneral())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe una labor activa para el trabajador con id " + docente.getIdPersonaGeneral()));

            DocenteUnidadEntity docenteUnidad = docenteUnidadRepository.findByIdPersonaGeneralAndIdLaborAndIdDocumentoVinculacionAndIdUnidad(
                trabajadorLabor.getIdPersonaGeneral(),
                trabajadorLabor.getIdLabor(),
                trabajadorLabor.getIdDocumentoVinculacion(),
                docente.getIdUnidadArea()
            ).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El docente no esta relacionado a la facultad con los datos de Trabajador Labor"));
            Long idDocenteUnidad = docenteUnidad.getId();

            // Guardar el ultimo docente sincronizado por grupo para marcarlo como TITULAR
            detallesActuales.stream()
                .map(DetalleDirectoEfectivoSincronizacionProjection::getIdGrupo)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(idGrupo -> titularPorGrupo.put(idGrupo, idDocenteUnidad));

            boolean haHechoSincronizacion = INDICADOR_ESTADO_VERDADERO.equals(docente.getContratacionSincronizado());

            // Si es la primera vez que se realiza la sincronización para el docente, agrega todos sus detalles
            // Si no, se evalua que se debe agregar y eliminar. Queda pendiente porque esto ocurre cunado hay Otro Si y se debe manejar un estado para identificar que detalles se sincronizaron
            if (!haHechoSincronizacion) {
                registrarPrimeraSincronizacionDocenteGrupos(detallesActuales, idDocenteUnidad, fechaActual, registradoPor);

                registrarPrimeraSincronizacionMateriasDocenteUnidad(detallesActuales, idDocenteUnidad, fechaActual, registradoPor);

                registrarPrimeraSincronizacionClasesSemanales(docente, detallesActuales, fechaActual, registradoPor);

                cantidadSincronizada++;
            } else {

            }

            //actualizarSincronizacionCargaDocente(docente.getIdCargaDocente(), fechaActual);
        }

        //sincronizarResponsablesGrupos(titularPorGrupo, fechaActual, registradoPor);

        return cantidadSincronizada;
    }

    private void registrarPrimeraSincronizacionDocenteGrupos(
        List<DetalleDirectoEfectivoSincronizacionProjection> detalles,
        Long idDocenteUnidad,
        Date fechaActual,
        String registradoPor
    ) {
        List<DocenteGrupoEntity> docenteGrupos = detalles
            .stream()
            .map(detalle -> {
                DocenteGrupoEntity entity = new DocenteGrupoEntity();

                entity.setIdGrupo(detalle.getIdGrupo());
                entity.setIdDocenteUnidad(idDocenteUnidad);
                entity.setResponsable(RESPONSABLE_TITULAR);
                entity.setFechaCambio(fechaActual);
                entity.setRegistradoPor(registradoPor);

                return entity;
            })
            .collect(Collectors.toList());

        //docenteGrupoRepository.saveAll(docenteGrupos);
        System.out.println("\n========== DOCENTE GRUPOS ==========");
        docenteGrupos.forEach(entity -> {
            System.out.println("ID Grupo: " + entity.getIdGrupo());
            System.out.println("ID Docente Unidad: " + entity.getIdDocenteUnidad());
            System.out.println("Responsable: " + entity.getResponsable());
            System.out.println("Fecha Cambio: " + entity.getFechaCambio());
            System.out.println("Registrado Por: " + entity.getRegistradoPor());
            System.out.println("------------------------------------");
        });
        System.out.println("Total registros: " + docenteGrupos.size());
    }

    private void registrarPrimeraSincronizacionMateriasDocenteUnidad(
        List<DetalleDirectoEfectivoSincronizacionProjection> detalles,
        Long idDocenteUnidad,
        Date fechaActual,
        String registradoPor
    ) {
        List<MateriaDocenteUnidadEntity> materias = detalles
            .stream()
            .map(detalle -> {
                MateriaDocenteUnidadEntity entity = new MateriaDocenteUnidadEntity();

                entity.setCodigoMateria(detalle.getCodigoMateria());
                entity.setIdDocenteUnidad(idDocenteUnidad);
                entity.setFechaCambio(fechaActual);
                entity.setRegistradoPor(registradoPor);

                return entity;
            })
            .collect(Collectors.toList());

        //materiaDocenteUnidadRepository.saveAll(materias);
        System.out.println("\n========== MATERIAS DOCENTE UNIDAD ==========");
        materias.forEach(entity -> {
            System.out.println("Código Materia: " + entity.getCodigoMateria());
            System.out.println("ID Docente Unidad: " + entity.getIdDocenteUnidad());
            System.out.println("Fecha Cambio: " + entity.getFechaCambio());
            System.out.println("Registrado Por: " + entity.getRegistradoPor());
            System.out.println("---------------------------------------------");
        });
        System.out.println("Total registros: " + materias.size());
    }

    private void registrarPrimeraSincronizacionClasesSemanales(
        DocenteEfectivoSincronizacionProjection docente,
        List<DetalleDirectoEfectivoSincronizacionProjection> detalles,
        Date fechaActual,
        String registradoPor
    ) {
        List<Long> idsGrupos = detalles
            .stream()
            .map(DetalleDirectoEfectivoSincronizacionProjection::getIdGrupo)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        List<ClaseSemanalEntity> clasesSemanales = claseSemanalRepository.findAllClasesByGrupos(idsGrupos);

        List<DocenteClaseSemanalEntity> docenteClases = clasesSemanales
            .stream()
            .map(clase -> {
                DocenteClaseSemanalEntity entity = new DocenteClaseSemanalEntity();

                entity.setIdPersonaGeneral(docente.getIdPersonaGeneral());
                entity.setIdClaseSemanal(clase.getId());
                entity.setSobrecarga(INDICADOR_ESTADO_FALSO);
                entity.setFechaCambio(fechaActual);
                entity.setRegistradoPor(registradoPor);

                return entity;
            })
            .collect(Collectors.toList());

        //docenteClaseSemanalRepository.saveAll(docenteClases);
        System.out.println("\n========== DOCENTE CLASES SEMANALES ==========");
        docenteClases.forEach(entity -> {
            System.out.println("ID Persona General: " + entity.getIdPersonaGeneral());
            System.out.println("ID Clase Semanal: " + entity.getIdClaseSemanal());
            System.out.println("Sobrecarga: " + entity.getSobrecarga());
            System.out.println("Fecha Cambio: " + entity.getFechaCambio());
            System.out.println("Registrado Por: " + entity.getRegistradoPor());
            System.out.println("----------------------------------------------");
        });
        System.out.println("Total registros: " + docenteClases.size());
    }


    private void actualizarSincronizacionCargaDocente(Long idCargaDocente, Date fechaActual) {
        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));
        
        cargaDocente.setContratacionSincronizado(SI);
        cargaDocente.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        cargaDocente.setFechaCambio(fechaActual);
        cargaDocenteRepository.save(cargaDocente);
    }
    
    private void sincronizarResponsablesGrupos(Map<Long, Long> titularPorGrupo, Date fechaActual, String registradoPor) {
        if (titularPorGrupo.isEmpty()) {
            return;
        }

        // Relaciones de los grupos usados en la sincronizacion
        List<DocenteGrupoEntity> relaciones = docenteGrupoRepository.findAllByIdGrupoIn(List.copyOf(titularPorGrupo.keySet()));
        List<DocenteGrupoEntity> relacionesModificadas = new ArrayList<>();

        // Determina si la relacion deberia cambiar de responsable porque el docente TITULAR debe ser otro
        for (DocenteGrupoEntity relacion : relaciones) {
            Long idUltimoDocenteTitularSincronizado = titularPorGrupo.get(relacion.getIdGrupo());

            if (idUltimoDocenteTitularSincronizado == null) {
                continue;
            }

            boolean esUltimoDocenteSincronizado = idUltimoDocenteTitularSincronizado.equals(relacion.getIdDocenteUnidad());
            String responsableActual = relacion.getResponsable();
            String responsableEsperado = esUltimoDocenteSincronizado ? RESPONSABLE_TITULAR : RESPONSABLE_AUXILIAR;

            if (!responsableEsperado.equals(responsableActual)) {
                relacion.setResponsable(responsableEsperado);
                relacion.setFechaCambio(fechaActual);
                relacion.setRegistradoPor(registradoPor);

                relacionesModificadas.add(relacion);
            }
        }

        if (!relacionesModificadas.isEmpty()) {
            docenteGrupoRepository.saveAll(relacionesModificadas);
        }

        log.info("sincronizarResponsablesGrupos ===> Grupos procesados: {}, relaciones modificadas: {}", titularPorGrupo.size(), relacionesModificadas.size());
    }
    
    private void validarDatosBasicos(Long idCarga, List<Long> idsCargaDocente) {
        if (idCarga == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga es obligatorio");
        }
        if (idsCargaDocente == null || idsCargaDocente.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Debe enviar al menos un docente");
        }
        if (idsCargaDocente.size() > 200) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Se permiten máximo 200 docentes por operación");
        }

        Integer allIn = cargaDocenteRepository.allProfessorsInCall(idsCargaDocente, idCarga, idsCargaDocente.size());
        if (allIn != 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Las cargas docente enviadas no corresponden correctamente a la carga");
        }
    }

    private void validarProfesoresCumplanFiltros(
        CargaEntity carga,
        List<DocenteEfectivoSincronizacionProjection> cargaDocenteProjections,
        Map<Long, List<DetalleDirectoEfectivoSincronizacionProjection>> detalleProjectionsPorDocente
    ) {
        Long idPeriodoUniversitario = findIdPeriodoUniversidad(carga);
        String numeroPeriodoUniversitario = findPeriodoUniversidad(carga);

        Set<Long> idsDocentesPlanta = personaGeneralRepository.findCareerProfessorsOptions()
            .stream()
            .map(CatalogoAdministracionProjection::getId)
            .collect(Collectors.toSet());

        List<PtdStatesProfessorsDTO> estadosPtd = planTrabajoDigitalRepository
            .findPtdStatesByPeriod(idPeriodoUniversitario, carga.getId())
            .stream()
            .map(planTrabajoDigitalMapper::toPtdStatesProfessorsDTO)
            .toList();
        
        Map<Long, String> estadosPtdPorPersona = estadosPtd
            .stream()
            .collect(Collectors.toMap(
                PtdStatesProfessorsDTO::idPersonaGeneral,
                PtdStatesProfessorsDTO::estado,
                (a, b) -> a
            ));
        
        
        for (DocenteEfectivoSincronizacionProjection docente : cargaDocenteProjections) {
            // Verifica el estado de la carga y si sus detalles tienen grupo, lo que se aplica para cualquier docente que venga
            validarFiltrosComunesDocente(docente, detalleProjectionsPorDocente);

            boolean cumpleContrato = validarDocenteConContrato(docente);

            boolean cumpleSinContrato = validarDocenteSinContrato(docente, idsDocentesPlanta, estadosPtdPorPersona);

            boolean cumpleHeredado = validarDocenteHeredado(docente, idsDocentesPlanta, estadosPtdPorPersona, numeroPeriodoUniversitario);

            if (!cumpleContrato && !cumpleSinContrato && !cumpleHeredado) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Existen docentes seleccionados que no cumplen los filtros para realizar sincronización");
            }
        }
    }

    private void validarFiltrosComunesDocente(DocenteEfectivoSincronizacionProjection docente, Map<Long, List<DetalleDirectoEfectivoSincronizacionProjection>> detalleProjectionsPorDocente) {
        List<DetalleDirectoEfectivoSincronizacionProjection> detalles = detalleProjectionsPorDocente.getOrDefault(docente.getIdCargaDocente(), List.of());

        boolean enAvalDesarrollo = ESTADO_APROBADO_DESARROLLO.equals(docente.getEstado());
        boolean tieneActividadesSinGrupo = detalles
            .stream()
            .anyMatch(detalle -> detalle.getIdGrupo() == null);

        if (!enAvalDesarrollo) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La carga docente no se encuentra en Aval desarrollo. No es posible la sincronización");
        }
        if (tieneActividadesSinGrupo) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existen docentes seleccionados que tienen actividades sin grupos relacionados. No es posible la sincronización");
        }
    }

    private boolean validarDocenteConContrato(DocenteEfectivoSincronizacionProjection docente) {
        boolean tieneContrato = INDICADOR_ESTADO_VERDADERO.equals(docente.getContratacionVinculado());
        
        boolean tieneModalidad = docente.getIdModalidadContratacion() != null;
        
        return tieneContrato && tieneModalidad;
    }

    private boolean validarDocenteSinContrato(DocenteEfectivoSincronizacionProjection docente, Set<Long> idsDocentesPlanta, Map<Long, String> estadosPtdPorPersona) {
        boolean tieneContrato = INDICADOR_ESTADO_VERDADERO.equals(docente.getContratacionVinculado());

        boolean ptdAceptado = ESTADO_PTD_ACEPTADO.equals(estadosPtdPorPersona.get(docente.getIdPersonaGeneral()));

        boolean esDocentePlanta = idsDocentesPlanta.contains(docente.getIdPersonaGeneral());

        System.out.println("Holaaaaa " + idsDocentesPlanta);

        return !tieneContrato && ptdAceptado && esDocentePlanta;
    }

    private boolean validarDocenteHeredado(DocenteEfectivoSincronizacionProjection docente, Set<Long> idsDocentesPlanta, Map<Long, String> estadosPtdPorPersona, String numeroPeriodoUniversitario) {
        boolean tieneContrato = INDICADOR_ESTADO_VERDADERO.equals(docente.getContratacionVinculado());

        boolean ptdAceptado = ESTADO_PTD_ACEPTADO.equals(estadosPtdPorPersona.get(docente.getIdPersonaGeneral()));

        boolean esDocentePlanta = idsDocentesPlanta.contains(docente.getIdPersonaGeneral());

        boolean tieneModalidad = docente.getIdModalidadContratacion() != null;

        boolean esHeredado = OnceMesesReporteFilter.excludeInheritedInSecondPeriod(docente.getOnceMeses(), numeroPeriodoUniversitario);

        return !tieneContrato && ptdAceptado && !esDocentePlanta && tieneModalidad && esHeredado;
    }


    private Map<Long, List<DetalleDirectoEfectivoSincronizacionProjection>> obtenerDetallesPorDocente(List<Long> idsCargaDocente) {
        return detalleCargaDocenteRepository
            .findSyncEffectiveDirectActivitiesDetailByCargaDocenteIds(idsCargaDocente)
            .stream()
            .collect(Collectors.groupingBy(
                DetalleDirectoEfectivoSincronizacionProjection::getIdCargaDocente
            ));
    }
    
    private Long findIdPeriodoUniversidad(CargaEntity carga) {
        return Optional.ofNullable(carga.getConvocatoria())
            .map(ConvocatoriaEntity::getPeriodoUniversidad)
            .map(PeriodoUniversidadEntity::getId)
            .orElse(null);
    }

    private String findPeriodoUniversidad(CargaEntity carga) {
        return Optional.ofNullable(carga.getConvocatoria())
                .map(ConvocatoriaEntity::getPeriodoUniversidad)
                .map(PeriodoUniversidadEntity::getPeriodo)
                .orElse(null);
    }

    private String formatRegistradoPor(Long valor) {
        return valor != null ? String.valueOf(valor) : null;
    }

    private AuthUserDetails requireUsuario() {
        try {
            AuthUserDetails usuario = SecurityUtils.requireUser();
            if (usuario.getIdPersonaGeneral() == null) {
                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "El token no trae idPersona para registrar el contratante");
            }
            return usuario;
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
}
