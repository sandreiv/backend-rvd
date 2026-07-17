/**
 * Aplicación: rvd
 * Archivo: RelacionCargaProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionCargaProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;

@Mapper(componentModel = "spring")
public interface RelacionCargaProyectoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idDetalleCargaDocente", source = "idDetalleCargaDocente")
    @Mapping(target = "idPersonaProyecto", source = "dto.idPersonaProyecto")
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    RelacionCargaProyectoEntity toEntity(Long idDetalleCargaDocente, RelacionCargaProyectoDTO dto);
}

/* 17/07/2026 @:Daniel Arias */