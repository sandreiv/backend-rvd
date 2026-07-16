package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoActividadesRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;

@Mapper(componentModel = "spring")
public interface DetalleCargaDocenteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCargaDocente", source = "idCargaDocente")
    @Mapping(target = "idTipoActividad", expression = "java(resolveTipoActividad(detalle))")
    @Mapping(target = "idPrograma", source = "detalle.idPrograma")
    @Mapping(target = "idGrupo", source = "detalle.idGrupo")
    @Mapping(target = "idCentroCosto", expression = "java(resolveIdCentroCosto(detalle))")
    @Mapping(target = "horas", expression = "java(toHoras(detalle.horas()))")
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "cargaDocente", ignore = true)
    @Mapping(target = "programa", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    @Mapping(target = "tipoActividad", ignore = true)
    @Mapping(target = "centroCosto", ignore = true)
    DetalleCargaDocenteEntity toEntity(
            Long idCargaDocente, DetalleCargaDocenteItemDTO detalle);

    default List<DetalleCargaDocenteDTO> toDtoList(List<DetalleCargaDocenteListadoProjection> projections,
            ProyectoMapper proyectoMapper,
            TipoActividadMapper tipoActividadMapper,
            TipoActividadesRepository tipoActividadesRepository) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>> agrupados =
                agruparPorCargaYDetalle(projections);
        List<DetalleCargaDocenteDTO> resultado = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>>
                cargaEntry : agrupados.entrySet()) {
            for (Map.Entry<Long, List<DetalleCargaDocenteListadoProjection>> detalleEntry : cargaEntry.getValue().entrySet()) {
                DetalleCargaDocenteActividadDTO detalleActividad = toActividadDto(
                        detalleEntry.getValue(),
                        proyectoMapper,
                        tipoActividadMapper,
                        tipoActividadesRepository);
                resultado.add(new DetalleCargaDocenteDTO(
                        detalleEntry.getKey(),
                        cargaEntry.getKey(),
                        List.of(detalleActividad)));
            }
        }
        return resultado;
    }

    default List<DetalleCargaDocenteFormularioDTO> toFormularioDtoList(
            List<DetalleCargaDocenteListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>> agrupados =
                agruparPorCargaYDetalle(projections);
        List<DetalleCargaDocenteFormularioDTO> resultado = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>>
                cargaEntry : agrupados.entrySet()) {
            List<DetalleCargaDocenteItemDTO> detalles = cargaEntry.getValue()
                    .values().stream()
                    .map(this::toItemDtoFromGroup)
                    .toList();
            resultado.add(new DetalleCargaDocenteFormularioDTO(
                    cargaEntry.getKey(), detalles));
        }
        return resultado;
    }

    default Map<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>>
            agruparPorCargaYDetalle(
                    List<DetalleCargaDocenteListadoProjection> projections) {
        Map<Long, Map<Long, List<DetalleCargaDocenteListadoProjection>>> agrupados =
                new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection projection : projections) {
            agrupados
                    .computeIfAbsent(
                            projection.getIdCargaDocente(),
                            k -> new LinkedHashMap<>())
                    .computeIfAbsent(
                            projection.getIdDetalleCargaDocente(),
                            k -> new ArrayList<>())
                    .add(projection);
        }
        return agrupados;
    }

    default DetalleCargaDocenteActividadDTO toActividadDto(
            List<DetalleCargaDocenteListadoProjection> filas,
            ProyectoMapper proyectoMapper,
            TipoActividadMapper tipoActividadMapper,
            TipoActividadesRepository tipoActividadesRepository) {
        DetalleCargaDocenteListadoProjection primera = filas.get(0);
        TipoActividadDTO tipoActividad = primera.getIdTipoActividadPadre() != null
                ? mapTipoActividadPadre(primera)
                : mapTipoActividadResuelto(primera);
        List<TipoActividadDTO> tipoActividadHija = resolveTipoActividadesHijas(
                tipoActividad,
                tipoActividadMapper,
                tipoActividadesRepository);
        return new DetalleCargaDocenteActividadDTO(
                tipoActividad,
                tipoActividadHija,
                mapUnidad(primera),
                mapPrograma(primera),
                mapMateria(primera),
                mapGrupo(primera),
                mapCentroCosto(primera),
                primera.getHoras(),
                collectRelacionesListado(filas, proyectoMapper));
    }

    default List<TipoActividadDTO> resolveTipoActividadesHijas(
            TipoActividadDTO tipoActividad,
            TipoActividadMapper tipoActividadMapper,
            TipoActividadesRepository tipoActividadesRepository) {
        if (tipoActividad == null || tipoActividad.id() == null) {
            return List.of();
        }
        return tipoActividadMapper.toDtoList(
                tipoActividadesRepository.findByIdPadre(tipoActividad.id()));
    }

    default MateriaDetalleDTO mapMateria(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getCodigoMateria() == null) {
            return null;
        }
        return new MateriaDetalleDTO(
                projection.getCodigoMateria(),
                projection.getNombreMateria());
    }

    default TipoActividadDTO mapTipoActividadResuelto(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdTipoActividadResuelto() == null) {
            return null;
        }
        return new TipoActividadDTO(
                projection.getIdTipoActividadResuelto(),
                projection.getIdTipoActividadPadre(),
                projection.getNombreTipoActividad(),
                projection.getDescripcionTipoActividad(),
                projection.getOrdenTipoActividad(),
                projection.getCodigoTipoActividad());
    }

    default TipoActividadDTO mapTipoActividadPadre(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdTipoActividadPadre() == null) {
            return null;
        }
        return new TipoActividadDTO(
                projection.getIdTipoActividadPadre(),
                null,
                projection.getNombreTipoActividadPadre(),
                projection.getDescripcionTipoActividadPadre(),
                projection.getOrdenTipoActividadPadre(),
                projection.getCodigoTipoActividadPadre());
    }

    default UnidadDTO mapUnidad(DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdUnidadRegional() == null) {
            return null;
        }
        return new UnidadDTO(
                projection.getIdUnidadRegional(),
                projection.getNombreUnidadRegional());
    }

    default ProgramaDTO mapPrograma(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdPrograma() == null) {
            return null;
        }
        return new ProgramaDTO(
                projection.getIdPrograma(),
                projection.getNombrePrograma());
    }

    default GrupoDTO mapGrupo(DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdGrupo() == null) {
            return null;
        }
        return new GrupoDTO(
                projection.getIdGrupo(),
                projection.getNombreGrupo(),
                projection.getCapacidadGrupo());
    }

    default CentroCostoDTO mapCentroCosto(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdCentroCosto() == null) {
            return null;
        }
        return new CentroCostoDTO(
                projection.getIdCentroCosto(),
                projection.getDescripcionCentroCosto());
    }

    default List<RelacionCargaProyectoListadoDTO> collectRelacionesListado(
            List<DetalleCargaDocenteListadoProjection> filas,
            ProyectoMapper proyectoMapper) {
        Map<Long, RelacionCargaProyectoListadoDTO> relaciones = new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection fila : filas) {
            if (fila.getIdPersonaProyecto() == null) {
                continue;
            }
            relaciones.putIfAbsent(
                    fila.getIdPersonaProyecto(),
                    new RelacionCargaProyectoListadoDTO(
                            fila.getIdPersonaProyecto(),
                            fila.getIdProyecto(),
                            proyectoMapper.toArbolDesdeDetalle(fila)));
        }
        return new ArrayList<>(relaciones.values());
    }

    default DetalleCargaDocenteItemDTO toItemDtoFromGroup(
            List<DetalleCargaDocenteListadoProjection> filas) {
        DetalleCargaDocenteListadoProjection primera = filas.get(0);
        return new DetalleCargaDocenteItemDTO(
                resolveIdTipoActividadPadre(primera),
                resolveIdTipoActividadHija(primera),
                primera.getCodigoTipoActividad(),
                parseHoras(primera.getHoras()),
                primera.getIdUnidadRegional(),
                primera.getIdPrograma(),
                new MateriaFormularioDTO(
                        primera.getCodigoMateria(),
                        primera.getIdCentroCosto()),
                primera.getIdGrupo(),
                primera.getIdCentroCosto(),
                collectRelacionesCargaProyecto(filas));
    }

    default Long resolveIdTipoActividadPadre(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdTipoActividadPadre() != null) {
            return projection.getIdTipoActividadPadre();
        }
        return projection.getIdTipoActividadResuelto();
    }

    default Long resolveIdTipoActividadHija(
            DetalleCargaDocenteListadoProjection projection) {
        if (projection.getIdTipoActividadPadre() != null) {
            return projection.getIdTipoActividadResuelto();
        }
        return null;
    }

    default List<RelacionCargaProyectoDTO> collectRelacionesCargaProyecto(
            List<DetalleCargaDocenteListadoProjection> filas) {
        Map<Long, RelacionCargaProyectoDTO> relaciones = new LinkedHashMap<>();
        for (DetalleCargaDocenteListadoProjection fila : filas) {
            if (fila.getIdPersonaProyecto() == null) {
                continue;
            }
            relaciones.putIfAbsent(
                    fila.getIdPersonaProyecto(),
                    new RelacionCargaProyectoDTO(
                            fila.getIdPersonaProyecto(),
                            fila.getIdProyecto()));
        }
        return new ArrayList<>(relaciones.values());
    }

    default DetalleCargaDocenteEntity toEntityFromDto(DetalleCargaDocenteDTO dto) {
        DetalleCargaDocenteActividadDTO actividad = dto.detalles().get(0);
        DetalleCargaDocenteEntity entity = new DetalleCargaDocenteEntity();
        entity.setId(dto.idDetalleCargaDocente());
        entity.setIdCargaDocente(dto.idCargaDocente());
        entity.setIdTipoActividad(resolveTipoActividadFromActividad(actividad));
        entity.setIdPrograma(resolveIdPrograma(actividad));
        entity.setIdGrupo(resolveIdGrupo(actividad));
        entity.setIdCentroCosto(resolveIdCentroCostoFromActividad(actividad));
        entity.setHoras(actividad.horas());
        return entity;
    }

    default List<RelacionCargaProyectoDTO> toRelacionesCargaProyecto(
            List<RelacionCargaProyectoListadoDTO> relaciones) {
        if (relaciones == null || relaciones.isEmpty()) {
            return List.of();
        }
        return relaciones.stream()
                .map(relacion -> new RelacionCargaProyectoDTO(
                        relacion.idPersonaProyecto(),
                        relacion.idProyecto()))
                .toList();
    }

    default Long resolveTipoActividadFromActividad(
            DetalleCargaDocenteActividadDTO actividad) {
        return resolveTipoActividadFromActividad(actividad, null);
    }

    default Long resolveTipoActividadFromActividad(
            DetalleCargaDocenteActividadDTO actividad,
            Long idTipoActividadPersistido) {
        List<TipoActividadDTO> hijas = actividad.tipoActividadHija();
        if (hijas != null && hijas.size() == 1 && hijas.get(0) != null) {
            return hijas.get(0).id();
        }
        if (hijas != null
                && hijas.size() > 1
                && idTipoActividadPersistido != null) {
            for (TipoActividadDTO hija : hijas) {
                if (idTipoActividadPersistido.equals(hija.id())) {
                    return idTipoActividadPersistido;
                }
            }
        }
        if (actividad.tipoActividad() != null) {
            return actividad.tipoActividad().id();
        }
        return null;
    }

    default Long resolveIdPrograma(DetalleCargaDocenteActividadDTO actividad) {
        return actividad.programa() != null ? actividad.programa().id() : null;
    }

    default Long resolveIdGrupo(DetalleCargaDocenteActividadDTO actividad) {
        return actividad.grupo() != null ? actividad.grupo().id() : null;
    }

    default Long resolveIdCentroCostoFromActividad(
            DetalleCargaDocenteActividadDTO actividad) {
        if (actividad.centroCosto() == null) {
            return null;
        }
        return actividad.centroCosto().id();
    }

    default Long resolveTipoActividad(DetalleCargaDocenteItemDTO detalle) {
        return detalle.idTipoActividadHija() != null
                ? detalle.idTipoActividadHija()
                : detalle.idTipoActividad();
    }

    default Long resolveIdCentroCosto(DetalleCargaDocenteItemDTO detalle) {
        if (detalle.materia() != null
                && detalle.materia().idCentroCosto() != null) {
            // Prioridad: centro de costo principal de la materia.
            return detalle.materia().idCentroCosto();
        }
        return detalle.idCentroCosto();
    }

    default String toHoras(Long horas) {
        return horas != null ? horas.toString() : null;
    }

    default Long parseHoras(String horas) {
        if (horas == null || horas.isBlank()) {
            return null;
        }
        return Long.valueOf(horas.trim());
    }
}
