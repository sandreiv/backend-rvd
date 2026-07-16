package co.edu.unipamplona.ciadti.rvd.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
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
}
