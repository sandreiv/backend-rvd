package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.UnidadEntity;

@Mapper(componentModel = "spring")
public interface UnidadMapper {

    UnidadDTO toDto(UnidadEntity entity);

    List<UnidadDTO> toDtoList(List<UnidadEntity> entities);
}
