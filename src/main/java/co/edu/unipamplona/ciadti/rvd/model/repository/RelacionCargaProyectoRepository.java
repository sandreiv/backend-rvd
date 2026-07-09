package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;

public interface RelacionCargaProyectoRepository extends JpaRepository<RelacionCargaProyectoEntity, Long> {

    void deleteByIdDetalleCargaDocente(Long idDetalleCargaDocente);
}
