package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.EscalafonEntity;

public interface EscalafonRepository
        extends JpaRepository<EscalafonEntity, Long> {

    @Query("""
            SELECT esca FROM EscalafonEntity esca
            WHERE esca.idCategoriaCatedratico = :idCategoriaCatedratico
                AND esca.idPersonaGeneral = :idPersonaGeneral
            """)
    EscalafonEntity findByIdCategoriaCatedratico(
            @Param("idCategoriaCatedratico") Long idCategoriaCatedratico,
            @Param("idPersonaGeneral") Long idPersonaGeneral);
}
