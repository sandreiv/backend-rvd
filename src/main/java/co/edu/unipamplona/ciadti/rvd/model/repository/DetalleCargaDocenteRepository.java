/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
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
                tiac_padre.TIAC_NOMBRE AS nombreTipoActividadPadre,
                tiac_padre.TIAC_DESCRIPCION AS descripcionTipoActividadPadre,
                tiac_padre.TIAC_ORDEN AS ordenTipoActividadPadre,
                tiac_padre.TIAC_CODIGO AS codigoTipoActividadPadre,
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

    @Procedure(name = "DetalleCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(@Param("P_DECD_ID") Long id, @Param("P_DECD_REGISTRADOPOR") String registradoPor);
}

/* 17/07/2026 @:Daniel Arias */
