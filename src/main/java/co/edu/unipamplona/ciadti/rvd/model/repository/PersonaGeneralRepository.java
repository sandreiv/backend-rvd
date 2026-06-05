package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;

public interface PersonaGeneralRepository
        extends JpaRepository<PersonaGeneralEntity, Long> {

    @Query("""
            SELECT pege FROM PersonaGeneralEntity pege
            INNER JOIN FETCH pege.personaNaturalGeneral peng
            WHERE pege.id = :id
            """)
    Optional<PersonaGeneralEntity> findByIdWithNatural(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT pege FROM PersonaGeneralEntity pege
            INNER JOIN FETCH pege.personaNaturalGeneral peng
            WHERE (:documento IS NULL
                OR UPPER(pege.documentoIdentidad)
                    LIKE UPPER(CONCAT('%', :documento, '%')))
            AND (:nombre IS NULL
                OR UPPER(peng.primerNombre)
                    LIKE UPPER(CONCAT('%', :nombre, '%'))
                OR UPPER(peng.segundoNombre)
                    LIKE UPPER(CONCAT('%', :nombre, '%'))
                OR UPPER(peng.primerApellido)
                    LIKE UPPER(CONCAT('%', :nombre, '%'))
                OR UPPER(peng.segundoApellido)
                    LIKE UPPER(CONCAT('%', :nombre, '%')))
            """)
    List<PersonaGeneralEntity> searchGeneralPerson(
            @Param("nombre") String nombre,
            @Param("documento") String documento);
}
