package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProyectoDocenteListadoProjection;

@Mapper(componentModel = "spring")
public interface ProyectoMapper {

    default List<ProyectoDTO> toDtoList(
            List<ProyectoDocenteListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, List<ProyectoDocenteListadoProjection>> agrupados =
                new LinkedHashMap<>();
        for (ProyectoDocenteListadoProjection projection : projections) {
            agrupados
                    .computeIfAbsent(projection.getId(), k -> new ArrayList<>())
                    .add(projection);
        }
        Map<Long, ProyectoDTO> porId = new LinkedHashMap<>();
        for (List<ProyectoDocenteListadoProjection> filas : agrupados.values()) {
            ProyectoDTO proyecto = toDtoFromGroup(filas);
            porId.put(proyecto.id(), proyecto);
        }
        Map<Long, List<ProyectoDTO>> productosPorPadre = new LinkedHashMap<>();
        for (Long id : porId.keySet()) {
            productosPorPadre.put(id, new ArrayList<>());
        }
        for (ProyectoDTO proyecto : porId.values()) {
            if (proyecto.idProyecto() != null
                    && porId.containsKey(proyecto.idProyecto())) {
                productosPorPadre
                        .get(proyecto.idProyecto())
                        .add(proyecto);
            }
        }
        List<ProyectoDTO> raices = new ArrayList<>();
        for (ProyectoDTO proyecto : porId.values()) {
            if (proyecto.idProyecto() == null) {
                raices.add(rebuildConProductos(proyecto, productosPorPadre));
            } else if (!porId.containsKey(proyecto.idProyecto())) {
                raices.add(rebuildConProductos(proyecto, productosPorPadre));
            }
        }
        return raices;
    }

    default ProyectoDTO rebuildConProductos(
            ProyectoDTO nodo,
            Map<Long, List<ProyectoDTO>> productosPorPadre) {
        List<ProyectoDTO> hijos = productosPorPadre.getOrDefault(
                nodo.id(), List.of());
        List<ProyectoDTO> productos = hijos.stream()
                .map(h -> rebuildConProductos(h, productosPorPadre))
                .toList();
        return new ProyectoDTO(
                nodo.id(),
                nodo.idProyecto(),
                nodo.nombre(),
                nodo.descripcion(),
                nodo.tipoProyecto(),
                nodo.personaProyecto(),
                productos);
    }

    default ProyectoDTO toDtoFromGroup(
            List<ProyectoDocenteListadoProjection> filas) {
        ProyectoDocenteListadoProjection primera = filas.get(0);
        return new ProyectoDTO(
                primera.getId(),
                primera.getIdProyecto(),
                primera.getNombre(),
                primera.getDescripcion(),
                mapTipoProyecto(primera),
                filas.stream().map(this::toPersonaProyectoDto).toList(),
                List.of());
    }

    default List<TipoProyectoDTO> mapTipoProyecto(
            ProyectoDocenteListadoProjection projection) {
        if (projection.getIdTipoProyecto() == null) {
            return List.of();
        }
        return List.of(new TipoProyectoDTO(
                projection.getIdTipoProyecto(),
                projection.getNombreTipoProyecto(),
                projection.getDescripcionTipoProyecto(),
                projection.getTipoTipoProyecto()));
    }

    default PersonaProyectoDTO toPersonaProyectoDto(
            ProyectoDocenteListadoProjection projection) {
        return new PersonaProyectoDTO(
                projection.getIdPersonaProyecto(),
                projection.getIdPersonaGeneral(),
                mapTipoActividad(projection),
                projection.getHoras(),
                projection.getTipo(),
                projection.getObservacion());
    }

    default TipoActividadDTO mapTipoActividad(
            ProyectoDocenteListadoProjection projection) {
        if (projection.getIdTipoActividad() == null) {
            return null;
        }
        return new TipoActividadDTO(
                projection.getIdTipoActividad(),
                projection.getIdPadreTipoActividad(),
                projection.getNombreTipoActividad(),
                projection.getDescripcionTipoActividad(),
                projection.getOrdenTipoActividad(),
                projection.getCodigoTipoActividad());
    }
}
