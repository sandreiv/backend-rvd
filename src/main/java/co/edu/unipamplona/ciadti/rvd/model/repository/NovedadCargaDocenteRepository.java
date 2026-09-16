package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.NovedadDocenteCargaCoordinacionProjection;

public interface NovedadCargaDocenteRepository
        extends JpaRepository<NovedadCargaDocenteEntity, Long> {

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
                AND CADO.MOCO_ID = :idModalidadContratacion
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
                        AND EXISTS (
                            SELECT 1
                            FROM RVD.DETALLECARGADOCENTE DECD
                            WHERE DECD.CADO_ID = CR.CADO_ID
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
}