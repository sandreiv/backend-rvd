/**
 * Aplicación: rvd
 * Archivo: NovedadReporteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad en PDF)
 * 07/10/2026 - Horas de la novedad anterior con detalle; la carga original solo si no hay novedad previa
 * 08/10/2026 - Daniel Arias - Modificación : PDFHISTORICO
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.EncabezadoPreasignacionProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HorasTipoActividadProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.RegistroNovedadProjection;

public interface NovedadReporteRepository
        extends JpaRepository<NovedadCargaDocenteEntity, Long> {

    // Novedad mas nueva, aprobada y vigente del docente.
    @Query(value = """
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
            AND NOCD.NOCD_ESTADONOVEDAD = '1'
            AND NOCD.NOCD_VIGENTE = '1'
            ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findCurrentApprovedNovelty(
            @Param("idCargaDocente") Long idCargaDocente);

    // Novedad aprobada exacta seleccionada desde el historial.
    // No exige NOCD_VIGENTE = '1' porque puede ser una aprobación histórica.
    @Query(value = """
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.NOCD_ID = :idNovedadCargaDocente
            AND NOCD.NOCD_ESTADONOVEDAD = '1'
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findApprovedNoveltyById(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente
    );        

    // Novedad aprobada inmediatamente anterior a la seleccionada.
    // La comparación se realiza cronológicamente por FECHACAMBIO
    // y NOCD_ID se utiliza como desempate cuando dos registros
    // tienen exactamente la misma fecha.
    @Query(value = """
            SELECT ANTERIOR.*
            FROM RVD.NOVEDADCARGADOCENTE ANTERIOR

            INNER JOIN RVD.NOVEDADCARGADOCENTE ACTUAL
                ON ACTUAL.NOCD_ID = :idNovedadActual

            WHERE ANTERIOR.CADO_ID = :idCargaDocente
            AND ANTERIOR.NOCD_ESTADONOVEDAD = '1'

            AND (
                ANTERIOR.NOCD_FECHACAMBIO < ACTUAL.NOCD_FECHACAMBIO

                OR (
                    ANTERIOR.NOCD_FECHACAMBIO = ACTUAL.NOCD_FECHACAMBIO
                    AND ANTERIOR.NOCD_ID < ACTUAL.NOCD_ID
                )
            )

            ORDER BY
                ANTERIOR.NOCD_FECHACAMBIO DESC,
                ANTERIOR.NOCD_ID DESC

            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findPreviousApprovedNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedadActual") Long idNovedadActual
    );

    // Foto de la novedad (nombre, documento, modalidad, montos y tipo de novedad).
    @Query(value = """
            SELECT
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombre,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                MOCO.MOCO_NOMBRE AS modalidad,
                NOCD.NOCD_PUNTOS AS puntos,
                NOCD.NOCD_HORAS AS horasSemana,
                NOCD.NOCD_VALORCONTRATO AS valorContrato,
                NOCD.NOCD_VALORPRESTACIONES AS prestaciones,
                NOCD.NOCD_TOTALCONTRATO AS totalContrato,
                NOVE.NOVE_TIPO AS tipoNovedad
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = NOCD.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN CONTRATOS.MODALIDADCONTRATACION MOCO
                ON MOCO.MOCO_ID = NOCD.MOCO_ID
            LEFT JOIN RVD.NOVEDADES NOVE
                ON NOVE.NOVE_ID = NOCD.NOVE_ID
            WHERE NOCD.NOCD_ID = :idNovedadCargaDocente
            """, nativeQuery = true)
    Optional<RegistroNovedadProjection> findNovedadSnapshot(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente);

    // Foto del registro original (carga docente).
    @Query(value = """
            SELECT
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombre,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documento,
                MOCO.MOCO_NOMBRE AS modalidad,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_HORAS AS horasSemana,
                CADO.CADO_VALORCONTRATO AS valorContrato,
                CADO.CADO_VALORPRESTACIONES AS prestaciones,
                CADO.CADO_TOTALCONTRATO AS totalContrato,
                CAST(NULL AS VARCHAR2(200)) AS tipoNovedad
            FROM RVD.CARGADOCENTE CADO
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN CONTRATOS.MODALIDADCONTRATACION MOCO
                ON MOCO.MOCO_ID = CADO.MOCO_ID
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    Optional<RegistroNovedadProjection> findCargaDocenteSnapshot(
            @Param("idCargaDocente") Long idCargaDocente);

    // Horas por tipo de actividad padre (FAD, FAI, CTEI, ISU, AC) de la novedad.
    @Query(value = """
            SELECT
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO))) AS codigoPadre,
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
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = DNCD.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE DNCD.NOCD_ID = :idNovedadCargaDocente
            AND NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO) IS NOT NULL
            GROUP BY
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)))
            """, nativeQuery = true)
    List<HorasTipoActividadProjection> findHorasNovedad(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente);


    @Query(value = """
            SELECT
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO))) AS codigoPadre,
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
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = DNCD.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE DNCD.NOCD_ID = (
                SELECT NOCD_ID
                FROM (
                    SELECT NOCD.NOCD_ID
                    FROM RVD.NOVEDADCARGADOCENTE NOCD
                    WHERE NOCD.CADO_ID = :idCargaDocente
                    AND NOCD.NOCD_ESTADONOVEDAD = '1'
                    AND (
                        NOCD.NOCD_FECHACAMBIO < (
                            SELECT ACT.NOCD_FECHACAMBIO
                            FROM RVD.NOVEDADCARGADOCENTE ACT
                            WHERE ACT.NOCD_ID = :idNovedadCargaDocente
                        )
                        OR (
                            NOCD.NOCD_FECHACAMBIO = (
                                SELECT ACT.NOCD_FECHACAMBIO
                                FROM RVD.NOVEDADCARGADOCENTE ACT
                                WHERE ACT.NOCD_ID = :idNovedadCargaDocente
                            )
                            AND NOCD.NOCD_ID < :idNovedadCargaDocente
                        )
                    )
                    AND EXISTS (
                        SELECT 1
                        FROM RVD.DETALLENOVEDADCARGADOCENTE DET
                        WHERE DET.NOCD_ID = NOCD.NOCD_ID
                    )
                    ORDER BY NOCD.NOCD_FECHACAMBIO DESC, NOCD.NOCD_ID DESC
                    FETCH FIRST 1 ROW ONLY
                )
            )
            AND NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO) IS NOT NULL
            GROUP BY
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)))
            """, nativeQuery = true)
    List<HorasTipoActividadProjection> findHorasNovedadAnterior(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente);

    // Horas por tipo de actividad padre del registro original.
    @Query(value = """
            SELECT
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO))) AS codigoPadre,
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
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC
                ON TIAC.TIAC_ID = DECD.TIAC_ID
            LEFT JOIN RVD.TIPOACTIVIDADES TIAC_PADRE
                ON TIAC_PADRE.TIAC_ID = TIAC.TIAC_IDPADRE
            WHERE DECD.CADO_ID = :idCargaDocente
            AND NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO) IS NOT NULL
            GROUP BY
                UPPER(TRIM(NVL(TIAC_PADRE.TIAC_CODIGO, TIAC.TIAC_CODIGO)))
            """, nativeQuery = true)
    List<HorasTipoActividadProjection> findHorasCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    // Encabezado (unidad, facultad, coordinacion, periodo, convocatoria) por carga docente.
    @Query(value = """
            SELECT
                CARG.CARG_ID AS idCarga,
                COOR.COOR_ID AS idCoordinacion,
                UNID_REG.UNID_NOMBRE AS unidad,
                UNID_AREA.UNID_NOMBRE AS facultad,
                COOR.COOR_NOMBRE AS coordinacion,
                PEUN.PEUN_ID AS idPeriodoUniversidad,
                TRIM(PEUN.PEUN_ANO || '-' || PEUN.PEUN_PERIODO) AS periodoAcademico,
                PEUN.PEUN_PERIODO AS periodo,
                PEUN.PEUN_ANO AS anio,
                CONV.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS convocatoria
            FROM RVD.CARGADOCENTE CADO
            INNER JOIN RVD.CARGA CARG
                ON CARG.CARG_ID = CADO.CARG_ID
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
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    Optional<EncabezadoPreasignacionProjection> findEncabezadoByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente);

    // Nombre completo de la persona (para el pie "generado por").
    @Query(value = """
            SELECT
                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )
            FROM GENERAL.PERSONAGENERAL PEGE
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            WHERE PEGE.PEGE_ID = :idPersonaGeneral
            """, nativeQuery = true)
    String findNombrePersona(
            @Param("idPersonaGeneral") Long idPersonaGeneral);
}
