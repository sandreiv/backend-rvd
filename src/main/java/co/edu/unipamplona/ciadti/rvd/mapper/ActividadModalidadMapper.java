/**
 * Aplicación: rvd
 * Archivo: ActividadModalidadMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 29/07/2026
 * Modificaciones:
 * 29/07/2026 - Creación inicial para listado de actividades por modalidad.
 * 29/07/2026 - Soporte de múltiples tipos de actividad por modalidad.
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadBasicoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ActividadModalidadProjection;

@Mapper(componentModel = "spring")
public interface ActividadModalidadMapper {

    default ActividadModalidadDTO toDto(
            Long idModalidadContratacion,
            List<ActividadModalidadProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return new ActividadModalidadDTO(
                    idModalidadContratacion,
                    Collections.emptyList());
        }
        return new ActividadModalidadDTO(
                idModalidadContratacion,
                toTipoActividadList(projections));
    }

    default List<TipoActividadBasicoDTO> toTipoActividadList(
            List<ActividadModalidadProjection> projections) {
        return projections.stream()
                .map(this::toTipoActividad)
                .toList();
    }

    default TipoActividadBasicoDTO toTipoActividad(
            ActividadModalidadProjection projection) {
        if (projection == null || projection.getIdTipoActividad() == null) {
            return null;
        }
        return new TipoActividadBasicoDTO(
                projection.getIdTipoActividad(),
                projection.getNombreTipoActividad(),
                projection.getCodigoTipoActividad(),
                projection.getEstadoTipoActividad(),
                projection.getComponenteTipoActividad());
    }
}

/* 29/07/2026 */
