package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.Date;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;

@Mapper(componentModel = "spring")
public interface DocenteCoordinacionMapper {

    @Mapping(target = "fechaInicio",
            expression = "java(resolveFechaInicio(projection))")
    @Mapping(target = "fechaFin",
            expression = "java(resolveFechaFin(projection))")
    @Mapping(target = "tieneCarga",
            expression = "java(projection.getIdCargaDocente() != null)")
    DocenteCoordinacionDTO toDto(
            DocenteCargaCoordinacionProjection projection);

    List<DocenteCoordinacionDTO> toDtoList(
            List<DocenteCargaCoordinacionProjection> projections);

    default Date resolveFechaInicio(
            DocenteCargaCoordinacionProjection projection) {
        if (projection.getCargaFechaInicio() != null) {
            return projection.getCargaFechaInicio();
        }
        return projection.getFechaConvocatoriaInicio();
    }

    default Date resolveFechaFin(
            DocenteCargaCoordinacionProjection projection) {
        if (projection.getCargaFechaFin() != null) {
            return projection.getCargaFechaFin();
        }
        return projection.getFechaConvocatoriaFin();
    }
}
