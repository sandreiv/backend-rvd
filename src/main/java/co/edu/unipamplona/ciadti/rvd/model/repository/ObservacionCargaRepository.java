/**
 * Aplicación: rvd
 * Archivo: ObservacionCargaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 25/08/2026
 * Modificaciones:
 * 25/08/2026 - Andrés Hernández - Creación inicial
 * 27/08/2026 - Andrés Hernández - Consulta de observaciones por precarga
 * 28/08/2026 - Andrés Hernández - Manejo de la visibilidad de observaciones
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ObservacionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ObservacionesCargaProjection;

public interface ObservacionCargaRepository extends JpaRepository<ObservacionCargaEntity, Long>{
    
    @Query(value = """
            SELECT
                OBCA.OBCA_ID AS idObservacion,
                OBCA.PEGE_IDREGISTRA AS idPersonaGeneral,
                TRIM(
                    PENG.PENG_PRIMERAPELLIDO || ' ' ||
                    NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                    PENG.PENG_PRIMERNOMBRE || ' ' ||
                    NVL(PENG.PENG_SEGUNDONOMBRE, '')
                ) AS nombrePersonaGeneral,
                OBCA.OBCA_PEGEROL AS rolPersonaGeneral,
                OBCA.OBSE_TEXTO AS observacion,
                OBCA.OBSE_FECHA AS fecha,
                OBCA.OBCA_VISTO AS visto
            FROM RVD.OBSERVACIONCARGA OBCA
            INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = OBCA.PEGE_IDREGISTRA
            WHERE OBCA.CARG_ID = :idCarga
            ORDER BY OBCA.OBSE_FECHA DESC
            """, nativeQuery = true)
    List<ObservacionesCargaProjection> findAllWithPreload(
        @Param("idCarga") Long idCarga
    );

    @Modifying
    @Query(value= """
            UPDATE RVD.OBSERVACIONCARGA OBCA
            SET OBCA.OBCA_VISTO = 1,
                OBCA.OBCA_REGISTRADOPOR = :registradoPor,
                OBCA.OBCA_FECHACAMBIO = SYSDATE
            WHERE OBCA.CARG_ID = :idCarga
            AND OBCA.OBCA_VISTO = 0
            """, nativeQuery = true)
    int updateSeenObservations(
        @Param("idCarga") Long idCarga,
        @Param("registradoPor") String registradoPor
    );
}
