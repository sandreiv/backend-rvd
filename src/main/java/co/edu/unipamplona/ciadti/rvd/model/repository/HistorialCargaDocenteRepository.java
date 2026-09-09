package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.HistorialCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HistorialCargaDocenteObservacionProjection;

public interface HistorialCargaDocenteRepository extends JpaRepository<HistorialCargaDocenteEntity, Long>{
    
    List<HistorialCargaDocenteEntity> findByIdCargaDocente(Long idCargaDocente);

    @Query(value = """
            SELECT
                HICD.HICD_ID AS idHistorial,
                HICD.PEGE_IDREGISTRA AS idPersonaGeneral,
                TRIM(
                    PENG.PENG_PRIMERAPELLIDO || ' ' ||
                    NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                    PENG.PENG_PRIMERNOMBRE || ' ' ||
                    NVL(PENG.PENG_SEGUNDONOMBRE, '')
                ) AS nombrePersonaGeneral,
                HICD.HICD_PEGEROL AS rolPersonaGeneral,
                HICD.HICD_OBSERVACION AS observacion,
                HICD.HICD_FECHA AS fecha,
                HICD.HICD_ESTADO AS estado
            FROM RVD.HISTORIALCARGADOCENTE HICD
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = HICD.PEGE_IDREGISTRA
            WHERE HICD.CADO_ID = :idCargaDocente
            AND HICD.HICD_OBSERVACION IS NOT NULL
            AND TRIM(HICD.HICD_OBSERVACION) IS NOT NULL
            ORDER BY HICD.HICD_FECHA DESC, HICD.HICD_ID DESC
            """, nativeQuery = true)
    List<HistorialCargaDocenteObservacionProjection> findObservationsByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente
    );

    @Procedure(name = "HistorialCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
        @Param("P_HICD_ID") Long id,
        @Param("P_HICD_REGISTRADOPOR") String registradoPor
    );
}
