package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.MateriaListadoProjection;

@Mapper(componentModel = "spring")
public interface MateriaMapper {

    @Mapping(target = "horasDirectas", expression = "java(calcularHorasDirectas(projection))")
    @Mapping(target = "tieneGrupo", ignore = true)
    MateriaDTO toDto(MateriaListadoProjection projection);

    @Mapping(target = "horasDirectas", expression = "java(calcularHorasDirectas(projection))")
    @Mapping(target = "tieneGrupo", source = "tieneGrupo")
    MateriaDTO toDto(MateriaListadoProjection projection, boolean tieneGrupo);

    List<MateriaDTO> toDtoList(List<MateriaListadoProjection> projections);

    default Long calcularHorasDirectas(MateriaListadoProjection projection) {
        long practicas = projection.getHorasPracticas() != null
                ? projection.getHorasPracticas() : 0L;
        long teoricas = projection.getHorasTeoricas() != null
                ? projection.getHorasTeoricas() : 0L;
        return practicas + teoricas;
    }
}
