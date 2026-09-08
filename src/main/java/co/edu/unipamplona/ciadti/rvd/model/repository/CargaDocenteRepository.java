/**
 * Aplicación: rvd
 * Archivo: CargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 31/08/2026 - Sebastian Jaimes - Horas en consulta de reporte de preasignación
 * 04/09/2026 - Once meses en consulta de reporte de preasignación
 * 07/09/2026 - Sebastian Jaimes - Listado docentes por periodo, convocatoria y coordinación
 * 07/09/2026 - Sebastian Jaimes - Filtro CADO_ESTADO = 1 en verificación
 * 07/09/2026 - Sebastian Jaimes - Pendientes de verificación para header
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteVerificacionPendienteProjection;

public interface CargaDocenteRepository extends JpaRepository<CargaDocenteEntity, Long> {

    @Query(value = """
                SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,
                TRIM(
                TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                || ' ' ||
                TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CADO.CADO_ID AS idCargaDocente,
                CADO.CADO_ESTADO AS estado,
                CADO.CARG_ID AS idCarga,
                CADO.MOCO_ID AS idModalidadContratacion,
                CADO.CACA_ID AS idCategoriaCatedratico,
                CADO.CADO_FECHAINICIO AS cargaFechaInicio,
                CADO.CADO_FECHAFIN AS cargaFechaFin,
                CADO.CADO_VALORCONTRATO AS valorContrato,
                CADO.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CADO.CADO_SALARIO AS asignacionSalarial,
                CADO.CADO_TOTALCONTRATO AS totalContrato,
                CADO.CADO_VALORHORA AS valorHora,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_VALORPUNTO AS valorPunto,
                CADO.CADO_SEMANAS AS semanas,
                CADO.CADO_ONCEMESES AS onceMeses,
                CADO.CADO_HORASDEEXCEPCION AS horasDeExcepcion,
                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin,

                CASE
                    WHEN CADO.CADO_ID IS NOT NULL
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLECARGADOCENTE DECD
                            WHERE DECD.CADO_ID = CADO.CADO_ID
                        )
                    THEN 1
                    ELSE 0
                END AS tieneActividades

                FROM RVD.CARGADOCENTE CADO
                INNER JOIN RVD.CARGA CARG
                ON CARG.CARG_ID = CADO.CARG_ID
                LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
                LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
                LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CADO.FECO_ID
                WHERE CADO.CARG_ID = :idCarga
                AND CADO.MOCO_ID = :idModalidadContratacion
                ORDER BY
                CASE
                    WHEN PEGE.PEGE_ID IS NULL THEN 1
                    WHEN TRIM(
                        TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                        || ' ' ||
                        TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                    ) IS NULL THEN 1
                    ELSE 0
                END,
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocenteCargaCoordinacionProjection> findProfessorsByCargaAndModality(
            @Param("idCarga") Long idCarga,
            @Param("idModalidadContratacion") Long idModalidadContratacion);

    @Query(value = """
                SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,
                TRIM(
                TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                || ' ' ||
                TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CADO.CADO_ID AS idCargaDocente,
                CADO.CADO_ESTADO AS estado,
                CADO.CARG_ID AS idCarga,
                CADO.MOCO_ID AS idModalidadContratacion,
                CADO.CACA_ID AS idCategoriaCatedratico,
                CADO.CADO_FECHAINICIO AS cargaFechaInicio,
                CADO.CADO_FECHAFIN AS cargaFechaFin,
                CADO.CADO_VALORCONTRATO AS valorContrato,
                CADO.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CADO.CADO_SALARIO AS asignacionSalarial,
                CADO.CADO_TOTALCONTRATO AS totalContrato,
                CADO.CADO_VALORHORA AS valorHora,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_VALORPUNTO AS valorPunto,
                CADO.CADO_SEMANAS AS semanas,
                CADO.CADO_ONCEMESES AS onceMeses,
                CADO.CADO_HORASDEEXCEPCION AS horasDeExcepcion,
                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin,

                CASE
                    WHEN CADO.CADO_ID IS NOT NULL
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLECARGADOCENTE DECD
                            WHERE DECD.CADO_ID = CADO.CADO_ID
                        )
                    THEN 1
                    ELSE 0
                END AS tieneActividades

                FROM RVD.CARGA CARG
                INNER JOIN RVD.DOCENTESPLANTACOORDINACION DOPC
                ON DOPC.COOR_ID = CARG.COOR_ID
                INNER JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = DOPC.PEGE_ID
                INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
                LEFT JOIN RVD.CARGADOCENTE CADO
                ON CADO.PEGE_ID = DOPC.PEGE_ID
                AND CADO.MOCO_ID = :idModalidadContratacion
                AND CADO.CARG_ID = CARG.CARG_ID
                LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CADO.FECO_ID
                WHERE CARG.CARG_ID = :idCarga
                ORDER BY
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocenteCargaCoordinacionProjection> findPlantProfessorsByCargaAndModality(
            @Param("idCarga") Long idCarga,
            @Param("idModalidadContratacion") Long idModalidadContratacion);

    @Procedure(name = "CargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_CADO_ID") Long id,
            @Param("P_CADO_REGISTRADOPOR") String registradoPor);

    boolean existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacionAndIdFechasConvocatoria(
            Long idPersonaGeneral, Long idCarga, Long idModalidadContratacion, Long idFechasConvocatoria);

    boolean existsByIdCarga(Long idCarga);    



    boolean existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacion(
            Long idPersonaGeneral, Long idCarga, Long idModalidadContratacion);

    List<CargaDocenteEntity> findByIdCargaAndOnceMeses(Long idCarga, String onceMeses);

    @Query(value = """
            SELECT
                COUNT(DISTINCT CADO.PEGE_ID)
                + SUM(
                    CASE
                        WHEN CADO.PEGE_ID IS NULL THEN 1
                        ELSE 0
                    END
                ),
                SUM(NVL(CADO.CADO_VALORPRESTACIONES, 0)),
                SUM(NVL(CADO.CADO_VALORCONTRATO, 0)),
                SUM(
                    NVL(CADO.CADO_VALORPRESTACIONES, 0)
                    + NVL(CADO.CADO_VALORCONTRATO, 0)
                )
            FROM RVD.CARGA CARG
            INNER JOIN RVD.CARGADOCENTE CADO
                ON CADO.CARG_ID = CARG.CARG_ID
            WHERE CARG.CARG_ID = :cargId
            """, nativeQuery = true)
    List<Object[]> findTotalPreasignacionByCargaId(@Param("cargId") Long cargId);

    @Modifying
    @Query(value = """
            UPDATE RVD.CARGADOCENTE CADO
            SET CADO.CADO_HORASDEEXCEPCION = NULL,
                CADO.CADO_REGISTRADOPOR = :registradoPor,
                CADO.CADO_FECHACAMBIO = SYSDATE
            WHERE CADO.MOCO_ID = :idModalidadContratacion
            AND NVL(CADO.CADO_VIGENTE, '1') = '1'
            AND CADO.CADO_HORASDEEXCEPCION IS NOT NULL
            """, nativeQuery = true)
    int clearHorasDeExcepcionByModalidad(
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("registradoPor") String registradoPor);

    @Modifying
    @Query(value = """
            UPDATE RVD.CARGADOCENTE CADO
            SET CADO.CADO_HORASDEEXCEPCION = :horasDeExcepcion,
                CADO.CADO_REGISTRADOPOR = :registradoPor,
                CADO.CADO_FECHACAMBIO = SYSDATE
            WHERE CADO.MOCO_ID = :idModalidadContratacion
            AND CADO.PEGE_ID = :idPersonaGeneral
            AND NVL(CADO.CADO_VIGENTE, '1') = '1'
            """, nativeQuery = true)
    int updateHorasDeExcepcionByModalidadAndPersona(
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("horasDeExcepcion") String horasDeExcepcion,
            @Param("registradoPor") String registradoPor);
    
    @Query(value = """
            SELECT CADO.CADO_ID
            FROM RVD.CARGADOCENTE CADO
            WHERE CADO.CARG_ID = :idCarga
                AND CADO.CADO_ESTADO = '2'
            """, nativeQuery = true)
    List<Long> findIdsPreassignmentsByIdCarga(@Param("idCarga") Long idCarga);

    @Modifying
    @Query(value = """
            UPDATE RVD.CARGADOCENTE CADO
            SET CADO.CADO_ESTADO = '4',
                CADO.CADO_REGISTRADOPOR = :registradoPor,
                CADO.CADO_FECHACAMBIO = SYSDATE
            WHERE CADO.CADO_ID IN (:ids)
            """, nativeQuery = true)
    int approvePreassignmentByIds(
            @Param("ids") List<Long> ids,
            @Param("registradoPor") String registradoPor);

    @Query(value = """
            SELECT
                CADO.CADO_ID AS idCargaDocente,
                PEGE.PEGE_ID AS idPersonaGeneral,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CADO.CADO_ESTADO AS estado,
                CADO.MOCO_ID AS idModalidadContratacion,
                MOCO.MOCO_NOMBRE AS modalidadContratacion,
                CADO.CACA_ID AS idCategoriaCatedratico,
                CACA.CACA_DESCRIPCION AS categoria,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_VALORPUNTO AS valorPunto,
                CADO.CADO_SALARIO AS salario,
                CADO.CADO_VALORHORA AS valorHora,
                CADO.CADO_FECHAINICIO AS fechaInicio,
                CADO.CADO_FECHAFIN AS fechaFin,
                CADO.CADO_SEMANAS AS semanas,
                CADO.CADO_HORAS AS horas,
                CADO.CADO_ONCEMESES AS onceMeses
            FROM RVD.CARGADOCENTE CADO
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN CONTRATOS.MODALIDADCONTRATACION MOCO
                ON MOCO.MOCO_ID = CADO.MOCO_ID
            LEFT JOIN TALENTOV3.CATEGORIACATEDRATICO CACA
                ON CACA.CACA_ID = CADO.CACA_ID
            WHERE CADO.CARG_ID = :idCarga
            ORDER BY
                UPPER(NVL(MOCO.MOCO_NOMBRE, ' ')),
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocentePreasignacionReporteProjection> findReportProfessorsByCarga(
            @Param("idCarga") Long idCarga);

    @Query(value = """
            SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CADO.CADO_ID AS idCargaDocente,
                CADO.CADO_ESTADO AS estado,
                CADO.CARG_ID AS idCarga,
                CADO.MOCO_ID AS idModalidadContratacion,
                CADO.CACA_ID AS idCategoriaCatedratico,
                CADO.CADO_FECHAINICIO AS cargaFechaInicio,
                CADO.CADO_FECHAFIN AS cargaFechaFin,
                CADO.CADO_VALORCONTRATO AS valorContrato,
                CADO.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CADO.CADO_SALARIO AS asignacionSalarial,
                CADO.CADO_TOTALCONTRATO AS totalContrato,
                CADO.CADO_VALORHORA AS valorHora,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_VALORPUNTO AS valorPunto,
                CADO.CADO_SEMANAS AS semanas,
                CADO.CADO_ONCEMESES AS onceMeses,
                CADO.CADO_HORASDEEXCEPCION AS horasDeExcepcion,
                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM RVD.DETALLECARGADOCENTE DECD
                        WHERE DECD.CADO_ID = CADO.CADO_ID
                    )
                    THEN 1
                    ELSE 0
                END AS tieneActividades
            FROM RVD.CARGADOCENTE CADO
            INNER JOIN RVD.CARGA CARG
                ON CARG.CARG_ID = CADO.CARG_ID
            INNER JOIN RVD.CONVOCATORIA CONV
                ON CONV.CONV_ID = CARG.CONV_ID
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CADO.FECO_ID
            WHERE CARG.COOR_ID = :idCoordinacion
                AND CARG.CONV_ID = :idConvocatoria
                AND CONV.PEUN_ID = :idPeriodoUniversidad
                AND CADO.CADO_ESTADO = '1'
                AND NVL(CADO.CADO_VIGENTE, '1') = '1'
            ORDER BY
                CASE
                    WHEN PEGE.PEGE_ID IS NULL THEN 1
                    WHEN TRIM(
                        TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                        || ' ' ||
                        TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                    ) IS NULL THEN 1
                    ELSE 0
                END,
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocenteCargaCoordinacionProjection>
            findProfessorsByCoordinationCallAndPeriod(
                    @Param("idCoordinacion") Long idCoordinacion,
                    @Param("idConvocatoria") Long idConvocatoria,
                    @Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

    @Modifying
    @Query(value = """
            UPDATE RVD.CARGADOCENTE
            SET CADO_ESTADO = '1',
                CADO_REGISTRADOPOR = :registradoPor,
                CADO_FECHACAMBIO = SYSDATE
            WHERE CADO_ID = :idCargaDocente
            AND CADO_ESTADO = '0'
            """, nativeQuery = true)
    int sendProfessorToVerificationById(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("registradoPor") String registradoPor
    );        

    @Query(value = """
            SELECT
                CADO.CADO_ID AS idCargaDocente,
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CONV.PEUN_ID AS idPeriodoUniversidad,
                CONV.CONV_ID AS idConvocatoria,
                COOR.COOR_ID AS idCoordinacion,
                COOR.COOR_NOMBRE AS nombreCoordinacion
            FROM RVD.CARGADOCENTE CADO
            INNER JOIN RVD.CARGA CARG
                ON CARG.CARG_ID = CADO.CARG_ID
            INNER JOIN RVD.CONVOCATORIA CONV
                ON CONV.CONV_ID = CARG.CONV_ID
            INNER JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = CARG.COOR_ID
            INNER JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = CONV.PEUN_ID
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            WHERE CADO.CADO_ESTADO = '1'
                AND NVL(CADO.CADO_VIGENTE, '1') = '1'
                AND COOR.COOR_IDPADRE IS NOT NULL
                AND TRIM(COOR.COOR_ESACADEMICA) = '1'
                AND CONV.CONV_ESTADO = '1'
                AND PEUN.PEUN_ID IN (
                    SELECT P.PEUN_ID
                    FROM ACADEMICO.PERIODOUNIVERSIDAD P
                    WHERE TRIM(P.PEUN_ACTUAL) = '1'
                    UNION ALL
                    SELECT P.PEUN_ID
                    FROM ACADEMICO.PERIODOUNIVERSIDAD P
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM ACADEMICO.PERIODOUNIVERSIDAD X
                        WHERE TRIM(X.PEUN_ACTUAL) = '1'
                    )
                    AND TRUNC(SYSDATE) BETWEEN
                        TRUNC(P.PEUN_FECHAINICIO)
                        AND TRUNC(NVL(P.PEUN_FECHAFIN, SYSDATE))
                )
            ORDER BY
                UPPER(COOR.COOR_NOMBRE),
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocenteVerificacionPendienteProjection>
            findPendingProfessorsForVerification();

}
