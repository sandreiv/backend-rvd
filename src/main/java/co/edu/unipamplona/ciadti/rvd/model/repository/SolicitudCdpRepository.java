package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.SolicitudCdpEntity;

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