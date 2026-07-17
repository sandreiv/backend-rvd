package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionBusquedaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionRestriccionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionPorCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionRestriccionProjection;

@Mapper(componentModel = "spring")
public interface RestriccionPorCoordinacionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "fechaConvocatoria", ignore = true)
    @Mapping(target = "coordinacion", ignore = true)
    RestriccionPorCoordinacionEntity toEntity(CoordinacionRestriccionFormularioDTO dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(CoordinacionRestriccionFormularioDTO dto, @MappingTarget RestriccionPorCoordinacionEntity entity);

    @Mapping(target = "coordinacion", source = ".")
    CoordinacionRestriccionDTO toDto(CoordinacionRestriccionProjection projection);

    List<CoordinacionRestriccionDTO> toDtoList(List<CoordinacionRestriccionProjection> projections);

    @Mapping(target = "id", source = "idCoordinacion")
    @Mapping(target = "nombre", source = "nombreCoordinacion")
    @Mapping(target = "descripcion", source = "descripcionCoordinacion")
    @Mapping(target = "codigo", source = "codigoCoordinacion")
    CoordinacionBusquedaDTO toCoordinacionBusqueda(
            CoordinacionRestriccionProjection projection);
}
