package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ProgramaListadoProjection;

@Mapper(componentModel = "spring")
public interface ProgramaMapper {

    @Mapping(target = "centroCosto", source = ".", qualifiedByName = "toCentroCosto")
    ProgramaDTO toDto(ProgramaListadoProjection projection);

    List<ProgramaDTO> toDtoList(List<ProgramaListadoProjection> projections);

    @Named("toCentroCosto")
    default CentroCostoDTO toCentroCosto(ProgramaListadoProjection projection) {
        if (projection.getIdCentroCosto() == null) {
            return null;
        }
        return new CentroCostoDTO(
                projection.getIdCentroCosto(),
                projection.getDescripcionCentroCosto());
    }
}
