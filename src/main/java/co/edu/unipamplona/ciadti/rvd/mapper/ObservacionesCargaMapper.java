package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ObservacionesCargaProjection;

@Mapper(componentModel = "spring")
public interface ObservacionesCargaMapper {
    
    ObservacionCargaDTO toDto(ObservacionesCargaProjection projection);

    List<ObservacionCargaDTO> toDtoList(List<ObservacionesCargaProjection> projections);
}
