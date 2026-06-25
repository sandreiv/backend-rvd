package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.FechaModalidadProjection;

@Mapper(componentModel = "spring")
public interface FechasConvocatoriaMapper {

    FechasConvocatoriaFormularioDTO toFormularioDto(FechasConvocatoriaEntity entity);

    List<FechasConvocatoriaFormularioDTO> toFormularioDtoList(List<FechasConvocatoriaEntity> entities);

    @Mapping(target = "rangoHoras", source = ".", qualifiedByName = "buildRangoHoras")
    FechaModalidadFormularioDTO toModalidadDto(FechaModalidadProjection projection);

    List<FechaModalidadFormularioDTO> toModalidadDtoList(List<FechaModalidadProjection> projections);

    @Named("buildRangoHoras")
    default String buildRangoHoras(FechaModalidadProjection projection) {
        if (projection.getMinimo() == null && projection.getMaximo() == null) {
            return null;
        }
        return projection.getMinimo() + " - " + projection.getMaximo();
    }
}
