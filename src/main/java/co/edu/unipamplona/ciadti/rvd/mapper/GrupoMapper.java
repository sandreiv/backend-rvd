package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoListadoProjection;

@Mapper(componentModel = "spring")
public interface GrupoMapper {

    GrupoDTO toDto(GrupoListadoProjection projection);

    List<GrupoDTO> toDtoList(List<GrupoListadoProjection> projections);
}
