package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.SolicitudCdpEntity;

import java.util.Optional;

public interface SolicitudCdpRepository
        extends JpaRepository<SolicitudCdpEntity, Long> {

    boolean existsByIdCoordinacion(
            Long idCoordinacion
    );

    Optional<SolicitudCdpEntity>
            findFirstByIdCoordinacionOrderByIdDesc(
                    Long idCoordinacion
            );
}