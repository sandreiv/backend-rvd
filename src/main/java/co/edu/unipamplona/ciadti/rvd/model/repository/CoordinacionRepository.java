package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.CoordinacionesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CoordinacionListadoProjection;

public interface CoordinacionRepository extends JpaRepository<CoordinacionesEntity, Long> {

    @Query(value = """
            SELECT DISTINCT
                COOR.COOR_ID AS idCoordinacion,
                COOR.COOR_NOMBRE AS nombreCoordinacion,
                COOR.COOR_DESCRIPCION AS descripcionCoordinacion,
                COOR.COOR_CODIGO AS codigo,
                COOR.COOR_ESACADEMICA AS esAcademica,
                UNID_REG.UNID_ID AS idUnidadRegional,
                UNID_REG.UNID_NOMBRE AS nombreUnidadRegional,
                UNID_AREA.UNID_ID AS idUnidadArea,
                UNID_AREA.UNID_NOMBRE AS nombreUnidadArea,
                METO.METO_ID AS idMetodologia,
                METO.METO_DESCRIPCION AS descripcionMetodologia,
                MODA.MODA_ID AS idModalidad,
                MODA.MODA_DESCRIPCION AS descripcionModalidad,
                CONV.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS nombreConvocatoria,
                CONV.CONV_DESCRIPCION AS descripcionConvocatoria,
                CONV.CONV_ESTADO AS estadoConvocatoria,
                NIED.NIED_ID AS idNivelEducativo,
                NIED.NIED_DESCRIPCION AS descripcionNivelEducativo,
                PEUN.PEUN_ID AS idPeriodoUniversidad,
                PEUN.PEUN_ANO AS anioPeriodo,
                PEUN.PEUN_PERIODO AS descripcionPeriodo,
                CARG.CARG_ID AS idCarga,
                ESCA.ESCA_ID AS idEstadoCarga,
                ESCA.ESCA_NOMBRE AS nombreEstadoCarga,
                ESCA.ESCA_DESCRIPCION AS descripcionEstadoCarga
            FROM RVD.CONVOCATORIA CONV
            INNER JOIN ACADEMICO.NIVELEDUCATIVO NIED
                ON NIED.NIED_ID = CONV.NIED_ID
            INNER JOIN ACADEMICO.PERIODOUNIVERSIDAD PEUN
                ON PEUN.PEUN_ID = CONV.PEUN_ID
            INNER JOIN RVD.CARGA CARG
                ON CARG.CONV_ID = CONV.CONV_ID
            INNER JOIN RVD.ESTADOCARGA ESCA
                ON ESCA.ESCA_ID = CARG.ESCA_ID
            INNER JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = CARG.COOR_ID
            INNER JOIN ACADEMICO.UNIDAD UNID_REG
                ON UNID_REG.UNID_ID = COOR.UNID_IDREGIONAL
            INNER JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            INNER JOIN ACADEMICO.METODOLOGIA METO
                ON METO.METO_ID = COOR.METO_ID
            INNER JOIN ACADEMICO.MODALIDAD MODA
                ON MODA.MODA_ID = COOR.MODA_ID
            INNER JOIN RVD.PERSONACOORDINACION PECO
                ON PECO.COOR_ID = COOR.COOR_ID
            WHERE CONV.CONV_ID = :convId
                AND PECO.PEGE_ID = :idPersonaGeneral
                AND CONV.CONV_ESTADO = '1'
            ORDER BY
                UNID_REG.UNID_NOMBRE,
                UNID_AREA.UNID_NOMBRE,
                COOR.COOR_NOMBRE
            """, nativeQuery = true)
    List<CoordinacionListadoProjection> findByConvocatoriaWithCarga(
            @Param("convId") Long convId,
            @Param("idPersonaGeneral") Long idPersonaGeneral);

    @Query(value = """
            SELECT DISTINCT
                COOR.COOR_ID AS idCoordinacion,
                COOR.COOR_NOMBRE AS nombreCoordinacion,
                COOR.COOR_DESCRIPCION AS descripcionCoordinacion,
                COOR.COOR_CODIGO AS codigo,
                COOR.COOR_ESACADEMICA AS esAcademica,
                UNID_REG.UNID_ID AS idUnidadRegional,
                UNID_REG.UNID_NOMBRE AS nombreUnidadRegional,
                UNID_AREA.UNID_ID AS idUnidadArea,
                UNID_AREA.UNID_NOMBRE AS nombreUnidadArea,
                METO.METO_ID AS idMetodologia,
                METO.METO_DESCRIPCION AS descripcionMetodologia,
                MODA.MODA_ID AS idModalidad,
                MODA.MODA_DESCRIPCION AS descripcionModalidad,
                CAST(NULL AS NUMBER) AS idConvocatoria,
                CAST(NULL AS VARCHAR2(4000)) AS nombreConvocatoria,
                CAST(NULL AS VARCHAR2(4000)) AS descripcionConvocatoria,
                CAST(NULL AS VARCHAR2(4000)) AS estadoConvocatoria,
                CAST(NULL AS NUMBER) AS idNivelEducativo,
                CAST(NULL AS VARCHAR2(4000)) AS descripcionNivelEducativo,
                CAST(NULL AS NUMBER) AS idPeriodoUniversidad,
                CAST(NULL AS NUMBER) AS anioPeriodo,
                CAST(NULL AS VARCHAR2(4000)) AS descripcionPeriodo,
                CAST(NULL AS NUMBER) AS idCarga,
                CAST(NULL AS NUMBER) AS idEstadoCarga,
                CAST(NULL AS VARCHAR2(4000)) AS nombreEstadoCarga,
                CAST(NULL AS VARCHAR2(4000)) AS descripcionEstadoCarga
            FROM RVD.COORDINACIONES COOR
            INNER JOIN ACADEMICO.UNIDAD UNID_REG
                ON UNID_REG.UNID_ID = COOR.UNID_IDREGIONAL
            INNER JOIN ACADEMICO.UNIDAD UNID_AREA
                ON UNID_AREA.UNID_ID = COOR.UNID_IDAREA
            INNER JOIN ACADEMICO.METODOLOGIA METO
                ON METO.METO_ID = COOR.METO_ID
            INNER JOIN ACADEMICO.MODALIDAD MODA
                ON MODA.MODA_ID = COOR.MODA_ID
            INNER JOIN RVD.PERSONACOORDINACION PECO
                ON COOR.COOR_ID = PECO.COOR_ID
            WHERE NOT EXISTS (
                SELECT 1
                FROM RVD.CARGA CARG
                WHERE CARG.COOR_ID = COOR.COOR_ID
            ) AND PECO.PEGE_ID = :idPersonaGeneral
            ORDER BY
                UNID_REG.UNID_NOMBRE,
                UNID_AREA.UNID_NOMBRE,
                COOR.COOR_NOMBRE
            """, nativeQuery = true)
    List<CoordinacionListadoProjection> findWithoutCarga(
            @Param("idPersonaGeneral") Long idPersonaGeneral);
}
