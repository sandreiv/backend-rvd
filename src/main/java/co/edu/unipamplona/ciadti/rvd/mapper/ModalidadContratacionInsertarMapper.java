package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionInsertarDTO;

import jakarta.persistence.Tuple;

@Mapper(componentModel = "spring")
public interface ModalidadContratacionInsertarMapper {

    default ModalidadContratacionInsertarDTO toDto(Tuple row) {
        return new ModalidadContratacionInsertarDTO(
                row.get("id", Number.class).longValue(),
                row.get("vacaciones", Number.class).longValue(),
                row.get("fechaInicio", Date.class),
                row.get("fechaFin", Date.class),
                row.get("semanas", String.class));
    }

    default List<ModalidadContratacionInsertarDTO> toDtoList(List<Tuple> rows) {
        return rows.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
