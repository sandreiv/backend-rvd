package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.PaisGeneralEntity;

public interface PaisGeneralRepository
        extends JpaRepository<
                PaisGeneralEntity,
                String
        > {

    @Query("""
            SELECT pais
            FROM PaisGeneralEntity pais
            WHERE pais.id <> '0'
              AND pais.nombre IS NOT NULL
            ORDER BY UPPER(pais.nombre)
            """)
    List<PaisGeneralEntity>
    findAvailableCountries();
}