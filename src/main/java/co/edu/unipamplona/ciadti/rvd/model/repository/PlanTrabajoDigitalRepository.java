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
                    AND PTDI.PEUN_ID = :idPeriodoUniversitario
            )
            WHERE rn = 1
            """, nativeQuery = true)
    List<PlanTrabajoDigitalEntity> findActivityWeightsByPersona(
        @Param("idPersonaGeneral") Long idPersonaGeneral,
        @Param("idPeriodoUniversitario") Long idPeriodoUniversitario
    );

    @Query(value = """
            SELECT *
            FROM (
                SELECT
                    PTDI.*,
                    ROW_NUMBER() OVER (
                        PARTITION BY PTDI.PEGE_ID
                        ORDER BY PTDI.PTDI_ID
                    ) AS rn
                FROM RVD.PLANTRABAJODIGITAL PTDI
                WHERE PTDI.PEUN_ID = :idPeriodoUniversitario
            )
            WHERE rn = 1
            """, nativeQuery = true)
    List<PlanTrabajoDigitalEntity> findPtdStatesByPeriod(@Param("idPeriodoUniversitario") Long idPeriodoUniversitario);

    boolean existsByIdPersonaGeneralAndIdPeriodoUniversitario(
        Long idPersonaGeneral,
        Long idPeriodoUniversitario
    );

    @Query(value = """
            WITH PTD_CANDIDATOS AS (

                SELECT
                    PTDI.PTDI_ID

                FROM RVD.PLANTRABAJODIGITAL PTDI

                WHERE PTDI.PEGE_ID = :idPersonaGeneral

                AND PTDI.PEUN_ID = :idPeriodoUniversitario

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

                            AND NOCD.NOCD_ESTADONOVEDAD = '1'

                            AND NVL(
                                NOCD.NOCD_ESTADOELIMINADO,
                                '0'
                            ) = '0'

                            AND DNCD.DNCD_VIGENTE = '1'
                        )
                    )
                )
            )

            SELECT COALESCE(

                MIN(
                    CASE
                        WHEN DOC.UNIQUEID IS NOT NULL
                        THEN PTD.PTDI_ID
                    END
                ),

                MIN(PTD.PTDI_ID)

            )

            FROM PTD_CANDIDATOS PTD

            LEFT JOIN RVD.PTDIDOCUMENTO DOC
                ON DOC.PTDI_ID = PTD.PTDI_ID

            """, nativeQuery = true)
    Long findReferencePtdIdByHiringAndProfessor(

            @Param("idCargaDocente")
            Long idCargaDocente,

            @Param("idPersonaGeneral")
            Long idPersonaGeneral,

            @Param("idPeriodoUniversitario")
            Long idPeriodoUniversitario
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM PlanTrabajoDigitalEntity p
            WHERE p.idPersonaGeneral = :idPersonaGeneral
            AND p.idPeriodoUniversitario = :idPeriodoUniversitario
            ORDER BY p.id ASC
            """)
    List<PlanTrabajoDigitalEntity> lockPtdForDecision(
            @Param("idPersonaGeneral")
            Long idPersonaGeneral,

            @Param("idPeriodoUniversitario")
            Long idPeriodoUniversitario
    );

}
