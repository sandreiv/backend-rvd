package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadesEntity;
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
}
