package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaCatedraticoEntity;

public interface CategoriaCatedraticoRepository
        extends JpaRepository<CategoriaCatedraticoEntity, Long> {

    @Query("""
            SELECT caca FROM CategoriaCatedraticoEntity caca
            WHERE caca.id IN (
                SELECT camo.idCategoriaCatedratico
                FROM CategoriaModalidadEntity camo
                WHERE camo.idModalidadContratacion = :idModalidadContratacion
            )
            ORDER BY caca.descripcion
            """)
    List<CategoriaCatedraticoEntity> findAllCategories(
            @Param("idModalidadContratacion") Long idModalidadContratacion);
}
