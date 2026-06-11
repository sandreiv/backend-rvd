package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoCargaEntity;

@Mapper(componentModel = "spring")
public interface EstadoCargaMapper {

    EstadoCargaDTO toDto(EstadoCargaEntity entity);

    List<EstadoCargaDTO> toDtoList(List<EstadoCargaEntity> entities);
}
