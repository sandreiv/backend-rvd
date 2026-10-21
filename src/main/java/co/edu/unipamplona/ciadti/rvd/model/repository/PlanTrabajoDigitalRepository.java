/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;

public interface PlanTrabajoDigitalRepository extends JpaRepository<PlanTrabajoDigitalEntity, Long> {
    
    @Procedure(name = "PlanTrabajoDigitalEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_PTDI_ID") Long id,
            @Param("P_CADO_REGISTRADOPOR") String registradoPor);
    
    @Query(value = """
            SELECT *
            FROM (
                SELECT
                    PTDI.*,
                    ROW_NUMBER() OVER (
                        PARTITION BY PTDI.TIAC_ID
                        ORDER BY PTDI.PTDI_ID
                    ) AS rn
                FROM RVD.PLANTRABAJODIGITAL PTDI
                WHERE PTDI.PEGE_ID = :idPersonaGeneral
                    AND PTDI.PTDI_ESTADO = '0'
            )
            WHERE rn = 1
            """, nativeQuery = true)
    List<PlanTrabajoDigitalEntity> findActivityWeightsByPersona(@Param("idPersonaGeneral") Long idPersonaGeneral);

    @Query(value = """
            SELECT MIN(PTDI.PTDI_ID)
            FROM RVD.PLANTRABAJODIGITAL PTDI
            WHERE PTDI.PEGE_ID = :idPersonaGeneral
            AND PTDI.PTDI_ESTADO = '0'
            AND (
                    (
                        PTDI.DECD_ID IS NOT NULL
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLECARGADOCENTE DECD
                            WHERE DECD.DECD_ID = PTDI.DECD_ID
                            AND DECD.CADO_ID = :idCargaDocente
                        )
                    )
                    OR
                    (
                        PTDI.DNCD_ID IS NOT NULL
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
                            INNER JOIN RVD.NOVEDADCARGADOCENTE NOCD
                                ON NOCD.NOCD_ID = DNCD.NOCD_ID
                            WHERE DNCD.DNCD_ID = PTDI.DNCD_ID
                            AND NOCD.CADO_ID = :idCargaDocente

                            -- Novedad aprobada
                            AND NOCD.NOCD_ESTADONOVEDAD = '1'

                            -- Novedad no eliminada
                            AND NVL(NOCD.NOCD_ESTADOELIMINADO, '0') = '0'

                            -- Detalle vigente
                            AND DNCD.DNCD_VIGENTE = '1'
                        )
                    )
            )
            """, nativeQuery = true)
    Long findReferencePtdIdByHiringAndProfessor(
            @Param("idCargaDocente")
            Long idCargaDocente,

            @Param("idPersonaGeneral")
            Long idPersonaGeneral
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM PlanTrabajoDigitalEntity p
            WHERE p.id = :idPlanTrabajoDigital
            """)
    Optional<PlanTrabajoDigitalEntity> lockForDocumentUpdate(
            @Param("idPlanTrabajoDigital")
            Long idPlanTrabajoDigital
    );

}
