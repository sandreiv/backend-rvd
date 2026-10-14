/**
 * Aplicación: rvd
 * Archivo: ParametrosContratacionMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ParametrosContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ParametrosContratacionProjection;

@Mapper(componentModel = "spring")
public interface ParametrosContratacionMapper {

    ParametrosContratacionDTO toDto(ParametrosContratacionProjection projection);
}
