package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;

@Mapper(componentModel = "spring")
public interface ConvocatoriaMapper {

    ConvocatoriaDTO toConvocatoriaDTO(ConvocatoriaEntity entity);

    ConvocatoriaEntity toConvocatoriaEntity(ConvocatoriaDTO dto);

    List<ConvocatoriaDTO> toConvocatoriaDTOList(List<ConvocatoriaEntity> list);
}
