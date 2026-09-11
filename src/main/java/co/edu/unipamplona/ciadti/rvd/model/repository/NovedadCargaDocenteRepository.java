package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;

public interface NovedadCargaDocenteRepository
        extends JpaRepository<NovedadCargaDocenteEntity, Long> {

    boolean existsByIdNovedadCatalogo(
            Long idNovedadCatalogo
    );
}