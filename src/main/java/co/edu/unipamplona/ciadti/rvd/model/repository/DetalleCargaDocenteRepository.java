/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 27/08/2026 - Horas agrupadas por actividad padre
 * 31/08/2026 - Sebastian Jaimes - Grupos y cupos para reporte PDF
 * 29/09/2026 - Resumen de horas para novedad con fallback
 * 06/10/2026 - Andrés Hernández - Correción al mostrar detalles de proyectos dentro del resumen de novedad
 * 06/10/2026 - Andrés Hernández - Uso de detalles efectivos para generar el CDP
 * 07/10/2026 - Andrés Hernández - Adición de detalles efectivos como método general
 * 08/10/2026 - Sebastian Jaimes - Detalles vigentes PTD (DNCD_VIGENTE = 1 o carga)
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoCuposReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasActividadPadreProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasCodigoActividadReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasProgramaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleNovedadResumenProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetallesCdpReporteProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TotalHorasPreasignacionProjection;

public interface DetalleCargaDocenteRepository
        extends JpaRepository<DetalleCargaDocenteEntity, Long> {

    @Query(value = """
            SELECT
                cado.CADO_ID AS idCargaDocente,
                decd.DECD_ID AS idDetalleCargaDocente,
                decd.DECD_HORAS AS horas,
                tiac.TIAC_ID AS idTipoActividadResuelto,
                tiac.TIAC_IDPADRE AS idTipoActividadPadre,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                tiac.TIAC_CODIGO AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad,
                tiac_padre.TIAC_NOMBRE AS nombreTipoActividadPadre,
                tiac_padre.TIAC_DESCRIPCION AS descripcionTipoActividadPadre,
                tiac_padre.TIAC_ORDEN AS ordenTipoActividadPadre,
                tiac_padre.TIAC_CODIGO AS codigoTipoActividadPadre,
                tiac_padre.TIAC_COMPONENTE AS componenteTipoActividadPadre,
                unid.UNID_ID AS idUnidadRegional,
                unid.UNID_NOMBRE AS nombreUnidadRegional,
                prog.PROG_ID AS idPrograma,
                prog.PROG_NOMBRE AS nombrePrograma,
                grup.GRUP_ID AS idGrupo,
                grup.GRUP_NOMBRE AS nombreGrupo,
                grup.GRUP_CAPACIDAD AS capacidadGrupo,
                grup.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombreMateria,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PROY_ID AS idProyecto,
                proy.PROY_IDPROYECTO AS idProyectoPadre,
                proy.PROY_NOMBRE AS nombreProyecto,
                proy.PROY_DESCRIPCION AS descripcionProyecto,
                tipr.TIPR_ID AS idTipoProyecto,
                tipr.TIPR_NOMBRE AS nombreTipoProyecto,
                tipr.TIPR_DESCRIPCION AS descripcionTipoProyecto,
                tipr.TIPR_TIPO AS tipoTipoProyecto,
                proy_padre.PROY_ID AS idProyectoPadreEntidad,
                proy_padre.PROY_NOMBRE AS nombreProyectoPadre,
                proy_padre.PROY_DESCRIPCION AS descripcionProyectoPadre,
                tipr_padre.TIPR_ID AS idTipoProyectoPadre,
                tipr_padre.TIPR_NOMBRE AS nombreTipoProyectoPadre,
                tipr_padre.TIPR_DESCRIPCION AS descripcionTipoProyectoPadre,
                tipr_padre.TIPR_TIPO AS tipoTipoProyectoPadre
            FROM RVD.CARGADOCENTE cado
            INNER JOIN RVD.DETALLECARGADOCENTE decd
                ON decd.CADO_ID = cado.CADO_ID
            LEFT JOIN RVD.TIPOACTIVIDADES tiac
                ON tiac.TIAC_ID = decd.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE
            LEFT JOIN ACADEMICO.GRUPO grup
                ON grup.GRUP_ID = decd.GRUP_ID
            LEFT JOIN ACADEMICO.MATERIA mate
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
            LEFT JOIN ACADEMICO.UNIDAD unid
                ON unid.UNID_ID = grup.UNID_IDREGIONAL
            LEFT JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = decd.PROG_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON ceco.CECO_ID = decd.CECO_ID
            LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                ON recp.DECD_ID = decd.DECD_ID
            LEFT JOIN RVD.PERSONAPROYECTO pepr
                ON pepr.PEPR_ID = recp.PEPR_ID
            LEFT JOIN RVD.PROYECTOS proy
                ON proy.PROY_ID = pepr.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr
                ON proy.TIPR_ID = tipr.TIPR_ID
            LEFT JOIN RVD.PROYECTOS proy_padre
                ON proy.PROY_IDPROYECTO = proy_padre.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr_padre
                ON proy_padre.TIPR_ID = tipr_padre.TIPR_ID
            WHERE cado.CADO_ID = :idCargaDocente
            ORDER BY decd.DECD_ID, recp.RECP_ID
            """, nativeQuery = true)
    List<DetalleCargaDocenteListadoProjection> findByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    @Query(value = """
            SELECT
                TA.TIAC_ID AS idTipoActividad,
                TA.TIAC_CODIGO AS codigo,
                TA.TIAC_NOMBRE AS nombre,
                SUM(
                    NVL(
                        TO_NUMBER(
                            REPLACE(TRIM(DECD.DECD_HORAS), ',', '.')
                            DEFAULT NULL ON CONVERSION ERROR
                        ),
                        0
                    )
                ) AS horas
            FROM RVD.CARGA CARG
            INNER JOIN RVD.CARGADOCENTE CADO
                ON CADO.CARG_ID = CARG.CARG_ID
            INNER JOIN RVD.DETALLECARGADOCENTE DECD
                ON DECD.CADO_ID = CADO.CADO_ID
            INNER JOIN RVD.TIPOACTIVIDADES TA
                ON TA.TIAC_ID = DECD.TIAC_ID
            WHERE CARG.CARG_ID = :cargId
            GROUP BY
                TA.TIAC_ID,
                TA.TIAC_CODIGO,
                TA.TIAC_NOMBRE
            ORDER BY
                TA.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TotalHorasPreasignacionProjection> findTotalHorasPreasignacionByCargaId(
            @Param("cargId") Long cargId);

    @Query(value = """
            SELECT
                PADRE.TIAC_CODIGO AS codigo,
                PADRE.TIAC_NOMBRE AS nombre,
                NVL(HORAS.totalHoras, 0) AS horas
            FROM RVD.TIPOACTIVIDADES PADRE
            LEFT JOIN (
                SELECT
                    NVL(TA.TIAC_IDPADRE, TA.TIAC_ID) AS idActividadPadre,
                    SUM(
                        NVL(
                            TO_NUMBER(
                                REPLACE(TRIM(DECD.DECD_HORAS), ',', '.')
                                DEFAULT NULL ON CONVERSION ERROR
                            ),
                            0
                        )
                    ) AS totalHoras
                FROM RVD.CARGA CARG
                INNER JOIN RVD.CARGADOCENTE CADO
                    ON CADO.CARG_ID = CARG.CARG_ID
                INNER JOIN RVD.DETALLECARGADOCENTE DECD
                    ON DECD.CADO_ID = CADO.CADO_ID
                INNER JOIN RVD.TIPOACTIVIDADES TA
                    ON TA.TIAC_ID = DECD.TIAC_ID
                WHERE CARG.CARG_ID = :cargId
                GROUP BY
                    NVL(TA.TIAC_IDPADRE, TA.TIAC_ID)
            ) HORAS
                ON HORAS.idActividadPadre = PADRE.TIAC_ID
            WHERE PADRE.TIAC_IDPADRE IS NULL
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(PADRE.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(PADRE.TIAC_ORDEN)
                    ELSE 999999
                END,
                PADRE.TIAC_NOMBRE
            """, nativeQuery = true)
    List<HorasActividadPadreProjection> findHorasPorActividadPadreByCargaId(
            @Param("cargId") Long cargId);

    @Query(value = """
            SELECT
                CADO.CADO_ID AS idCargaDocente,
                UPPER(TRIM(
                    NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)
                )) AS codigoPadre,
                SUM(
                    NVL(
                        TO_NUMBER(
                            REPLACE(TRIM(DECD.DECD_HORAS), ',', '.')
                            DEFAULT NULL ON CONVERSION ERROR
                        ),
                        0
                    )
                ) AS totalHoras
            FROM RVD.CARGADOCENTE CADO
            INNER JOIN RVD.DETALLECARGADOCENTE DECD
                ON DECD.CADO_ID = CADO.CADO_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = DECD.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE CADO.CARG_ID = :idCarga
            AND NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO) IS NOT NULL
            GROUP BY
                CADO.CADO_ID,
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)))
            """, nativeQuery = true)
    List<HorasCodigoActividadReporteProjection> findHorasPorCodigoPadreByCarga(
            @Param("idCarga") Long idCarga);

    @Query(value = """
            SELECT
                agrupado.IDCARGADOCENTE AS idCargaDocente,
                COUNT(*) AS cantidadGrupos,
                SUM(agrupado.CUPOS) AS cupos
            FROM (
                SELECT DISTINCT
                    DECD.CADO_ID AS IDCARGADOCENTE,
                    GRUP.GRUP_ID AS IDGRUPO,
                    GRUP.GRUP_CUPOS AS CUPOS
                FROM RVD.DETALLECARGADOCENTE DECD
                INNER JOIN RVD.CARGADOCENTE CADO
                    ON CADO.CADO_ID = DECD.CADO_ID
                INNER JOIN ACADEMICO.GRUPO GRUP
                    ON GRUP.GRUP_ID = DECD.GRUP_ID
                WHERE CADO.CARG_ID = :idCarga
                AND DECD.GRUP_ID IS NOT NULL
            ) agrupado
            GROUP BY agrupado.IDCARGADOCENTE
            """, nativeQuery = true)
    List<GrupoCuposReporteProjection> findGruposYCuposByCarga(
            @Param("idCarga") Long idCarga);

    @Query(value = """
            SELECT
                DECD.PROG_ID AS idPrograma,
                SUM(
                    NVL(
                        TO_NUMBER(
                            REPLACE(TRIM(DECD.DECD_HORAS), ',', '.')
                            DEFAULT NULL ON CONVERSION ERROR
                        ),
                        0
                    )
                ) AS totalHoras
            FROM RVD.DETALLECARGADOCENTE DECD
            WHERE DECD.CADO_ID = :idCargaDocente
            AND DECD.PROG_ID IS NOT NULL
            AND (:idDetalleExcluido IS NULL OR DECD.DECD_ID <> :idDetalleExcluido)
            GROUP BY DECD.PROG_ID
            """, nativeQuery = true)
    List<HorasProgramaProjection> findHorasByProgramaAndCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idDetalleExcluido") Long idDetalleExcluido);

    @Procedure(name = "DetalleCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(@Param("P_DECD_ID") Long id, @Param("P_DECD_REGISTRADOPOR") String registradoPor);

    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.DETALLECARGADOCENTE DECD
            INNER JOIN RVD.RELACIONCARGAPROYECTO RECP
                ON RECP.DECD_ID = DECD.DECD_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = DECD.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE DECD.CADO_ID = :idCargaDocente
            AND UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO))) IN ('CTEI', 'ISU')
            AND NVL(
                    TO_NUMBER(
                        REPLACE(TRIM(DECD.DECD_HORAS), ',', '.')
                        DEFAULT 0 ON CONVERSION ERROR
                    ),
                    0
            ) > 0
            """, nativeQuery = true)
    int countCteiOrIsuProjectAssociationsByCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    List<DetalleCargaDocenteEntity> findAllByIdCargaDocente(
            Long idCargaDocente);

    @Query(value = """
            SELECT
                decd.DECD_ID AS idDetalleNovedadCargaDocente,
                decd.DECD_HORAS AS horas,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_CODIGO AS codigoTipoActividad,
                tiac_padre.TIAC_NOMBRE AS nombreTipoActividadPadre,
                tiac_padre.TIAC_CODIGO AS codigoTipoActividadPadre,
                unid.UNID_NOMBRE AS nombreUnidadRegional,
                prog.PROG_NOMBRE AS nombrePrograma,
                grup.GRUP_NOMBRE AS nombreGrupo,
                mate.MATE_NOMBRE AS nombreMateria,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto,

                -- Info proyectos
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PROY_ID AS idProyecto,
                proy.PROY_IDPROYECTO AS idProyectoPadre,
                proy.PROY_NOMBRE AS nombreProyecto,
                proy.PROY_DESCRIPCION AS descripcionProyecto,
                tipr.TIPR_ID AS idTipoProyecto,
                tipr.TIPR_NOMBRE AS nombreTipoProyecto,
                tipr.TIPR_DESCRIPCION AS descripcionTipoProyecto,
                tipr.TIPR_TIPO AS tipoTipoProyecto,
                proy_padre.PROY_ID AS idProyectoPadreEntidad,
                proy_padre.PROY_NOMBRE AS nombreProyectoPadre,
                proy_padre.PROY_DESCRIPCION AS descripcionProyectoPadre,
                tipr_padre.TIPR_ID AS idTipoProyectoPadre,
                tipr_padre.TIPR_NOMBRE AS nombreTipoProyectoPadre,
                tipr_padre.TIPR_DESCRIPCION AS descripcionTipoProyectoPadre,
                tipr_padre.TIPR_TIPO AS tipoTipoProyectoPadre
            FROM RVD.DETALLECARGADOCENTE decd
            LEFT JOIN RVD.TIPOACTIVIDADES tiac
                ON tiac.TIAC_ID = decd.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE
            LEFT JOIN ACADEMICO.GRUPO grup
                ON grup.GRUP_ID = decd.GRUP_ID
            LEFT JOIN ACADEMICO.MATERIA mate
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
            LEFT JOIN ACADEMICO.UNIDAD unid
                ON unid.UNID_ID = grup.UNID_IDREGIONAL
            LEFT JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = decd.PROG_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON ceco.CECO_ID = decd.CECO_ID
            
            LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                ON recp.DECD_ID = decd.DECD_ID
            LEFT JOIN RVD.PERSONAPROYECTO pepr
                ON pepr.PEPR_ID = recp.PEPR_ID
            LEFT JOIN RVD.PROYECTOS proy
                ON proy.PROY_ID = pepr.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr
                ON proy.TIPR_ID = tipr.TIPR_ID
            LEFT JOIN RVD.PROYECTOS proy_padre
                ON proy.PROY_IDPROYECTO = proy_padre.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr_padre
                ON proy_padre.TIPR_ID = tipr_padre.TIPR_ID
            WHERE decd.CADO_ID = :idCargaDocente
            ORDER BY decd.DECD_ID
            """, nativeQuery = true)
    List<DetalleNovedadResumenProjection> findResumenByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    @Query(value = """
            WITH NOVEDADES_VALIDAS AS (
                SELECT
                    NOCD.*,
                    ROW_NUMBER() OVER (
                        PARTITION BY NOCD.CADO_ID
                        ORDER BY NOCD.NOCD_FECHACAMBIO DESC
                    ) AS RN
                FROM RVD.NOVEDADCARGADOCENTE NOCD
                WHERE NOCD.NOCD_ESTADONOVEDAD <> '2'
            ),
            CARGAS_RESUELTAS AS (
                SELECT
                    CADO.CADO_ID,
                    NOCD.NOCD_ID
                FROM RVD.CARGADOCENTE CADO

                LEFT JOIN NOVEDADES_VALIDAS NOCD
                    ON NOCD.CADO_ID = CADO.CADO_ID
                    AND NOCD.RN = 1

                WHERE CADO.CARG_ID = :idCarga
                    AND NVL(NOCD.NOCD_ESTADOELIMINADO, '0') = '0'
            ),
            DETALLES_NOVEDAD AS (
                SELECT
                    CR.CADO_ID AS idCargaDocente,
                    DNCD.DNCD_ID AS idDetalle,
                    DNCD.DNCD_HORAS AS horas,
                    DNCD.TIAC_ID AS idTipoActividad,
                    DNCD.PROG_ID AS idPrograma,
                    DNCD.GRUP_ID AS idGrupo,
                    DNCD.CECO_ID AS idCentroCosto,
                    UPPER(TRIM(
                        NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)
                    )) AS codigoPadre,
                    GRUP.GRUP_CUPOS AS cupos

                FROM CARGAS_RESUELTAS CR

                INNER JOIN RVD.DETALLENOVEDADCARGADOCENTE DNCD
                    ON DNCD.NOCD_ID = CR.NOCD_ID
                
                LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                    ON TIAC.TIAC_ID = DNCD.TIAC_ID

                LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                    ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE

                LEFT JOIN ACADEMICO.GRUPO GRUP
                    ON GRUP.GRUP_ID = DNCD.GRUP_ID

                WHERE CR.NOCD_ID IS NOT NULL
            ),
            DETALLES_VIGENTES_NOVEDAD AS (
                SELECT
                    CR.CADO_ID AS idCargaDocente,
                    DNCD.DNCD_ID AS idDetalle,
                    DNCD.DNCD_HORAS AS horas,
                    DNCD.TIAC_ID AS idTipoActividad,
                    DNCD.PROG_ID AS idPrograma,
                    DNCD.GRUP_ID AS idGrupo,
                    DNCD.CECO_ID AS idCentroCosto,
                    UPPER(TRIM(
                        NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)
                    )) AS codigoPadre,
                    GRUP.GRUP_CUPOS AS cupos
                
                FROM CARGAS_RESUELTAS CR

                INNER JOIN RVD.NOVEDADCARGADOCENTE NOCD
                    ON NOCD.CADO_ID = CR.CADO_ID
                
                INNER JOIN RVD.DETALLENOVEDADCARGADOCENTE DNCD
                    ON DNCD.NOCD_ID = NOCD.NOCD_ID

                LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                    ON TIAC.TIAC_ID = DNCD.TIAC_ID

                LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                    ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE

                LEFT JOIN ACADEMICO.GRUPO GRUP
                    ON GRUP.GRUP_ID = DNCD.GRUP_ID
                
                WHERE CR.NOCD_ID IS NOT NULL
                    AND NOCD.NOCD_ID <> CR.NOCD_ID
                    AND DNCD.DNCD_VIGENTE = '1'
                    AND NOT EXISTS (
                        SELECT 1
                        FROM DETALLES_NOVEDAD DN
                        WHERE DN.idCargaDocente = CR.CADO_ID
                    )
            ),
            DETALLES_ORIGINALES AS (
                SELECT
                    CR.CADO_ID AS idCargaDocente,
                    DECD.DECD_ID AS idDetalle,
                    DECD.DECD_HORAS AS horas,
                    DECD.TIAC_ID AS idTipoActividad,
                    DECD.PROG_ID AS idPrograma,
                    DECD.GRUP_ID AS idGrupo,
                    DECD.CECO_ID AS idCentroCosto,
                    UPPER(TRIM(
                        NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)
                    )) AS codigoPadre,
                    GRUP.GRUP_CUPOS AS cupos
                
                FROM CARGAS_RESUELTAS CR

                INNER JOIN RVD.DETALLECARGADOCENTE DECD
                    ON DECD.CADO_ID = CR.CADO_ID

                LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                    ON TIAC.TIAC_ID = DECD.TIAC_ID

                LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                    ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE

                LEFT JOIN ACADEMICO.GRUPO GRUP
                    ON GRUP.GRUP_ID = DECD.GRUP_ID
                
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM DETALLES_NOVEDAD DN
                    WHERE DN.idCargaDocente = CR.CADO_ID
                )

                AND NOT EXISTS (
                    SELECT 1
                    FROM DETALLES_VIGENTES_NOVEDAD DVN
                    WHERE DVN.idCargaDocente = CR.CADO_ID
                )
            ),
            DETALLES_EFECTIVOS AS (
                SELECT *
                FROM DETALLES_NOVEDAD

                UNION ALL

                SELECT *
                FROM DETALLES_VIGENTES_NOVEDAD

                UNION ALL

                SELECT *
                FROM DETALLES_ORIGINALES
            )

            SELECT
                idCargaDocente,
                idDetalle,
                horas,
                idTipoActividad,
                idPrograma,
                idGrupo,
                idCentroCosto,
                codigoPadre,
                cupos
            FROM DETALLES_EFECTIVOS
            ORDER BY idCargaDocente
            """, nativeQuery = true)
    List<DetallesCdpReporteProjection> findCdpDetailsByCarga(
            @Param("idCarga") Long idCarga);
    
    @Query(value = """
            WITH DETALLES_NOVEDAD AS (

                SELECT
                    DNCD.DNCD_ID,
                    NOCD.CADO_ID,
                    DNCD.PROG_ID,
                    DNCD.GRUP_ID,
                    DNCD.TIAC_ID,
                    DNCD.CECO_ID,
                    DNCD.DNCD_HORAS

                FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
                INNER JOIN RVD.NOVEDADCARGADOCENTE NOCD
                    ON NOCD.NOCD_ID = DNCD.NOCD_ID

                WHERE NOCD.CADO_ID = :idCargaDocente
                -- Fase 2: solo la novedad efectiva (ultima no rechazada con detalle);
                -- las demas novedades son historial y no deben mezclarse.
                AND NOCD.NOCD_ID = (
                    SELECT NOCD_ID
                    FROM (
                        SELECT N.NOCD_ID
                        FROM RVD.NOVEDADCARGADOCENTE N
                        WHERE N.CADO_ID = :idCargaDocente
                        AND N.NOCD_ESTADONOVEDAD <> '2'
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLENOVEDADCARGADOCENTE D
                            WHERE D.NOCD_ID = N.NOCD_ID
                        )
                        ORDER BY N.NOCD_FECHACAMBIO DESC
                    )
                    WHERE ROWNUM = 1
                )
            ),

            DETALLES_CARGA AS (

                SELECT
                    DECD.DECD_ID,
                    DECD.CADO_ID,
                    DECD.PROG_ID,
                    DECD.GRUP_ID,
                    DECD.TIAC_ID,
                    DECD.CECO_ID,
                    DECD.DECD_HORAS

                FROM RVD.DETALLECARGADOCENTE DECD

                WHERE DECD.CADO_ID = :idCargaDocente
            ),

            DETALLES_RESUELTOS AS (

                SELECT
                    DN.DNCD_ID AS DNCD_ID,
                    NULL AS DECD_ID,

                    DN.CADO_ID,
                    DN.PROG_ID,
                    DN.GRUP_ID,
                    DN.TIAC_ID,
                    DN.CECO_ID,
                    DN.DNCD_HORAS AS HORAS,
                    1 AS ESNOVEDAD

                FROM DETALLES_NOVEDAD DN

                UNION ALL

                SELECT
                    NULL AS DNCD_ID,
                    DC.DECD_ID AS DECD_ID,

                    DC.CADO_ID,
                    DC.PROG_ID,
                    DC.GRUP_ID,
                    DC.TIAC_ID,
                    DC.CECO_ID,
                    DC.DECD_HORAS AS HORAS,
                    0 AS ESNOVEDAD

                FROM DETALLES_CARGA DC

                WHERE NOT EXISTS (
                    SELECT 1
                    FROM DETALLES_NOVEDAD
                )
            )

            SELECT

                DR.CADO_ID AS idCargaDocente,
                COALESCE(DR.DECD_ID, DR.DNCD_ID) AS idDetalleCargaDocente,
                DR.ESNOVEDAD AS esDeNovedad,
                DR.HORAS AS horas,
                tiac.TIAC_ID AS idTipoActividadResuelto,
                tiac.TIAC_IDPADRE AS idTipoActividadPadre,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                tiac.TIAC_CODIGO AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad,
                tiac_padre.TIAC_NOMBRE AS nombreTipoActividadPadre,
                tiac_padre.TIAC_DESCRIPCION AS descripcionTipoActividadPadre,
                tiac_padre.TIAC_ORDEN AS ordenTipoActividadPadre,
                tiac_padre.TIAC_CODIGO AS codigoTipoActividadPadre,
                tiac_padre.TIAC_COMPONENTE AS componenteTipoActividadPadre,
                unid.UNID_ID AS idUnidadRegional,
                unid.UNID_NOMBRE AS nombreUnidadRegional,
                prog.PROG_ID AS idPrograma,
                prog.PROG_NOMBRE AS nombrePrograma,
                grup.GRUP_ID AS idGrupo,
                grup.GRUP_NOMBRE AS nombreGrupo,
                grup.GRUP_CAPACIDAD AS capacidadGrupo,
                grup.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombreMateria,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PROY_ID AS idProyecto,
                proy.PROY_IDPROYECTO AS idProyectoPadre,
                proy.PROY_NOMBRE AS nombreProyecto,
                proy.PROY_DESCRIPCION AS descripcionProyecto,
                tipr.TIPR_ID AS idTipoProyecto,
                tipr.TIPR_NOMBRE AS nombreTipoProyecto,
                tipr.TIPR_DESCRIPCION AS descripcionTipoProyecto,
                tipr.TIPR_TIPO AS tipoTipoProyecto,
                proy_padre.PROY_ID AS idProyectoPadreEntidad,
                proy_padre.PROY_NOMBRE AS nombreProyectoPadre,
                proy_padre.PROY_DESCRIPCION AS descripcionProyectoPadre,
                tipr_padre.TIPR_ID AS idTipoProyectoPadre,
                tipr_padre.TIPR_NOMBRE AS nombreTipoProyectoPadre,
                tipr_padre.TIPR_DESCRIPCION AS descripcionTipoProyectoPadre,
                tipr_padre.TIPR_TIPO AS tipoTipoProyecto

            FROM DETALLES_RESUELTOS DR

            LEFT JOIN RVD.TIPOACTIVIDADES tiac
                ON tiac.TIAC_ID = DR.TIAC_ID

            LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE

            LEFT JOIN ACADEMICO.GRUPO grup
                ON grup.GRUP_ID = DR.GRUP_ID

            LEFT JOIN ACADEMICO.MATERIA mate
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA

            LEFT JOIN ACADEMICO.UNIDAD unid
                ON unid.UNID_ID = grup.UNID_IDREGIONAL

            LEFT JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = DR.PROG_ID

            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON ceco.CECO_ID = DR.CECO_ID

            LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                ON (
                    DR.DNCD_ID IS NOT NULL
                    AND recp.DNCD_ID = DR.DNCD_ID
                )
                OR (
                    DR.DECD_ID IS NOT NULL
                    AND recp.DECD_ID = DR.DECD_ID
                )

            LEFT JOIN RVD.PERSONAPROYECTO pepr
                ON pepr.PEPR_ID = recp.PEPR_ID

            LEFT JOIN RVD.PROYECTOS proy
                ON proy.PROY_ID = pepr.PROY_ID

            LEFT JOIN RVD.TIPOPROYECTO tipr
                ON proy.TIPR_ID = tipr.TIPR_ID

            LEFT JOIN RVD.PROYECTOS proy_padre
                ON proy.PROY_IDPROYECTO = proy_padre.PROY_ID

            LEFT JOIN RVD.TIPOPROYECTO tipr_padre
                ON proy_padre.TIPR_ID = tipr_padre.TIPR_ID

            ORDER BY
                COALESCE(DR.DECD_ID, DR.DNCD_ID),
                recp.RECP_ID
            """, nativeQuery = true)
    List<DetalleCargaDocenteListadoProjection> findEffectiveDetailsByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    @Query(value = """
            WITH DETALLES_NOVEDAD AS (
                SELECT
                    DNCD.DNCD_ID,
                    NOCD.CADO_ID,
                    DNCD.PROG_ID,
                    DNCD.GRUP_ID,
                    DNCD.TIAC_ID,
                    DNCD.CECO_ID,
                    DNCD.DNCD_HORAS
                FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
                INNER JOIN RVD.NOVEDADCARGADOCENTE NOCD
                    ON NOCD.NOCD_ID = DNCD.NOCD_ID
                WHERE NOCD.CADO_ID = :idCargaDocente
                    AND DNCD.DNCD_VIGENTE = '1'
            ),
            DETALLES_CARGA AS (
                SELECT
                    DECD.DECD_ID,
                    DECD.CADO_ID,
                    DECD.PROG_ID,
                    DECD.GRUP_ID,
                    DECD.TIAC_ID,
                    DECD.CECO_ID,
                    DECD.DECD_HORAS
                FROM RVD.DETALLECARGADOCENTE DECD
                WHERE DECD.CADO_ID = :idCargaDocente
            ),
            DETALLES_RESUELTOS AS (
                SELECT
                    DN.DNCD_ID AS DNCD_ID,
                    NULL AS DECD_ID,
                    DN.CADO_ID,
                    DN.PROG_ID,
                    DN.GRUP_ID,
                    DN.TIAC_ID,
                    DN.CECO_ID,
                    DN.DNCD_HORAS AS HORAS,
                    1 AS ESNOVEDAD
                FROM DETALLES_NOVEDAD DN
                UNION ALL
                SELECT
                    NULL AS DNCD_ID,
                    DC.DECD_ID AS DECD_ID,
                    DC.CADO_ID,
                    DC.PROG_ID,
                    DC.GRUP_ID,
                    DC.TIAC_ID,
                    DC.CECO_ID,
                    DC.DECD_HORAS AS HORAS,
                    0 AS ESNOVEDAD
                FROM DETALLES_CARGA DC
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM DETALLES_NOVEDAD
                )
            )
            SELECT
                DR.CADO_ID AS idCargaDocente,
                COALESCE(DR.DECD_ID, DR.DNCD_ID) AS idDetalleCargaDocente,
                DR.ESNOVEDAD AS esDeNovedad,
                DR.HORAS AS horas,
                tiac.TIAC_ID AS idTipoActividadResuelto,
                tiac.TIAC_IDPADRE AS idTipoActividadPadre,
                tiac.TIAC_NOMBRE AS nombreTipoActividad,
                tiac.TIAC_DESCRIPCION AS descripcionTipoActividad,
                tiac.TIAC_ORDEN AS ordenTipoActividad,
                tiac.TIAC_CODIGO AS codigoTipoActividad,
                tiac.TIAC_COMPONENTE AS componenteTipoActividad,
                tiac_padre.TIAC_NOMBRE AS nombreTipoActividadPadre,
                tiac_padre.TIAC_DESCRIPCION AS descripcionTipoActividadPadre,
                tiac_padre.TIAC_ORDEN AS ordenTipoActividadPadre,
                tiac_padre.TIAC_CODIGO AS codigoTipoActividadPadre,
                tiac_padre.TIAC_COMPONENTE AS componenteTipoActividadPadre,
                unid.UNID_ID AS idUnidadRegional,
                unid.UNID_NOMBRE AS nombreUnidadRegional,
                prog.PROG_ID AS idPrograma,
                prog.PROG_NOMBRE AS nombrePrograma,
                grup.GRUP_ID AS idGrupo,
                grup.GRUP_NOMBRE AS nombreGrupo,
                grup.GRUP_CAPACIDAD AS capacidadGrupo,
                grup.MATE_CODIGOMATERIA AS codigoMateria,
                mate.MATE_NOMBRE AS nombreMateria,
                ceco.CECO_ID AS idCentroCosto,
                ceco.CECO_DESCRIPCION AS descripcionCentroCosto,
                pepr.PEPR_ID AS idPersonaProyecto,
                pepr.PROY_ID AS idProyecto,
                proy.PROY_IDPROYECTO AS idProyectoPadre,
                proy.PROY_NOMBRE AS nombreProyecto,
                proy.PROY_DESCRIPCION AS descripcionProyecto,
                tipr.TIPR_ID AS idTipoProyecto,
                tipr.TIPR_NOMBRE AS nombreTipoProyecto,
                tipr.TIPR_DESCRIPCION AS descripcionTipoProyecto,
                tipr.TIPR_TIPO AS tipoTipoProyecto,
                proy_padre.PROY_ID AS idProyectoPadreEntidad,
                proy_padre.PROY_NOMBRE AS nombreProyectoPadre,
                proy_padre.PROY_DESCRIPCION AS descripcionProyectoPadre,
                tipr_padre.TIPR_ID AS idTipoProyectoPadre,
                tipr_padre.TIPR_NOMBRE AS nombreTipoProyectoPadre,
                tipr_padre.TIPR_DESCRIPCION AS descripcionTipoProyectoPadre,
                tipr_padre.TIPR_TIPO AS tipoTipoProyecto
            FROM DETALLES_RESUELTOS DR
            LEFT JOIN RVD.TIPOACTIVIDADES tiac
                ON tiac.TIAC_ID = DR.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE
            LEFT JOIN ACADEMICO.GRUPO grup
                ON grup.GRUP_ID = DR.GRUP_ID
            LEFT JOIN ACADEMICO.MATERIA mate
                ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
            LEFT JOIN ACADEMICO.UNIDAD unid
                ON unid.UNID_ID = grup.UNID_IDREGIONAL
            LEFT JOIN ACADEMICO.PROGRAMA prog
                ON prog.PROG_ID = DR.PROG_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                ON ceco.CECO_ID = DR.CECO_ID
            LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                ON (
                    DR.DNCD_ID IS NOT NULL
                    AND recp.DNCD_ID = DR.DNCD_ID
                )
                OR (
                    DR.DECD_ID IS NOT NULL
                    AND recp.DECD_ID = DR.DECD_ID
                )
            LEFT JOIN RVD.PERSONAPROYECTO pepr
                ON pepr.PEPR_ID = recp.PEPR_ID
            LEFT JOIN RVD.PROYECTOS proy
                ON proy.PROY_ID = pepr.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr
                ON proy.TIPR_ID = tipr.TIPR_ID
            LEFT JOIN RVD.PROYECTOS proy_padre
                ON proy.PROY_IDPROYECTO = proy_padre.PROY_ID
            LEFT JOIN RVD.TIPOPROYECTO tipr_padre
                ON proy_padre.TIPR_ID = tipr_padre.TIPR_ID
            ORDER BY
                COALESCE(DR.DECD_ID, DR.DNCD_ID),
                recp.RECP_ID
            """, nativeQuery = true)
    List<DetalleCargaDocenteListadoProjection> findVigenteDetailsByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);
}

/* 17/07/2026 @:Daniel Arias */
