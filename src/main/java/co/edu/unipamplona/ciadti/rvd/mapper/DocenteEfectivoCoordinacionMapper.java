/**
 * Aplicación: rvd
 * Archivo: DocenteEfectivoCoordinacionMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/09/2026
 * Modificaciones:
 * 16/09/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.Date;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteEfectivoCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteEfectivoCoordinacionProjection;

@Mapper(componentModel = "spring")
public interface DocenteEfectivoCoordinacionMapper {

    @Mapping(target = "fechaInicio",
            expression = "java(resolveFechaInicio(projection))")
    @Mapping(target = "fechaFin",
            expression = "java(resolveFechaFin(projection))")
    @Mapping(target = "tieneCarga",
            expression = "java(projection.getIdCargaDocente() != null)")
    @Mapping(target = "tieneDetalleActividades",
            expression = "java(hasActivities(projection))")
    DocenteEfectivoCoordinacionDTO toDto(
            DocenteEfectivoCoordinacionProjection projection);

    List<DocenteEfectivoCoordinacionDTO> toDtoList(
            List<DocenteEfectivoCoordinacionProjection> projections);

    default Date resolveFechaInicio(
            DocenteEfectivoCoordinacionProjection projection) {
        if (projection.getCargaFechaInicio() != null) {
            return projection.getCargaFechaInicio();
        }
        return projection.getFechaConvocatoriaInicio();
    }

    default Date resolveFechaFin(
            DocenteEfectivoCoordinacionProjection projection) {
        if (projection.getCargaFechaFin() != null) {
            return projection.getCargaFechaFin();
        }
        return projection.getFechaConvocatoriaFin();
    }

    default boolean hasActivities(
            DocenteEfectivoCoordinacionProjection projection) {
        return projection.getTieneActividades() != null
                && projection.getTieneActividades() != 0;
    }
}
