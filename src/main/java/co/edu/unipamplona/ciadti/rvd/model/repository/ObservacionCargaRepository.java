package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ObservacionesCargaProjection;

public interface ObservacionCargaRepository extends JpaRepository<ObservacionCargaEntity, Long>{
    
    @Query(value = """
            SELECT
                OBCA.PEGE_IDREGISTRA AS idPersonaGeneral,
                TRIM(
                    PENG.PENG_PRIMERAPELLIDO || ' ' ||
                    NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                    PENG.PENG_PRIMERNOMBRE || ' ' ||
                    NVL(PENG.PENG_SEGUNDONOMBRE, '')
                ) AS nombrePersonaGeneral,
                OBCA.OBCA_PEGEROL AS rolPersonaGeneral,
                OBCA.OBSE_TEXTO AS observacion,
                OBCA.OBSE_FECHA AS fecha
            FROM RVD.OBSERVACIONCARGA OBCA
            INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = OBCA.PEGE_IDREGISTRA
            WHERE OBCA.CARG_ID = :idCarga
            ORDER BY OBCA.OBSE_FECHA ASC
            """, nativeQuery = true)
    List<ObservacionesCargaProjection> findAllWithPreload(
        @Param("idCarga") Long idCarga
    );
}
