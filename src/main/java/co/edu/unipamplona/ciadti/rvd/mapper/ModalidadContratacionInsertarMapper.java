package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionInsertarDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;

@Mapper(componentModel = "spring")
public interface ModalidadContratacionInsertarMapper {

    default ModalidadContratacionInsertarDTO toDto(
            FechasConvocatoriaEntity feco,
            Long idModalidadContratacion) {
        return new ModalidadContratacionInsertarDTO(
                idModalidadContratacion,
                feco.getVacaciones(),
                feco.getFechaInicio(),
                feco.getFechaFin(),
                feco.getSemanas());
    }

    default List<ModalidadContratacionInsertarDTO> toDtoList(
            List<FechasConvocatoriaEntity> fechas,
            Map<Long, Long> mocoByCotc) {
        return fechas.stream()
                .map(feco -> toDto(
                        feco,
                        mocoByCotc.get(feco.getIdConvocatoriaTipoContratacion())))
                .collect(Collectors.toList());
    }

}
