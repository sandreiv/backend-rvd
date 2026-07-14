package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.math.BigDecimal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadAdministracionListadoProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.TipoActividadListadoProjection;

public interface TipoActividadesRepository
        extends JpaRepository<TipoActividadesEntity, Long> {

    @Query(value = """
            SELECT
                tiac.TIAC_ID AS id,
                tiac.TIAC_IDPADRE AS idPadre,
                tiac.TIAC_NOMBRE AS nombre,
                tiac.TIAC_DESCRIPCION AS descripcion,
                tiac.TIAC_ORDEN AS orden,
                tiac.TIAC_CODIGO AS codigo
            FROM RVD.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_IDPADRE IS NULL
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadListadoProjection> findParentActivityTypes();

    @Query(value = """
            SELECT
                tiac.TIAC_ID AS id,
                tiac.TIAC_IDPADRE AS idPadre,
                tiac.TIAC_NOMBRE AS nombre,
                tiac.TIAC_DESCRIPCION AS descripcion,
                tiac.TIAC_ORDEN AS orden,
                tiac.TIAC_CODIGO AS codigo
            FROM RVD.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_IDPADRE = :idPadre
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadListadoProjection> findByIdPadre(
            @Param("idPadre") Long idPadre);

    @Query(value = """
            SELECT
                tiac.TIAC_ID,
                tiac.TIAC_IDPADRE,
                tiac.TIAC_NOMBRE,
                tiac.TIAC_DESCRIPCION,
                tiac.TIAC_ORDEN,
                tiac.TIAC_ESTADO,
                tiac.TIAC_CODIGO,
                tiac.TIAC_COMPONENTE,
                tiac.TIAC_CLASE,
                tiac.TIAC_MINIMOHORAS,
                tiac.TIAC_MAXIMOHORAS,
                tiac.TIAC_REGISTRADOPOR,
                tiac.TIAC_FECHACAMBIO
            FROM RVD.TIPOACTIVIDADES tiac
            WHERE tiac.TIAC_ID IN (
                SELECT descendant.TIAC_ID
                FROM RVD.TIPOACTIVIDADES descendant
                START WITH descendant.TIAC_ID = :idTipoActividad
                CONNECT BY PRIOR descendant.TIAC_ID = descendant.TIAC_IDPADRE
            )
            AND NOT EXISTS (
                SELECT 1
                FROM RVD.TIPOACTIVIDADES hijo
                WHERE hijo.TIAC_IDPADRE = tiac.TIAC_ID
            )
            ORDER BY tiac.TIAC_ORDEN, tiac.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadesEntity> findCriteriaByParentId(
            @Param("idTipoActividad") Long idTipoActividad);

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_CODIGO AS codigo,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_ESTADO AS estado
            FROM RVD.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_IDPADRE IS NULL
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findAdministrationParentList();

    @Query(value = """
            SELECT
                TIAC.TIAC_ID AS id,
                TIAC.TIAC_NOMBRE AS nombre,
                TIAC.TIAC_DESCRIPCION AS descripcion,
                TIAC.TIAC_CODIGO AS codigo,
                TIAC.TIAC_MINIMOHORAS AS minimoHoras,
                TIAC.TIAC_MAXIMOHORAS AS maximoHoras,
                TIAC.TIAC_ORDEN AS orden,
                TIAC.TIAC_ESTADO AS estado
            FROM RVD.TIPOACTIVIDADES TIAC
            WHERE TIAC.TIAC_IDPADRE = :idPadre
            ORDER BY
                CASE
                    WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                    THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                    ELSE 999999
                END,
                TIAC.TIAC_NOMBRE
            """, nativeQuery = true)
    List<TipoActividadAdministracionListadoProjection> findAdministrationChildrenList(
            @Param("idPadre") Long idPadre);

    @Query(value = """
            SELECT TO_CHAR(
                COALESCE(
                    MAX(
                        CASE
                            WHEN REGEXP_LIKE(NVL(TIAC.TIAC_ORDEN, '0'), '^[0-9]+$')
                            THEN TO_NUMBER(TIAC.TIAC_ORDEN)
                            ELSE 0
                        END
                    ),
                    0
                ) + 1
            )
            FROM RVD.TIPOACTIVIDADES TIAC
            WHERE (
                (:idPadre IS NULL AND TIAC.TIAC_IDPADRE IS NULL)
                OR
                (:idPadre IS NOT NULL AND TIAC.TIAC_IDPADRE = :idPadre)
            )
            """, nativeQuery = true)
    String findNextOrderByParent(@Param("idPadre") Long idPadre);

    boolean existsByIdPadre(Long idPadre);


    @Procedure(name = "TipoActividadesEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_TIAC_ID") Long id,
            @Param("P_TIAC_REGISTRADOPOR") String registradoPor
    );    
}