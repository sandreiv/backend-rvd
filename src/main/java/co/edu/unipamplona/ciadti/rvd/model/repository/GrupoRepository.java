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
            ORDER BY grup.GRUP_NOMBRE
            """, nativeQuery = true)
    List<GrupoListadoProjection> findByCodigoMateria(
            @Param("codigoMateria") String codigoMateria);
}
