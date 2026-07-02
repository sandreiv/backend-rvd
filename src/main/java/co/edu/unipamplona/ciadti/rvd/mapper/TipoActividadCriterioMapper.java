package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;

@Mapper(componentModel = "spring")
public interface TipoActividadCriterioMapper {

    TipoActividadCriterioDTO toDto(TipoActividadesEntity entity);

    List<TipoActividadCriterioDTO> toDtoList(List<TipoActividadesEntity> entities);
}
