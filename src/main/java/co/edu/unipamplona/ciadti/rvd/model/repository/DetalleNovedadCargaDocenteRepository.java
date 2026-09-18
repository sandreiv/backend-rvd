package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleCargaDocenteListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasProgramaProjection;

public interface DetalleNovedadCargaDocenteRepository
        extends JpaRepository<DetalleNovedadCargaDocenteEntity, Long> {

    List<DetalleNovedadCargaDocenteEntity> findByIdNovedadCargaDocente(
            Long idNovedadCargaDocente
    );

    @Procedure(name = "DetalleNovedadCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_DNCD_ID") Long id,
            @Param("P_DNCD_REGISTRADOPOR") String registradoPor
    );

    @Query(value = """
            WITH NOVEDAD_VALIDA AS (

                SELECT 1
                FROM RVD.NOVEDADCARGADOCENTE NOCD
                WHERE NOCD.CADO_ID = :idCargaDocente
                AND NOCD.NOCD_ESTADONOVEDAD <> '2'
                FETCH FIRST 1 ROW ONLY
            ),

            DETALLES_NOVEDAD AS (

                SELECT
                    DNCD.DNCD_ID,
                    DNCD.CADO_ID,
                    DNCD.PROG_ID,
                    DNCD.GRUP_ID,
                    DNCD.TIAC_ID,
                    DNCD.CECO_ID,
                    DNCD.DNCD_HORAS

                FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD

                WHERE DNCD.CADO_ID = :idCargaDocente

                AND EXISTS (
                    SELECT 1
                    FROM NOVEDAD_VALIDA
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
    List<DetalleCargaDocenteListadoProjection> findByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);
    
    @Query(value = """
            SELECT
                DNCD.PROG_ID AS idPrograma,
                SUM(
                    NVL(
                        TO_NUMBER(
                            REPLACE(TRIM(DNCD.DNCD_HORAS), ',', '.')
                            DEFAULT NULL ON CONVERSION ERROR
                        ),
                        0
                    )
                ) AS totalHoras
            FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
            WHERE DNCD.CADO_ID = :idCargaDocente
            AND DNCD.PROG_ID IS NOT NULL
            AND (:idDetalleExcluido IS NULL OR DNCD.DNCD_ID <> :idDetalleExcluido)
            GROUP BY DNCD.PROG_ID
            """, nativeQuery = true)
    List<HorasProgramaProjection> findHorasByProgramaAndCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idDetalleExcluido") Long idDetalleExcluido);
}
