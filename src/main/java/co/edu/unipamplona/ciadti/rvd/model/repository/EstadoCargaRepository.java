package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoCargaEntity;

public interface EstadoCargaRepository extends JpaRepository<EstadoCargaEntity, Long> {
    Optional<EstadoCargaEntity> findByNombre(String nombre);
}
