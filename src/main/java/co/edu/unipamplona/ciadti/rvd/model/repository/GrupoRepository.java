package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.GrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.GrupoListadoProjection;

public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {

    @Query(value = """
            SELECT
                grup.GRUP_ID AS id,
                grup.GRUP_NOMBRE AS nombre,
                grup.GRUP_CAPACIDAD AS capacidad
            FROM ACADEMICO.GRUPO grup
            WHERE grup.MATE_CODIGOMATERIA = :codigoMateria
                AND grup.PEUN_ID = :idPeriodoUniversidad
            ORDER BY grup.GRUP_NOMBRE
            """, nativeQuery = true)
    List<GrupoListadoProjection> findByCodigoMateriaAndIdPeriodoUniversidad(
            @Param("codigoMateria") String codigoMateria,
            @Param("idPeriodoUniversidad") Long idPeriodoUniversidad);

    @Query(value = """
            SELECT DISTINCT grup.MATE_CODIGOMATERIA
            FROM ACADEMICO.GRUPO grup
            WHERE grup.MATE_CODIGOMATERIA IN (:codigosMateria)
            """, nativeQuery = true)
    List<String> findCodigosMateriaConGrupo(
            @Param("codigosMateria") List<String> codigosMateria);
}
