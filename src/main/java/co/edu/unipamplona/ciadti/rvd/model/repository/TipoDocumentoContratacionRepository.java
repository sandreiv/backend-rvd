package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoDocumentoContratacionEntity;

public interface TipoDocumentoContratacionRepository
        extends JpaRepository<TipoDocumentoContratacionEntity, Long> {

    List<TipoDocumentoContratacionEntity> findAllByOrderByIdAsc();

}