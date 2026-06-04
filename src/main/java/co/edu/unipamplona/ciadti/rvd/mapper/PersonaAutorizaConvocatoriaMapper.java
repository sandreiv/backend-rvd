package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaNaturalGeneralEntity;

@Mapper(componentModel = "spring")
public interface PersonaAutorizaConvocatoriaMapper {

    @Mapping(target = "documentoIdentidad", source = "documentoIdentidad")
    @Mapping(target = "telefonoCelular", source = "telefonoCelular")
    @Mapping(
            target = "nombreCompleto",
            source = "personaNaturalGeneral",
            qualifiedByName = "buildNombreCompleto")
    PersonaAutorizaConvocatoriaDTO toDto(PersonaGeneralEntity entity);

    List<PersonaAutorizaConvocatoriaDTO> toDtoList(
            List<PersonaGeneralEntity> entities);

    @Named("buildNombreCompleto")
    default String buildNombreCompleto(PersonaNaturalGeneralEntity natural) {
        if (natural == null) {
            return "";
        }
        return Stream.of(
                        natural.getPrimerNombre(),
                        natural.getSegundoNombre(),
                        natural.getPrimerApellido(),
                        natural.getSegundoApellido())
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
    }
}
