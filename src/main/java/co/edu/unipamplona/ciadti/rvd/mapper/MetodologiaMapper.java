package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.MetodologiaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.MetodologiaEntity;

@Mapper(componentModel = "spring")
public interface MetodologiaMapper {

    MetodologiaDTO toDto(MetodologiaEntity entity);

    List<MetodologiaDTO> toDtoList(List<MetodologiaEntity> entities);
}
