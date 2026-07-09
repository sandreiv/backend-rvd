package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.CentroCostoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CatalogoAdministracionProjection;

public interface CentroCostoRepository extends JpaRepository<CentroCostoEntity, Long> {

    @Query(value = """
            SELECT
                CECO.CECO_ID AS id,
                CECO.CECO_DESCRIPCION AS label,
                CECO.CECO_CODIGO AS codigo
            FROM CONTABLEV3.CENTROCOSTO CECO
            WHERE CECO.CECO_DESCRIPCION IS NOT NULL
            ORDER BY CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CatalogoAdministracionProjection> findAdministrationOptions();
}