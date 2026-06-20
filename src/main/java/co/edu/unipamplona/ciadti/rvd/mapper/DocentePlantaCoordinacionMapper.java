package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaNaturalGeneralEntity;

@Mapper(componentModel = "spring")
public interface DocentePlantaCoordinacionMapper {

    @Mapping(target = "idPersonaGeneral", source = "personaGeneral.id")
    @Mapping(
            target = "nombreCompleto",
            source = "personaGeneral.personaNaturalGeneral",
            qualifiedByName = "buildNombreCompleto")
    @Mapping(target = "estado", source = "estado")
    DocentePlantaCoordinacionDTO toDto(DocentesPlantaCoordinacionEntity entity);

    List<DocentePlantaCoordinacionDTO> toDtoList(
            List<DocentesPlantaCoordinacionEntity> entities);

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
