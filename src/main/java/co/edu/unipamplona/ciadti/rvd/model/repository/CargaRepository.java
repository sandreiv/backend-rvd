/**
 * Aplicación: rvd
 * Archivo: CargaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/07/2026
 * Modificaciones:
 * 01/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EncabezadoPreasignacionProjection;

public interface CargaRepository extends JpaRepository<CargaEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);

    boolean existsByIdCoordinacionAndIdConvocatoria(Long idCoordinacion, Long idConvocatoria);

    Optional<CargaEntity> findFirstByIdCoordinacionOrderByIdDesc(Long idCoordinacion);

    Optional<CargaEntity> findFirstByIdCoordinacionAndIdConvocatoria(
            Long idCoordinacion,
            Long idConvocatoria);

    boolean existsByIdAndIdConvocatoria(Long id, Long idConvocatoria);

    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.CARGA CARG
            INNER JOIN RVD.CONVOCATORIA CONV_OTHER
                ON CONV_OTHER.CONV_ID = CARG.CONV_ID
            INNER JOIN RVD.CONVOCATORIA CONV_TARGET
                ON CONV_TARGET.CONV_ID = :idConvocatoria
            WHERE CARG.COOR_ID = :idCoordinacion
            AND CARG.CONV_ID IS NOT NULL
            AND CARG.CONV_ID <> :idConvocatoria
            AND CONV_OTHER.PEUN_ID = CONV_TARGET.PEUN_ID
            """, nativeQuery = true)
    Long countAssignedToAnotherPreloadCall(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idConvocatoria") Long idConvocatoria
    );

    @Query(value = """
            SELECT
                CARG.CARG_ID AS idCarga,
                COOR.COOR_ID AS idCoordinacion,
                UNID_REG.UNID_NOMBRE AS unidad,
                UNID_AREA.UNID_NOMBRE AS facultad,
                COOR.COOR_NOMBRE AS coordinacion,
                PEUN.PEUN_ID AS idPeriodoUniversidad,
                TRIM(PEUN.PEUN_ANO || '-' || PEUN.PEUN_PERIODO) AS periodoAcademico,
                CONV.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS convocatoria
            FROM RVD.CARGA CARG
            INNER JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = CARG.COOR_ID
            INNER JOIN ACADEMICO.UNIDAD UNID_REG
                ON UNID_REG.UNID_ID = COOR.UNID_IDREGIONAL
            INNER JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            LEFT JOIN RVD.CONVOCATORIA CONV
                ON CONV.CONV_ID = CARG.CONV_ID
            LEFT JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = CONV.PEUN_ID
            WHERE CARG.CARG_ID = :idCarga
            """, nativeQuery = true)
    Optional<EncabezadoPreasignacionProjection> findEncabezadoReporteByIdCarga(
            @Param("idCarga") Long idCarga);

}