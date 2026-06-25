package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EscalafonDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionProjection;

@Mapper(componentModel = "spring")
public interface DocentePreasignacionMapper {

    @Mapping(target = "id", source = "idPersonaGeneral")
    @Mapping(target = "documentoIdentidad", source = "documentoIdentidad")
    @Mapping(target = "nombreCompleto", source = ".", qualifiedByName = "buildNombreCompleto")
    @Mapping(target = "categoriaCatedratico", source = ".", qualifiedByName = "toCategoria")
    @Mapping(target = "escalafon", source = ".", qualifiedByName = "toEscalafon")
    DocentePreasignacionDTO toDto(DocentePreasignacionProjection projection);

    List<DocentePreasignacionDTO> toDtoList(
            List<DocentePreasignacionProjection> projections);

    @Named("buildNombreCompleto")
    default String buildNombreCompleto(DocentePreasignacionProjection projection) {
        return Stream.of(
                        projection.getPrimerNombre(),
                        projection.getSegundoNombre(),
                        projection.getPrimerApellido(),
                        projection.getSegundoApellido())
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
    }

    @Named("toCategoria")
    default CategoriaCatedraticoDTO toCategoria(
            DocentePreasignacionProjection projection) {
        if (projection.getIdCategoriaCatedratico() == null) {
            return null;
        }
        return new CategoriaCatedraticoDTO(
                projection.getIdCategoriaCatedratico(),
                projection.getDescripcionCategoriaCatedratico());
    }

    @Named("toEscalafon")
    default EscalafonDTO toEscalafon(DocentePreasignacionProjection projection) {
        if (projection.getIdEscalafon() == null) {
            return null;
        }
        return new EscalafonDTO(
                projection.getIdEscalafon(),
                projection.getIdCategoriaCatedratico(),
                projection.getIdModalidadContratacion(),
                projection.getIdPersonaGeneral(),
                projection.getPuntos());
    }
}
