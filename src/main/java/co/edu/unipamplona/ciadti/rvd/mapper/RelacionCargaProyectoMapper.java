/**
 * Aplicación: rvd
 * Archivo: RelacionCargaProyectoMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 18/09/2026 - Andrés Hernández - Relación de la persona proyecto con detalle novedad
 * 29/09/2026 - Ignora relaciones de solo lectura al crear
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
    @Mapping(target = "idDetalleNovedadCargaDocente", ignore = true)
    @Mapping(target = "idPersonaProyecto", source = "dto.idPersonaProyecto")
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "personaProyecto", ignore = true)
    @Mapping(target = "detalleCargaDocente", ignore = true)
    @Mapping(target = "detalleNovedadCargaDocente", ignore = true)
    RelacionCargaProyectoEntity toEntity(
            Long idDetalleCargaDocente,
            RelacionCargaProyectoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idDetalleCargaDocente", ignore = true)
    @Mapping(
            target = "idDetalleNovedadCargaDocente",
            source = "idDetalleNovedadCargaDocente")
    @Mapping(target = "idPersonaProyecto", source = "dto.idPersonaProyecto")
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "personaProyecto", ignore = true)
    @Mapping(target = "detalleCargaDocente", ignore = true)
    @Mapping(target = "detalleNovedadCargaDocente", ignore = true)
    RelacionCargaProyectoEntity toEntityFromDetalleNovedad(
            Long idDetalleNovedadCargaDocente,
            RelacionCargaProyectoDTO dto);
}

/* 17/07/2026 @:Daniel Arias */