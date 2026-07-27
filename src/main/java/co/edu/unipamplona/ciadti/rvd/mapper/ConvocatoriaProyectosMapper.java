/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ConvocatoriaProyectosListaProjection;

@Mapper(componentModel = "spring")
public interface ConvocatoriaProyectosMapper {

    ConvocatoriaProyectosListaDTO toConvocatoriaProyectosListaDTO(
            ConvocatoriaProyectosListaProjection projection);

    List<ConvocatoriaProyectosListaDTO> toConvocatoriaProyectosListaDTOList(
            List<ConvocatoriaProyectosListaProjection> list);
}
