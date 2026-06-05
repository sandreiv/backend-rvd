package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;

@Mapper(componentModel = "spring")
public interface FechasConvocatoriaMapper {

    FechasConvocatoriaFormularioDTO toFormularioDto(
            FechasConvocatoriaEntity entity);

    List<FechasConvocatoriaFormularioDTO> toFormularioDtoList(
            List<FechasConvocatoriaEntity> entities);
}
