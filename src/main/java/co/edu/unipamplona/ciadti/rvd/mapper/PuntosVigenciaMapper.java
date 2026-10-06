/**
 * Aplicación: rvd
 * Archivo: PuntosVigenciaMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 01/09/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PuntosVigenciaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;

@Mapper(componentModel = "spring")
public interface PuntosVigenciaMapper {

    PuntosVigenciaListadoDTO toListadoDTO(
            PuntosVigenciaEntity entity
    );

    List<PuntosVigenciaListadoDTO> toListadoDTOList(
            List<PuntosVigenciaEntity> list
    );
}