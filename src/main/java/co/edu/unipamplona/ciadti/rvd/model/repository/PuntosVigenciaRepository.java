package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosVigenciaEntity;

public interface PuntosVigenciaRepository
        extends JpaRepository<PuntosVigenciaEntity, Long> {

    Optional<PuntosVigenciaEntity> findByAnio(
            Long anio
    );

    boolean existsByAnio(
            Long anio
    );

    boolean existsByAnioAndIdNot(
            Long anio,
            Long id
    );

    @Query(value = """
            SELECT
                PUVI.*
            FROM RVD.PUNTOSVIGENCIA PUVI
            ORDER BY PUVI.PUVI_ANIO DESC
            """, nativeQuery = true)
    List<PuntosVigenciaEntity> findAllPointsValidity();

    @Procedure(
        name = "PuntosVigenciaEntity.deleteByProcedure"
    )
    BigDecimal deleteByProcedure(
            @Param("P_PUVI_ID")
            Long id,

            @Param("P_PUVI_REGISTRADOPOR")
            String registradoPor
    );
}