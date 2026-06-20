package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaGeneralEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocentePreasignacionProjection;

public interface PersonaGeneralRepository
        extends JpaRepository<PersonaGeneralEntity, Long> {

    @Query("""
            SELECT pege FROM PersonaGeneralEntity pege
            INNER JOIN FETCH pege.personaNaturalGeneral peng
            WHERE pege.id = :id
            """)
    Optional<PersonaGeneralEntity> findGeneralPersonById(@Param("id") Long id);

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

    @Query(value = """
            SELECT DISTINCT
                PEGE.PEGE_ID AS idPersonaGeneral,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documentoIdentidad,
                PENG.PENG_PRIMERNOMBRE AS primerNombre,
                PENG.PENG_SEGUNDONOMBRE AS segundoNombre,
                PENG.PENG_PRIMERAPELLIDO AS primerApellido,
                PENG.PENG_SEGUNDOAPELLIDO AS segundoApellido,
                CACA.CACA_ID AS idCategoriaCatedratico,
                CACA.CACA_DESCRIPCION AS descripcionCategoriaCatedratico
            FROM GENERAL.PERSONAGENERAL PEGE
            INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
            LEFT JOIN TALENTOV3.TRABAJADOR TRABA
                ON PENG.PEGE_ID = TRABA.PEGE_ID
            LEFT JOIN TALENTOV3.CATEGORIACATEDRATICO CACA
                ON TRABA.CACA_ID = CACA.CACA_ID
            WHERE
                (:documento IS NULL
                    OR UPPER(PEGE.PEGE_DOCUMENTOIDENTIDAD)
                        LIKE UPPER('%' || :documento || '%'))
            AND
                (:nombre IS NULL
                    OR UPPER(PENG.PENG_PRIMERNOMBRE)
                        LIKE UPPER('%' || :nombre || '%')
                    OR UPPER(PENG.PENG_SEGUNDONOMBRE)
                        LIKE UPPER('%' || :nombre || '%')
                    OR UPPER(PENG.PENG_PRIMERAPELLIDO)
                        LIKE UPPER('%' || :nombre || '%')
                    OR UPPER(PENG.PENG_SEGUNDOAPELLIDO)
                        LIKE UPPER('%' || :nombre || '%'))
            ORDER BY
                PENG.PENG_PRIMERAPELLIDO,
                PENG.PENG_SEGUNDOAPELLIDO,
                PENG.PENG_PRIMERNOMBRE,
                PENG.PENG_SEGUNDONOMBRE
            """, nativeQuery = true)
    List<DocentePreasignacionProjection> searchProfessorsForPreassignment(
            @Param("nombre") String nombre,
            @Param("documento") String documento);
}
