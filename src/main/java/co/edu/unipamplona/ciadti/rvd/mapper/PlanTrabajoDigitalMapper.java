/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/10/2026
 * Modificaciones:
 * 14/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActivityWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdSavePayloadDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;

@Mapper(componentModel = "spring")
public interface PlanTrabajoDigitalMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "detalleCargaDocenteEntity", ignore = true)
    @Mapping(target = "detalleNovedadCargaDocenteEntity", ignore = true)
    @Mapping(target = "tipoActividadesEntity", ignore = true)
    @Mapping(target = "personaGeneralEntity", ignore = true)
    PlanTrabajoDigitalEntity toEntity(PtdSavePayloadDTO dto);

    ActivityWeightsDTO toActivityWeightsDTO(PlanTrabajoDigitalEntity entity);
}
