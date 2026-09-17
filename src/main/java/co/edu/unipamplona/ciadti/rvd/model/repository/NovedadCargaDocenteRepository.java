package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntityId;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.NovedadDocenteCargaCoordinacionProjection;

public interface NovedadCargaDocenteRepository
        extends JpaRepository<
                NovedadCargaDocenteEntity,
                NovedadCargaDocenteEntityId> {

    Optional<NovedadCargaDocenteEntity> findByIdCargaDocente(
            Long idCargaDocente
    );

    boolean existsByIdNovedadCatalogo(
            Long idNovedadCatalogo
    );

    // Falta determinar si tieneActividades se toma con la tabla detalles o novedad detalles. Siguiendo una logica parecida
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

                    COALESCE(NOCD.PEGE_ID, CADO.PEGE_ID) AS PEGE_ID,
                    COALESCE(NOCD.CARG_ID, CADO.CARG_ID) AS CARG_ID,
                    COALESCE(NOCD.MOCO_ID, CADO.MOCO_ID) AS MOCO_ID,
                    COALESCE(NOCD.CACA_ID, CADO.CACA_ID) AS CACA_ID,
                    COALESCE(NOCD.FECO_ID, CADO.FECO_ID) AS FECO_ID,

                    COALESCE(NOCD.NOCD_FECHAINICIO, CADO.CADO_FECHAINICIO)
                        AS CADO_FECHAINICIO,

                    COALESCE(NOCD.NOCD_FECHAFIN, CADO.CADO_FECHAFIN)
                        AS CADO_FECHAFIN,

                    COALESCE(NOCD.NOCD_VALORCONTRATO, CADO.CADO_VALORCONTRATO)
                        AS CADO_VALORCONTRATO,

                    COALESCE(NOCD.NOCD_VALORPRESTACIONES, CADO.CADO_VALORPRESTACIONES)
                        AS CADO_VALORPRESTACIONES,

                    COALESCE(NOCD.NOCD_SALARIO, CADO.CADO_SALARIO)
                        AS CADO_SALARIO,

                    COALESCE(NOCD.NOCD_VALORHORA, CADO.CADO_VALORHORA)
                        AS CADO_VALORHORA,

                    COALESCE(NOCD.NOCD_PUNTOS, CADO.CADO_PUNTOS)
                        AS CADO_PUNTOS,

                    COALESCE(NOCD.NOCD_VALORPUNTO, CADO.CADO_VALORPUNTO)
                        AS CADO_VALORPUNTO,

                    COALESCE(NOCD.NOCD_TOTALCONTRATO, CADO.CADO_TOTALCONTRATO)
                        AS CADO_TOTALCONTRATO,

                    COALESCE(NOCD.NOCD_SEMANAS, CADO.CADO_SEMANAS)
                        AS CADO_SEMANAS,

                    COALESCE(NOCD.NOCD_ONCEMESES, CADO.CADO_ONCEMESES)
                        AS CADO_ONCEMESES,

                    COALESCE(NOCD.NOCD_HORASDEEXCEPCION, CADO.CADO_HORASDEEXCEPCION)
                        AS CADO_HORASDEEXCEPCION,

                    CADO.CADO_ESTADO,

                    NOCD.NOVE_ID,
                    NOCD.NOCD_ESTADONOVEDAD

                FROM RVD.CARGADOCENTE CADO

                LEFT JOIN NOVEDADES_VALIDAS NOCD
                    ON NOCD.CADO_ID = CADO.CADO_ID
                    AND NOCD.RN = 1

                WHERE CADO.CARG_ID = :idCarga
                AND COALESCE(NOCD.MOCO_ID, CADO.MOCO_ID)
                    = :idModalidadContratacion
            )
            SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,

                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,

                CR.CADO_ID AS idCargaDocente,
                CR.CADO_ESTADO AS estado,
                CR.CARG_ID AS idCarga,
                CR.MOCO_ID AS idModalidadContratacion,
                CR.CACA_ID AS idCategoriaCatedratico,

                CR.NOVE_ID AS idNovedadCatalogo,
                CR.NOCD_ESTADONOVEDAD AS estadoNovedad,

                CR.CADO_FECHAINICIO AS cargaFechaInicio,
                CR.CADO_FECHAFIN AS cargaFechaFin,

                CR.CADO_VALORCONTRATO AS valorContrato,
                CR.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CR.CADO_SALARIO AS asignacionSalarial,
                CR.CADO_TOTALCONTRATO AS totalContrato,
                CR.CADO_VALORHORA AS valorHora,
                CR.CADO_PUNTOS AS puntos,
                CR.CADO_VALORPUNTO AS valorPunto,
                CR.CADO_SEMANAS AS semanas,
                CR.CADO_ONCEMESES AS onceMeses,
                CR.CADO_HORASDEEXCEPCION AS horasDeExcepcion,

                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin,

                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
                        WHERE DNCD.CADO_ID = CR.CADO_ID
                    )
                    OR EXISTS (
                        SELECT 1
                        FROM RVD.DETALLECARGADOCENTE DECD
                        WHERE DECD.CADO_ID = CR.CADO_ID
                    )
                    THEN 1
                    ELSE 0
                END AS tieneActividades

            FROM CARGAS_RESUELTAS CR

            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CR.PEGE_ID

            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID

            LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CR.FECO_ID

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
    List<NovedadDocenteCargaCoordinacionProjection> findProfessorsByCargaAndModalityInNovelties(
            @Param("idCarga") Long idCarga,
            @Param("idModalidadContratacion") Long idModalidadContratacion);

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

                    COALESCE(NOCD.PEGE_ID, CADO.PEGE_ID) AS PEGE_ID,
                    COALESCE(NOCD.CARG_ID, CADO.CARG_ID) AS CARG_ID,
                    COALESCE(NOCD.MOCO_ID, CADO.MOCO_ID) AS MOCO_ID,
                    COALESCE(NOCD.CACA_ID, CADO.CACA_ID) AS CACA_ID,
                    COALESCE(NOCD.FECO_ID, CADO.FECO_ID) AS FECO_ID,

                    COALESCE(NOCD.NOCD_FECHAINICIO, CADO.CADO_FECHAINICIO)
                        AS CADO_FECHAINICIO,

                    COALESCE(NOCD.NOCD_FECHAFIN, CADO.CADO_FECHAFIN)
                        AS CADO_FECHAFIN,

                    COALESCE(NOCD.NOCD_VALORCONTRATO, CADO.CADO_VALORCONTRATO)
                        AS CADO_VALORCONTRATO,

                    COALESCE(NOCD.NOCD_VALORPRESTACIONES, CADO.CADO_VALORPRESTACIONES)
                        AS CADO_VALORPRESTACIONES,

                    COALESCE(NOCD.NOCD_SALARIO, CADO.CADO_SALARIO)
                        AS CADO_SALARIO,

                    COALESCE(NOCD.NOCD_VALORHORA, CADO.CADO_VALORHORA)
                        AS CADO_VALORHORA,

                    COALESCE(NOCD.NOCD_PUNTOS, CADO.CADO_PUNTOS)
                        AS CADO_PUNTOS,

                    COALESCE(NOCD.NOCD_VALORPUNTO, CADO.CADO_VALORPUNTO)
                        AS CADO_VALORPUNTO,

                    COALESCE(NOCD.NOCD_TOTALCONTRATO, CADO.CADO_TOTALCONTRATO)
                        AS CADO_TOTALCONTRATO,

                    COALESCE(NOCD.NOCD_SEMANAS, CADO.CADO_SEMANAS)
                        AS CADO_SEMANAS,

                    COALESCE(NOCD.NOCD_ONCEMESES, CADO.CADO_ONCEMESES)
                        AS CADO_ONCEMESES,

                    COALESCE(NOCD.NOCD_HORASDEEXCEPCION, CADO.CADO_HORASDEEXCEPCION)
                        AS CADO_HORASDEEXCEPCION,

                    CADO.CADO_ESTADO,

                    NOCD.NOVE_ID,
                    NOCD.NOCD_ESTADONOVEDAD

                FROM RVD.CARGADOCENTE CADO

                LEFT JOIN NOVEDADES_VALIDAS NOCD
                    ON NOCD.CADO_ID = CADO.CADO_ID
                    AND NOCD.RN = 1
            )
            SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,

                TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,

                CR.CADO_ID AS idCargaDocente,
                CR.CADO_ESTADO AS estado,
                CR.CARG_ID AS idCarga,
                CR.MOCO_ID AS idModalidadContratacion,
                CR.CACA_ID AS idCategoriaCatedratico,

                CR.NOVE_ID AS idNovedadCatalogo,
                CR.NOCD_ESTADONOVEDAD AS estadoNovedad,

                CR.CADO_FECHAINICIO AS cargaFechaInicio,
                CR.CADO_FECHAFIN AS cargaFechaFin,

                CR.CADO_VALORCONTRATO AS valorContrato,
                CR.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CR.CADO_SALARIO AS asignacionSalarial,
                CR.CADO_TOTALCONTRATO AS totalContrato,
                CR.CADO_VALORHORA AS valorHora,
                CR.CADO_PUNTOS AS puntos,
                CR.CADO_VALORPUNTO AS valorPunto,
                CR.CADO_SEMANAS AS semanas,
                CR.CADO_ONCEMESES AS onceMeses,
                CR.CADO_HORASDEEXCEPCION AS horasDeExcepcion,

                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin,

                CASE
                    WHEN CR.CADO_ID IS NOT NULL
                        AND (
                            EXISTS (
                                SELECT 1
                                FROM RVD.DETALLENOVEDADCARGADOCENTE DNCD
                                WHERE DNCD.CADO_ID = CR.CADO_ID
                            )
                            OR EXISTS (
                                SELECT 1
                                FROM RVD.DETALLECARGADOCENTE DECD
                                WHERE DECD.CADO_ID = CR.CADO_ID
                            )
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

            LEFT JOIN CARGAS_RESUELTAS CR
                ON CR.PEGE_ID = DOPC.PEGE_ID
                AND CR.CARG_ID = CARG.CARG_ID
                AND CR.MOCO_ID = :idModalidadContratacion

            LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CR.FECO_ID

            WHERE CARG.CARG_ID = :idCarga

            ORDER BY
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<NovedadDocenteCargaCoordinacionProjection> findPlantProfessorsByCargaAndModalityInNovelties(
            @Param("idCarga") Long idCarga,
            @Param("idModalidadContratacion") Long idModalidadContratacion);

    // Busca si el registro para duplicar esta en la tabla novedad. Si no lo retorna, se debe consultar el repositorio de carga docente y construir el nuevo
    @Query(value = """
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
                AND NOCD.NOCD_ESTADONOVEDAD <> '2'
            ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findProfessorRecordToDuplicateNovelty(
            @Param("idCargaDocente") Long idCargaDocente
    );

    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
            AND NOCD.NOCD_ESTADONOVEDAD = '0'
            """, nativeQuery = true)
    long countNoveltyInReview(
            @Param("idCargaDocente") Long idCargaDocente
    );
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                CADO_ID,
                CARG_ID,
                PEGE_ID,
                MOCO_ID,
                CACA_ID,
                FECO_ID,
                NOVE_ID,
                NOCD_FECHANOVEDAD,
                NOCD_OBSERVACIONNOVEDAD,
                NOCD_FECHAINICIO,
                NOCD_FECHAFIN,
                NOCD_VALORCONTRATO,
                NOCD_VALORPRESTACIONES,
                NOCD_SALARIO,
                NOCD_ESTADO,
                NOCD_VIGENTE,
                NOCD_HORAS,
                NOCD_HORASDEEXCEPCION,
                NOCD_VALORHORA,
                NOCD_PUNTOS,
                NOCD_VALORPUNTO,
                NOCD_TOTALCONTRATO,
                NOCD_SEMANAS,
                NOCD_NIVELFORMACION,
                NOCD_MOMENTO,
                NOCD_ONCEMESES,
                NOCD_ESTADONOVEDAD,
                NOCD_REGISTRADOPOR,
                NOCD_FECHACAMBIO
            )
            SELECT
                SRC.CADO_ID,
                SRC.CARG_ID,
                :idPersonaGeneral,
                SRC.MOCO_ID,
                SRC.CACA_ID,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                SRC.NOCD_VALORCONTRATO,
                SRC.NOCD_VALORPRESTACIONES,
                SRC.NOCD_SALARIO,
                SRC.NOCD_ESTADO,
                SRC.NOCD_VIGENTE,
                SRC.NOCD_HORAS,
                SRC.NOCD_HORASDEEXCEPCION,
                SRC.NOCD_VALORHORA,
                SRC.NOCD_PUNTOS,
                SRC.NOCD_VALORPUNTO,
                SRC.NOCD_TOTALCONTRATO,
                SRC.NOCD_SEMANAS,
                SRC.NOCD_NIVELFORMACION,
                SRC.NOCD_MOMENTO,
                SRC.NOCD_ONCEMESES,
                '0',
                :registradoPor,
                SYSDATE
            FROM
            (
                SELECT NOCD.*
                FROM RVD.NOVEDADCARGADOCENTE NOCD
                WHERE NOCD.CADO_ID = :idCargaDocente
                AND NOCD.NOCD_ESTADONOVEDAD <> '2'
                ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            ) SRC
            WHERE ROWNUM = 1
            """, nativeQuery = true)
    int insertAssignNameNnFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                CADO_ID,
                CARG_ID,
                PEGE_ID,
                MOCO_ID,
                CACA_ID,
                FECO_ID,
                NOVE_ID,
                NOCD_FECHANOVEDAD,
                NOCD_OBSERVACIONNOVEDAD,
                NOCD_FECHAINICIO,
                NOCD_FECHAFIN,
                NOCD_VALORCONTRATO,
                NOCD_VALORPRESTACIONES,
                NOCD_SALARIO,
                NOCD_ESTADO,
                NOCD_VIGENTE,
                NOCD_HORAS,
                NOCD_HORASDEEXCEPCION,
                NOCD_VALORHORA,
                NOCD_PUNTOS,
                NOCD_VALORPUNTO,
                NOCD_TOTALCONTRATO,
                NOCD_SEMANAS,
                NOCD_NIVELFORMACION,
                NOCD_MOMENTO,
                NOCD_ONCEMESES,
                NOCD_ESTADONOVEDAD,
                NOCD_REGISTRADOPOR,
                NOCD_FECHACAMBIO
            )
            SELECT
                CADO.CADO_ID,
                CADO.CARG_ID,
                :idPersonaGeneral,
                CADO.MOCO_ID,
                CADO.CACA_ID,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                CADO.CADO_VALORCONTRATO,
                CADO.CADO_VALORPRESTACIONES,
                CADO.CADO_SALARIO,
                CADO.CADO_ESTADO,
                CADO.CADO_VIGENTE,
                CADO.CADO_HORAS,
                CADO.CADO_HORASDEEXCEPCION,
                CADO.CADO_VALORHORA,
                CADO.CADO_PUNTOS,
                CADO.CADO_VALORPUNTO,
                CADO.CADO_TOTALCONTRATO,
                CADO.CADO_SEMANAS,
                CADO.CADO_NIVELFORMACION,
                CADO.CADO_MOMENTO,
                CADO.CADO_ONCEMESES,
                '0',
                :registradoPor,
                SYSDATE
            FROM RVD.CARGADOCENTE CADO
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    int insertAssignNameNnFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );

    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                CADO_ID,
                CARG_ID,
                PEGE_ID,
                MOCO_ID,
                CACA_ID,
                FECO_ID,
                NOVE_ID,
                NOCD_FECHANOVEDAD,
                NOCD_OBSERVACIONNOVEDAD,
                NOCD_FECHAINICIO,
                NOCD_FECHAFIN,
                NOCD_VALORCONTRATO,
                NOCD_VALORPRESTACIONES,
                NOCD_SALARIO,
                NOCD_ESTADO,
                NOCD_VIGENTE,
                NOCD_HORAS,
                NOCD_HORASDEEXCEPCION,
                NOCD_VALORHORA,
                NOCD_PUNTOS,
                NOCD_VALORPUNTO,
                NOCD_TOTALCONTRATO,
                NOCD_SEMANAS,
                NOCD_NIVELFORMACION,
                NOCD_MOMENTO,
                NOCD_ONCEMESES,
                NOCD_ESTADONOVEDAD,
                NOCD_REGISTRADOPOR,
                NOCD_FECHACAMBIO
            )
            SELECT
                SRC.CADO_ID,
                SRC.CARG_ID,
                :idPersonaGeneral,
                SRC.MOCO_ID,
                SRC.CACA_ID,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                SRC.NOCD_VALORCONTRATO,
                SRC.NOCD_VALORPRESTACIONES,
                SRC.NOCD_SALARIO,
                SRC.NOCD_ESTADO,
                SRC.NOCD_VIGENTE,
                SRC.NOCD_HORAS,
                SRC.NOCD_HORASDEEXCEPCION,
                SRC.NOCD_VALORHORA,
                SRC.NOCD_PUNTOS,
                SRC.NOCD_VALORPUNTO,
                SRC.NOCD_TOTALCONTRATO,
                SRC.NOCD_SEMANAS,
                SRC.NOCD_NIVELFORMACION,
                SRC.NOCD_MOMENTO,
                SRC.NOCD_ONCEMESES,
                '0',
                :registradoPor,
                SYSDATE
            FROM
            (
                SELECT NOCD.*
                FROM RVD.NOVEDADCARGADOCENTE NOCD
                WHERE NOCD.CADO_ID = :idCargaDocente
                AND NOCD.NOCD_ESTADONOVEDAD <> '2'
                ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            ) SRC
            WHERE ROWNUM = 1
            """, nativeQuery = true)
    int insertChangeProfessorFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );

    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                CADO_ID,
                CARG_ID,
                PEGE_ID,
                MOCO_ID,
                CACA_ID,
                FECO_ID,
                NOVE_ID,
                NOCD_FECHANOVEDAD,
                NOCD_OBSERVACIONNOVEDAD,
                NOCD_FECHAINICIO,
                NOCD_FECHAFIN,
                NOCD_VALORCONTRATO,
                NOCD_VALORPRESTACIONES,
                NOCD_SALARIO,
                NOCD_ESTADO,
                NOCD_VIGENTE,
                NOCD_HORAS,
                NOCD_HORASDEEXCEPCION,
                NOCD_VALORHORA,
                NOCD_PUNTOS,
                NOCD_VALORPUNTO,
                NOCD_TOTALCONTRATO,
                NOCD_SEMANAS,
                NOCD_NIVELFORMACION,
                NOCD_MOMENTO,
                NOCD_ONCEMESES,
                NOCD_ESTADONOVEDAD,
                NOCD_REGISTRADOPOR,
                NOCD_FECHACAMBIO
            )
            SELECT
                CADO.CADO_ID,
                CADO.CARG_ID,
                :idPersonaGeneral,
                CADO.MOCO_ID,
                CADO.CACA_ID,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                CADO.CADO_VALORCONTRATO,
                CADO.CADO_VALORPRESTACIONES,
                CADO.CADO_SALARIO,
                CADO.CADO_ESTADO,
                CADO.CADO_VIGENTE,
                CADO.CADO_HORAS,
                CADO.CADO_HORASDEEXCEPCION,
                CADO.CADO_VALORHORA,
                CADO.CADO_PUNTOS,
                CADO.CADO_VALORPUNTO,
                CADO.CADO_TOTALCONTRATO,
                CADO.CADO_SEMANAS,
                CADO.CADO_NIVELFORMACION,
                CADO.CADO_MOMENTO,
                CADO.CADO_ONCEMESES,
                '0',
                :registradoPor,
                SYSDATE
            FROM RVD.CARGADOCENTE CADO
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    int insertChangeProfessorFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );


    @Query(value = """
            SELECT COUNT(1)
            FROM RVD.CARGADOCENTE CADO

            LEFT JOIN (
                SELECT
                    NOCD.CADO_ID,
                    NOCD.PEGE_ID,
                    NOCD.MOCO_ID,
                    ROW_NUMBER() OVER (
                        PARTITION BY NOCD.CADO_ID
                        ORDER BY NOCD.NOCD_FECHACAMBIO DESC
                    ) AS RN
                FROM RVD.NOVEDADCARGADOCENTE NOCD
                WHERE NOCD.NOCD_ESTADONOVEDAD <> '2'
            ) NOCD
                ON NOCD.CADO_ID = CADO.CADO_ID
                AND NOCD.RN = 1

            WHERE CADO.CARG_ID = :idCarga
            AND CADO.CADO_ID <> :idCargaDocente
            AND (
                    CASE
                        WHEN NOCD.CADO_ID IS NOT NULL
                            THEN NOCD.MOCO_ID
                        ELSE CADO.MOCO_ID
                    END
                ) = :idModalidadContratacion
            AND (
                    CASE
                        WHEN NOCD.CADO_ID IS NOT NULL
                            THEN NOCD.PEGE_ID
                        ELSE CADO.PEGE_ID
                    END
                ) = :idPersonaGeneral
            """, nativeQuery = true)
    long countProfessorAssignedToAnotherLoad(
            @Param("idCarga") Long idCarga,
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("idPersonaGeneral") Long idPersonaGeneral
    );

    
}