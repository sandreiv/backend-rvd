package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipamplona.ciadti.rvd.model.dto.CargaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MetodologiaDTO;
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
    @Mapping(target = "convocatoria", source = ".", qualifiedByName = "toConvocatoria")
    @Mapping(target = "carga", source = ".", qualifiedByName = "toCarga")
    CoordinacionDTO toDto(CoordinacionListadoProjection projection);

    List<CoordinacionDTO> toDtoList(List<CoordinacionListadoProjection> projections);

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
                projection.getDescripcionModalidad());
    }

    @Named("toConvocatoria")
    default ConvocatoriaListadoDTO toConvocatoria(
            CoordinacionListadoProjection projection) {
        if (projection.getIdConvocatoria() == null) {
            return null;
        }
        return new ConvocatoriaListadoDTO(
                projection.getIdConvocatoria(),
                projection.getNombreConvocatoria(),
                projection.getDescripcionConvocatoria(),
                projection.getEstadoConvocatoria(),
                toNivelEducativo(projection),
                toPeriodoUniversidad(projection));
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
