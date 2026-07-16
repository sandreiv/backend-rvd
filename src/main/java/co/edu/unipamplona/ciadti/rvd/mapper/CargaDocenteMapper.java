/**
 * Aplicación: rvd
 * Archivo: CargaDocenteMapper.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.mapper
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/07/2026
 * Modificaciones:
 * 01/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;

@Mapper(componentModel = "spring")
public interface CargaDocenteMapper {

    @Mapping(target = "idFechasConvocatoria", source = "fechasConvocatoria.id")
    @Mapping(target = "fechaInicio", source = "fechasConvocatoria.fechaInicio")
    @Mapping(target = "fechaFin", source = "fechasConvocatoria.fechaFin")
    @Mapping(target = "salario", source = "asignacionSalarial")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idTipoNovedad", ignore = true)
    @Mapping(target = "idNovedad", ignore = true)
    @Mapping(target = "fechaNovedad", ignore = true)
    @Mapping(target = "observacionNovedad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "vigente", ignore = true)
    @Mapping(target = "nivelFormacion", ignore = true)
    @Mapping(target = "momento", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "carga", ignore = true)
    @Mapping(target = "personaGeneral", ignore = true)
    @Mapping(target = "modalidadContratacion", ignore = true)
    @Mapping(target = "categoriaCatedratico", ignore = true)
    @Mapping(target = "fechaConvocatoria", ignore = true)
    @Mapping(target = "tipoNovedad", ignore = true)
    @Mapping(target = "novedad", ignore = true)
    @Mapping(target = "horas", ignore = true)
    CargaDocenteEntity toEntity(CargaDocenteFormularioDTO dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(CargaDocenteFormularioDTO dto, @MappingTarget CargaDocenteEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCategoriaCatedratico", ignore = true)
    @Mapping(target = "idFechasConvocatoria", ignore = true)
    @Mapping(target = "idTipoNovedad", ignore = true)
    @Mapping(target = "idNovedad", ignore = true)
    @Mapping(target = "fechaNovedad", ignore = true)
    @Mapping(target = "observacionNovedad", ignore = true)
    @Mapping(target = "fechaInicio", ignore = true)
    @Mapping(target = "fechaFin", ignore = true)
    @Mapping(target = "valorContrato", ignore = true)
    @Mapping(target = "valorPrestaciones", ignore = true)
    @Mapping(target = "salario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "vigente", ignore = true)
    @Mapping(target = "horas", ignore = true)
    @Mapping(target = "valorHora", ignore = true)
    @Mapping(target = "puntos", ignore = true)
    @Mapping(target = "valorPunto", ignore = true)
    @Mapping(target = "totalContrato", ignore = true)
    @Mapping(target = "semanas", ignore = true)
    @Mapping(target = "nivelFormacion", ignore = true)
    @Mapping(target = "momento", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "fechaCambio", ignore = true)
    @Mapping(target = "cargas", ignore = true)
    @Mapping(target = "personasGenerales", ignore = true)
    @Mapping(target = "modalidadesContratacion", ignore = true)
    @Mapping(target = "categoriasCatedratico", ignore = true)
    @Mapping(target = "fechasConvocatoria", ignore = true)
    @Mapping(target = "tiposNovedad", ignore = true)
    @Mapping(target = "novedades", ignore = true)
    CargaDocenteEntity toEntityFromPlanta(CargaDocentePlantaDTO dto);
}
