package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.NivelEducativoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;

@Mapper(componentModel = "spring")
public interface NivelEducativoMapper {

    NivelEducativoDTO toDto(NivelEducativoEntity entity);

    List<NivelEducativoDTO> toDtoList(List<NivelEducativoEntity> entities);
}
