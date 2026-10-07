/**
 * Aplicación: rvd
 * Archivo: DetalleNovedadCargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/09/2026
 * Modificaciones:
 * 17/09/2026 - Daniel Arias - Creación inicial
 * 17/09/2026 - Andrés Hernández - Consulta de los detalles de actividades como novedad
 * 05/10/2026 - Andrés Hernández - Consulta de los detalles de actividades que ve desarrollo académico
 * 07/10/2026 - Andrés Hernández - Cambio de detalles efectivos a DetalleCargaDocenteRepository
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DetalleNovedadResumenProjection;

public interface DetalleNovedadCargaDocenteRepository
        extends JpaRepository<DetalleNovedadCargaDocenteEntity, Long> {

    List<DetalleNovedadCargaDocenteEntity> findByIdNovedadCargaDocente(
            Long idNovedadCargaDocente
    );

    long countByIdNovedadCargaDocente(Long idNovedadCargaDocente);

    // Fase 3: al aprobar, se activa la foto de detalle de esa novedad y se desactiva
    // la de las demas novedades de la carga.
    @Modifying
    @Query(value = """
            UPDATE RVD.DETALLENOVEDADCARGADOCENTE DNCD
            SET DNCD.DNCD_VIGENTE = '0'
            WHERE DNCD.NOCD_ID IN (
                SELECT NOCD_ID
                FROM RVD.NOVEDADCARGADOCENTE
                WHERE CADO_ID = :idCargaDocente
            )
            """, nativeQuery = true)
    int clearDetalleVigenteByCargaDocente(@Param("idCargaDocente") Long idCargaDocente);

    @Modifying
    @Query(value = """
            UPDATE RVD.DETALLENOVEDADCARGADOCENTE DNCD
            SET DNCD.DNCD_VIGENTE = '1'
            WHERE DNCD.NOCD_ID = :idNovedadCargaDocente
            """, nativeQuery = true)
    int setDetalleVigenteByNovedad(@Param("idNovedadCargaDocente") Long idNovedadCargaDocente);

    @Procedure(name = "DetalleNovedadCargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_DNCD_ID") Long id,
            @Param("P_DNCD_REGISTRADOPOR") String registradoPor
    );

    @Query(value = """
            WITH detalles_novedad AS (
                SELECT
                    dncd.DNCD_ID AS idDetalleNovedadCargaDocente,
                    dncd.DNCD_HORAS AS horas,
                    tiac.TIAC_NOMBRE AS nombreTipoActividad,
                    tiac.TIAC_CODIGO AS codigoTipoActividad,
                    tiac.TIAC_COMPONENTE AS componenteTipoActividad,
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
                FROM RVD.DETALLENOVEDADCARGADOCENTE dncd
                LEFT JOIN RVD.TIPOACTIVIDADES tiac
                    ON tiac.TIAC_ID = dncd.TIAC_ID
                LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                    ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE
                LEFT JOIN ACADEMICO.GRUPO grup
                    ON grup.GRUP_ID = dncd.GRUP_ID
                LEFT JOIN ACADEMICO.MATERIA mate
                    ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
                LEFT JOIN ACADEMICO.UNIDAD unid
                    ON unid.UNID_ID = grup.UNID_IDREGIONAL
                LEFT JOIN ACADEMICO.PROGRAMA prog
                    ON prog.PROG_ID = dncd.PROG_ID
                LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                    ON ceco.CECO_ID = dncd.CECO_ID
                
                LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                    ON recp.DNCD_ID = dncd.DNCD_ID
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
                WHERE dncd.NOCD_ID = :idNovedadCargaDocente
            ),

            detalles_efectivos AS (
                SELECT
                    dncd.DNCD_ID AS idDetalleNovedadCargaDocente,
                    dncd.DNCD_HORAS AS horas,
                    tiac.TIAC_NOMBRE AS nombreTipoActividad,
                    tiac.TIAC_CODIGO AS codigoTipoActividad,
                    tiac.TIAC_COMPONENTE AS componenteTipoActividad,
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
                FROM RVD.DETALLENOVEDADCARGADOCENTE dncd
                INNER JOIN RVD.NOVEDADCARGADOCENTE nocd
                    ON nocd.NOCD_ID = dncd.NOCD_ID
                LEFT JOIN RVD.TIPOACTIVIDADES tiac
                    ON tiac.TIAC_ID = dncd.TIAC_ID
                LEFT JOIN RVD.TIPOACTIVIDADES tiac_padre
                    ON tiac_padre.TIAC_ID = tiac.TIAC_IDPADRE
                LEFT JOIN ACADEMICO.GRUPO grup
                    ON grup.GRUP_ID = dncd.GRUP_ID
                LEFT JOIN ACADEMICO.MATERIA mate
                    ON mate.MATE_CODIGOMATERIA = grup.MATE_CODIGOMATERIA
                LEFT JOIN ACADEMICO.UNIDAD unid
                    ON unid.UNID_ID = grup.UNID_IDREGIONAL
                LEFT JOIN ACADEMICO.PROGRAMA prog
                    ON prog.PROG_ID = dncd.PROG_ID
                LEFT JOIN CONTABLEV3.CENTROCOSTO ceco
                    ON ceco.CECO_ID = dncd.CECO_ID

                LEFT JOIN RVD.RELACIONCARGAPROYECTO recp
                    ON recp.DNCD_ID = dncd.DNCD_ID

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
                WHERE nocd.CADO_ID = :idCargaDocente
                    AND dncd.DNCD_VIGENTE = '1'
            ),

            resultado AS (
                SELECT *
                FROM detalles_novedad

                UNION ALL

                SELECT *
                FROM detalles_efectivos
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM detalles_novedad
                )
            )
            
            SELECT *
            FROM resultado
            ORDER BY idDetalleNovedadCargaDocente
            """, nativeQuery = true)
    List<DetalleNovedadResumenProjection> findResumenByIdNovedadCargaDocente(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente,
            @Param("idCargaDocente") Long idCargaDocente);
}
