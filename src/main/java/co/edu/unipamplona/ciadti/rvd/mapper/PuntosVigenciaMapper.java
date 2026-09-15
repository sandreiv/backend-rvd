package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;

@Mapper(componentModel = "spring")
public interface PuntosVigenciaMapper {

    PuntosVigenciaListadoDTO toListadoDTO(
            PuntosVigenciaEntity entity
    );

    List<PuntosVigenciaListadoDTO> toListadoDTOList(
            List<PuntosVigenciaEntity> list
    );
}