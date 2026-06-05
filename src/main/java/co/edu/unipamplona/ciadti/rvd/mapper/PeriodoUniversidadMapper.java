package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;

@Mapper(componentModel = "spring")
public interface PeriodoUniversidadMapper {

    @Mapping(source = "ano", target = "anio")
    PeriodoUniversidadDTO toDto(PeriodoUniversidadEntity entity);

    List<PeriodoUniversidadDTO> toDtoList(
            List<PeriodoUniversidadEntity> entities);
}
