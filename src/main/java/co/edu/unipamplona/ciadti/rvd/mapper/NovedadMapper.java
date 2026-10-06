/**
 * Aplicación: rvd
 * Archivo: NovedadMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 01/09/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.NovedadListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;

@Mapper(componentModel = "spring")
public interface NovedadMapper {

    NovedadListadoDTO toNovedadListadoDTO(
            NovedadEntity entity
    );

    List<NovedadListadoDTO> toNovedadListadoDTOList(
            List<NovedadEntity> list
    );
}