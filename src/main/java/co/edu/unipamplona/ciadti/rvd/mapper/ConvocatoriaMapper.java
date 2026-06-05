package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NivelEducativoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PeriodoUniversidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;

@Mapper(
        componentModel = "spring",
        uses = PersonaAutorizaConvocatoriaMapper.class)
public interface ConvocatoriaMapper {

    @Mapping(source = "convocatoria.id", target = "id")
    @Mapping(source = "convocatoria.descripcion", target = "descripcion")
    @Mapping(
            source = "periodo",
            target = "periodoUniversidad",
            qualifiedByName = "formatPeriodoUniversidad")
    @Mapping(source = "nivel.descripcion", target = "nivelEducativo")
    @Mapping(source = "fechaCnv.fechaInicio", target = "fechaInicio")
    @Mapping(source = "fechaCnv.fechaFin", target = "fechaFin")
    @Mapping(
            source = "autoriza.personaNaturalGeneral",
            target = "nombreCompleto",
            qualifiedByName = "buildNombreCompleto")
    ConvocatoriaDTO toListDto(
            ConvocatoriaEntity convocatoria,
            PeriodoUniversidadEntity periodo,
            NivelEducativoEntity nivel,
            FechasConvocatoriaEntity fechaCnv,
            PersonaGeneralEntity autoriza);

    @Named("formatPeriodoUniversidad")
    default String formatPeriodoUniversidad(PeriodoUniversidadEntity periodo) {
        if (periodo == null || periodo.getAno() == null) {
            return "";
        }
        String periodoStr = periodo.getPeriodo() != null
                ? periodo.getPeriodo()
                : "";
        return periodo.getAno() + " - " + periodoStr;
    }
}
