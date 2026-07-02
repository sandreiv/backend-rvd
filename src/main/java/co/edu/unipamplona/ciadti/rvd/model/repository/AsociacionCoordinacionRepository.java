package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.AsociacionCoordinacionEntity;

public interface AsociacionCoordinacionRepository
        extends JpaRepository<AsociacionCoordinacionEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);
}
