/**
 * Aplicación: rvd
 * Archivo: PersonaProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.IdNombreDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaProyectoListaProjection;

@Mapper(componentModel = "spring")
public interface PersonaProyectoMapper {

    default PersonaProyectoListaDTO toPersonaProyectoListaDTO(
            PersonaProyectoListaProjection projection) {
        if (projection == null) {
            return null;
        }
        IdNombreDTO tipoActividad = projection.getIdTipoActividad() == null
                ? null
                : new IdNombreDTO(
                        projection.getIdTipoActividad(),
                        projection.getNombreTipoActividad());
        return new PersonaProyectoListaDTO(
                projection.getId(),
                projection.getIdProyecto(),
                projection.getIdPersonaGeneral(),
                projection.getNombreCompleto(),
                projection.getIdTipoActividad(),
                tipoActividad,
                projection.getTipo(),
                projection.getHoras(),
                projection.getObservacion()
        );
    }

    default List<PersonaProyectoListaDTO> toPersonaProyectoListaDTOList(
            List<PersonaProyectoListaProjection> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(this::toPersonaProyectoListaDTO).toList();
    }
}
