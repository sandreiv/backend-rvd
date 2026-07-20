/**
 * Aplicación: rvd
 * Archivo: TotalPreasignacionMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/07/2026
 * Modificaciones:
 * 20/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TotalHorasPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TotalHorasPreasignacionProjection;

@Mapper(componentModel = "spring")
public interface TotalPreasignacionMapper {

    TotalHorasPreasignacionDTO toHorasDto(
            TotalHorasPreasignacionProjection projection);

    List<TotalHorasPreasignacionDTO> toHorasDtoList(
            List<TotalHorasPreasignacionProjection> projections);
}
