package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaGeneralDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;

@Mapper(componentModel = "spring")
public interface PersonaGeneralMapper {

    @Mapping(
            target = "tipoDocumentoIdentidadNombre",
            source = "tipoDocumentoIdentidad.descripcion")
    @Mapping(
            target = "tipoDocumentoIdentidadAbreviatura",
            source = "tipoDocumentoIdentidad.abreviatura")
    PersonaGeneralDTO toPersonaGeneralDTO(PersonaGeneralEntity entity);

    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "tipoDocumentoIdentidad", ignore = true)
    @Mapping(target = "personaGeneralFoto", ignore = true)
    @Mapping(target = "personaNaturalGeneral.personaGeneral", ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.estadoCivil.registradoPor",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.estadoCivil.fechaCambio",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.lugarNacimiento.registradoPor",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.lugarNacimiento.fechaCambio",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.nacionalidad.registradoPor",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.nacionalidad.fechaCambio",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.nacimiento.registradoPor",
            ignore = true)
    @Mapping(
            target = "personaNaturalGeneral.nacimiento.fechaCambio",
            ignore = true)
    PersonaGeneralEntity toPersonaGeneralEntity(PersonaGeneralDTO dto);

    List<PersonaGeneralDTO> toPersonaGeneralDTOList(List<PersonaGeneralEntity> list);
}
