/**
 * Aplicación: rvd
 * Archivo: TipoProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.TipoProyectoEntity;

@Mapper(componentModel = "spring")
public interface TipoProyectoMapper {

    TipoProyectoListaDTO toTipoProyectoListaDTO(TipoProyectoEntity entity);

    List<TipoProyectoListaDTO> toTipoProyectoListaDTOList(List<TipoProyectoEntity> list);
}
