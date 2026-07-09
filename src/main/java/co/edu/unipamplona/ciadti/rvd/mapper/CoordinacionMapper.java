package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MetodologiaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelEducativoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;

@Mapper(componentModel = "spring")
public interface CoordinacionMapper {

    @Mapping(target = "id", source = "idCoordinacion")
    @Mapping(target = "nombre", source = "nombreCoordinacion")
    @Mapping(target = "descripcion", source = "descripcionCoordinacion")
    @Mapping(target = "unidadRegional", source = ".", qualifiedByName = "toUnidadRegional")
    @Mapping(target = "unidadArea", source = ".", qualifiedByName = "toUnidadArea")
    @Mapping(target = "metodologia", source = ".", qualifiedByName = "toMetodologia")
    @Mapping(target = "modalidad", source = ".", qualifiedByName = "toModalidad")
    @Mapping(target = "convocatoria", ignore = true)
    @Mapping(target = "carga", source = ".", qualifiedByName = "toCarga")
    @Mapping(target = "centroCosto", source = ".", qualifiedByName = "toCentroCosto")
    CoordinacionDTO toDto(CoordinacionListadoProjection projection);

    default List<CoordinacionDTO> toDtoList(
            List<CoordinacionListadoProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        Map<Long, List<CoordinacionListadoProjection>> grouped =
                new LinkedHashMap<>();
        for (CoordinacionListadoProjection projection : projections) {
            grouped.computeIfAbsent(
                    projection.getIdCoordinacion(),
                    key -> new ArrayList<>())
                    .add(projection);
        }
        return grouped.values().stream()
                .map(this::toDtoFromGroup)
                .toList();
    }

    default CoordinacionDTO toDtoFromGroup(
            List<CoordinacionListadoProjection> group) {
        CoordinacionListadoProjection first = group.getFirst();
        CoordinacionDTO dto = toDto(first);
        return new CoordinacionDTO(
                dto.id(),
                dto.nombre(),
                dto.descripcion(),
                dto.codigo(),
                dto.esAcademica(),
                dto.unidadRegional(),
                dto.unidadArea(),
                dto.metodologia(),
                dto.modalidad(),
                toConvocatoria(first, collectModalidadesContratacion(group)),
                dto.carga(),
                dto.centroCosto());
    }

    @Named("toCentroCosto")
    default CentroCostoDTO toCentroCosto(CoordinacionListadoProjection projection) {
        if (projection.getIdCentroCosto() == null) {
            return null;
        }
        return new CentroCostoDTO(
                projection.getIdCentroCosto(),
                projection.getDescripcionCentroCosto());
    }

    @Named("toUnidadRegional")
    default UnidadDTO toUnidadRegional(CoordinacionListadoProjection projection) {
        return new UnidadDTO(
                projection.getIdUnidadRegional(),
                projection.getNombreUnidadRegional());
    }

    @Named("toUnidadArea")
    default UnidadDTO toUnidadArea(CoordinacionListadoProjection projection) {
        return new UnidadDTO(
                projection.getIdUnidadArea(),
                projection.getNombreUnidadArea());
    }

    @Named("toMetodologia")
    default MetodologiaDTO toMetodologia(CoordinacionListadoProjection projection) {
        return new MetodologiaDTO(
                projection.getIdMetodologia(),
                projection.getDescripcionMetodologia());
    }

    @Named("toModalidad")
    default ModalidadDTO toModalidad(CoordinacionListadoProjection projection) {
        return new ModalidadDTO(
                projection.getIdModalidad(),
                projection.getDescripcionModalidad(),
                null);
    }

    default ConvocatoriaListadoDTO toConvocatoria(
            CoordinacionListadoProjection projection,
            List<ModalidadContratacionListadoDTO> modalidadesContratacion) {
        if (projection.getIdConvocatoria() == null) {
            return null;
        }
        return new ConvocatoriaListadoDTO(
                projection.getIdConvocatoria(),
                projection.getNombreConvocatoria(),
                projection.getDescripcionConvocatoria(),
                projection.getEstadoConvocatoria(),
                toNivelEducativo(projection),
                toPeriodoUniversidad(projection),
                modalidadesContratacion);
    }

    default List<ModalidadContratacionListadoDTO> collectModalidadesContratacion(
            List<CoordinacionListadoProjection> group) {
        Map<Long, ModalidadContratacionListadoDTO> modalidades =
                new LinkedHashMap<>();
        for (CoordinacionListadoProjection projection : group) {
            if (projection.getIdModalidadContratacion() == null) {
                continue;
            }
            modalidades.putIfAbsent(
                    projection.getIdModalidadContratacion(),
                    new ModalidadContratacionListadoDTO(
                            projection.getIdModalidadContratacion(),
                            projection.getNombreModalidadContratacion(),
                            esModalidadPlanta(
                                    projection.getNombreModalidadContratacion())));
        }
        return new ArrayList<>(modalidades.values());
    }

    default boolean esModalidadPlanta(String nombreModalidadContratacion) {
        if (nombreModalidadContratacion == null
                || nombreModalidadContratacion.isBlank()) {
            return false;
        }
        return "planta".equalsIgnoreCase(nombreModalidadContratacion.trim());
    }

    @Named("toCarga")
    default CargaListadoDTO toCarga(CoordinacionListadoProjection projection) {
        if (projection.getIdCarga() == null) {
            return null;
        }
        return new CargaListadoDTO(
                projection.getIdCarga(),
                toEstadoCarga(projection));
    }

    default NivelEducativoDTO toNivelEducativo(
            CoordinacionListadoProjection projection) {
        if (projection.getIdNivelEducativo() == null) {
            return null;
        }
        return new NivelEducativoDTO(
                projection.getIdNivelEducativo(),
                projection.getDescripcionNivelEducativo());
    }

    default PeriodoUniversidadDTO toPeriodoUniversidad(
            CoordinacionListadoProjection projection) {
        if (projection.getIdPeriodoUniversidad() == null) {
            return null;
        }
        return new PeriodoUniversidadDTO(
                projection.getIdPeriodoUniversidad(),
                projection.getAnioPeriodo(),
                projection.getDescripcionPeriodo());
    }

    default EstadoCargaDTO toEstadoCarga(CoordinacionListadoProjection projection) {
        if (projection.getIdEstadoCarga() == null) {
            return null;
        }
        return new EstadoCargaDTO(
                projection.getIdEstadoCarga(),
                projection.getNombreEstadoCarga(),
                projection.getDescripcionEstadoCarga());
    }
}
