package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;

@Mapper(componentModel = "spring")
public interface NovedadMapper {

    NovedadListadoDTO toNovedadListadoDTO(
            NovedadEntity entity
    );

    List<NovedadListadoDTO> toNovedadListadoDTOList(
            List<NovedadEntity> list
    );
}