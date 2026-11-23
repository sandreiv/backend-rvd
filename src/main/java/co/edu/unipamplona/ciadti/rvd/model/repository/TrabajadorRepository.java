package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorEntity;

public interface TrabajadorRepository extends JpaRepository<TrabajadorEntity, Long> {
    
    boolean existsByIdPersonaGeneral(Long idPersonaGeneral);
}
