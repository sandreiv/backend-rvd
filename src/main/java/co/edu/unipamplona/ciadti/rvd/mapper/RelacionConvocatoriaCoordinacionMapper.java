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
    @Mapping(target = "convocatoria", ignore = true)
    @Mapping(target = "coordinacion", ignore = true)
    @Mapping(target = "centroCosto", ignore = true)
    @Mapping(target = "estadoCarga", ignore = true)
    CargaEntity toEntity(RelacionConvocatoriaCoordinacionDTO dto);
}
