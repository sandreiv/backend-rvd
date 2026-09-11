package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;

public interface NovedadRepository
        extends JpaRepository<NovedadEntity, Long> {

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVD.NOVEDADES NOVE
            ORDER BY
                NOVE.NOVE_ACCION,
                NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedades();

    @Query(value = """
            SELECT
                NOVE.*
            FROM RVD.NOVEDADES NOVE
            WHERE NOVE.NOVE_ACCION = 'ACTUALIZAR'
            ORDER BY
                NOVE.NOVE_ACCION,
                NOVE.NOVE_TIPO
            """, nativeQuery = true)
    List<NovedadEntity> findAllNovedadesDeActualizacion();

    @Procedure(name = "NovedadEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_NOVE_ID")
            Long id,

            @Param("P_NOVE_REGISTRADOPOR")
            String registradoPor
    );
}