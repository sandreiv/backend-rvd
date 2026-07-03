package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadListadoProjection;

@Mapper(componentModel = "spring")
public interface TipoActividadMapper {

    TipoActividadDTO toDto(TipoActividadListadoProjection projection);

    List<TipoActividadDTO> toDtoList(List<TipoActividadListadoProjection> projections);
}
