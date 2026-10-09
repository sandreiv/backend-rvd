/**
 * Aplicación: rvd
 * Archivo: InformacionDocenteContratacionMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 07/10/2026
 * Modificaciones:
 * 07/10/2026 - Andrés Hernández - Creación inicial
 * 08/10/2026 - Sebastian Jaimes - Combina datos del docente con horas PTD
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.InformacionDocenteContratacionProjection;

@Mapper(componentModel = "spring")
public interface InformacionDocenteContratacionMapper {

    @Mapping(target = "horasActividades", ignore = true)
    InformacionDocenteContratacionDTO toDto(InformacionDocenteContratacionProjection projection);

    default InformacionDocenteContratacionDTO toDto(
            InformacionDocenteContratacionProjection projection,
            List<ActividadHorasResumenDTO> horasActividades) {
        InformacionDocenteContratacionDTO base = toDto(projection);
        if (base == null) {
            return null;
        }
        return new InformacionDocenteContratacionDTO(
                base.idPersonaGeneral(),
                base.nombreCompleto(),
                base.documentoIdentidad(),
                base.direccionDomicilio(),
                base.correoPersonal(),
                base.correoInstitucional(),
                base.modalidadContratacion(),
                base.fechaInicio(),
                base.fechaFin(),
                base.categoriaDocente(),
                base.puntos(),
                horasActividades == null ? List.of() : horasActividades);
    }
}
