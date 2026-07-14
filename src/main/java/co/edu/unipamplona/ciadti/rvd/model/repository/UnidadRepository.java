package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.UnidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface UnidadRepository extends JpaRepository<UnidadEntity, Long> {

    @Query("""
            SELECT u FROM UnidadEntity u
            INNER JOIN CoordinacionesEntity c ON u.id = c.idRegional
            WHERE c.id = :idCoordinacion
                AND u.regional = '1'
            ORDER BY u.nombre
            """)
    List<UnidadEntity> findRegionalUnits(@Param("idCoordinacion") Long idCoordinacion);

    @Query(value = """
            SELECT
                UNID.UNID_ID AS id,
                UNID.UNID_NOMBRE AS label,
                UNID.UNID_CODIGO AS codigo
            FROM ACADEMICO.UNIDAD UNID
            WHERE UNID.UNID_NOMBRE IS NOT NULL
            AND (
                :term IS NULL
                OR LOWER(UNID.UNID_NOMBRE) LIKE '%' || LOWER(:term) || '%'
                OR LOWER(UNID.UNID_CODIGO) LIKE '%' || LOWER(:term) || '%'
            )
            ORDER BY UNID.UNID_NOMBRE
            FETCH FIRST 20 ROWS ONLY
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> searchAdministrationUnits(
            @Param("term") String term
    );


}
