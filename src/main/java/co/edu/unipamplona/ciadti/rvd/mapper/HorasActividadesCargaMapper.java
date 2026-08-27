/**
 * Aplicación: rvd
 * Archivo: HorasActividadesCargaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/08/2026
 * Modificaciones:
 * 27/08/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.HorasActividadPadreDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasActividadPadreProjection;

@Mapper(componentModel = "spring")
public interface HorasActividadesCargaMapper {

    HorasActividadPadreDTO toDto(HorasActividadPadreProjection projection);

    List<HorasActividadPadreDTO> toDtoList(
            List<HorasActividadPadreProjection> projections);
}
