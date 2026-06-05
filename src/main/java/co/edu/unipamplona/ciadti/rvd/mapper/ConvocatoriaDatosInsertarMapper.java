package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDatosInsertarDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;

@Mapper(
        componentModel = "spring",
        uses = {
            PersonaAutorizaConvocatoriaMapper.class,
            PeriodoUniversidadMapper.class,
            NivelEducativoMapper.class
        })
public interface ConvocatoriaDatosInsertarMapper {

    @Mapping(source = "convocatoria.nombre", target = "nombre")
    @Mapping(source = "convocatoria.descripcion", target = "descripcion")
    @Mapping(source = "autoriza", target = "autoriza")
    @Mapping(source = "periodoUniversidad", target = "periodo")
    @Mapping(source = "nivelEducativo", target = "nivelEducativo")
    ConvocatoriaDatosInsertarDTO toDto(
            ConvocatoriaEntity convocatoria,
            PersonaGeneralEntity autoriza,
            PeriodoUniversidadEntity periodoUniversidad,
            NivelEducativoEntity nivelEducativo);
}
