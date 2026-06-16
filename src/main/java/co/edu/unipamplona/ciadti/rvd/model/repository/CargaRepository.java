package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;

public interface CargaRepository extends JpaRepository<CargaEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);

    boolean existsByIdCoordinacionAndIdConvocatoria(Long idCoordinacion, Long idConvocatoria);
}
