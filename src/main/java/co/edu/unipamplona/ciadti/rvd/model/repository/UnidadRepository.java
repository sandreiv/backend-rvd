package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.UnidadEntity;

public interface UnidadRepository extends JpaRepository<UnidadEntity, Long> {

    @Query("""
            SELECT u FROM UnidadEntity u
            INNER JOIN CoordinacionesEntity c ON u.id = c.idRegional
            WHERE c.id = :idCoordinacion
                AND u.regional = '1'
            ORDER BY u.nombre
            """)
    List<UnidadEntity> findRegionalUnits(@Param("idCoordinacion") Long idCoordinacion);
}
