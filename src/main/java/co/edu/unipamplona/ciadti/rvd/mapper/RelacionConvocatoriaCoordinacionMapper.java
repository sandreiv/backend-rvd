package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;

@Mapper(componentModel = "spring")
public interface RelacionConvocatoriaCoordinacionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCentroCosto", ignore = true)
    @Mapping(target = "idEstadoCarga", ignore = true)
    @Mapping(target = "valor", ignore = true)
    @Mapping(target = "valorAutorizado", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "convocatorias", ignore = true)
    @Mapping(target = "coordinaciones", ignore = true)
    @Mapping(target = "centrosCosto", ignore = true)
    @Mapping(target = "estadosCarga", ignore = true)
    CargaEntity toEntity(RelacionConvocatoriaCoordinacionDTO dto);
}
