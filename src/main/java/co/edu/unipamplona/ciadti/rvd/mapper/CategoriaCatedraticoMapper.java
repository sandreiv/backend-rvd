package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaCatedraticoEntity;

@Mapper(componentModel = "spring")
public interface CategoriaCatedraticoMapper {

    CategoriaCatedraticoDTO toDto(CategoriaCatedraticoEntity entity);

    List<CategoriaCatedraticoDTO> toDtoList(List<CategoriaCatedraticoEntity> entities);
}
