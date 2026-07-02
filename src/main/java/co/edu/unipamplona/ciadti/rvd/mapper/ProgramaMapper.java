package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaListadoProjection;

@Mapper(componentModel = "spring")
public interface ProgramaMapper {

    ProgramaDTO toDto(ProgramaListadoProjection projection);

    List<ProgramaDTO> toDtoList(List<ProgramaListadoProjection> projections);
}
