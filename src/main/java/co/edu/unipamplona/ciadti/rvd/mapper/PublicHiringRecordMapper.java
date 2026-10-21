package co.edu.unipamplona.ciadti.rvd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unipamplona.ciadti.rvd.model.dto.PublicHiringRecordDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PublicHiringRecordProjection;

@Mapper(componentModel = "spring")
public interface PublicHiringRecordMapper {

    PublicHiringRecordDTO toDto(
            PublicHiringRecordProjection projection
    );

    List<PublicHiringRecordDTO> toDtoList(
            List<PublicHiringRecordProjection> projections
    );
}