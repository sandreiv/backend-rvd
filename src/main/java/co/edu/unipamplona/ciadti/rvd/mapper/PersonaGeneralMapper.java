package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaGeneralDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;

@Mapper(componentModel = "spring")
public interface PersonaGeneralMapper {

    PersonaGeneralDTO toPersonaGeneralDTO(PersonaGeneralEntity entity);

    PersonaGeneralEntity toPersonaGeneralEntity(PersonaGeneralDTO dto);

    List<PersonaGeneralDTO> toPersonaGeneralDTOList(List<PersonaGeneralEntity> list);
}
