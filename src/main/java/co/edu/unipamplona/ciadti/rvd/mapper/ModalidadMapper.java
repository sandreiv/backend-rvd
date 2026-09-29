package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadEntity;

@Mapper(componentModel = "spring")
public interface ModalidadMapper {

    @Mapping(target = "esPlanta", ignore = true)
    ModalidadDTO toDto(ModalidadEntity entity);

    List<ModalidadDTO> toDtoList(List<ModalidadEntity> entities);
}
