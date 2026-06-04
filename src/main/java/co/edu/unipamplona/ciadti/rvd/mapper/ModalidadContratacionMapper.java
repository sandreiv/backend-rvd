package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadContratacionEntity;

@Mapper(componentModel = "spring")
public interface ModalidadContratacionMapper {

    ModalidadContratacionDTO toDto(ModalidadContratacionEntity entity);

    List<ModalidadContratacionDTO> toDtoList(
            List<ModalidadContratacionEntity> entities);
}
