package co.edu.unipamplona.ciadti.rvd.mapper;

import java.sql.Clob;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.AnexosSolicitudCdpDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MetodologiaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenSolicitudCdpDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.SolicitudCdpListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ResumenSolicitudCdpProjection;

@Mapper(componentModel = "spring")
public interface SolicitudCdpMapper {
    
    @Mapping(target = "id", source = "idCoordinacion")
    @Mapping(target = "nombre", source = "nombreCoordinacion")
    @Mapping(target = "descripcion", source = "descripcionCoordinacion")
    @Mapping(target = "unidadRegional", source = ".", qualifiedByName = "toUnidadRegional")
    @Mapping(target = "unidadArea", source = ".", qualifiedByName = "toUnidadArea")
    @Mapping(target = "metodologia", source = ".", qualifiedByName = "toMetodologia")
    @Mapping(target = "modalidad", source = ".", qualifiedByName = "toModalidad")
    @Mapping(target = "solicitud", source = ".", qualifiedByName = "toSolicitud")
    @Mapping(target = "periodoUniversidad", source = ".", qualifiedByName = "toPeriodo")
    ResumenSolicitudCdpDTO toDto(ResumenSolicitudCdpProjection projection);

    List<ResumenSolicitudCdpDTO> toDtoList(List<ResumenSolicitudCdpProjection> projections);

    @Named("toUnidadRegional")
    default UnidadDTO toUnidadRegional(ResumenSolicitudCdpProjection projection) {
        return new UnidadDTO(
                projection.getIdUnidadRegional(),
                projection.getNombreUnidadRegional());
    }

    @Named("toUnidadArea")
    default UnidadDTO toUnidadArea(ResumenSolicitudCdpProjection projection) {
        return new UnidadDTO(
                projection.getIdUnidadArea(),
                projection.getNombreUnidadArea());
    }

    @Named("toMetodologia")
    default MetodologiaDTO toMetodologia(ResumenSolicitudCdpProjection projection) {
        return new MetodologiaDTO(
                projection.getIdMetodologia(),
                projection.getDescripcionMetodologia());
    }

    @Named("toModalidad")
    default ModalidadDTO toModalidad(ResumenSolicitudCdpProjection projection) {
        return new ModalidadDTO(
                projection.getIdModalidad(),
                projection.getDescripcionModalidad(),
                null);
    }

    @Named("toSolicitud")
    default SolicitudCdpListadoDTO toSolicitud(ResumenSolicitudCdpProjection projection) {
        return new SolicitudCdpListadoDTO(
                projection.getIdSolicitud(),
                projection.getEstadoSolicitud(),
                projection.getObservacionSolicitud(),
                toAnexos(projection.getAdjuntoSolicitud()));
    }

    @Named("toPeriodo")
    default PeriodoUniversidadDTO toPeriodo(ResumenSolicitudCdpProjection projection) {
        return new PeriodoUniversidadDTO(
                projection.getIdPeriodoUniversidad(),
                projection.getAnioPeriodo(),
                projection.getDescripcionPeriodo());
    }

    default List<AnexosSolicitudCdpDTO> toAnexos(Clob adjunto) {
        if (adjunto == null) {
            return List.of();
        }

        try {
            String json = adjunto.getSubString(1, (int) adjunto.length());

            ObjectMapper objectMapper = new ObjectMapper();
            
            return objectMapper.readValue(
                json,
                new TypeReference<List<AnexosSolicitudCdpDTO>>() {}
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "No fue posible convertir el CLOB de adjuntos a AnexosSolicitudCdpDTO",
                e
            );
        }
    }
}