/**
 * Aplicación: rvd
 * Archivo: NovedadCargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 * 07/10/2026 - Andrés Hernández - Cambio de docentes efectivos a CargaDocenteRepository
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.HistorialNovedadResumenProjection;

public interface NovedadCargaDocenteRepository
        extends JpaRepository<
                NovedadCargaDocenteEntity,
                Long> {

    Optional<NovedadCargaDocenteEntity> findByIdCargaDocente(
            Long idCargaDocente
    );

    boolean existsByIdNovedadCatalogo(
            Long idNovedadCatalogo
    );

    // Fase 2: las inserciones usan SEQ_NOVEDADCARGADOCENTE.NEXTVAL; CURRVAL devuelve
    // ese NOCD_ID dentro de la misma transaccion para colgarle el detalle de la novedad.
    @Query(value = """
            SELECT RVD.SEQ_NOVEDADCARGADOCENTE.CURRVAL
            FROM DUAL
            """, nativeQuery = true)
    Long currentNovedadCargaDocenteId();

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
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
            AND NOCD.NOCD_VIGENTE = '1'
            ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findVigenteByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente
    );

    @Query(value = """
            SELECT
                NOCD.NOVE_ID AS idNovedad,
                NOVE.NOVE_TIPO AS tipo,
                NOVE.NOVE_DESCRIPCION AS descripcion,
                NOVE.NOVE_ACCION AS accion,
                NOVE.NOVE_COMPONENTE AS componente,
                NOCD.NOCD_FECHANOVEDAD AS fecha,
                NOCD.NOCD_ESTADONOVEDAD AS estadoNovedad,
                NOCD.NOCD_VIGENTE AS vigente
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            LEFT JOIN RVD.NOVEDADES NOVE
                ON NOVE.NOVE_ID = NOCD.NOVE_ID
            WHERE NOCD.CADO_ID = :idCargaDocente
            ORDER BY
                NOCD.NOCD_FECHANOVEDAD ASC,
                NOCD.NOCD_FECHACAMBIO ASC
            """, nativeQuery = true)
    List<HistorialNovedadResumenProjection> findHistorialByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente
    );

    @Query(value = """
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
                AND NOCD.NOCD_ESTADONOVEDAD = '1'
            ORDER BY NOCD.NOCD_FECHACAMBIO DESC
            FETCH FIRST 1 ROW ONLY
            """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findLastApprovedNovelty(
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

    @Query(value = """
        SELECT NOCD.*
        FROM RVD.NOVEDADCARGADOCENTE NOCD
        WHERE NOCD.CADO_ID = :idCargaDocente
        AND NOCD.NOCD_ESTADONOVEDAD = '0'
        ORDER BY NOCD.NOCD_FECHACAMBIO DESC
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    Optional<NovedadCargaDocenteEntity> findNoveltyInReview(
            @Param("idCargaDocente") Long idCargaDocente
    );

    @Modifying
    @Query(value = """
            UPDATE RVD.NOVEDADCARGADOCENTE NOCD
            SET NOCD.NOCD_VIGENTE = '0'
            WHERE NOCD.CADO_ID = :idCargaDocente
            AND NOCD.NOCD_VIGENTE = '1'
            """, nativeQuery = true)
    int clearVigenteByIdCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente
    );

    // Fase 3: aprueba una novedad por su NOCD_ID exacto (antes usaba MAX(FECHACAMBIO),
    // que puede empatar al segundo y aprobar la fila equivocada).
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE RVD.NOVEDADCARGADOCENTE NOCD
        SET NOCD.NOCD_ESTADONOVEDAD = '1',
            NOCD.NOCD_ESTADOELIMINADO = :estadoEliminado,
            NOCD.NOCD_VIGENTE = '1',
            NOCD.NOCD_REGISTRADOPOR = :registradoPor,
            NOCD.NOCD_FECHACAMBIO = SYSDATE
        WHERE NOCD.NOCD_ID = :idNovedadCargaDocente
        AND NOCD.NOCD_ESTADONOVEDAD = '0'
        """, nativeQuery = true)
    int approveNoveltyById(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente,
            @Param("estadoEliminado") String estadoEliminado,
            @Param("registradoPor") String registradoPor
    );

    // Fase 4: rechazo logico. La novedad queda en '2' y sin vigencia; no se tocan los
    // detalles (su foto queda en DNCD_VIGENTE='0') ni CARG_VALOR.
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE RVD.NOVEDADCARGADOCENTE NOCD
        SET NOCD.NOCD_ESTADONOVEDAD = '2',
            NOCD.NOCD_VIGENTE = '0',
            NOCD.NOCD_REGISTRADOPOR = :registradoPor,
            NOCD.NOCD_FECHACAMBIO = SYSDATE
        WHERE NOCD.NOCD_ID = :idNovedadCargaDocente
        AND NOCD.NOCD_ESTADONOVEDAD = '0'
        """, nativeQuery = true)
    int rejectNoveltyById(
            @Param("idNovedadCargaDocente") Long idNovedadCargaDocente,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                SRC.CADO_ID,
                SRC.CARG_ID,
                :idPersonaGeneral,
                SRC.MOCO_ID,
                :idCategoriaCatedratico,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                SRC.NOCD_ESTADO,
                '0',
                SRC.NOCD_HORAS,
                SRC.NOCD_HORASDEEXCEPCION,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
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
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                :idPersonaGeneral,
                CADO.MOCO_ID,
                :idCategoriaCatedratico,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                CADO.CADO_ESTADO,
                '0',
                CADO.CADO_HORAS,
                CADO.CADO_HORASDEEXCEPCION,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
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
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                SRC.CADO_ID,
                SRC.CARG_ID,
                :idPersonaGeneral,
                SRC.MOCO_ID,
                :idCategoriaCatedratico,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                SRC.NOCD_ESTADO,
                '0',
                SRC.NOCD_HORAS,
                SRC.NOCD_HORASDEEXCEPCION,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
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
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                :idPersonaGeneral,
                CADO.MOCO_ID,
                :idCategoriaCatedratico,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                CADO.CADO_ESTADO,
                '0',
                CADO.CADO_HORAS,
                CADO.CADO_HORASDEEXCEPCION,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
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
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
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
                    NOCD.NOCD_ESTADOELIMINADO,
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

            /*
            * Si nunca tuvo novedad:
            *     NOCD es NULL → se considera activo.
            *
            * Si la última fotografía tiene eliminado = '1':
            *     ese CADO ya no cuenta como asignación.
            */
            AND NVL(NOCD.NOCD_ESTADOELIMINADO, '0') = '0'

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

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
        INSERT INTO RVD.NOVEDADCARGADOCENTE
        (
            NOCD_ID,
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
            NOCD_ESTADOELIMINADO,
            NOCD_REGISTRADOPOR,
            NOCD_FECHACAMBIO
        )
        SELECT
            RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
            SRC.CADO_ID,
            SRC.CARG_ID,
            SRC.PEGE_ID,
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
            '0',
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
            '0',
            :registradoPor,
            SYSDATE
        FROM (
            SELECT NOCD.*
            FROM RVD.NOVEDADCARGADOCENTE NOCD
            WHERE NOCD.CADO_ID = :idCargaDocente
            AND NOCD.NOCD_ESTADONOVEDAD <> '2'
            ORDER BY NOCD.NOCD_FECHACAMBIO DESC
        ) SRC
        WHERE ROWNUM = 1
        """, nativeQuery = true)
    int insertDeleteProfessorFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );


    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
        INSERT INTO RVD.NOVEDADCARGADOCENTE
        (
            NOCD_ID,
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
            NOCD_ESTADOELIMINADO,
            NOCD_REGISTRADOPOR,
            NOCD_FECHACAMBIO
        )
        SELECT
            RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
            CADO.CADO_ID,
            CADO.CARG_ID,
            CADO.PEGE_ID,
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
            '0',
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
            '0',
            :registradoPor,
            SYSDATE
        FROM RVD.CARGADOCENTE CADO
        WHERE CADO.CADO_ID = :idCargaDocente
        """, nativeQuery = true)
    int insertDeleteProfessorFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );


    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                SRC.CADO_ID,
                SRC.CARG_ID,
                NVL(:idPersonaGeneral, SRC.PEGE_ID),
                :idModalidadContratacion,
                :idCategoriaCatedratico,
                :idFechasConvocatoria,
                :idNovedad,
                SYSDATE,
                NULL,
                :fechaInicio,
                :fechaFin,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                SRC.NOCD_ESTADO,
                '0',
                :horas,
                :horasDeExcepcion,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
                :semanas,
                SRC.NOCD_NIVELFORMACION,
                SRC.NOCD_MOMENTO,
                :onceMeses,
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
    int insertContractModalityFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idFechasConvocatoria") Long idFechasConvocatoria,
            @Param("idNovedad") Long idNovedad,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("semanas") String semanas,
            @Param("horas") String horas,
            @Param("horasDeExcepcion") String horasDeExcepcion,
            @Param("onceMeses") String onceMeses,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                NVL(:idPersonaGeneral, CADO.PEGE_ID),
                :idModalidadContratacion,
                :idCategoriaCatedratico,
                :idFechasConvocatoria,
                :idNovedad,
                SYSDATE,
                NULL,
                :fechaInicio,
                :fechaFin,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                CADO.CADO_ESTADO,
                '0',
                :horas,
                :horasDeExcepcion,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
                :semanas,
                CADO.CADO_NIVELFORMACION,
                CADO.CADO_MOMENTO,
                :onceMeses,
                '0',
                :registradoPor,
                SYSDATE
            FROM RVD.CARGADOCENTE CADO
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    int insertContractModalityFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idPersonaGeneral") Long idPersonaGeneral,
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idFechasConvocatoria") Long idFechasConvocatoria,
            @Param("idNovedad") Long idNovedad,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("semanas") String semanas,
            @Param("horas") String horas,
            @Param("horasDeExcepcion") String horasDeExcepcion,
            @Param("onceMeses") String onceMeses,
            @Param("registradoPor") String registradoPor
    );
    

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                SRC.CADO_ID,
                SRC.CARG_ID,
                SRC.PEGE_ID,
                SRC.MOCO_ID,
                SRC.CACA_ID,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                SRC.NOCD_ESTADO,
                '0',
                :horas,
                :horasDeExcepcion,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
                :semanas,
                SRC.NOCD_NIVELFORMACION,
                SRC.NOCD_MOMENTO,
                :onceMeses,
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
                FETCH FIRST 1 ROW ONLY
            ) SRC
            """, nativeQuery = true)
    int insertChangeProjectActivitiesFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("horas") String horas,
            @Param("horasDeExcepcion") String horasDeExcepcion,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("semanas") String semanas,
            @Param("onceMeses") String onceMeses,
            @Param("registradoPor") String registradoPor
    );
    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                CADO.PEGE_ID,
                CADO.MOCO_ID,
                CADO.CACA_ID,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                CADO.CADO_ESTADO,
                '0',
                :horas,
                :horasDeExcepcion,
                :valorHora,
                :puntos,
                :valorPunto,
                :totalContrato,
                :semanas,
                CADO.CADO_NIVELFORMACION,
                CADO.CADO_MOMENTO,
                :onceMeses,
                '0',
                :registradoPor,
                SYSDATE
            FROM RVD.CARGADOCENTE CADO
            WHERE CADO.CADO_ID = :idCargaDocente
            """, nativeQuery = true)
    int insertChangeProjectActivitiesFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("horas") String horas,
            @Param("horasDeExcepcion") String horasDeExcepcion,
            @Param("valorHora") BigDecimal valorHora,
            @Param("puntos") String puntos,
            @Param("valorPunto") BigDecimal valorPunto,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("semanas") String semanas,
            @Param("onceMeses") String onceMeses,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                SRC.CADO_ID,
                SRC.CARG_ID,
                SRC.PEGE_ID,
                SRC.MOCO_ID,
                SRC.CACA_ID,
                SRC.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                SRC.NOCD_FECHAINICIO,
                SRC.NOCD_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                SRC.NOCD_ESTADO,
                '0',
                SRC.NOCD_HORAS,
                SRC.NOCD_HORASDEEXCEPCION,
                SRC.NOCD_VALORHORA,
                :puntos,
                SRC.NOCD_VALORPUNTO,
                :totalContrato,
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
                FETCH FIRST 1 ROW ONLY
            ) SRC
            """, nativeQuery = true)
    int insertUpdateContractValueFromNovelty(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("puntos") String puntos,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                CADO.PEGE_ID,
                CADO.MOCO_ID,
                CADO.CACA_ID,
                CADO.FECO_ID,
                :idNovedad,
                SYSDATE,
                NULL,
                CADO.CADO_FECHAINICIO,
                CADO.CADO_FECHAFIN,
                :valorContrato,
                :valorPrestaciones,
                :salario,
                CADO.CADO_ESTADO,
                '0',
                CADO.CADO_HORAS,
                CADO.CADO_HORASDEEXCEPCION,
                CADO.CADO_VALORHORA,
                :puntos,
                CADO.CADO_VALORPUNTO,
                :totalContrato,
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
    int insertUpdateContractValueFromCargaDocente(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("valorContrato") BigDecimal valorContrato,
            @Param("valorPrestaciones") BigDecimal valorPrestaciones,
            @Param("salario") BigDecimal salario,
            @Param("puntos") String puntos,
            @Param("totalContrato") BigDecimal totalContrato,
            @Param("registradoPor") String registradoPor
    );

    // Fase 2: incluye NOCD_ID (PK de la novedad) tomado de SEQ_NOVEDADCARGADOCENTE.
    @Modifying
    @Query(value = """
            INSERT INTO RVD.NOVEDADCARGADOCENTE
            (
                NOCD_ID,
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
                RVD.SEQ_NOVEDADCARGADOCENTE.NEXTVAL,
                CADO.CADO_ID,
                CADO.CARG_ID,
                CADO.PEGE_ID,
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
                '0',
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
    int insertAddNoveltyProfessor(
            @Param("idCargaDocente") Long idCargaDocente,
            @Param("idNovedad") Long idNovedad,
            @Param("registradoPor") String registradoPor
    );
}