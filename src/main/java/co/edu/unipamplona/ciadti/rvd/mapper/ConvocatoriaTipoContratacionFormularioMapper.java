package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaTipoContratacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaTipoContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;

@Mapper(componentModel = "spring")
public interface ConvocatoriaTipoContratacionFormularioMapper {

    default FechaModalidadFormularioDTO toFechaDto(FechasConvocatoriaEntity entity) {
        return new FechaModalidadFormularioDTO(
                entity.getId(),
                entity.getVacaciones(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getSemanas(),
                null);
    }

    default List<ConvocatoriaTipoContratacionFormularioDTO> toFormularioDtoList(List<ConvocatoriaTipoContratacionEntity> cotcList, List<FechasConvocatoriaEntity> fechasModalidad) {
        Map<Long, List<FechasConvocatoriaEntity>> fechasByCotc =
                fechasModalidad.stream()
                        .collect(Collectors.groupingBy(
                                FechasConvocatoriaEntity
                                        ::getIdConvocatoriaTipoContratacion));
        return cotcList.stream()
                .map(cotc -> new ConvocatoriaTipoContratacionFormularioDTO(
                        cotc.getId(),
                        cotc.getIdModalidadContratacion(),
                        fechasByCotc.getOrDefault(cotc.getId(), List.of())
                                .stream()
                                .map(this::toFechaDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
