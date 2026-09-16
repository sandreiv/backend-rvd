package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.Date;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadDocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.NovedadDocenteCargaCoordinacionProjection;

@Mapper(componentModel = "spring")
public interface NovedadDocenteCoordinacionMapper {

    @Mapping(target = "fechaInicio",
            expression = "java(resolveFechaInicio(projection))")
    @Mapping(target = "fechaFin",
            expression = "java(resolveFechaFin(projection))")
    @Mapping(target = "tieneCarga",
            expression = "java(projection.getIdCargaDocente() != null)")
    @Mapping(target = "tieneDetalleActividades",
            expression = "java(hasActivities(projection))")
    NovedadDocenteCoordinacionDTO toDto(
            NovedadDocenteCargaCoordinacionProjection projection);

    List<NovedadDocenteCoordinacionDTO> toDtoList(
            List<NovedadDocenteCargaCoordinacionProjection> projections);

    default Date resolveFechaInicio(
            NovedadDocenteCargaCoordinacionProjection projection) {
        if (projection.getCargaFechaInicio() != null) {
            return projection.getCargaFechaInicio();
        }
        return projection.getFechaConvocatoriaInicio();
    }

    default Date resolveFechaFin(
            NovedadDocenteCargaCoordinacionProjection projection) {
        if (projection.getCargaFechaFin() != null) {
            return projection.getCargaFechaFin();
        }
        return projection.getFechaConvocatoriaFin();
    }

    default boolean hasActivities(
            NovedadDocenteCargaCoordinacionProjection projection) {
        return projection.getTieneActividades() != null
                && projection.getTieneActividades() != 0;
    }
}
