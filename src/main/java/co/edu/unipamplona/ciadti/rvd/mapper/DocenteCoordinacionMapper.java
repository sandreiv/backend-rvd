package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;

@Mapper(componentModel = "spring")
public interface DocenteCoordinacionMapper {

    @Mapping(target = "fechaInicio", source = "fechaConvocatoriaInicio")
    @Mapping(target = "fechaFin", source = "fechaConvocatoriaFin")
    DocenteCoordinacionDTO toDto(DocenteCargaCoordinacionProjection projection);
    
    List<DocenteCoordinacionDTO> toDtoList(List<DocenteCargaCoordinacionProjection> projections);
}